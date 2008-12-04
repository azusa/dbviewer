/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
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
 * CalcIndexSpaceクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/01 ZIGEN create.
 * 
 */
public class CalcIndexSpace {

	/**
	 * 変更可能プロパティ calcurateメソッド実行前に値をSetすること
	 */
	int block_header = 100; // 100バイト

	double safeCoefficient = 2; // 安全係数

	/**
	 * コンストラクタで値を設定するプロパティ
	 */
	private final long maxRecord; // 予測件数

	private final Table table;

	private final OracleIndexColumn[] columns; // 索引となるカラム

	private final String ownerName;

	private final String tableName;

	private final String indexName;

	private final int pctFree;

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
	 * コンストラクタ
	 * 
	 * @param table
	 * @param blockSize
	 *            任意のDB_BLOCK_SIZEを指定する
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
	 * 計算メソッド
	 */
	public void calcurate() throws Exception {
		try {

			IDBConfig config = table.getDbConfig();
			Connection con = Transaction.getInstance(config).getConnection();

			// ブロックサイズ算出
			if (blockSize == 0) {
				blockSize = OracleDbBlockSizeSearcher.execute(con);
			}
//			log.debug("ブロックサイズ:" + this.blockSize); //$NON-NLS-1$
			// カラム領域の取得
			OracleColumnSizeUtil cs = new OracleColumnSizeUtil();
			int columnAreaSize = cs.getRowLength(con, columns);

			// 安全係数なし
			this.tableSpaceSize = new BigDecimal((getNecessaryBlockSize(columns, columnAreaSize, maxRecord) * blockSize) / (1024d * 1024d));
			// 切り上げ
			// this.tableSpaceSize = this.tableSpaceSize.setScale(1,
			// BigDecimal.ROUND_UP); // 切上げ
			this.tableSpaceSize = this.tableSpaceSize.setScale(3, BigDecimal.ROUND_UP); // 切上げ

			// 安全係数後
			this.tableSpaceSafeSize = this.tableSpaceSize.multiply(new BigDecimal(safeCoefficient));
			// 切り上げ
			// this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(1,
			// BigDecimal.ROUND_UP); // 切上げ
			this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(3, BigDecimal.ROUND_UP); // 切上げ

//			log.debug("必要なサイズ(MB)を取得:" + this.tableSpaceSafeSize); //$NON-NLS-1$

		} catch (Exception e) {
			throw e;

		}

	}

	/**
	 * <索引>1.ブロックヘッダー 固定長ヘッダー＋可変長トランザクション・ヘッダ(24*INITRANS) 表とは異なり、索引ではINITRNASは2
	 * 
	 * @return
	 */
	public double getBlockHeaderSize() {
		double d = 113 + 24 * 2;
//		log.debug("1.ブロックヘッダー:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * <索引>2.ブロックあたりの利用可能領域の取得
	 * 
	 * @return
	 */
	private final double getRiyouKanouArea() {
		double d = Math.ceil((blockSize - getBlockHeaderSize()) * (1 - pctFree / 100d));
//		log.debug("2.利用可能領域の取得:" + d); //$NON-NLS-1$
		return d;

	}

	/**
	 * <索引>3.全体の索引値サイズの計算
	 * 
	 * @return
	 */
	private double getIndexValueSize(IColumn[] columns, int columnAreaSize) {

		int entryHeader = 2;
		int rowId = 6;
		int f = 0; // 127バイト以下のデータを格納する列数
		int v = 0; // 128バイト以下のデータを格納する列数

		for (int i = 0; i < columns.length; i++) {
			IColumn column = columns[i];
			if (column.getColumn_length() < 128) {
				f++;
			} else {
				v++;
			}

		}
		double d = entryHeader + rowId + (f * 1 + v * 2) + columnAreaSize;
//		log.debug("3.全体の索引値サイズの計算:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * <索引>4.ブロックあたりの平均索引エントリ数の計算
	 * 
	 * @return
	 */
	private final double getAverageRowCountOfBlock(IColumn[] columns, int columnAreaSize) throws CalcTableSpaceException {
		double d = Math.floor(getRiyouKanouArea() / getIndexValueSize(columns, columnAreaSize));
//		log.debug("4.ブロックあたりの平均行数の計算:" + d); //$NON-NLS-1$
		return d;
	}

	/**
	 * <索引>5.必要なブロック数
	 * 
	 * @param totalRow
	 * @return
	 */
	private final double getNecessaryBlockSize(IColumn[] columns, int columnAreaSize, long totalRow) throws CalcTableSpaceException {
		double d = Math.ceil(1.05 * totalRow / getAverageRowCountOfBlock(columns, columnAreaSize));
//		log.debug("5.必要なブロック数:" + d); //$NON-NLS-1$
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
		sb.append(" [索引領域]:" + indexName + " "); //$NON-NLS-1$ //$NON-NLS-2$
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
		list.add(indexName); // Index名

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
