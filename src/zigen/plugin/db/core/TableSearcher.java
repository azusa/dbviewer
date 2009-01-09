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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zigen.plugin.db.ext.oracle.internal.OracleCommentInfo;
import zigen.plugin.db.ext.oracle.internal.OracleTableCommentsSearcher;

/**
 * TableSearcherクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/18 ZIGEN create.
 * 
 */
public class TableSearcher {

	// <!-- [002] 修正 ZIGEN 2005/07/30
	public synchronized static TableInfo[] execute(IDBConfig config, String schemaPattern, String[] types) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, schemaPattern, types);

		} catch (Exception e) {
			throw e;
		}

	}

	// コメント取得用
	private static Map getRemarks(Connection con, String schemaPattern) throws Exception {
		Map remarks = null;
		DatabaseMetaData objMet = con.getMetaData();
		switch (DBType.getType(objMet)) {
		case DBType.DB_TYPE_ORACLE:
			remarks = OracleTableCommentsSearcher.execute(con, schemaPattern);
			break;
		}
		return remarks;
	}

	public synchronized static TableInfo[] execute(Connection con, String schemaPattern, String[] types) throws Exception {

		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;
		try {

			DatabaseMetaData objMet = con.getMetaData();

			// 独自のコメント情報を
			Map remarks = getRemarks(con, schemaPattern);

			if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {
				StringBuffer sb = new StringBuffer();
				sb.append("SELECT");
				sb.append("        TABLE_NAME");
				sb.append("        ,TABLE_TYPE");
				sb.append(" ,TABLE_COMMENT REMARKS");
				// sb.append(" ,'' REMARKS");
				sb.append("    FROM");
				sb.append("        information_schema.TABLES");
				sb.append("    WHERE");
				sb.append("        TABLE_SCHEMA = '" + SQLUtil.encodeQuotation(schemaPattern) + "'");
				if (types.length > 0) {
					sb.append("    AND (");
					for (int i = 0; i < types.length; i++) {
						if (i > 0) {
							sb.append(" OR ");
						}
						sb.append("    TABLE_TYPE Like '%" + SQLUtil.encodeQuotation(types[i]) + "'");
					}
					sb.append("    )");
				}

				st = con.createStatement();
				rs = st.executeQuery(sb.toString());
			} else {

				if (SchemaSearcher.isSupport(con)) {
					rs = objMet.getTables(null, schemaPattern, "%", types); //$NON-NLS-1$

				} else {
					rs = objMet.getTables(null, "%", "%", types); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}

			list = new ArrayList();

			// for SymfoWARE
			Map map = new HashMap();

			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME"); //$NON-NLS-1$
				// for SymfoWARE： 同名のテーブルがある場合はリストに登録しない
				if (!map.containsKey(tableName)) {
					map.put(tableName, tableName);

					TableInfo info = new TableInfo();
					info.setName(tableName);
					info.setTableType(rs.getString("TABLE_TYPE")); //$NON-NLS-1$

					// REMARKSでコメントが取れるDBがあるのか？
					// → Postgresqlでは採れる
					info.setComment(rs.getString("REMARKS")); //$NON-NLS-1$

					// <- [001] 2005/11/22 add zigen
					// setComment(con, schemaPattern, tableName, info);
					if (remarks != null && remarks.containsKey(tableName)) {
						OracleCommentInfo ora = (OracleCommentInfo) remarks.get(tableName);
						if (ora != null)
							info.setComment(ora.getRemarks());
					}

					list.add(info);
				}

			}

			Collections.sort(list, new TableInfoSorter());

			return (TableInfo[]) list.toArray(new TableInfo[0]);

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);

		}

	}

	public static TableInfo execute(Connection con, String schemaPattern, String tablePattern, String type) throws Exception {
		TableInfo info = null;
		ResultSet rs = null;
		try {

			Map remarks = null;
			switch (DBType.getType(con.getMetaData())) {
			case DBType.DB_TYPE_ORACLE:
				remarks = OracleTableCommentsSearcher.execute(con, schemaPattern);
				break;
			}

			DatabaseMetaData objMet = con.getMetaData();
			if (SchemaSearcher.isSupport(con)) {
				rs = objMet.getTables(null, schemaPattern, tablePattern, new String[] {type});

			} else {
				// 第1引数からそれぞれデータベース名、スキーマ名、テーブル名、テーブルの型
				rs = objMet.getTables(null, "%", tablePattern, new String[] {type}); //$NON-NLS-1$
			}

			if (rs.next()) {
				String tableName = rs.getString("TABLE_NAME"); //$NON-NLS-1$
				// for SymfoWARE： 同名のテーブルがある場合はリストに登録しない
				info = new TableInfo();
				info.setName(tableName);
				info.setTableType(rs.getString("TABLE_TYPE")); //$NON-NLS-1$
				info.setComment(rs.getString("REMARKS")); //$NON-NLS-1$
				// setComment(con, schemaPattern, tableName, info);

				if (remarks != null && remarks.containsKey(tableName)) {
					OracleCommentInfo ora = (OracleCommentInfo) remarks.get(tableName);
					if (ora != null)
						info.setComment(ora.getRemarks());
				}
			}
			return info;

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);

		}

	}

	// protected static void setComment(Connection con, String schemaPattern,
	// String tableName, TableInfo info) throws Exception {
	// switch (DBType.getType(con.getMetaData())) {
	// case DBType.DB_TYPE_ORACLE:
	// String comment = OracleTableCommentSearcher.execute(con, schemaPattern,
	// tableName);
	// info.setComment(comment);
	// break;
	//
	// default:
	// break;
	// }
	// }

	// TABLE-TYPEに指定出来る文字列
	// "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
	// "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
}

class TableInfoSorter implements Comparator {

	public TableInfoSorter() {}

	public int compare(Object o1, Object o2) {

		String firstType = ((TableInfo) o1).getTableType();
		String secondType = ((TableInfo) o2).getTableType();

		if (firstType.equals(secondType)) {
			return 0;
		} else if (firstType.equals("TABLE")) { //$NON-NLS-1$
			return -1;
		} else {
			return (firstType.compareTo(secondType)) * -1;
		}
	}
}
