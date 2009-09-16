package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;


public class DefaultCommentSearchFactory extends AbstractTableInfoSearchFactory{
	protected DatabaseMetaData meta;

	protected DefaultCommentSearchFactory(DatabaseMetaData meta) {
		this.meta = meta;
	}

	public String getTableInfoAllSql(String schema, String[] types) {
		return null;
	}

	public String getTableInfoSql(String schema, String tableName, String type) {
		return null;
	}

	public String getDbName() {
		return null;
	}


}
