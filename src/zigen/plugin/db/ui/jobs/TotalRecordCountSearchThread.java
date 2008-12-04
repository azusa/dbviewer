/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
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
			// カウント取得用に新規のConnectionを取得
			con = ConnectionManager.getConnection(trans.getConfig());		
			this.count = SQLInvoker.executeQueryTotalCount(con, query, timeoutSec);
			this.isComplete = true;
		} catch (Exception e) {
			System.err.println("実行したSQL = " + query);
			DbPlugin.log(e);
		} finally {
			ConnectionManager.closeConnection(con);
		}
	}

}
