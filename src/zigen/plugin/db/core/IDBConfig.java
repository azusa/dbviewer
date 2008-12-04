/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */


package zigen.plugin.db.core;

import java.util.Properties;

/**
 * IDBConfigクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/16 ZIGEN create.
 * 
 */
public interface IDBConfig {

	public Properties getProperties();

	public String getDbName();

	public String getDriverName();

	public String getUrl();

	public String getUserId();

	public String getPassword();

	public String getSchema();

	public String[] getClassPaths();

	public String getCharset();

	public boolean isConvertUnicode();

	public int getDbType(); // DB判定用

	public String getDriverVersion();

	public boolean isAutoCommit();

	public boolean isOnlyDefaultSchema();

	// public String getNullSymbol();

	public void setDbName(String dbName);

	public void setDriverName(String driverName);

	public void setUrl(String url);

	public void setUserId(String userid);

	public void setPassword(String password);

	public void setSchema(String schema);

	public void setClassPaths(String[] classPaths);

	public void setCharset(String charset);

	public void setConvertUnicode(boolean b);

	public void setDriverVersion(String version);

	public void setAutoCommit(boolean b);

	public void setOnlyDefaultSchema(boolean b);

	// public void setNullSymbol(String nullSymbol);

	public void setJdbcType(int type);

	public int getJdbcType();

	public boolean isSavePassword();

	public void setSavePassword(boolean b);

	// for Symfoware
	public boolean isNoLockMode();

	public void setNoLockMode(boolean b);

	public Object clone();

	public int getDataBaseProductMajorVersion();

	public int getDataBaseProductMinorVersion();

	public String getDatabaseProductVersion();

	public void setDatabaseProductMajorVersion(int databaseProductMajorVersion);

	public void setDatabaseProductMinorVersion(int databaseProductMinorVersion);

	public void setDatabaseProductVersion(String databaseProductVersion);
	
	// for Oracle
	public boolean isConnectAsSYSDBA();
	public void setConnectAsSYSDBA(boolean b);
	
	// for Oracle
	public boolean isConnectAsSYSOPER();
	public void setConnectAsSYSOPER(boolean b);
	
	// for MySQL5以降
	public boolean isConnectAsInformationSchema();
	public void setConnectAsInformationSchema(boolean isConnectAsInformationSchema);
	
	public SchemaInfo[] getDisplayedSchemas();
	public void setDisplayedSchemas(SchemaInfo[] schemas);
	
	
	public String getFilterPattern();
	public void setFilterPattern(String filterPattern);

	public boolean isCheckFilterPattern();
	public void setCheckFilterPattern(boolean checkFilterPattern);
}
