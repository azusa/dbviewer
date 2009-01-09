/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.mysql.MySQLColumnSearcharFactory;
import zigen.plugin.db.core.rule.oracle.OracleColumnSearcharFactory;

/**
 * AbstractColumnSearcherFactory.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/25 ZIGEN create.
 * 
 */
public abstract class AbstractColumnSearcherFactory implements IColumnSearcherFactory {

	protected boolean convertUnicode;

	/**
	 * コンストラクタ
	 * 
	 * @param config
	 */
	public static IColumnSearcherFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName(), config.isConvertUnicode());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param objMet
	 * @param isConvertUnicode
	 */
	public static IColumnSearcherFactory getFactory(DatabaseMetaData objMet, boolean isConvertUnicode) {
		try {
			return getFactory(objMet.getDriverName(), isConvertUnicode);

		} catch (SQLException e) {
			throw new IllegalStateException("DriverNameの取得に失敗しました");
		}

	}

	/**
	 * MappingFactoryのキャッシュ化
	 */
	private static Map map = new HashMap();

	public static IColumnSearcherFactory getFactory(String driverName, boolean isConvertUnicode) {
		IColumnSearcherFactory factory = null;

		String key = driverName + ":" + isConvertUnicode;

		if (map.containsKey(key)) {
			factory = (IColumnSearcherFactory) map.get(key);
			factory.setConvertUnicode(isConvertUnicode);
		} else {
			switch (DBType.getType(driverName)) {
			case DBType.DB_TYPE_ORACLE:
				factory = new OracleColumnSearcharFactory(isConvertUnicode);
				break;
			case DBType.DB_TYPE_MYSQL:
				factory = new MySQLColumnSearcharFactory(isConvertUnicode);
				break;
			default:
				factory = new DefaultColumnSearcherFactory(isConvertUnicode);
				break;
			}

			map.put(key, factory);
		}

		return factory;

	}

	public void setConvertUnicode(boolean convertUnicode) {
		this.convertUnicode = convertUnicode;
	}

}
