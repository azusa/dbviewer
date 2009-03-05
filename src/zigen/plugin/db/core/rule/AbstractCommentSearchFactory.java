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
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.rule.mysql.MySQLColumnSearcharFactory;
import zigen.plugin.db.core.rule.oracle.OracleColumnSearcharFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareColumnSearcharFactory;


abstract public class AbstractCommentSearchFactory implements ICommentFactory {

	/**
	 * Factory‚ÌƒLƒƒƒbƒVƒ…‰»
	 */
	private static Map map = new HashMap();
	


	public static ICommentFactory getFactory(DatabaseMetaData meta) throws SQLException{
		ICommentFactory factory = null;
		String key = meta.getDriverName();
		if (map.containsKey(key)) {
			factory = (ICommentFactory) map.get(key);
		} else {
			switch (DBType.getType(key)) {
				case DBType.DB_TYPE_ORACLE:
					factory = new OracleCommentSearchFactory(meta);
					break;
				case DBType.DB_TYPE_SYMFOWARE:
					factory = new SymfowareCommentSearchFactory(meta);
					break;
					
				default:
					factory = new DefaultCommentSearchFactory(meta);
					break;
			}
			
			map.put(key, factory);
		}
		
		return factory;
		
	}
	
	
	public Map getRemarkMap(Connection con, String owner) throws Exception {
		Map result = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			String query = getCustomColumnInfoSQL(getDbName(), owner);
			if (query != null) {
				result = new HashMap();
				rs = st.executeQuery(query);
				while (rs.next()) {
					TableComment info = new TableComment();
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
	
	abstract public String getDbName();

	abstract String getCustomColumnInfoSQL(String dbNamem, String owner);
}
