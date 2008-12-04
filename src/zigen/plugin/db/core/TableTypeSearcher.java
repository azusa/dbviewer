/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TableTypeSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/18 ZIGEN create.
 */
public class TableTypeSearcher {

	public static String[] execute(IDBConfig config) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con);

		} catch (Exception e) {
			throw e;
		}

	}

	public static String[] execute(Connection con) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();
			rs = objMet.getTableTypes();

			list = new ArrayList();
			while (rs.next()) {
				// list.add(rs.getString("TABLE_TYPE"));
				// 2006.02.02 zigen sybase用にtrimして格納
				list.add(rs.getString("TABLE_TYPE").trim()); //$NON-NLS-1$

			}

			switch (DBType.getType(con.getMetaData())) {
			case DBType.DB_TYPE_ORACLE:
				list.add("SEQUENCE"); //$NON-NLS-1$
				//ここを追加する場合は、コード補完の場合の処理を見直すこと。
				//list.add("INDEX"); // ADD //$NON-NLS-1$
				// コード補完で、Functionも表示する
				list.add("FUNCTION"); //$NON-NLS-1$
				break;

			default:
				break;
			}
			return (String[]) list.toArray(new String[0]);

		} catch (SQLException e) {

			list = new ArrayList();
			switch (DBType.getType(con.getMetaData())) {
			case DBType.DB_TYPE_SYMFOWARE:
				list.add("TABLE"); //$NON-NLS-1$
				list.add("VIEW"); //$NON-NLS-1$
				list.add("SEQUENCE"); //$NON-NLS-1$
				break;

			default:
				list.add("TABLE"); //$NON-NLS-1$
				list.add("VIEW"); //$NON-NLS-1$
				break;
			}
			// 未サポートのDB用
			return (String[]) list.toArray(new String[0]);
			
		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
		}

	}
}
