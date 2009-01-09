/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

/**
 * TableFKColumn�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/18 ZIGEN create.
 * 
 */
public class TableFKColumn extends TablePKColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	// reference���̒�`
	private String pkSchema = null;

	private String pkTableName = null;

	private String pkColumnName = null;

	private String pkName = null;

	// CASUCADE DELETE �̔���
	private boolean isCasucade = false; // DELETE_RULE=0 �Ȃ��true(ON CASUCADE)

	/**
	 * @return isCasucade ��߂��܂��B
	 */
	public boolean isCasucade() {
		return isCasucade;
	}

	/**
	 * @param isCasucade
	 *            isCasucade ��ݒ�B
	 */
	public void setCasucade(boolean isCasucade) {
		this.isCasucade = isCasucade;
	}

	/**
	 * @return pkColumnName ��߂��܂��B
	 */
	public String getPkColumnName() {
		return pkColumnName;
	}

	/**
	 * @param pkColumnName
	 *            pkColumnName ��ݒ�B
	 */
	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	/**
	 * @return pkName ��߂��܂��B
	 */
	public String getPkName() {
		return pkName;
	}

	/**
	 * @param pkName
	 *            pkName ��ݒ�B
	 */
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	/**
	 * @return pkSchema ��߂��܂��B
	 */
	public String getPkSchema() {
		return pkSchema;
	}

	/**
	 * @param pkSchema
	 *            pkSchema ��ݒ�B
	 */
	public void setPkSchema(String pkSchema) {
		this.pkSchema = pkSchema;
	}

	/**
	 * @return pkTableName ��߂��܂��B
	 */
	public String getPkTableName() {
		return pkTableName;
	}

	/**
	 * @param pkTableName
	 *            pkTableName ��ݒ�B
	 */
	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableFKColumn:");
		buffer.append(" pkSchema: ");
		buffer.append(pkSchema);
		buffer.append(" pkTableName: ");
		buffer.append(pkTableName);
		buffer.append(" pkColumnName: ");
		buffer.append(pkColumnName);
		buffer.append(" pkName: ");
		buffer.append(pkName);
		buffer.append(" isCasucade: ");
		buffer.append(isCasucade);
		buffer.append("]");
		return buffer.toString();
	}
}
