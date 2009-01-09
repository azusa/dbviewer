/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.Schema;

public class DDLDiffForSchemaAction extends Action implements Runnable {

	private boolean showDialog = true;

	private TreeViewer viewer = null;

	private Schema left = null;

	private Schema right = null;

	public DDLDiffForSchemaAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DDLDiffForSchemaAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DDLDiffForSchemaAction.1")); //$NON-NLS-1$
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof Schema) {
					Schema schema = (Schema) obj;
					if (index == 0) {
						left = schema;
						index++;
					} else if (index == 1) {
						right = schema;
						index++;
					} else {
						break;
					}
				}
			}

			if (index == 2) {

				// ２つ選択されている場合のみ実行
				DDLDiffForSchemaJob job = new DDLDiffForSchemaJob(viewer, left, right);
				// job.setPriority(DDLDiffJob.SHORT);
				job.setPriority(DDLDiffForSchemaJob.SHORT);
				// job.setPriority(DDLDiffJob.DECORATE); // もっとも軽い

				job.setUser(showDialog); // ダイアログを出す
				job.schedule();

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
