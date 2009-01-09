/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.ext.oracle.internal.OracleSequenceInfo;

/**
 * OracleSequenceクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class OracleSequence extends TreeNode {

	private static final long serialVersionUID = 1L;

	private OracleSequenceInfo oracleSequenceInfo;

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public OracleSequence(String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public OracleSequence() {
		super();
	}

	public String getName() {
		if (oracleSequenceInfo != null) {
			return this.oracleSequenceInfo.getSequence_name();
		} else {
			return super.name;
		}
	}

	public OracleSequenceInfo getOracleSequenceInfo() {
		return oracleSequenceInfo;
	}

	public void setOracleSequenceInfo(OracleSequenceInfo oracleSequenceInfo) {
		this.oracleSequenceInfo = oracleSequenceInfo;
	}

}
