/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;

/**
 * ShowDriverVersionActionクラス.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 *
 */
public class ShowPluginVersionAction extends Action implements Runnable {

	/**
	 * コンストラクタ
	 *
	 * @param viewer
	 */
	public ShowPluginVersionAction() {
		this.setText(Messages.getString("ShowPluginVersionAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ShowPluginVersionAction.1")); //$NON-NLS-1$
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		try {

			StringBuffer sb = new StringBuffer();
			sb.append("DBViewerPlugin Version : " + DbPlugin.getPluginVersion()); //$NON-NLS-1$

			DbPlugin.getDefault().showInformationMessage(sb.toString());

		} catch (Exception e) {
			DbPlugin.log(e);
		}

	}

}
