/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.math.BigDecimal;

import zigen.plugin.db.ext.oracle.tablespace.CalcIndexSpace;
import zigen.plugin.db.ext.oracle.tablespace.OracleIndexColumn;
import zigen.plugin.db.ui.internal.Table;

/**
 * IndexItemクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/02 ZIGEN create.
 * 
 */
public class IndexItem extends TableItem {

	String indexName;

	OracleIndexColumn[] indexColumns;

	private CalcIndexSpace indexSpace;

	public IndexItem(Table table, String indexName, OracleIndexColumn[] indexColumns) {
		super(table, true);
		this.indexName = indexName;
		this.indexColumns = indexColumns;

	}

	public String getIndexName() {
		return indexName;
	}

	// IndexはCalcIndexSpaceからレコード件数を取得する
	public long getRecordSize() {
		if (indexSpace != null) {
			return indexSpace.getMaxRecord();
		} else {
			return -1;
		}
	}

	public void setCalcIndexSpace(CalcIndexSpace indexSpace) throws Exception {
		this.indexSpace = indexSpace;
	}

	public BigDecimal getTableSpaceSize() {
		if (indexSpace != null) {
			return indexSpace.getTableSpaceSize();
		} else {
			return null;
		}
	}

	public double getSafeCoefficient() {
		if (indexSpace != null) {
			return indexSpace.getSafeCoefficient();
		} else {
			return 0;
		}
	}

	public BigDecimal getTableSpaceSafeSize() {
		if (indexSpace != null) {
			return indexSpace.getTableSpaceSafeSize();
		} else {
			return null;
		}
	}

}
