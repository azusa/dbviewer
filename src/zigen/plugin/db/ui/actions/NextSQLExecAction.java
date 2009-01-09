/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLHistory;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.views.SQLExecuteView;

/**
 * CommitActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class NextSQLExecAction extends Action implements Runnable {

	SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();

	private SQLExecuteView view;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public NextSQLExecAction(SQLExecuteView view) {
		this.setText(Messages.getString("NextSQLExecAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("NextSQLExecAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLNextCommand"); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_FORWARD));
		// this.setDisabledImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPluginConstant.IMG_CODE_E_FORWARD));
		this.view = view;
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {

		SQLHistory history = mgr.nextHisotry();
		if (history != null) {
			String sql = mgr.loadContents(history);
			view.getSqlViewer().getDocument().set(sql);
			// view.getSqlViewer().setSelectedRange(sql.length(), 0);
			view.getSqlViewer().invalidateTextPresentation(); // テキストエディタを再描画
		} else {
			view.getSqlViewer().getDocument().set(""); //$NON-NLS-1$
		}
		view.updateHistoryButton();

	}

}
