/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.sql.Connection;

import zigen.plugin.db.core.IDBConfig;

/**
 * DataBaseクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create. [2] 2005/03/10 ZIGEN IDBConfigを更新できるメソッドを追加.
 * 
 */
public class DataBase extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	IDBConfig dbConfig = null;
	
	boolean isConnected = false; // 接続状態であればtrue
	
	boolean isSchemaSupport = false; // スキーマサポートか
	
	String defaultSchema;
	
	String[] tableType = null;
	
	Connection con = null;
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public DataBase(IDBConfig dbConfig) {
		super(dbConfig.getDbName());
		this.dbConfig = dbConfig;
	}
	
	/**
	 * IDBConfigの取得
	 * 
	 * @return
	 */
	public IDBConfig getDbConfig() {
		return this.dbConfig;
	}
	
	// ↓ [002] 2005/08/05 追加 ZIGEN
	/**
	 * DBConfigの設定 DB接続情報変更時にconfigを上書きするために実装
	 * 
	 * @param config
	 */
	public void setDbConfig(IDBConfig dbConfig) {
		this.setName(dbConfig.getDbName());
		this.dbConfig = dbConfig;
		
	}
	
	// ↑ [002] 2005/08/05 追加 ZIGEN
	
	/**
	 * @return isConnected を戻します。
	 */
	public boolean isConnected() {
		return isConnected;
	}
	
	/**
	 * @param isConnected
	 *            isConnected を設定。
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	/**
	 * @return defaultSchema を戻します。
	 */
	public String getDefaultSchema() {
		return defaultSchema;
	}
	
	/**
	 * @param defaultSchema
	 *            defaultSchema を設定。
	 */
	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}
	
	/**
	 * @return isSchemaSupport を戻します。
	 */
	public boolean isSchemaSupport() {
		return isSchemaSupport;
	}
	
	/**
	 * @param isSchemaSupport
	 *            isSchemaSupport を設定。
	 */
	public void setSchemaSupport(boolean isSchemaSupport) {
		this.isSchemaSupport = isSchemaSupport;
	}
	
	/**
	 * @return tableType を戻します。
	 */
	public String[] getTableType() {
		return tableType;
	}
	
	/**
	 * @param tableType
	 *            tableType を設定。
	 */
	public void setTableType(String[] tableType) {
		this.tableType = tableType;
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public DataBase() {
		super();
	}
	
	public Object clone() {
		DataBase inst = new DataBase();
		inst.name = this.name == null ? null : new String(this.name);
		inst.dbConfig = this.dbConfig == null ? null : (IDBConfig) this.dbConfig.clone();
		inst.isConnected = this.isConnected;
		inst.isSchemaSupport = this.isSchemaSupport;
		inst.defaultSchema = this.defaultSchema == null ? null : new String(this.defaultSchema);
		if (this.tableType != null) {
			inst.tableType = new String[this.tableType.length];
			for (int i0 = 0; i0 < this.tableType.length; i0++) {
				inst.tableType[i0] = this.tableType[i0] == null ? null : new String(this.tableType[i0]);
			}
		} else {
			inst.tableType = null;
		}
		return inst;
	}
	
	/**
	 * 以下のequalsメソッドは変更しないこと
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
		DataBase castedObj = (DataBase) o;
		IDBConfig config = castedObj.getDbConfig();
		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig())) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
