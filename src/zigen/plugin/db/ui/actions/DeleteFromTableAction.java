/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.sql.Connection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.ui.internal.ITable;

/**
 * PURGE RECYCLEBIN���sAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note
 * 
 */
public class DeleteFromTableAction extends Action implements Runnable {

	public static final String SQL = "DELETE FROM "; //$NON-NLS-1$

	private StructuredViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public DeleteFromTableAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DeleteFromTableAction.1")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DeleteFromTableAction.2")); //$NON-NLS-1$

	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		Connection con = null;
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof ITable) {
			ITable table = (ITable) element;
			IDBConfig config = table.getDbConfig();
			try {
				if (DbPlugin.getDefault().confirmDialog(Messages.getString("DeleteFromTableAction.3"))) { //$NON-NLS-1$

					// <-- ��Q�Ή��F������ɃR�l�N�V�������N���[�Y����Ă��܂��B�̃G���[���ł�B(�V�����R�l�N�V�������g�p����)
					// con = ConnectionManager.getConnection(config);
					// con = Transaction.getInstance(config).getConnection();
					con = ConnectionManager.getConnection(config);

					con.setAutoCommit(false);
					// SQLInvoker.executeUpdate(table.getDbConfig(), SQL + table.getSqlTableName());
					// <��Q�Ή�>
					SQLInvoker.executeUpdate(con, SQL + table.getSqlTableName());
					con.commit();
				}

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			} finally {
				ConnectionManager.closeConnection(con);
			}
		}

	}

}
