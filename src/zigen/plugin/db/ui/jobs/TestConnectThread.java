/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.Transaction;

public class TestConnectThread implements Runnable {

	int timeout;

	IDBConfig config;

	boolean isSuccess = false;

	String message;

	Throwable throwable;

	boolean isAlive = false;

	public TestConnectThread(IDBConfig config, int timeout) {
		this(config, timeout, false);
	}

	public TestConnectThread(IDBConfig config, int timeout, boolean isAlive) {
		this.config = config;
		this.timeout = timeout;
		this.isAlive = isAlive;

		StringBuffer sb = new StringBuffer();
		sb.append(Messages.getString("TestConnectThread.0")); //$NON-NLS-1$
		sb.append(Messages.getString("TestConnectThread.1")); //$NON-NLS-1$
		message = sb.toString();
	}

	/**
	 * �e�X�g�p�R�l�N�V�����ڑ��́ATransaction�ł͂Ȃ��AConnectionManager����R�l�N�V�������擾����
	 */
	public void run() {
		Connection con = null;
//		Transaction trans = Transaction.getInstance(config);
		try {
//			con = trans.getConnection();
			con = ConnectionManager.getConnection(config);

//			if(SchemaSearcher.isSupport(con)){
//				SchemaSearcher.existSchemaName(con, config.getSchema());
//			}

			this.isSuccess = true;
			this.message = Messages.getString("TestConnectThread.2"); //$NON-NLS-1$

		} catch (Exception e) {
			DbPlugin.log(e);
			this.message = Messages.getString("TestConnectThread.3"); //$NON-NLS-1$
			this.message += "\n" + e.getMessage(); // ��O�̃��b�Z�[�W��\������悤�ɕύX
			this.throwable = e;
			this.isSuccess = false;

		} finally {
//			if (isSuccess && !isAlive) {
//				trans.cloesConnection();
//			}
			ConnectionManager.closeConnection(con);
		}
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getMessage() {
		return message;
	}

	protected Throwable getThrowable() {
		return throwable;
	}

	protected void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

}
