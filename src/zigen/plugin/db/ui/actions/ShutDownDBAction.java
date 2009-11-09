/*
 * Copyright (c) 2007�|2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.sql.SQLException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.ui.internal.DataBase;

/**
 * CloseDBAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 */
public class ShutDownDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public ShutDownDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("ShutDownDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ShutDownDBAction.1")); //$NON-NLS-1$

	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof DataBase) {
			DataBase db = (DataBase) element;
			try {
				ConnectionManager.shutdown(db.getDbConfig());
			} catch (SQLException e) {
				if (DBType.getType(db.getDbConfig()) == DBType.DB_TYPE_DERBY) {
					if (e.getErrorCode() == 50000) {
						// Derby�̐���V���b�g�_�E���́A�G���[�R�[�h��50000
						DbPlugin.getDefault().showInformationMessage(e.getMessage());
						return;
					}
				}
				DbPlugin.getDefault().showErrorDialog(e);

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);

			}
		}
	}

}
