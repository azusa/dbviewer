/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import zigen.plugin.db.DbPlugin;

/**
 * TransactionForTableEditor�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/17 ZIGEN create.
 * 
 */
public class TransactionForTableEditor {

	private class TransactionElement {
		IDBConfig config = null;

		Connection con = null;

		int count = 0;

		private TransactionElement(IDBConfig config) {
			this.config = config;
		}

		private boolean isConnecting() {
			return !(con == null);
		}
	}

	private static Hashtable map = new Hashtable();

	private static TransactionForTableEditor instance;

	private IDBConfig config;

	/**
	 * �e�[�u���ҏW��(���R�[�h�̓\��t���j�ł̃G���[�_�C�A���O���o����
	 */
	private boolean showErrorDialog = true;

	/**
	 * �C���X�^���X����
	 * 
	 * @param<code>_instance</code>
	 */
	public synchronized static TransactionForTableEditor getInstance(IDBConfig config) {
		if (instance == null) {
			instance = new TransactionForTableEditor();
		}
		instance.config = config; // Config�̐؂�ւ�
		instance.create();
		return instance;

	}

	/**
	 * �R���X�g���N�^
	 */
	private TransactionForTableEditor() {
	}

	private void create() {
		TransactionElement elem = null;

		if (!map.containsKey(config.getDbName())) {
			map.put(config.getDbName(), new TransactionElement(config));

		} else {
			TransactionElement element = (TransactionElement) map.get(config.getDbName());

			// �R�l�N�V������NULL �܂��� �ڑ����[�U���ύX���ꂽ�ꍇ�̏���
			if (element.con == null || !config.getUserId().equals(element.config.getUserId())) {
				ConnectionManager.closeConnection(element.con);
				element.con = null;
				element.config = config;

			}
		}
	}

	public Connection getConnection() throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		if (element.con == null) {
			element.con = ConnectionManager.getConnection(config);
			element.con.setAutoCommit(false);
			element.count = 0;
		}

		// �e�[�u���ҏW�p�̃R�l�N�V�����͏�Ɏ蓮�R�~�b�g�Ƃ���
		// if (config.isAutoCommit()) {
		// log.debug("�����R�~�b�g���[�h�̃R�l�N�V�������擾���܂���");
		// if (!element.con.getAutoCommit()) {
		// element.con.setAutoCommit(true);
		// }
		// } else {
		// log.debug("�蓮�R�~�b�g���[�h�̃R�l�N�V�������擾���܂���");
		// if (element.con.getAutoCommit()) {
		// element.con.setAutoCommit(false);
		// }
		// }
		
		//System.out.println("TransactionForTableEditor#�R�~�b�g�̏�Ԃ� " + element.con.getAutoCommit() + ", @" + element.con.toString());
		return element.con;

	}

	public void cloesConnection() {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		ConnectionManager.closeConnection(element.con);
		element.con = null; // �����I��NULL��ݒ肷��
	}

	public boolean isConneting() {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		return element.isConnecting();
	}

	public void setCount(int rowAffected) throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		element.count = element.count + rowAffected;
	}

	public int getTransactionCount() {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		return element.count;
	}

	public void commit() throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		if (element.con != null) {
			element.con.commit();
			element.count = 0;
		}
	}

	public void rollback() {
		try {
			TransactionElement element = (TransactionElement) map.get(config.getDbName());
			if (element.con != null) {
				element.con.rollback();
				element.count = 0;
			}
		} catch (SQLException e) {
			DbPlugin.log(e);
		}
	}

	public IDBConfig getConfig() {
		return this.config;
	}

	public synchronized static void destroy() {
		Enumeration e = map.keys();
		while (e.hasMoreElements()) {
			Object k = e.nextElement();
			TransactionElement elem = (TransactionElement) map.get(k);
			ConnectionManager.closeConnection(elem.con);
			elem.con = null;
		}
	}

	public boolean isShowErrorDialog() {
		return showErrorDialog;
	}

	public void setShowErrorDialog(boolean showErrorDialog) {
		this.showErrorDialog = showErrorDialog;
	}
}
