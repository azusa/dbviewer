/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.Transaction;

public class TotalRecordCountSearchThread implements Runnable {
	long count = 0;

	int timeoutSec;

	String query;

	boolean isComplete = false;

	Transaction trans;

	public TotalRecordCountSearchThread(Transaction trans, String query, int timeoutSec) {
		this.query = query;
		this.timeoutSec = timeoutSec;
		this.trans = trans;
	}

	public void run() {
		Connection con = null;
		try {
			// �J�E���g�擾�p�ɐV�K��Connection���擾
			con = ConnectionManager.getConnection(trans.getConfig());		
			this.count = SQLInvoker.executeQueryTotalCount(con, query, timeoutSec);
			this.isComplete = true;
		} catch (Exception e) {
			System.err.println("���s����SQL = " + query);
			DbPlugin.log(e);
		} finally {
			ConnectionManager.closeConnection(con);
		}
	}

}
