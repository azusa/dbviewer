/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

/**
 * 
 * TableIDXColumn.javaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/11/19 ZIGEN create.
 * 
 */
public class TableUniqueIDXColumn implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int ordinal_position = 0;

	private String name = null;

	private String columnName = null;

	private boolean nonUnique = false;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNonUnique() {
		return nonUnique;
	}

	public void setNonUnique(boolean nonUnique) {
		this.nonUnique = nonUnique;
	}

	public int getOrdinal_position() {
		return ordinal_position;
	}

	public void setOrdinal_position(int ordinal_position) {
		this.ordinal_position = ordinal_position;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableIDXColumn:");
		buffer.append(" ordinal_position: ");
		buffer.append(ordinal_position);
		buffer.append(" name: ");
		buffer.append(name);
		buffer.append(" columnName: ");
		buffer.append(columnName);
		buffer.append(" nonUnique: ");
		buffer.append(nonUnique);
		buffer.append("]");
		return buffer.toString();
	}

}
