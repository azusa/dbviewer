/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.editors.exceptions.NotFoundSynonymInfoException;

/**
 * Synonymクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class Synonym extends Table implements ITable {

	private static final long serialVersionUID = 1L;

	private SynonymInfo synonymInfo;

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Synonym(String name, String remarks) {
		super(name, remarks);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Synonym(String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Synonym() {
		super();
	}

	public void update(Synonym node) {
		super.update(node);
		this.synonymInfo = node.synonymInfo;

	}

	public SynonymInfo getSynonymInfo() {
		return synonymInfo;
	}

	public void setSynonymInfo(SynonymInfo synonymInfo) {
		this.synonymInfo = synonymInfo;

		// シノニム情報からコメントをRemarkに設定する
		if (synonymInfo != null) {
			this.setRemarks(synonymInfo.getComments());
		}
	}

	public String getRemarks() {
		if (synonymInfo != null) {
			String commenet = synonymInfo.getComments();
			return (commenet == null) ? "" : commenet; //$NON-NLS-1$
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	public void setRemarks(String remarks) {
		if (synonymInfo != null) {
			synonymInfo.setComments(remarks);
		} else {
			;
		}
	}

	public String getDb_link() {
		if (synonymInfo != null) {
			return synonymInfo.getDb_link();
		} else {
			return null;
		}
	}

	public String getLabel() {
		StringBuffer sb = new StringBuffer();

		sb.append(this.getName());
		if (getRemarks() != null && getRemarks().length() > 0) {
			sb.append(" ["); //$NON-NLS-1$
			sb.append(getRemarks());
			sb.append("]"); //$NON-NLS-1$
		}
		return sb.toString();

	}

	// public String getSynonym_name() {
	// if(synonymInfo != null){
	// return synonymInfo.getSynonym_name();
	// }else{
	// return null;
	// }
	// }

	public String getTable_name() throws Exception {
		if (synonymInfo != null) {
			return synonymInfo.getTable_name();
		} else {
			throw new NotFoundSynonymInfoException(Messages.getString("Synonym.4")); //$NON-NLS-1$
		}
	}

	public String getTable_owner() throws Exception {
		if (synonymInfo != null) {
			return synonymInfo.getTable_owner();
		} else {
			throw new NotFoundSynonymInfoException(Messages.getString("Synonym.5")); //$NON-NLS-1$
		}
	}

}
