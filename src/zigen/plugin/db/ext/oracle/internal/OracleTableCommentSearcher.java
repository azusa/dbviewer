/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.internal;


/**
 * OracleTableCommentSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class OracleTableCommentSearcher {

	// public static String execute(IDBConfig config, String owner, String table) throws Exception {
	// try {
	// Connection con = Transaction.getInstance(config).getConnection();
	// return execute(con, owner, table);
	//
	// } catch (Exception e) {
	// throw e;
	// }
	// }
	//
	// public static String execute(Connection con, String owner, String table) throws Exception {
	// ResultSet rs = null;
	// Statement st = null;
	// try {
	// st = con.createStatement();
	// rs = st.executeQuery(getSQL(owner, table));
	//
	// if (rs.next()) {
	// return rs.getString("COMMENTS"); //$NON-NLS-1$
	// }
	// return null;
	//
	// } catch (Exception e) {
	// DbPlugin.log(e);
	// throw e;
	// } finally {
	// ResultSetUtil.close(rs);
	// StatementUtil.close(st);
	// }
	//
	// }
	//
	// // Oracle用SQL
	// private static String getSQL(String owner, String table) {
	// StringBuffer sb = new StringBuffer();
	// sb.append("SELECT COMMENTS"); //$NON-NLS-1$
	// sb.append(" FROM ALL_TAB_COMMENTS"); //$NON-NLS-1$
	// sb.append(" WHERE OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	// sb.append(" AND TABLE_NAME = '" + SQLUtil.encodeQuotation(table) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	// return sb.toString();
	// }
	
}
