/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DropSQLInvoker;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;

public class DropTreeNodeAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public DropTreeNodeAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DropTreeNodeAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DropTreeNodeAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));

	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		try {

			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

			Iterator iter = selection.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof OracleSource) {

					OracleSource elem = (OracleSource) obj;

					TreeNode parent = elem.getParent();
					Schema schema = elem.getSchema();
					String owner = schema.getName();
					String type = elem.getType();
					String name = elem.getName();

					if (DbPlugin.getDefault().confirmDialog(name + Messages.getString("DropTreeNodeAction.2") + type + Messages.getString("DropTreeNodeAction.3"))) { //$NON-NLS-1$ //$NON-NLS-2$
						DropSQLInvoker.execute(elem.getDbConfig(), owner, type, name);
						parent.removeChild(elem);
						viewer.refresh(parent);
					}

				} else if (obj instanceof OracleSequence) {
					OracleSequence elem = (OracleSequence) obj;
					TreeNode parent = elem.getParent();
					Schema schema = elem.getSchema();
					String owner = schema.getName();
					String type = "SEQUENCE"; //$NON-NLS-1$
					String name = elem.getName();

					if (DbPlugin.getDefault().confirmDialog(name + Messages.getString("DropTreeNodeAction.5") + type + Messages.getString("DropTreeNodeAction.6"))) { //$NON-NLS-1$ //$NON-NLS-2$
						DropSQLInvoker.execute(elem.getDbConfig(), owner, type, name);
						parent.removeChild(elem);
						viewer.refresh(parent);
					}

				}
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}
}
