/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import zigen.plugin.db.core.IDBConfig;

public interface IQueryViewEditor {

	public abstract IDBConfig getDBConfig();

	public abstract String getQuery();

}
