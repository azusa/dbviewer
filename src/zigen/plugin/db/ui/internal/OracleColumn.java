/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;

/**
 * Column�N���X.
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
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public OracleColumn(TableColumn column) {
		super(column);
	}

	/**
	 * �R���X�g���N�^
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
	 * ���O�̎擾
	 * 
	 * @return
	 */
	public String getColumnLabel() {
		StringBuffer sb = new StringBuffer();

		sb.append(column.getColumnName());
		sb.append(" ");
		sb.append(column.getTypeName().toLowerCase());

		// modify Oracle�̃p�����[�^�����̃J�����Ή� �� NUMBER�^
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

		// �J�����̃R�����gON
		if (DbPlugin.getDefault().getPreferenceStore().getBoolean(DBTreeViewPreferencePage.P_DISPLAY_COL_COMMENT)) {

			// remarks�ɒl������Βǉ�����
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
