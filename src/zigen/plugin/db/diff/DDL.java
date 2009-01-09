/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.io.Serializable;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class DDL implements IDDL, Serializable {

	private static final long serialVersionUID = 1L;

	boolean isSchemaSupport;

	String dbName;

	String schemaName;

	String targetName;

	String ddl;

	ITable table;

	public DDL() {}


	public DDL(ITable table) {
		setTable(table);
	}

	private String getDDLString(ITable tableNode) {
		String result = ""; //$NON-NLS-1$
		try {
			if (tableNode != null) {
				IDBConfig config = tableNode.getDbConfig();
				ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactoryNoCache(config, tableNode);
				factory.setVisibleSchemaName(false);
				result = factory.createDDL();
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#getDisplayedName()
	 */
	public String getDisplayedName() {
		StringBuffer sb = new StringBuffer();
		if (isSchemaSupport) {
			sb.append(schemaName + "." + targetName); //$NON-NLS-1$
		} else {
			sb.append(targetName);
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#getDbName()
	 */
	public String getDbName() {
		return dbName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#setDbName(java.lang.String)
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#getDdl()
	 */
	public String getDdl() {
		return ddl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#setDdl(java.lang.String)
	 */
	public void setDdl(String ddl) {
		this.ddl = ddl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#getSchemaName()
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#setSchemaName(java.lang.String)
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#getTargetName()
	 */
	public String getTargetName() {
		return targetName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#setTargetName(java.lang.String)
	 */
	public void setTargetName(String tableName) {
		this.targetName = tableName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#isSchemaSupport()
	 */
	public boolean isSchemaSupport() {
		return isSchemaSupport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDL#setSchemaSupport(boolean)
	 */
	public void setSchemaSupport(boolean isSchemaSupport) {
		this.isSchemaSupport = isSchemaSupport;
	}

	public String getType() {
		return table.getFolderName();
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
		this.dbName = table.getDbConfig().getDbName();
		this.schemaName = table.getSchemaName();
		this.targetName = table.getName();
		this.isSchemaSupport = table.getDataBase().isSchemaSupport();
		this.ddl = getDDLString(table);
	}

}
