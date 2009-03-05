package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;


public class DefaultCommentSearchFactory extends AbstractCommentSearchFactory{
	protected DatabaseMetaData meta;
	
	protected DefaultCommentSearchFactory(DatabaseMetaData meta) {
		this.meta = meta;
	}
	public String getCustomColumnInfoSQL(String dbName, String owner) {
		return null;
	}
	public String getDbName() {
		return null;
	}


}
