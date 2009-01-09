/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import zigen.plugin.db.core.TableColumn;

/**
 * UnSupportedTypeException.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class UnSupportedTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	TableColumn column;

	Object value;

	public UnSupportedTypeException(TableColumn column, Object value) {
		super();
		this.column = column;
		this.value = value;

	}

	public TableColumn getColumn() {
		return column;
	}

	public Object getValue() {
		return value;
	}

}
