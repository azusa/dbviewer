/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;

/**
 * OracleTableCommentSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class OracleTableCommentsSearcher {

	public static Map execute(IDBConfig config, String owner) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner);

		} catch (Exception e) {
			throw e;
		}
	}

	public static Map execute(Connection con, String owner) throws Exception {
		Map result = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner));

			result = new HashMap();
			while (rs.next()) {
				OracleCommentInfo info = new OracleCommentInfo();
				info.setSchemaName(owner);
				info.setTableName(rs.getString("TABLE_NAME"));
				info.setRemarks(rs.getString("COMMENTS"));
				result.put(info.getTableName(), info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return result;
	}

	// Oracle用SQL
	private static String getSQL(String owner) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TABLE_NAME, COMMENTS"); //$NON-NLS-1$
		sb.append(" FROM ALL_TAB_COMMENTS"); //$NON-NLS-1$
		sb.append(" WHERE OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

}
