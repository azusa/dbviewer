/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.io.Serializable;

public class TableComment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// protected String schemaName;
	
	protected String tableName;
	
	protected String remarks;
	
	// public String getSchemaName() {
	// return schemaName;
	// }
	//
	// public void setSchemaName(String schemaName) {
	// this.schemaName = schemaName;
	// }
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * コンストラクタ
	 */
	public TableComment() {}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
