/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.views.SQLExecuteView;

/**
 * RemoveSQLActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class RemoveSQLAction extends Action implements Runnable {

	private SQLExecuteView view;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public RemoveSQLAction(SQLExecuteView view) {
		this.setText(Messages.getString("RemoveSQLAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RemoveSQLAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_CLEAR));
		this.view = view;
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		try {
			view.setSqlText(Messages.getString("RemoveSQLAction.2")); // クリア
			// //$NON-NLS-1$

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
