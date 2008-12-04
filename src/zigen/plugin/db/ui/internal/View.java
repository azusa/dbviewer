/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.IDBConfig;

/**
 * Viewクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class View extends Table implements ITable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public View(String name, String remarks) {
		super(name);
		this.remarks = remarks;
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public View(String name) {
		super(name);
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public View() {
		super();
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
		
		View castedObj = (View) o;
		IDBConfig config = castedObj.getDbConfig();
		Schema schema = castedObj.getSchema();
		
		// 追加 2007/08/20
		if (config == null) {
			System.err.println("View#equals() DBConfigを取得できませんでした。");
			return false;
		}
		
		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig()) && schema.equals(getSchema())) {
			return true;
		} else {
			return false;
		}
		
	}
}
