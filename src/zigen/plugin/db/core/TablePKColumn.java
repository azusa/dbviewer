/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

/**
 * TablePKColumnクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/18 ZIGEN create.
 * 
 */
public class TablePKColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	private int sep = 0;

	private String name = null;

	private String columnName = null;

	public TablePKColumn() {

	}

	/**
	 * @return sep を戻します。
	 */
	public int getSep() {
		return sep;
	}

	/**
	 * @param sep
	 *            sep を設定。
	 */
	public void setSep(int sep) {
		this.sep = sep;
	}

	/**
	 * @return columnName を戻します。
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            columnName を設定。
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return name を戻します。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            name を設定。
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TablePKColumn:");
		buffer.append(" sep: ");
		buffer.append(sep);
		buffer.append(" name: ");
		buffer.append(name);
		buffer.append(" columnName: ");
		buffer.append(columnName);
		buffer.append("]");
		return buffer.toString();
	}
}
