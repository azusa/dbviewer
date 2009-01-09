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

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.jobs.ConnectDBJob;

/**
 * ConnectDBAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 */
public class ConnectDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public ConnectDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("ConnectDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ConnectDBAction.1")); //$NON-NLS-1$
		this.setEnabled(true);

	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		try {

			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object element = (Object) iter.next();
				if (element instanceof DataBase) {
					DataBase db = (DataBase) element;
					if (!db.isConnected()) {
						db.setConnected(true); // 2�x������h��
						ConnectDBJob job = new ConnectDBJob(viewer, db);
						job.setPriority(ConnectDBJob.SHORT);
						job.setUser(false);
						job.setSystem(false);
						job.schedule(); // �ڑ��Ɏ��s����΁Adb.setConnected(false);
					} else {
					}
				}
			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

}
