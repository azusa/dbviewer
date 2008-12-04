/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

/**
 * TableInfo�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/07/30 ZIGEN create.
 * 
 */
public class TableInfo {

	/**
	 * �e�[�u����
	 */
	private String name;

	/**
	 * �R�����g
	 */
	private String comment;

	/**
	 * �e�[�u���^�C�v
	 */
	private String tableType;

	public TableInfo(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}

	public TableInfo() {

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasComment() {
		if (this.comment != null && this.comment.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

}
