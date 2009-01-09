/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.DataBase;

/**
 * ShowDriverVersionActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class ShowDriverVersionAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public ShowDriverVersionAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("ShowDriverVersionAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ShowDriverVersionAction.1")); //$NON-NLS-1$
		this.setEnabled(false);

	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof DataBase) {
			DataBase db = (DataBase) element;

			try {

				IDBConfig config = db.getDbConfig();
				StringBuffer sb = new StringBuffer();
				sb.append("Driver version : " + config.getDriverVersion());
				sb.append("\n\n");
				sb.append(config.getDatabaseProductVersion());

				DbPlugin.getDefault().showInformationMessage(sb.toString()); //$NON-NLS-1$

			} catch (Exception e) {
				DbPlugin.log(e);
			}
		}
	}
}
