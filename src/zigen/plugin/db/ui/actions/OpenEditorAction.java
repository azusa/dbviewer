/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.OpenEditorJob;

/**
 * CloseDBAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create. [2] 2005/11/23 ZIGEN �G�f�B�^�[�N�����ɃJ������ǂݍ��ނ悤�ɏC��.
 * 
 * 
 */
public class OpenEditorAction extends Action implements Runnable {

	TreeViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public OpenEditorAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("OpenEditorAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("OpenEditorAction.1")); //$NON-NLS-1$

	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (element instanceof ITable) {
				ITable table = (ITable) element;

				OpenEditorJob job = new OpenEditorJob(viewer, table);
				job.setPriority(OpenEditorJob.SHORT);
				job.setUser(false);
				job.schedule();

			} else {
				throw new IllegalStateException("���̗v�f�ł̃_�u���N���b�N"); //$NON-NLS-1$
			}
		}

	}

}
