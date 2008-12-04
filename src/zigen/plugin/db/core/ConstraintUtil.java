/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.util.ArrayList;
import java.util.List;

/**
 * ConstraintSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class ConstraintUtil {

	/**
	 * 指定したカラムがPKがどうか判定
	 * 
	 * @param pks
	 * @param columnName
	 * @return
	 */
	public static boolean isPKColumn(TablePKColumn[] pks, String columnName) {
		if (pks == null)
			return false;
		for (int i = 0; i < pks.length; i++) {
			if (pks[i].getColumnName().equals(columnName)) {
				return true;
			}
		}
		return false;
	}

	public static TableIDXColumn[] getFirstUniqueIndex(TableIDXColumn[] indxs) {

		if (indxs != null && indxs.length > 0) {
			List result = new ArrayList(indxs.length);
			String constraintName = indxs[0].getName(); // 最初の制約名を使用する

			for (int i = 0; i < indxs.length; i++) {
				TableIDXColumn indx = indxs[i];
				if (constraintName.equals(indx.getName())) {
					result.add(indx);
				} else {
					break;
				}
			}
			return (TableIDXColumn[]) result.toArray(new TableIDXColumn[0]);

		} else {
			return null;
		}

	}

	/**
	 * 指定したカラムがユニークINDEXがどうか判定
	 * 
	 * @param uniqueIndexs
	 * @param columnName
	 * @return
	 */
	public static boolean isUniqueIDXColumn(TableIDXColumn[] uniqueIndexs, String columnName) {
		if (uniqueIndexs == null) {
			return false;
		}
		for (int i = 0; i < uniqueIndexs.length; i++) {
			if (uniqueIndexs[i].getColumnName().equals(columnName)) {
				return !uniqueIndexs[i].isNonUnique();
			}
		}

		return false;
	}


}
