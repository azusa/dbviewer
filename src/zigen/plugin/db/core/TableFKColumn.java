/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

/**
 * TableFKColumnクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/18 ZIGEN create.
 * 
 */
public class TableFKColumn extends TablePKColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	// reference側の定義
	private String pkSchema = null;

	private String pkTableName = null;

	private String pkColumnName = null;

	private String pkName = null;

	// CASUCADE DELETE の判定
	private boolean isCasucade = false; // DELETE_RULE=0 ならはtrue(ON CASUCADE)

	/**
	 * @return isCasucade を戻します。
	 */
	public boolean isCasucade() {
		return isCasucade;
	}

	/**
	 * @param isCasucade
	 *            isCasucade を設定。
	 */
	public void setCasucade(boolean isCasucade) {
		this.isCasucade = isCasucade;
	}

	/**
	 * @return pkColumnName を戻します。
	 */
	public String getPkColumnName() {
		return pkColumnName;
	}

	/**
	 * @param pkColumnName
	 *            pkColumnName を設定。
	 */
	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	/**
	 * @return pkName を戻します。
	 */
	public String getPkName() {
		return pkName;
	}

	/**
	 * @param pkName
	 *            pkName を設定。
	 */
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	/**
	 * @return pkSchema を戻します。
	 */
	public String getPkSchema() {
		return pkSchema;
	}

	/**
	 * @param pkSchema
	 *            pkSchema を設定。
	 */
	public void setPkSchema(String pkSchema) {
		this.pkSchema = pkSchema;
	}

	/**
	 * @return pkTableName を戻します。
	 */
	public String getPkTableName() {
		return pkTableName;
	}

	/**
	 * @param pkTableName
	 *            pkTableName を設定。
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
