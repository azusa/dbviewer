package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;

import zigen.plugin.db.core.SQLUtil;


public class OracleCommentSearchFactory extends DefaultCommentSearchFactory{

	public OracleCommentSearchFactory(DatabaseMetaData meta){
		super(meta);
	}
	public String getCustomColumnInfoSQL(String dbName, String owner) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TABLE_NAME, COMMENTS"); //$NON-NLS-1$
		sb.append(" FROM ALL_TAB_COMMENTS"); //$NON-NLS-1$
		sb.append(" WHERE OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String getDbName() {
		return null;
	}


}
