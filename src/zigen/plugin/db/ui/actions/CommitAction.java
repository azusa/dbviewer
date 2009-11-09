/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.views.SQLExecuteView;

/**
 * CommitActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/03/12 ZIGEN create. [002] 2005/05/29 ZIGEN コミット時のメッセージを変更.
 */
public class CommitAction extends Action implements Runnable {


	private SQLExecuteView view;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public CommitAction(SQLExecuteView view) {
		this.setText(Messages.getString("CommitAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CommitAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_COMMIT));

		this.view = view;

	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		try {
			// SQL実行ビューからIDBConfigを取得

			Transaction trans = Transaction.getInstance(view.getConfig());

			if (!trans.isConneting()) {
				DbPlugin.getDefault().showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
				return;
			}

			int transCount = trans.getTransactionCount();

			trans.commit();
			view.setStatusMessage(createMessage(transCount));
			// DbPlugin.getDefault().showInformationMessage(getMessage(transCount));

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private String createMessage(int count) {
		// <!-- [002] 削除 ZIGEN 2005/05/29
		// return count + "件、コミットしました";
		return count + Messages.getString("CommitAction.3"); //$NON-NLS-1$
		// [002] 削除 ZIGEN 2005/05/29 -->
		// return "コミット完了";

	}

}
