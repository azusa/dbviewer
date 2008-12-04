/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.IDBConfig;

/**
 * HistoryDataBaseFolder�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class HistoryDataBaseFolder extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	private IDBConfig config;
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public HistoryDataBaseFolder(IDBConfig config) {
		super();
		this.config = config;
	}
	
	public String getName() {
		if (config != null) {
			return config.getDbName();
		} else {
			return null;
		}
	}
	
	// equals ���\�b�h�̓I�[�o���C�h���Ȃ�
	
	/**
	 * Returns <code>true</code> if this <code>HistoryFolder</code> is the same as the o argument.
	 * 
	 * @return <code>true</code> if this <code>HistoryFolder</code> is the same as the o argument.
	 */
	/*
	 * public boolean equals(Object o) { if (this == o) { return true; } if (o == null) { return false; } if (o.getClass() != getClass()) { return false; }
	 * 
	 * HistoryFolder castedObj = (HistoryFolder) o; return ((this.dateFormat == null ? castedObj.dateFormat == null : this.dateFormat.equals(castedObj.dateFormat)) && (this.date == null ?
	 * castedObj.date == null : this.date.equals(castedObj.date))); }
	 */

}
