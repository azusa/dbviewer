/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Driver;
import java.util.HashMap;

/**
 * DriverManagerクラス. Singletonパターンを採用したクラス
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
	 * Privateのコンストラクタ
	 *
	 */
	private DriverManager() {}

	private String getKey(IDBConfig config) {
		String key;
		if (config.getJdbcType() == 2) {
			// TYPE2の場合は、Driver名でキャッシュする
			// ※Version違いのDriverは試すことはできません
			key = config.getDriverName();
		} else {
			// TYPE4の場合は、論理DB名＋Driver名でキャッシュ
			// ※Version違いのDriverを登録できるようにするため
			key = config.getDbName() + config.getDriverName();
		}

		return key;
	}

	public void removeCach(IDBConfig config) {
//		driverMap.remove(getKey(config));
		if (config.getJdbcType() == 2) {
			// TYPE2の場合は、Driverのキャッシュを削除しません。(対SymfoWARE用)
			// ※Version違いのDriverは試すことはできません
		} else {
			// TYPE4の場合は、Driverのキャッシュを削除する
			driverMap.remove(getKey(config));
		}
	}

	/**
	 * インスタンス生成
	 */
	public synchronized static DriverManager getInstance() {
		if (_instance == null) {
			_instance = new DriverManager();
		} else {
		}
		return _instance;

	}

	/**
	 * Driverオブジェクトの取得
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
	 * Driverオブジェクトの取得
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
