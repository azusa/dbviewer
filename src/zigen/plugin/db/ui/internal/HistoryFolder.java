/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tableクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class HistoryFolder extends TreeNode {

	private static final long serialVersionUID = 1L;

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Date date;

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public HistoryFolder(Date date) {
		super();
		this.date = date;
	}

	public String getName() {
		if (date != null) {
			return dateFormat.format(date);
		} else {
			return null;
		}
	}

	// equals メソッドはオーバライドしない

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
