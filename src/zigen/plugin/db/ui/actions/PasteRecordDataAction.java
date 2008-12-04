/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.event.TableKeyAdapter;
import zigen.plugin.db.ui.editors.event.TableKeyEventHandler;

/**
 * CopyRecordDataAction.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/12/04 ZIGEN create.
 * 
 */
public class PasteRecordDataAction extends TableViewEditorAction implements IWorkbenchWindowActionDelegate {

	public PasteRecordDataAction() {
		// �e�L�X�g��c�[���`�b�v�A�A�C�R���̐ݒ�
		// this.setText("�N���b�v�{�[�h�f�[�^��\��t��(&P)");
		// this.setToolTipText("�N���b�v�{�[�h�f�[�^��\��t��");
		setImage(ITextOperationTarget.PASTE);
		setEnabled(false);

	}

	public void run() {
		try {

			// TableKeyEventHandler handler = new
			// TableKeyEventHandler(editor.getViewer());
			TableKeyEventHandler handler = new TableKeyEventHandler((TableViewEditorFor31) editor);

			TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);
			keyAdapter.createNewElement();

			//editor.getViewer().getTable().notifyListeners(SWT.Selection, null); // �I����Ԃ�ʒm

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {

			// �I����Ԃ��ēx�ʒm����
			editor.getViewer().getControl().notifyListeners(SWT.Selection, null);
			/*
			 * if (editor instanceof TableViewEditorFor31) { //
			 * �\��t����AAction��Reflesh���� ((TableViewEditorFor31)
			 * editor).refleshAction(); }
			 */
		}

	}

	/**
	 * Enable���[�h��ݒ肷��
	 * 
	 */
	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (editor.getViewer() == null) {
			setEnabled(false);
		} else {
			// �N���b�v�{�[�h�ɓK�؂ȃf�[�^�������True��ݒ肷��

			// TableKeyEventHandler handler = new
			// TableKeyEventHandler(editor.getViewer());
			TableKeyEventHandler handler = new TableKeyEventHandler((TableViewEditorFor31) editor);
			TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);

			boolean canPaste = keyAdapter.canCreateNewElement();
			setEnabled(canPaste);

		}
	}

	protected IWorkbenchWindow window;

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void run(IAction action) {
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
