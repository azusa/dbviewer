/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

/**
 * TableColumnクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class TableColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	private int seq;

	private String columnName;

	private int dataType;

	private String typeName;

	private int columnSize;

	private int decimalDigits;

	private String remarks; // デフォルト値(NULLの場合がある)

	private boolean notNull;

	private String defaultValue; // デフォルト値(NULLの場合がある)

	private boolean isUniqueKey;

	// データタイプがパラメータ無しの場合(Oracle用)
	private boolean withoutParam = false;

	public TableColumn() {}

	/**
	 * @return columnName を戻します。
	 */
	public String getColumnName() {
		return (columnName == null) ? "" : columnName;
	}

	/**
	 * @param columnName
	 *            columnName を設定。
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return columnSize を戻します。
	 */
	public int getColumnSize() {
		return columnSize;
	}

	/**
	 * @param columnSize
	 *            columnSize を設定。
	 */
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	/**
	 * @return dataType を戻します。
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            dataType を設定。
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return decimalDigits を戻します。
	 */
	public int getDecimalDigits() {
		return decimalDigits;
	}

	/**
	 * @param decimalDigits
	 *            decimalDigits を設定。
	 */
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	/**
	 * @return notNull を戻します。
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * @param notNull
	 *            notNull を設定。
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * @return remarks を戻します。
	 */
	public String getRemarks() {
		return (remarks == null) ? "" : remarks;
	}

	/**
	 * @param remarks
	 *            remarks を設定。
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return typeName を戻します。
	 */
	public String getTypeName() {
		return (typeName == null) ? "" : typeName;
	}

	/**
	 * @param typeName
	 *            typeName を設定。
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return seq を戻します。
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq
	 *            seq を設定。
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	public boolean isUniqueKey() {
		return isUniqueKey;
	}

	public void setUniqueKey(boolean isUniqueKey) {
		this.isUniqueKey = isUniqueKey;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableColumn:");
		buffer.append(" seq: ");
		buffer.append(seq);
		buffer.append(" columnName: ");
		buffer.append(columnName);
		buffer.append(" dataType: ");
		buffer.append(dataType);
		buffer.append(" typeName: ");
		buffer.append(typeName);
		buffer.append(" columnSize: ");
		buffer.append(columnSize);
		buffer.append(" decimalDigits: ");
		buffer.append(decimalDigits);
		buffer.append(" remarks: ");
		buffer.append(remarks);
		buffer.append(" notNull: ");
		buffer.append(notNull);
		buffer.append(" defaultValue: ");
		buffer.append(defaultValue);
		buffer.append(" isUniqueKey: ");
		buffer.append(isUniqueKey);
		buffer.append(" withoutParam: ");
		buffer.append(withoutParam);
		buffer.append("]");
		return buffer.toString();
	}

	public String getDefaultValue() {
		return (defaultValue == null) ? "" : defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	// ※ TableColumnのequalsメソッドは実装しない(インスタンスが同じかどうかでチェックするため）
	// /**
	// * Returns <code>true</code> if this <code>TableColumn</code> is the
	// * same as the o argument.
	// *
	// * @return <code>true</code> if this <code>TableColumn</code> is the
	// * same as the o argument.
	// */
	// public boolean equals(Object o) {
	// if (this == o) {
	// return true;
	// }
	// if (o == null) {
	// return false;
	// }
	// if (o.getClass() != getClass()) {
	// return false;
	// }
	// TableColumn castedObj = (TableColumn) o;
	// return ((this.seq == castedObj.seq) && (this.columnName == null ? castedObj.columnName == null : this.columnName.equals(castedObj.columnName)) && (this.dataType == castedObj.dataType)
	// && (this.typeName == null ? castedObj.typeName == null : this.typeName.equals(castedObj.typeName)) && (this.columnSize == castedObj.columnSize)
	// && (this.decimalDigits == castedObj.decimalDigits) && (this.remarks == null ? castedObj.remarks == null : this.remarks.equals(castedObj.remarks))
	// && (this.notNull == castedObj.notNull) && (this.defaultValue == null ? castedObj.defaultValue == null : this.defaultValue.equals(castedObj.defaultValue))
	// && (this.isUniqueKey == castedObj.isUniqueKey)&& (this.withoutParam == castedObj.withoutParam));
	// }

	/**
	 * Returns <code>true</code> if this <code>TableColumn</code> is the same as the o argument.
	 * 
	 * @return <code>true</code> if this <code>TableColumn</code> is the same as the o argument.
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
		TableColumn castedObj = (TableColumn) o;
		return ((this.seq == castedObj.seq) && (this.columnName == null ? castedObj.columnName == null : this.columnName.equals(castedObj.columnName))
				&& (this.dataType == castedObj.dataType) && (this.typeName == null ? castedObj.typeName == null : this.typeName.equals(castedObj.typeName))
				&& (this.columnSize == castedObj.columnSize) && (this.decimalDigits == castedObj.decimalDigits)
				&& (this.remarks == null ? castedObj.remarks == null : this.remarks.equals(castedObj.remarks)) && (this.notNull == castedObj.notNull)
				&& (this.defaultValue == null ? castedObj.defaultValue == null : this.defaultValue.equals(castedObj.defaultValue)) && (this.isUniqueKey == castedObj.isUniqueKey) && (this.withoutParam == castedObj.withoutParam));
	}

	public boolean isWithoutParam() {
		return withoutParam;
	}

	public void setWithoutParam(boolean withoutParam) {
		this.withoutParam = withoutParam;
	}

}
