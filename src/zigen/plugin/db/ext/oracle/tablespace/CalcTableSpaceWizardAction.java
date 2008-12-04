/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ext.oracle.tablespace.wizard.CalcTableSpaceWizard;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.views.TableTypeSearchAction;

/**
 * <Oracle専用>AnalyzeTableActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/07/08 ZIGEN create.
 * 
 */
public class CalcTableSpaceWizardAction extends Action {

	// private ISelection selection = null;
	private TreeViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public CalcTableSpaceWizardAction(TreeViewer viewer) {
		// this.selection = selection;
		this.viewer = viewer;
		this.setText(Messages.getString("CalcTableSpaceWizardAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CalcTableSpaceWizardAction.1")); //$NON-NLS-1$
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof Schema) {
			Schema schema = (Schema) element;
			if (!schema.isExpanded()) {
				// 展開フラグをTrueにする(テーブル要素をキャッシュする）
				schema.setExpanded(true);
				// テーブル一覧を検索
				// new TableTypeSearchAction(viewer, schema).run();

				// テーブル一覧を検索（非同期にて実行）
				Display display = viewer.getControl().getDisplay();
				display.syncExec(new TableTypeSearchAction(viewer, schema));

			}

		}
		Shell shell = DbPlugin.getDefault().getShell();
		CalcTableSpaceWizard wizard = new CalcTableSpaceWizard(viewer.getSelection());
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();
	}

}
