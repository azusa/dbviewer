package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import zigen.plugin.db.core.SQLUtil;


public class SymfowareCommentSearchFactory extends DefaultCommentSearchFactory{
	
	public SymfowareCommentSearchFactory(DatabaseMetaData meta){
		super(meta);
	}
	public String getCustomColumnInfoSQL(String dbName, String owner) {
		StringBuffer sb = new StringBuffer();
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
		sb.append("        AND T.DB_NAME = '" + SQLUtil.encodeQuotation(dbName) + "'");
		sb.append("        AND T.SCHEMA_NAME = '" + SQLUtil.encodeQuotation(owner) + "'");
		return sb.toString();
	}

	public String getDbName() {
		String name = null;
		try {
			String url = meta.getURL();
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}


}
