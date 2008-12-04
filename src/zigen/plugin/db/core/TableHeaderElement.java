/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

import zigen.plugin.db.ui.internal.ITable;

/**
 * TableHeaderElement�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class TableHeaderElement extends TableElement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Query�p��Header
	 * 
	 * @param columns
	 */
	public TableHeaderElement(TableColumn[] columns) {
		super(null, 0, columns, null, null, null);
	}

	/**
	 * �e�[�u���ҏW�pHeader
	 * 
	 * @param table
	 * @param columns
	 * @param uniqueColumns
	 */
	public TableHeaderElement(ITable table, TableColumn[] columns, TableColumn[] uniqueColumns) {
		super(table, 0, columns, null, uniqueColumns, null);
	}

}
