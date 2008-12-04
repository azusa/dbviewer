/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.math.BigDecimal;

/**
 * IItem�N���X.
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
