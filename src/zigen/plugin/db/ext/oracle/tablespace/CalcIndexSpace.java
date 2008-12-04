/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleColumnSizeUtil;
import zigen.plugin.db.ext.oracle.internal.OracleDbBlockSizeSearcher;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Table;

/**
 * CalcIndexSpace�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/01 ZIGEN create.
 * 
 */
public class CalcIndexSpace {

	/**
	 * �ύX�\�v���p�e�B calcurate���\�b�h���s�O�ɒl��Set���邱��
	 */
	int block_header = 100; // 100�o�C�g

	double safeCoefficient = 2; // ���S�W��

	/**
	 * �R���X�g���N�^�Œl��ݒ肷��v���p�e�B
	 */
	private final long maxRecord; // �\������

	private final Table table;

	private final OracleIndexColumn[] columns; // �����ƂȂ�J����

	private final String ownerName;

	private final String tableName;

	private final String indexName;

	private final int pctFree;

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
	 * @param indexName
	 * @param columns
	 * @param pctFree
	 * @param maxRecord
	 */
	public CalcIndexSpace(Table table, String indexName, OracleIndexColumn[] columns, int pctFree, long maxRecord) {
		this.table = table;
		this.indexName = indexName;
		this.columns = columns;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	/**
	 * �R���X�g���N�^
	 * 
	 * @param table
	 * @param blockSize
	 *            �C�ӂ�DB_BLOCK_SIZE���w�肷��
	 * @param indexName
	 * @param columns
	 * @param pctFree
	 * @param maxRecord
	 */
	public CalcIndexSpace(Table table, int blockSize, String indexName, OracleIndexColumn[] columns, int pctFree, long maxRecord) {
		this.table = table;
		this.blockSize = blockSize;
		this.indexName = indexName;
		this.columns = columns;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	/**
	 * �v�Z���\�b�h
	 */
	public void calcurate() throws Exception {
		try {

			IDBConfig config = table.getDbConfig();
			Connection con = Transaction.getInstance(config).getConnection();

			// �u���b�N�T�C�Y�Z�o
			if (blockSize == 0) {
				blockSize = OracleDbBlockSizeSearcher.execute(con);
			}
//			log.debug("�u���b�N�T�C�Y:" + this.blockSize); //$NON-NLS-1$
			// �J�����̈�̎擾
			OracleColumnSizeUtil cs = new OracleColumnSizeUtil();
			int columnAreaSize = cs.getRowLength(con, columns);

			// ���S�W���Ȃ�
			this.tableSpaceSize = new BigDecimal((getNecessaryBlockSize(columns, columnAreaSize, maxRecord) * blockSize) / (1024d * 1024d));
			// �؂�グ
			// this.tableSpaceSize = this.tableSpaceSize.setScale(1,
			// BigDecimal.ROUND_UP); // �؏グ
			this.tableSpaceSize = this.tableSpaceSize.setScale(3, BigDecimal.ROUND_UP); // �؏グ

			// ���S�W����
			this.tableSpaceSafeSize = this.tableSpaceSize.multiply(new BigDecimal(safeCoefficient));
			// �؂�グ
			// this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(1,
			// BigDecimal.ROUND_UP); // �؏グ
			this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(3, BigDecimal.ROUND_UP); // �؏グ

//			log.debug("�K�v�ȃT�C�Y(MB)���擾:" + this.tableSpaceSafeSize); //$NON-NLS-1$

		} catch (Exception e) {
			throw e;

		}

	}

	/**
	 * <����>1.�u���b�N�w�b�_�[ �Œ蒷�w�b�_�[�{�ϒ��g�����U�N�V�����E�w�b�_(24*INITRANS) �\�Ƃ͈قȂ�A�����ł�INITRNAS��2
	 * 
	 * @return
	 */
	public double getBlockHeaderSize() {
		double d = 113 + 24 * 2;
//		log.debug("1.�u���b�N�w�b�_�[:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * <����>2.�u���b�N������̗��p�\�̈�̎擾
	 * 
	 * @return
	 */
	private final double getRiyouKanouArea() {
		double d = Math.ceil((blockSize - getBlockHeaderSize()) * (1 - pctFree / 100d));
//		log.debug("2.���p�\�̈�̎擾:" + d); //$NON-NLS-1$
		return d;

	}

	/**
	 * <����>3.�S�̂̍����l�T�C�Y�̌v�Z
	 * 
	 * @return
	 */
	private double getIndexValueSize(IColumn[] columns, int columnAreaSize) {

		int entryHeader = 2;
		int rowId = 6;
		int f = 0; // 127�o�C�g�ȉ��̃f�[�^���i�[�����
		int v = 0; // 128�o�C�g�ȉ��̃f�[�^���i�[�����

		for (int i = 0; i < columns.length; i++) {
			IColumn column = columns[i];
			if (column.getColumn_length() < 128) {
				f++;
			} else {
				v++;
			}

		}
		double d = entryHeader + rowId + (f * 1 + v * 2) + columnAreaSize;
//		log.debug("3.�S�̂̍����l�T�C�Y�̌v�Z:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * <����>4.�u���b�N������̕��ύ����G���g�����̌v�Z
	 * 
	 * @return
	 */
	private final double getAverageRowCountOfBlock(IColumn[] columns, int columnAreaSize) throws CalcTableSpaceException {
		double d = Math.floor(getRiyouKanouArea() / getIndexValueSize(columns, columnAreaSize));
//		log.debug("4.�u���b�N������̕��ύs���̌v�Z:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * <����>5.�K�v�ȃu���b�N��
	 * 
	 * @param totalRow
	 * @return
	 */
	private final double getNecessaryBlockSize(IColumn[] columns, int columnAreaSize, long totalRow) throws CalcTableSpaceException {
		double d = Math.ceil(1.05 * totalRow / getAverageRowCountOfBlock(columns, columnAreaSize));
//		log.debug("5.�K�v�ȃu���b�N��:" + d); //$NON-NLS-1$
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

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[CalcIndexSpace:"); //$NON-NLS-1$
		buffer.append(" block_header: "); //$NON-NLS-1$
		buffer.append(block_header);
		buffer.append(" safeCoefficient: "); //$NON-NLS-1$
		buffer.append(safeCoefficient);
		buffer.append(" maxRecord: "); //$NON-NLS-1$
		buffer.append(maxRecord);
		buffer.append(" table: "); //$NON-NLS-1$
		buffer.append(table);
		buffer.append(" { "); //$NON-NLS-1$
		for (int i0 = 0; columns != null && i0 < columns.length; i0++) {
			buffer.append(" columns[" + i0 + "]: "); //$NON-NLS-1$ //$NON-NLS-2$
			buffer.append(columns[i0]);
		}
		buffer.append(" } "); //$NON-NLS-1$
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

	public String getCalcResult() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" [�����̈�]:" + indexName + " "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			OracleIndexColumn column = columns[i];
			if (i == 0) {
				sb.append(column.getColumn_name());
			} else {
				sb.append(", " + column.getColumn_name()); //$NON-NLS-1$
			}
		}
		sb.append(")"); //$NON-NLS-1$
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
		list.add(indexName); // Index��

		list.add(String.valueOf(blockSize));
		list.add(String.valueOf(pctFree));

		list.add(String.valueOf(maxRecord));
		list.add(String.valueOf(tableSpaceSize));
		list.add(String.valueOf(safeCoefficient));
		list.add(String.valueOf(tableSpaceSafeSize));
		return list;
	}

	public int getBlock_header() {
		return block_header;
	}

	public OracleIndexColumn[] getColumns() {
		return columns;
	}

	public String getIndexName() {
		return indexName;
	}

	public long getMaxRecord() {
		return maxRecord;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public int getPctFree() {
		return pctFree;
	}

	public ITable getTable() {
		return table;
	}

	public String getTableName() {
		return tableName;
	}
}
