/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.swt.widgets.Table;

/**
 * 
 * TextCellEditorクラス. Validateエラーでも、入力値を取得するためのメソッドを拡張
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class TextCellEditor extends org.eclipse.jface.viewers.TextCellEditor {

	private int columnIndex = 0;

	public TextCellEditor(Table table, int columnIndex) {
		super(table);
		this.columnIndex = columnIndex;
	}

	public Object getInputValue() {
		return doGetValue();
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
