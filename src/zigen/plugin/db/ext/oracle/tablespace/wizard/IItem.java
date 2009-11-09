/*
 * Copyright (c) 2007Å|2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.math.BigDecimal;

/**
 * IItemÉNÉâÉX.
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
