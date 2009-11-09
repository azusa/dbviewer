/*
 * Copyright (c) 2007－2009 ZIGEN
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
 * CalcTableSpaceクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/01 ZIGEN create.
 * 
 */
public class CalcTableSpace {

	/**
	 * 変更可能プロパティ calcurateメソッド実行前に値をSetすること
	 */
	int block_header = 100; // 100バイト

	double safeCoefficient = 1.2; // 安全係数

	/**
	 * コンストラクタで値を設定するプロパティ
	 */
	private final long maxRecord; // レコード件数

	private final Table table; // 対象テーブル

	private final String ownerName; // 所有者

	private final String tableName; // テーブル名

	private final int pctFree; // pctfree

	/**
	 * DB_BLOCK_SIZE
	 */
	private int blockSize;

	/**
	 * 計算結果取得用(安全係数なし）
	 */
	private BigDecimal tableSpaceSize;

	/**
	 * 計算結果取得用（安全係数をかけたもの）
	 */
	private BigDecimal tableSpaceSafeSize;

	/**
	 * 安全係数をかけた表領域の見積りサイズ
	 * 
	 * @return tableSpaceSafeSize を戻します。
	 */
	public BigDecimal getTableSpaceSafeSize() {
		return tableSpaceSafeSize;
	}

	/**
	 * 表領域の見積りサイズ
	 * 
	 * @return tableSpaceSize を戻します。
	 */
	public BigDecimal getTableSpaceSize() {
		return tableSpaceSize;
	}

	/**
	 * コンストラクタ
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
	 * コンストラクタ
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
	 * 計算メソッド
	 */
	public void calcurate() throws CalcTableSpaceException {
		try {
			IDBConfig config = table.getDbConfig();
			Connection con = Transaction.getInstance(config).getConnection();

			// ブロックサイズ算出
			if (blockSize == 0) {
				blockSize = OracleDbBlockSizeSearcher.execute(con);
			}
			// log.debug("ブロックサイズ:" + this.blockSize); //$NON-NLS-1$

			// 必要なBlock数
			double necessaryBlock = getNecessaryBlockSize(con, maxRecord);

			// KB, MBの判定
			// log.debug("表領域のサイズ：" + tableSpaceSize);

			// 安全係数なし
			// this.tableSpaceSize = new BigDecimal((getNecessaryBlockSize(con,
			// maxRecord) * blockSize) / (1024d * 1024d));
			this.tableSpaceSize = new BigDecimal((necessaryBlock * blockSize) / (1024d * 1024d));

			// 切り上げ
			// this.tableSpaceSize = this.tableSpaceSize.setScale(1,
			// BigDecimal.ROUND_UP); // 切上げ
			this.tableSpaceSize = this.tableSpaceSize.setScale(3, BigDecimal.ROUND_UP); // 切上げ

			// 安全係数後
			this.tableSpaceSafeSize = tableSpaceSize.multiply(new BigDecimal(safeCoefficient));
			// 切り上げ
			// this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(1,
			// BigDecimal.ROUND_UP); // 切上げ
			this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(3, BigDecimal.ROUND_UP); // 切上げ

			// log.debug("必要なサイズ(MB)を取得:" + tableSpaceSafeSize); //$NON-NLS-1$

		} catch (CalcTableSpaceException e) {
			throw e;

		} catch (Exception e) {
			throw new CalcTableSpaceException("表領域の見積り処理でエラーが発生しました", e.getCause()); //$NON-NLS-1$

		}

	}

	/**
	 * 1.ブロックヘッダー領域の取得
	 * 
	 * @return
	 */
	public int getBlockHeaderSize() {
		// log.debug("1.ブロックヘッダー領域の取得:" + this.block_header); //$NON-NLS-1$
		return this.block_header;
	}

	/**
	 * 2.利用可能領域の取得
	 * 
	 * @return
	 */
	private final double getRiyouKanouArea() {
		double d = Math.ceil((blockSize - this.getBlockHeaderSize()) * (1 - pctFree / 100d));
		// log.debug("2.利用可能領域の取得:" + d); //$NON-NLS-1$
		return d;

	}

	/**
	 * 3.平均行サイズの取得
	 * 
	 * @return
	 */
	private final int getRowLength(Connection con) {
		int columnAreaSize = -1;
		try {

			// RowLengthを取得するだけなので、convertUnicodeはfalse固定
			boolean convertUnicode = false;

			OracleTableColumn[] columns = OracleTableColumnSearcher.execute(con, table, convertUnicode);

			// カラム領域の取得
			OracleColumnSizeUtil cs = new OracleColumnSizeUtil();
			columnAreaSize = cs.getRowLength(con, columns);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

		// log.debug("3.平均行サイズの取得:" + columnAreaSize); //$NON-NLS-1$
		return columnAreaSize;
	}

	/**
	 * 4.ブロックあたりの平均行数の計算
	 * 
	 * @return
	 */
	private final double getAverageRowCountOfBlock(Connection con) throws CalcTableSpaceException {
		// double d = Math.floor(getRiyouKanouArea() / getRowLength(con));

		// modify start 2006/09/23 利用可能領域の方が小さい場合を考慮 ZIGEN
		double d = 0.0d;
		double riyoukanou = getRiyouKanouArea();
		double rowLen = getRowLength(con);

		if (riyoukanou >= rowLen) {
			// 切捨て
			d = Math.floor(riyoukanou / rowLen); // 通常は利用可能領域の方が大きい
		} else {
			// 利用可能領域の方が小さい場合の処理を追加
			d = 1.0d / Math.ceil(rowLen / riyoukanou);

		}
		// modify end
		// log.debug("4.ブロックあたりの平均行数の計算:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * 必要なブロック数
	 * 
	 * @param totalRow
	 * @return
	 */
	private final double getNecessaryBlockSize(Connection con, long totalRow) throws CalcTableSpaceException {
		double d = Math.ceil(totalRow / getAverageRowCountOfBlock(con));
		// log.debug("5.必要なブロック数:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * @param block_header
	 *            block_header を設定。
	 */
	public void setBlock_header(int block_header) {
		this.block_header = block_header;
	}

	/**
	 * @param blockSize
	 *            blockSize を設定。
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * @param safeCoefficient
	 *            safeCoefficient を設定。
	 */
	public void setSafeCoefficient(double safeCoefficient) {
		this.safeCoefficient = safeCoefficient;
	}

	/**
	 * @return safeCoefficient を戻します。
	 */
	public double getSafeCoefficient() {
		return safeCoefficient;
	}

	/**
	 * @return blockSize を戻します。
	 */
	public int getBlockSize() {
		return blockSize;
	}

	public String getCalcResult() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" [テーブル領域]:" + tableName + " "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		sb.append("   見積りサイズ:"); //$NON-NLS-1$
		sb.append("  " + getTableSpaceSize() + " MB"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		sb.append("   見積りサイズ×" + getSafeCoefficient() + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("  " + getTableSpaceSafeSize() + " MB"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		return sb.toString();
	}

	public List getCsvRow() {
		List list = new ArrayList();

		list.add(ownerName);
		list.add(tableName);
		list.add(""); // Index名 //$NON-NLS-1$

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
		sb.append(""); // indexNameはなし //$NON-NLS-1$
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
