/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.internal.action;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.actions.TableViewEditorAction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.wizard.ColumnWizard;
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
public class EditColumnAction extends TableViewEditorAction {

	public EditColumnAction() {
		// テキストやツールチップ、アイコンの設定
		this.setText(Messages.getString("EditColumnAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("EditColumnAction.1")); //$NON-NLS-1$
		// this.setImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_COLUMN_EDIT));
	}

	public void run() {

		if (editor instanceof TableViewEditorFor31) {
			TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;

			IStructuredSelection selection = (IStructuredSelection) tEditor.getTableDefineEditor().getDefineViewer().getSelection();

			Object obj = selection.getFirstElement();
			if (obj instanceof Column) {
				Column col = (Column) obj;
				IDBConfig config = editor.getDBConfig();
				ITable tableNode = editor.getTableNode();
				ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactory(config, tableNode);
				Shell shell = DbPlugin.getDefault().getShell();

				ColumnWizard wizard = new ColumnWizard(factory, tableNode, col, false);
				WizardDialog dialog2 = new WizardDialog(shell, wizard);
				int ret = dialog2.open();
				if (ret == IDialogConstants.OK_ID) {
				}

			} else {
			}
		}

	}

}
