/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.sqlserver;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

/**
 * 
 * OracleInsertFactory.java�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/07 ZIGEN create.
 * 
 */
public class SQLServerSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public SQLServerSQLCreatorFactory(ITable table) {
		super(table);
	}

	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();

		if (limit > 0) {
			sb.append("SELECT TOP ");
			sb.append(++limit);
		} else {
			sb.append("SELECT ");
		}

		sb.append(" * FROM ");
		sb.append(table.getSqlTableName());

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}

		return sb.toString();
	}

	public String[] createAddColumnDDL(Column column) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	public String createCommentOnColumnDDL(Column column) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	public String createCommentOnTableDDL(String commnets) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	public String createRenameColumnDDL(Column from, Column to) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("sp_rename");
		sb.append(" '" + SQLUtil.encodeQuotation(table.getSqlTableName()) + "'");
		sb.append("  ,'" + SQLUtil.encodeQuotation(newTableName) + "'");
		return sb.toString();
	}

	public boolean supportsModifyColumnSize(String columnType) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	public boolean supportsModifyColumnType() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	public boolean supportsRemarks() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	// SQLServer=true
	public boolean supportsRollbackDDL() {
		return true;
	}
}
