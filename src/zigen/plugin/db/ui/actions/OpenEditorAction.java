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

import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.OpenEditorJob;

/**
 * CloseDBActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create. [2] 2005/11/23 ZIGEN エディター起動時にカラムを読み込むように修正.
 * 
 * 
 */
public class OpenEditorAction extends Action implements Runnable {

	TreeViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public OpenEditorAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("OpenEditorAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("OpenEditorAction.1")); //$NON-NLS-1$

	}

	/**
	 * Action実行時の処理
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
				throw new IllegalStateException("他の要素でのダブルクリック"); //$NON-NLS-1$
			}
		}

	}

}
