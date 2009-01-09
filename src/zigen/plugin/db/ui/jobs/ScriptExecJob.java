/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.actions.ConfirmConnectDBAction;

public class ScriptExecJob extends SqlExecJob {

	public ScriptExecJob(Transaction trans, String sqlString, String secondarlyId) {
		super(trans, sqlString, secondarlyId);
		super.setName(Messages.getString("ScriptExecJob.0")); //$NON-NLS-1$
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			if (!trans.isConneting()) {
				Display.getDefault().syncExec(new ConfirmConnectDBAction(trans));
				if (!trans.isConneting()) {
					showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
					return Status.CANCEL_STATUS;
				}
			}

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			executeSingleSQL(trans, sqlString);

			return Status.OK_STATUS;

		} catch (SQLException e) {
			showWarningMessage(e.getMessage());

		} catch (Exception e) {
			showErrorMessage(Messages.getString("SqlExecJob.2"), e); //$NON-NLS-1$

		} finally {
			// �������Ԃ̕\��
			// updateResponseTime(getTotalTime());
		}
		return Status.OK_STATUS; // �G���[�_�C�A���O��\�����邽�߂�OK�ŕԂ�

	}

}
