/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

import zigen.plugin.db.ui.internal.ITable;

/**
 * TableHeaderElementクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class TableHeaderElement extends TableElement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Query用のHeader
	 * 
	 * @param columns
	 */
	public TableHeaderElement(TableColumn[] columns) {
		super(null, 0, columns, null, null, null);
	}

	/**
	 * テーブル編集用Header
	 * 
	 * @param table
	 * @param columns
	 * @param uniqueColumns
	 */
	public TableHeaderElement(ITable table, TableColumn[] columns, TableColumn[] uniqueColumns) {
		super(table, 0, columns, null, uniqueColumns, null);
	}

}
