/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * CellEditorListener�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/24 ZIGEN create.
 * 
 */
public class CellEditorListener implements ICellEditorListener {

	private TextCellEditor cellEditor;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public CellEditorListener(TextCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorListener#applyEditorValue()
	 */
	public void applyEditorValue() {
		// �m���t�H�[�J�X���̕ҏW��s�\�ɂ���
		// viewer.setCellEditors(null);
		if (!cellEditor.isValueValid()) {

		}

	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorListener#cancelEditor()
	 */
	public void cancelEditor() {
	// �m���t�H�[�J�X���̕ҏW��s�\�ɂ���
	// viewer.setCellEditors(null);
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorListener#editorValueChanged(boolean, boolean)
	 */
	public void editorValueChanged(boolean oldValidState, boolean newValidState) {
		; // �������Ȃ�
	}

}
