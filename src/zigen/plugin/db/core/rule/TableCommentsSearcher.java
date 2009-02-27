/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;

/**
 * TableCommentSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class TableCommentsSearcher {
	
	public static Map execute(IDBConfig config, String owner) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static Map execute(Connection con, String owner) throws Exception {
		Map result = new HashMap();
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			
			String query = getSQL(con.getMetaData(), owner);
			if (query != null) {
				rs = st.executeQuery(query);
				while (rs.next()) {
					TableComment info = new TableComment();
					// info.setSchemaName(owner);
					// info.setTableName(rs.getString("TABLE_NAME"));
					// info.setRemarks(rs.getString("COMMENTS"));
					info.setTableName(rs.getString(1));
					info.setRemarks(rs.getString(2));
					result.put(info.getTableName(), info);
				}
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
	private static String getSQL(DatabaseMetaData objMet, String owner) {
		StringBuffer sb = new StringBuffer();
		switch (DBType.getType(objMet)) {
			case DBType.DB_TYPE_ORACLE:
				sb.append("SELECT TABLE_NAME, COMMENTS"); //$NON-NLS-1$
				sb.append(" FROM ALL_TAB_COMMENTS"); //$NON-NLS-1$
				sb.append(" WHERE OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				return sb.toString();
			case DBType.DB_TYPE_SYMFOWARE:

				sb.append("SELECT");
				sb.append("        TRIM(T.TABLE_NAME) TABLE_NAME");
				sb.append("        ,COM.COMMENT_VALUE");
				sb.append("    FROM");
				sb.append("        RDBII_SYSTEM.RDBII_TABLE T");
				sb.append("        ,RDBII_SYSTEM.RDBII_COMMENT COM");
				sb.append("        WHERE ");
				sb.append("         T.DB_CODE = COM.DB_CODE");
				sb.append("         AND T.SCHEMA_CODE = COM.SCHEMA_CODE");
				sb.append("         AND T.TABLE_CODE = COM.TABLE_CODE");
				sb.append("         AND COM.COMMENT_TYPE = 'TV'");
				sb.append("        AND T.DB_NAME = '" + getDBName(objMet) + "'");
				sb.append("        AND T.SCHEMA_NAME = '" + SQLUtil.encodeQuotation(owner) + "'");
				
				return sb.toString();
				
			default:
				break;
		}
		return null;
	}
	
	public static String getDBName(DatabaseMetaData objMet) {
		String name = null;
		try {
			switch (DBType.getType(objMet)) {
				case DBType.DB_TYPE_SYMFOWARE:
					String url = objMet.getURL();
					String[] wk = url.split("/");
					if (wk.length >= 4) {
						String s = wk[3];
						int index = s.indexOf(';'); // パラメータの区切り文字
						if (index >= 0) {
							name = s.substring(0, index);
							// の前までを使う
						} else {
							name = s;
						}
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
}
