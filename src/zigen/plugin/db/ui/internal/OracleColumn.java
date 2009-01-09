/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;

/**
 * Columnクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class OracleColumn extends Column {

	private static final long serialVersionUID = 1L;

	public OracleColumn() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public OracleColumn(TableColumn column) {
		super(column);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public OracleColumn(TableColumn column, TablePKColumn pkColumn, TableFKColumn[] fkColumns) {
		super(column, pkColumn, fkColumns);
	}

	public void update(OracleColumn node) {
		super.update(node);
	}

	/**
	 * 名前の取得
	 * 
	 * @return
	 */
	public String getColumnLabel() {
		StringBuffer sb = new StringBuffer();

		sb.append(column.getColumnName());
		sb.append(" ");
		sb.append(column.getTypeName().toLowerCase());

		// modify Oracleのパラメータ無しのカラム対応 例 NUMBER型
		// if (isVisibleColumnSize()) {
		if (isVisibleColumnSize() && !column.isWithoutParam()) {
			if (column.getDecimalDigits() == 0) {
				sb.append("(" + column.getColumnSize() + ")");
			} else {
				sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
			}
		}
		if (pkColumn != null) {
			sb.append(" PK");
		}
		if (fkColumns != null && fkColumns.length > 0) {
			sb.append(" FK");
		}

		// カラムのコメントON
		if (DbPlugin.getDefault().getPreferenceStore().getBoolean(DBTreeViewPreferencePage.P_DISPLAY_COL_COMMENT)) {

			// remarksに値があれば追加する
			if (column.getRemarks() != null && column.getRemarks().length() > 0) {
				sb.append(" [");
				sb.append(column.getRemarks());
				sb.append("]");
			}

		}
		// debug dataType
		// sb.append(" (DataType:" + column.getDataType() + ")");

		return sb.toString();

	}

}
