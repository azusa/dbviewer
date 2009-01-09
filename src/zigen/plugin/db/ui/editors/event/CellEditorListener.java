/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * CellEditorListenerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/24 ZIGEN create.
 * 
 */
public class CellEditorListener implements ICellEditorListener {

	private TextCellEditor cellEditor;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public CellEditorListener(TextCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorListener#applyEditorValue()
	 */
	public void applyEditorValue() {
		// 確定後フォーカス時の編集を不可能にする
		// viewer.setCellEditors(null);
		if (!cellEditor.isValueValid()) {

		}

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorListener#cancelEditor()
	 */
	public void cancelEditor() {
	// 確定後フォーカス時の編集を不可能にする
	// viewer.setCellEditors(null);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorListener#editorValueChanged(boolean, boolean)
	 */
	public void editorValueChanged(boolean oldValidState, boolean newValidState) {
		; // 何もしない
	}

}
