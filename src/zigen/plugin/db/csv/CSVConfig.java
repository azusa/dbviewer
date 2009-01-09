/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.csv;

import java.io.Serializable;

/**
 * CSVConfigクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 */
public class CSVConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String query;

	private String csvFile;

	private String csvEncoding;

	private String separator;

	private boolean nonHeader;

	private boolean nonDoubleQuate;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public boolean isNonHeader() {
		return nonHeader;
	}

	public void setNonHeader(boolean nonHeader) {
		this.nonHeader = nonHeader;
	}

	public boolean isNonDoubleQuate() {
		return nonDoubleQuate;
	}

	public void setNonDoubleQuate(boolean nonDoubleQuate) {
		this.nonDoubleQuate = nonDoubleQuate;
	}

	/**
	 * @return csvEncoding を戻します。
	 */
	public String getCsvEncoding() {
		return csvEncoding;
	}

	/**
	 * @param csvEncoding
	 *            csvEncoding を設定。
	 */
	public void setCsvEncoding(String csvEncoding) {
		this.csvEncoding = csvEncoding;
	}

	/**
	 * @return csvFile を戻します。
	 */
	public String getCsvFile() {
		return csvFile;
	}

	/**
	 * @param csvFile
	 *            csvFile を設定。
	 */
	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

	/**
	 * @return query を戻します。
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            query を設定。
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[CSVConfig:"); //$NON-NLS-1$
		buffer.append(" query: "); //$NON-NLS-1$
		buffer.append(query);
		buffer.append(" csvFile: "); //$NON-NLS-1$
		buffer.append(csvFile);
		buffer.append(" csvEncoding: "); //$NON-NLS-1$
		buffer.append(csvEncoding);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}
}
