/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SchemaSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/18 ZIGEN create.
 * 
 */
public class SchemaSearcher {

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
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();

			// add ZIGEN スキーマサポートのチェックを追加
			if (!isSupport(con)) {
				return new String[0];
			}

			list = new ArrayList();

			if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {
				String s = "SELECT SCHEMA_NAME AS TABLE_SCHEM FROM information_schema.SCHEMATA";
				st = con.createStatement();
				rs = st.executeQuery(s);
			} else {
				rs = objMet.getSchemas();
			}

			while (rs.next()) {
				String wk = rs.getString("TABLE_SCHEM"); //$NON-NLS-1$
				list.add(wk);
			}


			return (String[]) list.toArray(new String[0]);

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}

	}


	public static boolean isSupport(Connection con) {
		try {
			DatabaseMetaData objMet = con.getMetaData();

			if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {
				// 強制的にTRUE
				return true;
			} else {
				return objMet.supportsSchemasInTableDefinitions();
			}

		} catch (Exception e) {
			return false;
		}
	}

}
