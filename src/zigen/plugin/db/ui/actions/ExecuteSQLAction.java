/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

/**
 * ExecuteQueryActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/03/12 ZIGEN create. [002] 2005/06/02 ZIGEN ;や/ を付で、実行ボタンを押下した場合を想定
 * 
 */
public class ExecuteSQLAction extends AbstractExecuteSQLAction implements Runnable {

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public ExecuteSQLAction(IDBConfig config, SQLSourceViewer viewer, String secondaryId) {
		super(config, viewer, secondaryId);
		this.setText(Messages.getString("ExecuteSQLAction.ExecuteAllSQL")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ExecuteSQLAction.ExecuteAllSQLToolTip")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLExecuteActionCommand"); //$NON-NLS-1$
	}

	public void run() {
		try {
			executeSql(getAllSql());

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}
	}

}
