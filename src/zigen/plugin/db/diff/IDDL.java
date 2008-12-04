/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

public interface IDDL {

	public abstract String getDisplayedName();

	public abstract String getDbName();

	public abstract String getDdl();

	public abstract String getSchemaName();

	public abstract String getTargetName();

	public abstract boolean isSchemaSupport();

	public String getType();
}
