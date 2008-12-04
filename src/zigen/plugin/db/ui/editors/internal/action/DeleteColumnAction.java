/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.internal.action;

import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.actions.TableViewEditorAction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.thread.AbstractSQLThread;
import zigen.plugin.db.ui.editors.internal.thread.DropColumnThread;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

/**
 * InsertRecordActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class DeleteColumnAction extends TableViewEditorAction {

	public DeleteColumnAction() {
		// テキストやツールチップ、アイコンの設定
		this.setText(Messages.getString("DeleteColumnAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DeleteColumnAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
	}

	public void run() {

		try {
			if (editor instanceof TableViewEditorFor31) {
				TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;

				IStructuredSelection selection = (IStructuredSelection) tEditor.getTableDefineEditor().getDefineViewer().getSelection();

				Object obj = selection.getFirstElement();
				if (obj instanceof Column) {
					Column col = (Column) obj;

					IDBConfig config = tEditor.getDBConfig();
					ITable table = tEditor.getTableNode();
					ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactory(config, table);

					AbstractSQLThread thread;

					String msg = Messages.getString("DeleteColumnAction.2"); //$NON-NLS-1$
					if (factory.supportsDropColumnCascadeConstraints()) {
						String opt = Messages.getString("DeleteColumnAction.3"); //$NON-NLS-1$
						MessageDialogWithToggle dialog = DbPlugin.getDefault().confirmDialogWithToggle(msg, opt, true);
						final int YES = 2;
						if (dialog.getReturnCode() == YES) {
							thread = new DropColumnThread(table, col, dialog.getToggleState());
							thread.run();
						}
					} else {
						// cascade constraintsオプションをサポートしていない場合はこちら
						if (DbPlugin.getDefault().confirmDialog(msg)) {
							thread = new DropColumnThread(table, col, false);
							thread.run();
						}
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
