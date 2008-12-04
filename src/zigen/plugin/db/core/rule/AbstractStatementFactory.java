/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.mysql.MySQLStatementFactory;
import zigen.plugin.db.core.rule.oracle.OracleStatementFactory;
import zigen.plugin.db.preference.PreferencePage;

/**
 * 
 * AbstractInsertMappingFactory.java�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/06 ZIGEN create.
 * 
 */
public abstract class AbstractStatementFactory implements IStatementFactory {

	protected static String NULL = "null";

	protected String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

	protected boolean convertUnicode;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param config
	 */
	public static IStatementFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName(), config.isConvertUnicode());
	}

	/**
	 * �R���X�g���N�^
	 * 
	 * @param objMet
	 * @param isConvertUnicode
	 */
	public static IStatementFactory getFactory(DatabaseMetaData objMet, boolean isConvertUnicode) {
		try {
			return getFactory(objMet.getDriverName(), isConvertUnicode);

		} catch (SQLException e) {
			throw new IllegalStateException("DriverName�̎擾�Ɏ��s���܂���");
		}

	}

	/**
	 * Factory�̃L���b�V����
	 */
	private static Map map = new HashMap();

	public static IStatementFactory getFactory(String driverName, boolean isConvertUnicode) {

		IStatementFactory factory = null;

		String key = driverName + ":" + isConvertUnicode;

		if (map.containsKey(key)) {
			factory = (IStatementFactory) map.get(key);
		} else {
			switch (DBType.getType(driverName)) {

			case DBType.DB_TYPE_ORACLE:
				factory = new OracleStatementFactory(isConvertUnicode);
				break;
			case DBType.DB_TYPE_MYSQL:
				factory = new MySQLStatementFactory(isConvertUnicode);
				break;
			default:
				factory = new DefaultStatementFactory(isConvertUnicode);
				break;
			}

			map.put(key, factory);
		}
		return factory;

	}

}
