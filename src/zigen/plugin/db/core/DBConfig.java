/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;
import java.util.Properties;

/**
 * DBConfigクラス.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create. [2] 2005/09/27 ZIGEN create.
 *
 */
public class DBConfig implements IDBConfig, Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	public static final int JDBC_DRIVER_TYPE_2 = 2;

	public static final int JDBC_DRIVER_TYPE_4 = 4;

	private String dbName;

	private String driverName;

	private String url;

	private String userId;

	private String password;

	private String schema;

	private String[] classPaths;

	private String charset;

	private boolean isConvertUnicode;

	private int dbType = DBType.DB_TYPE_UNKNOWN;

	private boolean isAutoCommit;

	private boolean onlyDefaultSchema;

	private int jdbcType = JDBC_DRIVER_TYPE_4;

	private boolean isSavePassword;

	private String driverVersion;

	private String databaseProductVersion;

	private int dataBaseProductMajorVersion;

	private int dataBaseProductMinorVersion;

	private boolean isConnectAsSYSDBA; // for Oracle

	private boolean isConnectAsSYSOPER; // for Oracle

	private boolean isConnectAsInformationSchema; // for MySQL5

	private SchemaInfo[] displayedSchemas;

	private String filterPattern;

	private boolean checkFilterPattern;


	/**
	 * コンストラクタ
	 */
	public DBConfig() {}

	/**
	 * @return classPath を戻します。
	 */
	public String[] getClassPaths() {
		return classPaths;
	}

	/**
	 * @param classPath
	 *            classPath を設定。
	 */
	public void setClassPaths(String[] classPaths) {
		this.classPaths = classPaths;
	}

	/**
	 * @return dbName を戻します。
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName
	 *            dbName を設定。
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return driver を戻します。
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverNae
	 *            driverName を設定。
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
		this.dbType = DBType.getType(driverName);

	}

	/**
	 * @return password を戻します。
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            password を設定。
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * JDBC接続用のPropertiesクラスを返す。
	 *
	 * @return properties を戻します。
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty("user", this.getUserId()); //$NON-NLS-1$
		properties.setProperty("password", this.getPassword()); //$NON-NLS-1$
		if(this.getSchema() != null){
			properties.setProperty("schema", this.getSchema());//$NON-NLS-1$
		}

		// charSetの追加
		if (this.getCharset() != null && !this.getCharset().equals("")) { //$NON-NLS-1$

			switch (DBType.getType(driverName)) {
			case DBType.DB_TYPE_MYSQL:
				properties.setProperty("charSet", this.getCharset()); //$NON-NLS-1$
				properties.setProperty("useUnicode", "true"); //$NON-NLS-1$ //$NON-NLS-2$
				properties.setProperty("characterEncoding", this.getCharset()); //$NON-NLS-1$

				break;
			default:
				properties.setProperty("charSet", this.getCharset()); //$NON-NLS-1$
				properties.setProperty("useUnicode", "true"); //$NON-NLS-1$ //$NON-NLS-2$
				properties.setProperty("characterEncoding", this.getCharset()); //$NON-NLS-1$
				break;

			}

		}
		if (DBType.getType(driverName) == DBType.DB_TYPE_ORACLE) {
			if (isConnectAsSYSDBA) {
				properties.setProperty("internal_logon", "sysdba"); //$NON-NLS-1$
			}
			if (isConnectAsSYSOPER) {
				properties.setProperty("internal_logon", "sysoper"); //$NON-NLS-1$
			}
		} else if (DBType.getType(driverName) == DBType.DB_TYPE_MYSQL) {
			if (isConnectAsInformationSchema) {
				properties.setProperty("useInformationSchema", "true"); //$NON-NLS-1$
			} else {
				properties.setProperty("useInformationSchema", "false"); //$NON-NLS-1$
			}

		}

		return properties;
	}

	/**
	 * @return url を戻します。
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            url を設定。
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return userId を戻します。
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            userId を設定。
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return charset を戻します。
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset
	 *            charset を設定。
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return schema を戻します。
	 */
	public String getSchema() {
		if (this.schema != null) {
			return schema;
		} else {
			// スキーマ指定が無い場合は、接続ユーザとする
			return userId;
		}
	}

	/**
	 * @param schema
	 *            schema を設定。
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public boolean isConvertUnicode() {
		return isConvertUnicode;
	}

	public void setConvertUnicode(boolean isConvertUnicode) {
		this.isConvertUnicode = isConvertUnicode;
	}

	public int getDbType() {
		return dbType;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	public boolean isOnlyDefaultSchema() {
		return this.onlyDefaultSchema;
	}

	public void setOnlyDefaultSchema(boolean b) {
		this.onlyDefaultSchema = b;

	}

	boolean isNoLockMode;

	public boolean isNoLockMode() {
		return isNoLockMode;
	}

	public void setNoLockMode(boolean isNoLockMode) {
		this.isNoLockMode = isNoLockMode;
	}

	/**
	 * Returns <code>true</code> if this <code>DBConfig</code> is the same as the o argument.
	 *
	 * @return <code>true</code> if this <code>DBConfig</code> is the same as the o argument.
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		DBConfig castedObj = (DBConfig) o;
		return ((this.dbName == null ? castedObj.dbName == null : this.dbName.equals(castedObj.dbName))
				&& (this.driverName == null ? castedObj.driverName == null : this.driverName.equals(castedObj.driverName))
				&& (this.url == null ? castedObj.url == null : this.url.equals(castedObj.url))
				&& (this.userId == null ? castedObj.userId == null : this.userId.equals(castedObj.userId))
				&& (this.password == null ? castedObj.password == null : this.password.equals(castedObj.password))
				&& (this.schema == null ? castedObj.schema == null : this.schema.equals(castedObj.schema)) && java.util.Arrays.equals(this.classPaths, castedObj.classPaths)
				&& (this.charset == null ? castedObj.charset == null : this.charset.equals(castedObj.charset)) && (this.isConvertUnicode == castedObj.isConvertUnicode)
				&& (this.dbType == castedObj.dbType) && (this.driverVersion == null ? castedObj.driverVersion == null : this.driverVersion.equals(castedObj.driverVersion))
				&& (this.isAutoCommit == castedObj.isAutoCommit) && (this.onlyDefaultSchema == castedObj.onlyDefaultSchema) && (this.jdbcType == castedObj.jdbcType)
				&& (this.isSavePassword == castedObj.isSavePassword) && (this.isNoLockMode == castedObj.isNoLockMode));
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
	}

	public boolean isSavePassword() {
		return isSavePassword;
	}

	public void setSavePassword(boolean b) {
		this.isSavePassword = b;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[DBConfig:"); //$NON-NLS-1$
		buffer.append(" JDBC_DRIVER_TYPE_2: "); //$NON-NLS-1$
		buffer.append(JDBC_DRIVER_TYPE_2);
		buffer.append(" JDBC_DRIVER_TYPE_4: "); //$NON-NLS-1$
		buffer.append(JDBC_DRIVER_TYPE_4);
		buffer.append(" dbName: "); //$NON-NLS-1$
		buffer.append(dbName);
		buffer.append(" driverName: "); //$NON-NLS-1$
		buffer.append(driverName);
		buffer.append(" url: "); //$NON-NLS-1$
		buffer.append(url);
		buffer.append(" userId: "); //$NON-NLS-1$
		buffer.append(userId);
		buffer.append(" password: "); //$NON-NLS-1$
		buffer.append(password);
		buffer.append(" schema: "); //$NON-NLS-1$
		buffer.append(schema);
		buffer.append(" { "); //$NON-NLS-1$
		for (int i0 = 0; classPaths != null && i0 < classPaths.length; i0++) {
			buffer.append(" classPaths[" + i0 + "]: "); //$NON-NLS-1$ //$NON-NLS-2$
			buffer.append(classPaths[i0]);
		}
		buffer.append(" } "); //$NON-NLS-1$
		buffer.append(" charset: "); //$NON-NLS-1$
		buffer.append(charset);
		buffer.append(" isConvertUnicode: "); //$NON-NLS-1$
		buffer.append(isConvertUnicode);
		buffer.append(" dbType: "); //$NON-NLS-1$
		buffer.append(dbType);
		buffer.append(" driverVersion: "); //$NON-NLS-1$
		buffer.append(driverVersion);
		buffer.append(" isAutoCommit: "); //$NON-NLS-1$
		buffer.append(isAutoCommit);
		buffer.append(" onlyDefaultSchema: "); //$NON-NLS-1$
		buffer.append(onlyDefaultSchema);
		buffer.append(" jdbcType: "); //$NON-NLS-1$
		buffer.append(jdbcType);
		buffer.append(" isSavePassword: "); //$NON-NLS-1$
		buffer.append(isSavePassword);
		buffer.append(" isNoLockMode: "); //$NON-NLS-1$
		buffer.append(isNoLockMode);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	public Object clone() {
		DBConfig inst = new DBConfig();
		inst.dbName = this.dbName == null ? null : new String(this.dbName);
		inst.driverName = this.driverName == null ? null : new String(this.driverName);
		inst.url = this.url == null ? null : new String(this.url);
		inst.userId = this.userId == null ? null : new String(this.userId);
		inst.password = this.password == null ? null : new String(this.password);
		inst.schema = this.schema == null ? null : new String(this.schema);
		if (this.classPaths != null) {
			inst.classPaths = new String[this.classPaths.length];
			for (int i0 = 0; i0 < this.classPaths.length; i0++) {
				inst.classPaths[i0] = this.classPaths[i0] == null ? null : new String(this.classPaths[i0]);
			}
		} else {
			inst.classPaths = null;
		}
		inst.charset = this.charset == null ? null : new String(this.charset);
		inst.isConvertUnicode = this.isConvertUnicode;
		inst.dbType = this.dbType;
		inst.driverVersion = this.driverVersion == null ? null : new String(this.driverVersion);
		inst.isAutoCommit = this.isAutoCommit;
		inst.onlyDefaultSchema = this.onlyDefaultSchema;
		inst.jdbcType = this.jdbcType;
		inst.isSavePassword = this.isSavePassword;
		inst.isNoLockMode = this.isNoLockMode;

		inst.dataBaseProductMajorVersion = this.dataBaseProductMajorVersion;
		inst.dataBaseProductMinorVersion = this.dataBaseProductMinorVersion;
		inst.databaseProductVersion = this.databaseProductVersion;

		inst.isConnectAsSYSDBA = this.isConnectAsSYSDBA;
		inst.isConnectAsSYSOPER = this.isConnectAsSYSOPER;
		// add
		inst.displayedSchemas = this.displayedSchemas;

		if (this.displayedSchemas != null) {
			inst.displayedSchemas = new SchemaInfo[this.displayedSchemas.length];
			for (int i0 = 0; i0 < this.displayedSchemas.length; i0++) {
				inst.displayedSchemas[i0] = this.displayedSchemas[i0] == null ? null : this.displayedSchemas[i0];
				if (inst.displayedSchemas != null) {
					inst.displayedSchemas[i0].setConfig(inst);// コピーしたものにSchemaInfoの参照を与えること
				}
			}
		} else {
			inst.displayedSchemas = null;
		}
		inst.checkFilterPattern = this.checkFilterPattern;
		inst.filterPattern = this.filterPattern;

		return inst;
	}

	public int getDataBaseProductMajorVersion() {
		return dataBaseProductMajorVersion;
	}

	public void setDatabaseProductMajorVersion(int dataBaseProductMajorVersion) {
		this.dataBaseProductMajorVersion = dataBaseProductMajorVersion;
	}

	public int getDataBaseProductMinorVersion() {
		return dataBaseProductMinorVersion;
	}

	public void setDatabaseProductMinorVersion(int dataBaseProductMinorVersion) {
		this.dataBaseProductMinorVersion = dataBaseProductMinorVersion;
	}

	public String getDatabaseProductVersion() {
		return databaseProductVersion;
	}

	public void setDatabaseProductVersion(String databaseProductVersion) {
		this.databaseProductVersion = databaseProductVersion;
	}


	public boolean isConnectAsSYSDBA() {
		return isConnectAsSYSDBA;
	}

	public boolean isConnectAsSYSOPER() {
		return isConnectAsSYSOPER;
	}

	public void setConnectAsSYSDBA(boolean b) {
		this.isConnectAsSYSDBA = b;
	}

	public void setConnectAsSYSOPER(boolean b) {
		this.isConnectAsSYSOPER = b;
	}


	public SchemaInfo[] getDisplayedSchemas() {
		if (displayedSchemas != null) {
			for (int i = 0; i < displayedSchemas.length; i++) {
				displayedSchemas[i].setConfig(this);
			}
		}
		return this.displayedSchemas;
	}

	public void setDisplayedSchemas(SchemaInfo[] schemas) {
		this.displayedSchemas = schemas;
	}

	public String getFilterPattern() {
		return filterPattern;
	}

	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}


	public boolean isCheckFilterPattern() {
		return checkFilterPattern;
	}

	public void setCheckFilterPattern(boolean checkFilterPattern) {
		this.checkFilterPattern = checkFilterPattern;
	}

	public boolean isConnectAsInformationSchema() {
		return isConnectAsInformationSchema;
	}

	public void setConnectAsInformationSchema(boolean isConnectAsInformationSchema) {
		this.isConnectAsInformationSchema = isConnectAsInformationSchema;
	}
}
