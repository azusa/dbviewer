/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.sql.Connection;

import zigen.plugin.db.core.IDBConfig;

/**
 * DataBase�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create. [2] 2005/03/10 ZIGEN IDBConfig���X�V�ł��郁�\�b�h��ǉ�.
 * 
 */
public class DataBase extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	IDBConfig dbConfig = null;
	
	boolean isConnected = false; // �ڑ���Ԃł����true
	
	boolean isSchemaSupport = false; // �X�L�[�}�T�|�[�g��
	
	String defaultSchema;
	
	String[] tableType = null;
	
	Connection con = null;
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public DataBase(IDBConfig dbConfig) {
		super(dbConfig.getDbName());
		this.dbConfig = dbConfig;
	}
	
	/**
	 * IDBConfig�̎擾
	 * 
	 * @return
	 */
	public IDBConfig getDbConfig() {
		return this.dbConfig;
	}
	
	// �� [002] 2005/08/05 �ǉ� ZIGEN
	/**
	 * DBConfig�̐ݒ� DB�ڑ����ύX����config���㏑�����邽�߂Ɏ���
	 * 
	 * @param config
	 */
	public void setDbConfig(IDBConfig dbConfig) {
		this.setName(dbConfig.getDbName());
		this.dbConfig = dbConfig;
		
	}
	
	// �� [002] 2005/08/05 �ǉ� ZIGEN
	
	/**
	 * @return isConnected ��߂��܂��B
	 */
	public boolean isConnected() {
		return isConnected;
	}
	
	/**
	 * @param isConnected
	 *            isConnected ��ݒ�B
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	/**
	 * @return defaultSchema ��߂��܂��B
	 */
	public String getDefaultSchema() {
		return defaultSchema;
	}
	
	/**
	 * @param defaultSchema
	 *            defaultSchema ��ݒ�B
	 */
	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}
	
	/**
	 * @return isSchemaSupport ��߂��܂��B
	 */
	public boolean isSchemaSupport() {
		return isSchemaSupport;
	}
	
	/**
	 * @param isSchemaSupport
	 *            isSchemaSupport ��ݒ�B
	 */
	public void setSchemaSupport(boolean isSchemaSupport) {
		this.isSchemaSupport = isSchemaSupport;
	}
	
	/**
	 * @return tableType ��߂��܂��B
	 */
	public String[] getTableType() {
		return tableType;
	}
	
	/**
	 * @param tableType
	 *            tableType ��ݒ�B
	 */
	public void setTableType(String[] tableType) {
		this.tableType = tableType;
	}
	
	/**
	 * �R���X�g���N�^
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
	 * �ȉ���equals���\�b�h�͕ύX���Ȃ�����
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
