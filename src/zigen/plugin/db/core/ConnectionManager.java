/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */


package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import zigen.plugin.db.DbPlugin;

/**
 * ConnectionManager�N���X.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/16 ZIGEN create.
 */
public class ConnectionManager {

	/**
	 * Connection�I�u�W�F�N�g�̎擾
	 *
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(IDBConfig config) throws Exception {

		if (config == null) {
			throw new IllegalStateException(Messages.getString("ConnectionManager.0")); //$NON-NLS-1$

		}

		Connection con = null;
		DriverManager manager = DriverManager.getInstance();
		Driver driver = manager.getDriver(config);

		if (driver != null) {
			con = driver.connect(config.getUrl(), config.getProperties());
			config.setDriverVersion(con.getMetaData().getDriverVersion());

			config.setDatabaseProductVersion(getDatabaseProductVersion(con));
			config.setDatabaseProductMajorVersion(getDatabaseMajorVersion(con));
			config.setDatabaseProductMinorVersion(getDatabaseMinorVersion(con));

			checkIsolution(con);

		}


		return con;

	}

	private static String getDatabaseProductVersion(Connection con) {
		String version = "Unknown";
		try {
			version = con.getMetaData().getDatabaseProductVersion();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		} catch (Error e) {
			; // symfoware �ł� java.lang.AbstractMethodError����������
		}
		return version;
	}

	private static int getDatabaseMajorVersion(Connection con) {
		int version = 0;
		try {
			version = con.getMetaData().getDatabaseMajorVersion();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		} catch (Error e) {
			;// symfoware �ł� java.lang.AbstractMethodError����������
		}
		return version;
	}

	private static int getDatabaseMinorVersion(Connection con) {
		int version = 0;
		try {
			version = con.getMetaData().getDatabaseMinorVersion();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		} catch (Error e) {
			;// symfoware �ł� java.lang.AbstractMethodError����������
		}
		return version;
	}

	private static void checkIsolution(Connection con) {
		try {
			switch (con.getTransactionIsolation()) {
			case Connection.TRANSACTION_NONE:
				break;
			case Connection.TRANSACTION_READ_COMMITTED:
				break;
			case Connection.TRANSACTION_READ_UNCOMMITTED:
				break;
			case Connection.TRANSACTION_REPEATABLE_READ:
				break;
			case Connection.TRANSACTION_SERIALIZABLE:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showWarningMessage(e.getMessage());
		}
	}

	/**
	 * �R�l�N�V������CLOSE����
	 *
	 * @param con
	 */
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				rollbackConnection(con);
				con.close();
				con = null;
			} catch (SQLException e) {
				DbPlugin.log(e);
			}

			// apache derby �̃t�@�C�����[�h�̏ꍇ�́A�ȉ��̏������K�v

		}
	}

	/**
	 * �R�l�N�V������CLOSE����
	 *
	 * @param con
	 */
	public static void closeConnection(IDBConfig config, Connection con) {
		if (con != null) {
			try {
				rollbackConnection(con);
				con.close();
				con = null;
			} catch (SQLException e) {
				DbPlugin.log(e);
			}

			// apache derby �̃t�@�C�����[�h�̏ꍇ�́A�ȉ��̏������K�v

		}
	}

	public static void shutdown(IDBConfig config) throws Exception {
		DriverManager manager = DriverManager.getInstance();
		try {
			switch (DBType.getType(config)) {

			case DBType.DB_TYPE_DERBY:

				String jdbcDriver = config.getDriverName();
				if (jdbcDriver.indexOf("EmbeddedDriver") > 0) { //$NON-NLS-1$
					// �g�ݍ��݂̏ꍇ�̂�
					Driver driver = manager.getDriver(config);
					driver.connect("jdbc:derby:;shutdown=true", null); //$NON-NLS-1$
				}

				break;
			default:
				break;
			}

		} catch (SQLException e) {
			// derby�̃V���b�g�_�E���͕K��SQL��O����������
			throw e;
		} finally {
			// �L���b�V������폜����
			manager.removeCach(config);
		}

	}

	/**
	 * ���[���o�b�N����
	 *
	 * @param con
	 */
	static void rollbackConnection(Connection con) {
		if (con != null) {
			try {
				if (!con.getAutoCommit()) {
					con.rollback();
				}
			} catch (SQLException e) {
				DbPlugin.log(e);
			}
		}
	}

}
