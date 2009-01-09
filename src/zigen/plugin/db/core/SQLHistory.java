/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * SQLExecHistoryクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/05/25 ZIGEN create.
 * 
 */
public class SQLHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date date;

	private String sql;

	private boolean isBlank; // 空白用

	private IDBConfig config;

	/**
	 * SQL履歴を1ファイルずつ管理するモード（高速化のため）
	 */
	private boolean fileMode = false;


	public SQLHistory() {
		this.sql = "";
		this.date = Calendar.getInstance().getTime();
	}

	public SQLHistory(IDBConfig config, String sql) {
		this.date = Calendar.getInstance().getTime();
		this.config = config;
		this.sql = sql;
	}

	public SQLHistory(boolean isBlank) {
		this.date = Calendar.getInstance().getTime();
		this.sql = "";
		this.isBlank = isBlank;
		this.fileMode = true;
	}

	public boolean isBlank() {
		return this.isBlank;
	}

	public Date getDate() {
		return date;
	}

	public String getSql() {
		return sql;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[SQLHistory:");
		buffer.append(" date: ");
		buffer.append(date);
		buffer.append(" sql: ");
		buffer.append(sql);
		buffer.append("]");
		return buffer.toString();
	}

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
		SQLHistory castedObj = (SQLHistory) o;
		return ((this.date == null ? castedObj.date == null : this.date.equals(castedObj.date)) && (this.sql == null ? castedObj.sql == null : this.sql.equals(castedObj.sql)) && (this.isBlank == castedObj.isBlank));
	}

	public IDBConfig getConfig() {
		return config;
	}

	public void setConfig(IDBConfig config) {
		this.config = config;
	}

	protected SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyyMMdd");

	protected SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

	public String getFolderName() {
		return ymdFormat.format(getDate());
	}

	public String getFileName() {
		return timeFormat.format(getDate());
	}

	public boolean isFileMode() {
		return fileMode;
	}

	public void setFileMode(boolean fileMode) {
		this.fileMode = fileMode;
	}

}
