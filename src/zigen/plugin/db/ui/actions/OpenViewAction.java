/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class OpenViewAction extends Action {

	private final IWorkbenchWindow window;

	private int instanceNum;

	private final String viewId = DbPluginConstant.VIEW_ID_SQLExecute;

	private IDBConfig config;

	private SQLExecuteView view;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public OpenViewAction(SQLExecuteView view, IWorkbenchWindow window) {
		this.view = view;
		this.setText(Messages.getString("OpenViewAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("OpenViewAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_OPEN));

		this.window = window;

	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		try {
			checkSecondaryId();
			IViewPart part = window.getActivePage().showView(viewId, Integer.toString(instanceNum), IWorkbenchPage.VIEW_ACTIVATE);
			instanceNum++;

			if (part instanceof SQLExecuteView) {
				SQLExecuteView sv = (SQLExecuteView) part;
				// 起動元のデータベースロック状態を引き継ぐ
				sv.setLockedDataBase(view.isLockedDataBase());

				sv.setCommitMode(config, config.isAutoCommit());
				// sv.updateCombo(config);


			}

		} catch (PartInitException e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private void checkSecondaryId() {
		IWorkbenchPage page = window.getActivePage();
		IViewReference vr = page.findViewReference(viewId, Integer.toString(instanceNum));
		if (vr != null) {
			instanceNum++;
			checkSecondaryId();
		} else {
			return;
		}

	}

	public void setDbConfig(IDBConfig config) {
		this.config = config;
	}

}
