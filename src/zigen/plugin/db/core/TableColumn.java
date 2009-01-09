/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

/**
 * TableColumn�N���X.
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

	private String remarks; // �f�t�H���g�l(NULL�̏ꍇ������)

	private boolean notNull;

	private String defaultValue; // �f�t�H���g�l(NULL�̏ꍇ������)

	private boolean isUniqueKey;

	// �f�[�^�^�C�v���p�����[�^�����̏ꍇ(Oracle�p)
	private boolean withoutParam = false;

	public TableColumn() {}

	/**
	 * @return columnName ��߂��܂��B
	 */
	public String getColumnName() {
		return (columnName == null) ? "" : columnName;
	}

	/**
	 * @param columnName
	 *            columnName ��ݒ�B
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return columnSize ��߂��܂��B
	 */
	public int getColumnSize() {
		return columnSize;
	}

	/**
	 * @param columnSize
	 *            columnSize ��ݒ�B
	 */
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	/**
	 * @return dataType ��߂��܂��B
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            dataType ��ݒ�B
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return decimalDigits ��߂��܂��B
	 */
	public int getDecimalDigits() {
		return decimalDigits;
	}

	/**
	 * @param decimalDigits
	 *            decimalDigits ��ݒ�B
	 */
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	/**
	 * @return notNull ��߂��܂��B
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * @param notNull
	 *            notNull ��ݒ�B
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * @return remarks ��߂��܂��B
	 */
	public String getRemarks() {
		return (remarks == null) ? "" : remarks;
	}

	/**
	 * @param remarks
	 *            remarks ��ݒ�B
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return typeName ��߂��܂��B
	 */
	public String getTypeName() {
		return (typeName == null) ? "" : typeName;
	}

	/**
	 * @param typeName
	 *            typeName ��ݒ�B
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return seq ��߂��܂��B
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq
	 *            seq ��ݒ�B
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


	// �� TableColumn��equals���\�b�h�͎������Ȃ�(�C���X�^���X���������ǂ����Ń`�F�b�N���邽�߁j
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
