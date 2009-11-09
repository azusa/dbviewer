/*
 * Copyright (c) 2007�|2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleColumnSizeUtil;
import zigen.plugin.db.ext.oracle.internal.OracleDbBlockSizeSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleTableColumnSearcher;
import zigen.plugin.db.ui.internal.Table;

/**
 * CalcTableSpace�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/01 ZIGEN create.
 * 
 */
public class CalcTableSpace {

	/**
	 * �ύX�\�v���p�e�B calcurate���\�b�h���s�O�ɒl��Set���邱��
	 */
	int block_header = 100; // 100�o�C�g

	double safeCoefficient = 1.2; // ���S�W��

	/**
	 * �R���X�g���N�^�Œl��ݒ肷��v���p�e�B
	 */
	private final long maxRecord; // ���R�[�h����

	private final Table table; // �Ώۃe�[�u��

	private final String ownerName; // ���L��

	private final String tableName; // �e�[�u����

	private final int pctFree; // pctfree

	/**
	 * DB_BLOCK_SIZE
	 */
	private int blockSize;

	/**
	 * �v�Z���ʎ擾�p(���S�W���Ȃ��j
	 */
	private BigDecimal tableSpaceSize;

	/**
	 * �v�Z���ʎ擾�p�i���S�W�������������́j
	 */
	private BigDecimal tableSpaceSafeSize;

	/**
	 * ���S�W�����������\�̈�̌��ς�T�C�Y
	 * 
	 * @return tableSpaceSafeSize ��߂��܂��B
	 */
	public BigDecimal getTableSpaceSafeSize() {
		return tableSpaceSafeSize;
	}

	/**
	 * �\�̈�̌��ς�T�C�Y
	 * 
	 * @return tableSpaceSize ��߂��܂��B
	 */
	public BigDecimal getTableSpaceSize() {
		return tableSpaceSize;
	}

	/**
	 * �R���X�g���N�^
	 * 
	 * @param table
	 * @param pctFree
	 * @param maxRecord
	 */
	public CalcTableSpace(Table table, int pctFree, long maxRecord) {
		this.table = table;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	/**
	 * �R���X�g���N�^
	 * 
	 * @param table
	 * @param pctFree
	 * @param maxRecord
	 */
	public CalcTableSpace(Table table, int blockSize, int pctFree, long maxRecord) {
		this.table = table;
		this.blockSize = blockSize;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	/**
	 * �v�Z���\�b�h
	 */
	public void calcurate() throws CalcTableSpaceException {
		try {
			IDBConfig config = table.getDbConfig();
			Connection con = Transaction.getInstance(config).getConnection();

			// �u���b�N�T�C�Y�Z�o
			if (blockSize == 0) {
				blockSize = OracleDbBlockSizeSearcher.execute(con);
			}
			// log.debug("�u���b�N�T�C�Y:" + this.blockSize); //$NON-NLS-1$

			// �K�v��Block��
			double necessaryBlock = getNecessaryBlockSize(con, maxRecord);

			// KB, MB�̔���
			// log.debug("�\�̈�̃T�C�Y�F" + tableSpaceSize);

			// ���S�W���Ȃ�
			// this.tableSpaceSize = new BigDecimal((getNecessaryBlockSize(con,
			// maxRecord) * blockSize) / (1024d * 1024d));
			this.tableSpaceSize = new BigDecimal((necessaryBlock * blockSize) / (1024d * 1024d));

			// �؂�グ
			// this.tableSpaceSize = this.tableSpaceSize.setScale(1,
			// BigDecimal.ROUND_UP); // �؏グ
			this.tableSpaceSize = this.tableSpaceSize.setScale(3, BigDecimal.ROUND_UP); // �؏グ

			// ���S�W����
			this.tableSpaceSafeSize = tableSpaceSize.multiply(new BigDecimal(safeCoefficient));
			// �؂�グ
			// this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(1,
			// BigDecimal.ROUND_UP); // �؏グ
			this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(3, BigDecimal.ROUND_UP); // �؏グ

			// log.debug("�K�v�ȃT�C�Y(MB)���擾:" + tableSpaceSafeSize); //$NON-NLS-1$

		} catch (CalcTableSpaceException e) {
			throw e;

		} catch (Exception e) {
			throw new CalcTableSpaceException("�\�̈�̌��ς菈���ŃG���[���������܂���", e.getCause()); //$NON-NLS-1$

		}

	}

	/**
	 * 1.�u���b�N�w�b�_�[�̈�̎擾
	 * 
	 * @return
	 */
	public int getBlockHeaderSize() {
		// log.debug("1.�u���b�N�w�b�_�[�̈�̎擾:" + this.block_header); //$NON-NLS-1$
		return this.block_header;
	}

	/**
	 * 2.���p�\�̈�̎擾
	 * 
	 * @return
	 */
	private final double getRiyouKanouArea() {
		double d = Math.ceil((blockSize - this.getBlockHeaderSize()) * (1 - pctFree / 100d));
		// log.debug("2.���p�\�̈�̎擾:" + d); //$NON-NLS-1$
		return d;

	}

	/**
	 * 3.���ύs�T�C�Y�̎擾
	 * 
	 * @return
	 */
	private final int getRowLength(Connection con) {
		int columnAreaSize = -1;
		try {

			// RowLength���擾���邾���Ȃ̂ŁAconvertUnicode��false�Œ�
			boolean convertUnicode = false;

			OracleTableColumn[] columns = OracleTableColumnSearcher.execute(con, table, convertUnicode);

			// �J�����̈�̎擾
			OracleColumnSizeUtil cs = new OracleColumnSizeUtil();
			columnAreaSize = cs.getRowLength(con, columns);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

		// log.debug("3.���ύs�T�C�Y�̎擾:" + columnAreaSize); //$NON-NLS-1$
		return columnAreaSize;
	}

	/**
	 * 4.�u���b�N������̕��ύs���̌v�Z
	 * 
	 * @return
	 */
	private final double getAverageRowCountOfBlock(Connection con) throws CalcTableSpaceException {
		// double d = Math.floor(getRiyouKanouArea() / getRowLength(con));

		// modify start 2006/09/23 ���p�\�̈�̕����������ꍇ���l�� ZIGEN
		double d = 0.0d;
		double riyoukanou = getRiyouKanouArea();
		double rowLen = getRowLength(con);

		if (riyoukanou >= rowLen) {
			// �؎̂�
			d = Math.floor(riyoukanou / rowLen); // �ʏ�͗��p�\�̈�̕����傫��
		} else {
			// ���p�\�̈�̕����������ꍇ�̏�����ǉ�
			d = 1.0d / Math.ceil(rowLen / riyoukanou);

		}
		// modify end
		// log.debug("4.�u���b�N������̕��ύs���̌v�Z:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * �K�v�ȃu���b�N��
	 * 
	 * @param totalRow
	 * @return
	 */
	private final double getNecessaryBlockSize(Connection con, long totalRow) throws CalcTableSpaceException {
		double d = Math.ceil(totalRow / getAverageRowCountOfBlock(con));
		// log.debug("5.�K�v�ȃu���b�N��:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * @param block_header
	 *            block_header ��ݒ�B
	 */
	public void setBlock_header(int block_header) {
		this.block_header = block_header;
	}

	/**
	 * @param blockSize
	 *            blockSize ��ݒ�B
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * @param safeCoefficient
	 *            safeCoefficient ��ݒ�B
	 */
	public void setSafeCoefficient(double safeCoefficient) {
		this.safeCoefficient = safeCoefficient;
	}

	/**
	 * @return safeCoefficient ��߂��܂��B
	 */
	public double getSafeCoefficient() {
		return safeCoefficient;
	}

	/**
	 * @return blockSize ��߂��܂��B
	 */
	public int getBlockSize() {
		return blockSize;
	}

	public String getCalcResult() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" [�e�[�u���̈�]:" + tableName + " "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		sb.append("   ���ς�T�C�Y:"); //$NON-NLS-1$
		sb.append("  " + getTableSpaceSize() + " MB"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		sb.append("   ���ς�T�C�Y�~" + getSafeCoefficient() + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("  " + getTableSpaceSafeSize() + " MB"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		return sb.toString();
	}

	public List getCsvRow() {
		List list = new ArrayList();

		list.add(ownerName);
		list.add(tableName);
		list.add(""); // Index�� //$NON-NLS-1$

		list.add(String.valueOf(blockSize));
		list.add(String.valueOf(pctFree));

		list.add(String.valueOf(maxRecord));
		list.add(String.valueOf(tableSpaceSize));
		list.add(String.valueOf(safeCoefficient));
		list.add(String.valueOf(tableSpaceSafeSize));
		return list;
	}

	public String csvString() {
		StringBuffer sb = new StringBuffer();
		sb.append(ownerName);
		sb.append(","); //$NON-NLS-1$
		sb.append(tableName);
		sb.append(","); //$NON-NLS-1$
		sb.append(""); // indexName�͂Ȃ� //$NON-NLS-1$
		sb.append(","); //$NON-NLS-1$
		sb.append(maxRecord);
		sb.append(","); //$NON-NLS-1$
		sb.append(tableSpaceSize);
		sb.append(","); //$NON-NLS-1$
		sb.append(safeCoefficient);
		sb.append(","); //$NON-NLS-1$
		sb.append(tableSpaceSafeSize);
		sb.append("\n"); //$NON-NLS-1$
		return sb.toString();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[CalcTableSpace:"); //$NON-NLS-1$
		buffer.append(" block_header: "); //$NON-NLS-1$
		buffer.append(block_header);
		buffer.append(" safeCoefficient: "); //$NON-NLS-1$
		buffer.append(safeCoefficient);
		buffer.append(" maxRecord: "); //$NON-NLS-1$
		buffer.append(maxRecord);
		buffer.append(" table: "); //$NON-NLS-1$
		buffer.append(table);
		buffer.append(" ownerName: "); //$NON-NLS-1$
		buffer.append(ownerName);
		buffer.append(" tableName: "); //$NON-NLS-1$
		buffer.append(tableName);
		buffer.append(" pctFree: "); //$NON-NLS-1$
		buffer.append(pctFree);
		buffer.append(" blockSize: "); //$NON-NLS-1$
		buffer.append(blockSize);
		buffer.append(" tableSpaceSize: "); //$NON-NLS-1$
		buffer.append(tableSpaceSize);
		buffer.append(" tableSpaceSafeSize: "); //$NON-NLS-1$
		buffer.append(tableSpaceSafeSize);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}
}
