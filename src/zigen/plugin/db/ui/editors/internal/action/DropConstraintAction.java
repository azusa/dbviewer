/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.internal.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.actions.TableViewEditorAction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.thread.AbstractSQLThread;
import zigen.plugin.db.ui.editors.internal.thread.DropConstraintThread;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ITable;

/**
 * InsertRecordActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class DropConstraintAction extends TableViewEditorAction {

	public DropConstraintAction() {
		this.setText(Messages.getString("DropConstraintAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DropConstraintAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
	}

	public void run() {

		try {
			if (editor instanceof TableViewEditorFor31) {
				TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;

				IStructuredSelection selection = (IStructuredSelection) tEditor.getTableDefineEditor().getConstraintViewer().getSelection();

				Object obj = selection.getFirstElement();
				if (obj instanceof Constraint) {
					Constraint constraint = (Constraint) obj;
					ITable table = tEditor.getTableNode();
					AbstractSQLThread invoker;
					if (DbPlugin.getDefault().confirmDialog(Messages.getString("DropConstraintAction.2"))) { //$NON-NLS-1$
						invoker = new DropConstraintThread(table, constraint);
						invoker.run();
					}

				} else {
					throw new IllegalStateException("想定しているクラスではありません " + obj.getClass().getName()); //$NON-NLS-1$

				}
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
