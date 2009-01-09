/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import zigen.plugin.db.DbPlugin;

/**
 * 表領域の見積もり結果をCSVに出力するクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/01 ZIGEN create.
 * 
 */
public class CSVResultWriter {

	private char demiliter = ',';

	private boolean append = false;

	private String encording = Messages.getString("CSVResultWriter.0"); //$NON-NLS-1$

	// private List csvList = null;
	private String[] headers = null;

	public CSVResultWriter() {}

	public CSVResultWriter(char demiliter) {
		this.demiliter = demiliter;
	}

	public CSVResultWriter(char demiliter, boolean append) {
		this.demiliter = demiliter;
		this.append = append;
	}

	public CSVResultWriter(char demiliter, boolean append, String encording) {
		this.demiliter = demiliter;
		this.append = append;
		this.encording = encording;
	}

	/**
	 * コネクションを維持しながらSQLを連続実行する場合は直接起動する. その場合は、自分でConnectionのCloseを行うこと.
	 * 
	 * @param con
	 * @param query
	 * @throws Exception
	 */
	public void execute(File csvFile, List csvList) throws Exception {
		PrintStream pout = null;

		try {
			pout = new PrintStream(new FileOutputStream(csvFile, append), true, // flashMode
					encording); // encording

			// カラムラベルの書き込み
			if (!append) {
				// 追加モードでなければヘッダーを出力
				writeHeader(pout);
			}

			// カラム値の書き込み
			writeValue(pout, csvList);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			if (pout != null)
				pout.close();
		}

	}

	// カラムデータの書き込み
	private void writeHeader(PrintStream pout) throws SQLException {

		for (int i = 0; i < headers.length; i++) {
			String header = encode(headers[i]);
			if (i == headers.length - 1) {
				// 最後は改行付
				pout.println(header);
			} else {
				pout.print(header + demiliter);
			}

		}
	}

	// カラムデータの書き込み
	private void writeValue(PrintStream pout, List csvList) throws SQLException {

		for (Iterator iter = csvList.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof List) {
				List list = (List) obj;

				for (int i = 0; i < list.size(); i++) {
					String value = encode(list.get(i).toString());
					if (i == list.size() - 1) {
						// 最後は改行付
						pout.println(value);
					} else {
						pout.print(value + demiliter);
					}

				}
			} else {
				throw new IllegalArgumentException("csvListの中身はList型である必要があります"); //$NON-NLS-1$
			}

		}

	}

	private String encode(String value) {
		// " ⇒ "" へ
		value = value.replaceAll("\"", "\"\""); //$NON-NLS-1$ //$NON-NLS-2$

		// カンマを含むデータの場合は、前後を"で囲む
		if (value.indexOf("\"") > 0 || value.indexOf(",") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			value = "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return value;
	}

	/**
	 * @return append を戻します。
	 */
	public boolean isAppend() {
		return append;
	}

	/**
	 * @param append
	 *            append を設定。
	 */
	public void setAppend(boolean append) {
		this.append = append;
	}

	/**
	 * @return demiliter を戻します。
	 */
	public char getDemiliter() {
		return demiliter;
	}

	/**
	 * @param demiliter
	 *            demiliter を設定。
	 */
	public void setDemiliter(char demiliter) {
		this.demiliter = demiliter;
	}

	/**
	 * @return encording を戻します。
	 */
	public String getEncording() {
		return encording;
	}

	/**
	 * @param encording
	 *            encording を設定。
	 */
	public void setEncording(String encording) {
		this.encording = encording;
	}

	/**
	 * @return header を戻します。
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * @param header
	 *            header を設定。
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
}
