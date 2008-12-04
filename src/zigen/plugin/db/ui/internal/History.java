/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.text.SimpleDateFormat;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLHistory;

/**
 * Tableクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class History extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	protected SQLHistory sqlHistory;
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public History(SQLHistory history) {
		this.sqlHistory = history;
	}
	
	public SQLHistory getSqlHistory() {
		return sqlHistory;
	}
	
	public static final int MAX_LEN = 100;
	
	/*
	 * public String getDate() { return dateFormat.format(history.getDate()); }
	 */

	public String getTime() {
		return timeFormat.format(sqlHistory.getDate());
	}
	
	public String getName() {
		
		StringBuffer sb = new StringBuffer();
		String shortSql = getShortSql();
		if (!"".equals(shortSql)) {
			sb.append(getTime());
			sb.append(" ");
			sb.append(getShortSql());
		}
		
		return sb.toString();
	}
	
	private String getShortSql() {
		
		// String sql = SQLFormatter.unformat(sqlHistory.getSql());
		String sql = sqlHistory.getSql(); // レスポンス悪化のため、Unformatしない
		if (sql == null)
			return "";
		if (sql.length() > MAX_LEN) {
			return sql.substring(0, MAX_LEN) + "...";
		} else {
			return sql;
		}
	}
	
	public IDBConfig getDbConfig() {
		return sqlHistory.getConfig();
	}
	
	/**
	 * Returns <code>true</code> if this <code>History</code> is the same as the o argument.
	 * 
	 * @return <code>true</code> if this <code>History</code> is the same as the o argument.
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		History castedObj = (History) o;
		return ((this.timeFormat == null ? castedObj.timeFormat == null : this.timeFormat.equals(castedObj.timeFormat)) && (this.sqlHistory == null ? castedObj.sqlHistory == null : this.sqlHistory
				.equals(castedObj.sqlHistory)));
	}
	
}
