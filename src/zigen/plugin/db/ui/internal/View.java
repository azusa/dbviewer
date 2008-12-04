/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.IDBConfig;

/**
 * View�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class View extends Table implements ITable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public View(String name, String remarks) {
		super(name);
		this.remarks = remarks;
	}
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public View(String name) {
		super(name);
	}
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public View() {
		super();
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
		
		View castedObj = (View) o;
		IDBConfig config = castedObj.getDbConfig();
		Schema schema = castedObj.getSchema();
		
		// �ǉ� 2007/08/20
		if (config == null) {
			System.err.println("View#equals() DBConfig���擾�ł��܂���ł����B");
			return false;
		}
		
		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig()) && schema.equals(getSchema())) {
			return true;
		} else {
			return false;
		}
		
	}
}
