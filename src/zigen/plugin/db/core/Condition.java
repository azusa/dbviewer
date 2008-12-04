/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.util.List;

/**
 * �e�[�u���ҏW�G�f�B�^��̌��������N���X. XML�ɕۑ����邽�߂�JavaBeans
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/09/25 ZIGEN create.
 * 
 */
public class Condition {

	private String connectionUrl;

	private String schema;

	private String table;

	private List conditions;

	private String filterPattern;
	
	private boolean checkFilterPattern;
	
	public Condition() {
	}

	public List getConditions() {
		return conditions;
	}

	public void setConditions(List conditions) {
		this.conditions = conditions;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getFilterPattern() {
		return filterPattern;
	}

	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}


	public boolean isCheckFilterPattern() {
		return checkFilterPattern;
	}

	public void setCheckFilterPattern(boolean checkFilterPattern) {
		this.checkFilterPattern = checkFilterPattern;
	}
}
