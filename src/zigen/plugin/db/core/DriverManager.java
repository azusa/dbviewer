/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Driver;
import java.util.HashMap;

/**
 * DriverManager�N���X. Singleton�p�^�[�����̗p�����N���X
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/16 ZIGEN create.
 *
 */
public class DriverManager {

	private static DriverManager _instance;

	private HashMap driverMap = new HashMap();

	/**
	 * Private�̃R���X�g���N�^
	 *
	 */
	private DriverManager() {}

	private String getKey(IDBConfig config) {
		String key;
		if (config.getJdbcType() == 2) {
			// TYPE2�̏ꍇ�́ADriver���ŃL���b�V������
			// ��Version�Ⴂ��Driver�͎������Ƃ͂ł��܂���
			key = config.getDriverName();
		} else {
			// TYPE4�̏ꍇ�́A�_��DB���{Driver���ŃL���b�V��
			// ��Version�Ⴂ��Driver��o�^�ł���悤�ɂ��邽��
			key = config.getDbName() + config.getDriverName();
		}

		return key;
	}

	public void removeCach(IDBConfig config) {
//		driverMap.remove(getKey(config));
		if (config.getJdbcType() == 2) {
			// TYPE2�̏ꍇ�́ADriver�̃L���b�V�����폜���܂���B(��SymfoWARE�p)
			// ��Version�Ⴂ��Driver�͎������Ƃ͂ł��܂���
		} else {
			// TYPE4�̏ꍇ�́ADriver�̃L���b�V�����폜����
			driverMap.remove(getKey(config));
		}
	}

	/**
	 * �C���X�^���X����
	 */
	public synchronized static DriverManager getInstance() {
		if (_instance == null) {
			_instance = new DriverManager();
		} else {
		}
		return _instance;

	}

	/**
	 * Driver�I�u�W�F�N�g�̎擾
	 *
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public Driver getDriver(IDBConfig config) throws Exception {
		String key = getKey(config);

		if (driverMap.containsKey(key)) {
			return (Driver) driverMap.get(key);
		} else {
			Driver driver = getDriver(config.getDriverName(), config.getClassPaths());
			driverMap.put(key, driver);
			return driver;
		}

	}

	/**
	 * Driver�I�u�W�F�N�g�̎擾
	 *
	 * @param driverName
	 * @param classpaths
	 * @return
	 * @throws Exception
	 */
	private Driver getDriver(String driverName, String[] classpaths) throws Exception {
		Class driverClass = null;
		PluginClassLoader loader = PluginClassLoader.getClassLoader(classpaths, getClass().getClassLoader());
		try {
			driverClass = loader.loadClass(driverName);
		} catch (ClassNotFoundException e) {
			try {
				driverClass = PluginClassLoader.getSystemClassLoader().loadClass(driverName);
			} catch (ClassNotFoundException ex) {
				throw ex;
			}
		}
		return (Driver) driverClass.newInstance();
	}

}
