/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;

/**
 * DropSQLInvokerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 * 
 */
public class DropSQLInvoker {

	public static void execute(IDBConfig config, String owner, String type, String name) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			execute(con, owner, type, name);

		} catch (Exception e) {
			throw e;
		}
	}

	public static void execute(Connection con, String owner, String type, String name) throws Exception {
		Statement st = null;
		List list = new ArrayList();

		try {
			st = con.createStatement();
			int rowAffected = st.executeUpdate(getSQL(owner, type, name));

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			StatementUtil.close(st);
		}

	}

	private static String getSQL(String owner, String type, String name) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP "); //$NON-NLS-1$
		sb.append(type);
		sb.append(" "); //$NON-NLS-1$
		sb.append(owner);
		sb.append("."); //$NON-NLS-1$
		sb.append(name);

		return sb.toString();

	}

}
