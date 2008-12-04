/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

import zigen.plugin.db.ui.internal.ITable;


public class TableNewElement extends TableElement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * コンストラクタ(INSERT用） ユニークカラムを指定するが、対応するユニークデータは無い（新規のため）
	 */
	public TableNewElement(ITable table, int recordNo, TableColumn[] columns, Object[] items, TableColumn[] uniqueColumns) {
		super(table, recordNo, columns, items, null, null);
		this.uniqueColumns = uniqueColumns;
		this.isNew = true;
	
	
		convertItems();
	}
	
	
	// Excelから貼り付ける場合、CHAR型の空白が落ちる場合があるため、ここで強制的にパディングする
	private void convertItems(){
		for (int i = 0; i < items.length; i++) {
			items[i] = padding(i, items[i]);
		}		
	}
	

}
