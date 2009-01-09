/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

/**
 * TablePKColumn�N���X.
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
	 * @return sep ��߂��܂��B
	 */
	public int getSep() {
		return sep;
	}

	/**
	 * @param sep
	 *            sep ��ݒ�B
	 */
	public void setSep(int sep) {
		this.sep = sep;
	}

	/**
	 * @return columnName ��߂��܂��B
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            columnName ��ݒ�B
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return name ��߂��܂��B
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            name ��ݒ�B
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
