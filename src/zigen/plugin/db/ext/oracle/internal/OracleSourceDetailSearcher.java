/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;

/**
 * OracleViewSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class OracleSourceDetailSearcher {

	public static OracleSourceDetailInfo execute(IDBConfig config, String owner, String name, String type, boolean visibleSchema) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, name, type, visibleSchema);

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleSourceDetailInfo execute(Connection con, String owner, String name, String type, boolean visibleSchema) throws Exception {
		ResultSet rs = null;
		Statement st = null;

		OracleSourceDetailInfo info = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, name, type));

			StringBuffer sb = new StringBuffer();

			int i = 0;
			while (rs.next()) {

				if (i == 0) {
					info = new OracleSourceDetailInfo();
					info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
					info.setName(rs.getString("NAME")); //$NON-NLS-1$
					info.setType(rs.getString("TYPE")); //$NON-NLS-1$

					String str = rs.getString("TEXT");//$NON-NLS-1$
					
					int pos = str.toUpperCase().indexOf(info.getName().toUpperCase()); // ソース名のポジションを取得
					
					if(visibleSchema){
						sb.append("CREATE OR REPLACE ").append(info.getType());
						sb.append(" ").append(info.getOwner()).append(".");
						sb.append(str.substring(pos));

					}else{
						sb.append("CREATE OR REPLACE ").append(info.getType());
						sb.append(" ").append(str.substring(pos));
					}


				} else {
					sb.append(rs.getString("TEXT")); //$NON-NLS-1$
				}
				i++;
			}

			if (info != null) {
				// StringBufferに入れた文字列を設定する
				info.setText(sb.toString());
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return info;

	}

	// Oracle用SQL
	private static String getSQL(String owner, String name, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         OWNER"); //$NON-NLS-1$
		sb.append("         ,NAME"); //$NON-NLS-1$
		sb.append("         ,TYPE"); //$NON-NLS-1$
		sb.append("         ,LINE"); //$NON-NLS-1$
		sb.append("         ,TEXT"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         ALL_SOURCE"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("         AND NAME = '" + SQLUtil.encodeQuotation(name) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("         AND TYPE = '" + SQLUtil.encodeQuotation(type) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ORDER BY LINE"); //$NON-NLS-1$

		return sb.toString();

	}

}
