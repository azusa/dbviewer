/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPlugin;

/**
 * SelectAllRecordAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class SelectAllRecordAction extends TableViewEditorAction {

	protected IStructuredSelection selection;

	public SelectAllRecordAction() {
		super();
		// �e�L�X�g��c�[���`�b�v�A�A�C�R���̐ݒ�
		setText(Messages.getString("SelectAllRecordAction.0")); //$NON-NLS-1$
		setToolTipText(Messages.getString("SelectAllRecordAction.1")); //$NON-NLS-1$
//		setAccelerator(SWT.CTRL | 'A');
	}

	public void run() {
		try {
			TableViewer viewer = editor.getViewer();
			viewer.getTable().selectAll();
			viewer.getTable().notifyListeners(SWT.Selection, null);
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}
}
