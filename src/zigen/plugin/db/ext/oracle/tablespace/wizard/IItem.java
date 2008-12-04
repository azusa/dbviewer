/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.math.BigDecimal;

/**
 * IItemクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/02 ZIGEN create.
 * 
 */
public interface IItem {
	public String getTableName();

	public String getIndexName();

	// public int getDbBlockSize();

	public int getPctFree();

	public long getRecordSize();

	public BigDecimal getTableSpaceSize();

	public double getSafeCoefficient();

	public BigDecimal getTableSpaceSafeSize();

	// public void setDbBlockSize(int dbBlockSize);

	public boolean isChecked();
}
