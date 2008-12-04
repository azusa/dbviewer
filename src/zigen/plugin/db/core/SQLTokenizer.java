/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import zigen.plugin.db.DbPluginConstant;

/**
 * SQLTokenizerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/25 ZIGEN create.
 * 
 */
public class SQLTokenizer implements Enumeration {

	// public final String LINE_SEP = "\r"; // 改行コードは1文字で扱う
	public static final String LINE_SEP = "\n"; // BlancoFormattertにあわせて、改行コードは\nで扱う

	private String text = null;

	private int currentPosition;

	private int maxPosition;

	private String demiliter;

	public SQLTokenizer(String str, String demiliter) {
		if(str == null || str.length() == 0) return;
			
		this.text = convertLineSep(str);
		this.demiliter = demiliter + LINE_SEP;

		if (!text.endsWith(LINE_SEP)) {
			text += LINE_SEP; // 最後が改行で終わっていない場合は、追加する
		}
		currentPosition = 0;
		maxPosition = this.text.length();
	}

	private String convertLineSep(String value) {
		if (value != null && value.length() > 0) {
			value = StringUtil.convertLineSep(value, LINE_SEP);
		}
		return value;
	}

	private boolean hasDemiliter(int fromIndex) {
		if (text.startsWith(demiliter, fromIndex)) {
			// "*/" で無いことをチェックする
			return !text.startsWith("*/", fromIndex - 1);
		} else {
			return false;
		}

	}

	private int nextDemiliter(int i) {
		boolean flg = false;

		while (i < maxPosition) {
			char ch = text.charAt(i);
			int pos = -1;

			if (hasDemiliter(i)) {
				pos = text.indexOf(demiliter, i);
			}

			if (!flg && pos == i) {
				break;
			} else if ('\'' == ch) {
				flg = !flg;
			}
			i++;
		}
		return i;
	}

	public int getTokenCount() {
		if(text == null) return 0;
		
		int i = 0;
		int ret = 1;
		while ((i = nextDemiliter(i)) < maxPosition) {
			i++;
			ret++;
		}
		return ret;
	}

	public String nextToken() {
		if (currentPosition > maxPosition)
			throw new NoSuchElementException(toString());

		int start = currentPosition;
		currentPosition = nextDemiliter(currentPosition);
		StringBuffer sb = new StringBuffer();
		while (start < currentPosition) {
			char ch = text.charAt(start++);
			sb.append(ch);
		}

		currentPosition += (demiliter).length();

		// // 改行だけの場合は省略する
		// String out = sb.toString();
		// if (out.trim().length() == 0) {
		// return null;
		// } else {
		// return out;
		// }

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		// 改行だけの場合は省略する
		String out = sb.toString().trim();
		if (out.length() == 0) {
			return null;
		} else {
//			return out;
			return StringUtil.convertLineSep(out, DbPluginConstant.LINE_SEP);
		}
	}

	public Object nextElement() {
		return nextToken();
	}

	public boolean hasMoreElements() {
		return (nextDemiliter(currentPosition) <= maxPosition);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[SQLTokenizer:");
		buffer.append(" text: ");
		buffer.append(text);
		buffer.append(" currentPosition: ");
		buffer.append(currentPosition);
		buffer.append(" maxPosition: ");
		buffer.append(maxPosition);
		buffer.append("]");
		return buffer.toString();
	}
}
