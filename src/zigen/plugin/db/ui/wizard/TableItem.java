/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.wizard;

import zigen.plugin.db.core.IDBConfig;

/**
 * TableItemクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/21 ZIGEN create.
 * 
 */
public class TableItem implements IItem {

	IDBConfig config;

	boolean checked = false;

	public TableItem(IDBConfig config) {
		this(config, false);
	}

	public TableItem(IDBConfig config, boolean b) {
		this.config = config;
		this.checked = b;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getDbName() {
		return config.getDbName();
	}

	public String getUrl() {
		return config.getUrl();
	}

	public IDBConfig getConfig() {
		return config;
	}

}
