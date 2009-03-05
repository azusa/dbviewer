/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.symfoware;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

/**
 *
 * SymfowareSQLCreatorFactory.javaクラス.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/07 ZIGEN create.
 *
 */
public class SymfowareSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public SymfowareSQLCreatorFactory(ITable table) {
		super(table);
	}

	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ");
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

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

		/*
		 * if (table.getDbConfig().isNoLockMode()) { if (!sb.toString().trim().endsWith("WITH OPTION LOCK_MODE(NL)")) { sb.append(" WITH OPTION LOCK_MODE(NL)"); } }
		 */
		// テーブル編集エディターからの更新、削除がロックするため、以下を追加する
		if (!sb.toString().trim().endsWith("WITH OPTION LOCK_MODE(NL)")) {
			sb.append(" WITH OPTION LOCK_MODE(NL)");
		}

		// sb.append(" LIMIT " + limit+1); //ダイアログを出す為に＋１

		return sb.toString();
	}

	public String createCountAll(String condition) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) FROM ");
		sb.append(table.getSqlTableName());

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}
		sb.append(" WITH OPTION LOCK_MODE(NL)");

		return sb.toString();
	}

	public String createCountForQuery(String query) {
		StringBuffer sb = new StringBuffer();
		sb.append(super.createCountForQuery(query));
		sb.append(" WITH OPTION LOCK_MODE(NL)");
		return sb.toString();
	}

	protected String getCreateView() {
		StringBuffer wk = new StringBuffer();
		try {
			boolean onPatch = DbPlugin.getDefault().getPreferenceStore().getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
			int type = DbPlugin.getDefault().getPreferenceStore().getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);

			// Symfowareでは、CREATE OR REPLACE は無い
			// wk.append("CREATE OR REPLACE VIEW "); //$NON-NLS-1$
			wk.append("CREATE VIEW "); //$NON-NLS-1$
			wk.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

			wk.append(DbPluginConstant.LINE_SEP);

			wk.append("(");
			for (int i = 0; i < cols.length; i++) {
				Column col = cols[i];
				if (i > 0) {
					wk.append(",");
				}
				wk.append(col.getName());
				wk.append(DbPluginConstant.LINE_SEP);
			}

			wk.append(")");
			wk.append(DbPluginConstant.LINE_SEP);
			wk.append("AS"); //$NON-NLS-1$
			wk.append(DbPluginConstant.LINE_SEP);

			wk.append(getViewDDL(table.getDbConfig(), table.getSchemaName(), table.getName()));

			StringBuffer sb = new StringBuffer();
			sb.append(SQLFormatter.format(wk.toString(), type, onPatch));
			setDemiliter(sb);

			return sb.toString();
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return null;

	}

	protected String getViewDDL_SQL(String owner, String view) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        D.DESC_VALUE");
		sb.append("    FROM");
		sb.append("        RDBII_SYSTEM.RDBII_TABLE T");
		sb.append("        ,RDBII_SYSTEM.RDBII_DESCRIPTION D");
		sb.append("    WHERE");
		sb.append("        T.TABLE_CODE = D.OBJECT_CODE");
		sb.append("        AND T.SCHEMA_NAME = '" + SQLUtil.encodeQuotation(owner) + "'");
		sb.append("        AND T.TABLE_NAME = '" + SQLUtil.encodeQuotation(view) + "'");
		sb.append("        AND T.TABLE_TYPE = 'VW'");
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
		// TODO 自動生成されたメソッド・スタブ
		return null;
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

	public String VisibleColumnSizePattern() {
		return "^CHAR|^VARCHAR.*|^NCHAR.*|^NATIONAL.*|^NUMERIC|^DEC.*|^FLOAT|^BLOB|^INTERVAL.*"; //$NON-NLS-1$
	}
}
