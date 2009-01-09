/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
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
 * ConnectDBActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 */
public class ConnectDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	/**
	 * コンストラクタ
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
	 * Action実行時の処理
	 */
	public void run() {
		try {

			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object element = (Object) iter.next();
				if (element instanceof DataBase) {
					DataBase db = (DataBase) element;
					if (!db.isConnected()) {
						db.setConnected(true); // 2度押しを防ぐ
						ConnectDBJob job = new ConnectDBJob(viewer, db);
						job.setPriority(ConnectDBJob.SHORT);
						job.setUser(false);
						job.setSystem(false);
						job.schedule(); // 接続に失敗すれば、db.setConnected(false);
					} else {
					}
				}
			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

}
