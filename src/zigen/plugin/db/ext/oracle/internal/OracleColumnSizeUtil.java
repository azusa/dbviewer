/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;

import zigen.plugin.db.ext.oracle.tablespace.IColumn;

/**
 * OracleColumnSizeUtilクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/1 ZIGEN create.
 * 
 */
public class OracleColumnSizeUtil {

	private OracleTypeSizeUtil ts;

	public OracleColumnSizeUtil() {}

	/**
	 * 行あたりに使用される領域サイズ
	 * 
	 * @param tableColumns
	 * @return
	 * @throws Exception
	 */
	public int getRowLength(Connection con, IColumn[] columns) throws Exception {
		this.ts = new OracleTypeSizeUtil(con);

		// UB1*3+UB4+SB2
		int a = ts.getInt(OracleTypeSizeUtil.UB1) * 3 + ts.getInt(OracleTypeSizeUtil.UB4) + +ts.getInt(OracleTypeSizeUtil.SB2);

		// 索引行サイズ
		int b = sumColumnSize(columns);

		// Max(a, b) aとbと大きい方を採用しSB2を加算する
		int out = 0;
		if (a > b) {
			out = a + ts.getInt(OracleTypeSizeUtil.SB2);
		} else {
			out = b + ts.getInt(OracleTypeSizeUtil.SB2);
		}

		return out;
	}

	/**
	 * オーバヘッドの取得
	 * 
	 * @param length
	 * @return
	 */
	private int getOverHead(int length) {
		if (length <= 255) {
			return 1;
		} else {
			return 3;
		}
	}

	/**
	 * 列データサイズ取得
	 * 
	 * @param columnType
	 * @param length
	 * @return
	 */
	private int getColumnSize(String columnType, int length) {
		String type = columnType.toUpperCase();
		if ("CHAR".equals(type)) { //$NON-NLS-1$
			return length;
		} else if ("VARCHAR2".equals(type)) { //$NON-NLS-1$
			return length;
		} else if ("NUMBER".equals(type)) { //$NON-NLS-1$
			return (1 + (int) Math.ceil(length / 2)) + 1;
		} else if ("DATE".equals(type)) { //$NON-NLS-1$
			return 7;
		} else {
			throw new IllegalStateException("サポートされていない型です 型:" + columnType); //$NON-NLS-1$
		}

	}

	/**
	 * バイト長を含む列データ領域サイズ
	 * 
	 * @param columnType
	 * @param length
	 * @return
	 */
	private int getColumnSizeAddOverHead(String columnType, int length) {
		int normal = getColumnSize(columnType, length);
		int out = normal + getOverHead(normal);
		// log.debug("1.列データサイズの取得:" + out);
		return out;
	}

	/**
	 * 行サイズ
	 * 
	 * @param indexColumns
	 * @return
	 * @throws Exception
	 */
	private int sumColumnSize(IColumn[] columns) throws Exception {
		// 行ヘッダー(3*UB1)
		int columnHeader = 3 * ts.getInt(OracleTypeSizeUtil.UB1);

		int sum = 0;
		for (int i = 0; i < columns.length; i++) {
			IColumn column = columns[i];
			// log.debug(column.getColumn_name() + ":" + column.getColumn_type()
			// + "("+ column.getColumn_length() + ")");
			sum = sum + getColumnSizeAddOverHead(column.getColumn_type(), column.getColumn_length());
		}
		int out = columnHeader + sum;
		// log.debug("2.行サイズ " + out);
		return out;

	}

}
