/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.sqlserver;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

/**
 * 
 * OracleInsertFactory.javaクラス.
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
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String createCommentOnColumnDDL(Column column) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String createCommentOnTableDDL(String commnets) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String createRenameColumnDDL(Column from, Column to) {
		// TODO 自動生成されたメソッド・スタブ
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
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public boolean supportsModifyColumnType() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public boolean supportsRemarks() {
		// TODO 自動生成されたメソッド・スタブ
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
