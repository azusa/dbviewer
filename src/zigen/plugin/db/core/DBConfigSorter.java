/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * DBConfigSorterクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/05/23 ZIGEN create.
 * 
 */
public class DBConfigSorter implements Comparator {
	boolean isDesc = false;

	public DBConfigSorter() {
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		IDBConfig first = (IDBConfig) o1;
		IDBConfig second = (IDBConfig) o2;

		// ----------------------------------------------------
		// 比較するフィールドを取得する
		// ----------------------------------------------------
		String v1 = first.getDbName(); // 比較する値１
		String v2 = second.getDbName(); // 比較する値２

		try {
			// 数値変換できる場合は、数値比較を行う
			BigDecimal d1 = new BigDecimal(v1); // 数値変換
			BigDecimal d2 = new BigDecimal(v2); // 数値変換

			if (d1.doubleValue() < d2.doubleValue()) {
				if (isDesc) {
					return (1);
				} else {
					return (-1);
				}
			} else if (d2.doubleValue() < d1.doubleValue()) {
				if (isDesc) {
					return (-1);
				} else {
					return (1);
				}
			} else {
				return (0);
			}

		} catch (NumberFormatException ex) {
			// 数値変換できない場合は、文字列比較を行う
			if (isDesc) {
				return (v2.compareTo(v1)); // 降順
			} else {
				return (v1.compareTo(v2)); // 昇順
			}

		}
	}

}
