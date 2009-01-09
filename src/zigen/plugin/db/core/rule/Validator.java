/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Validator.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class Validator {

	/**
	 * 文字列が未入力かどうかチェックします。
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String entry_Check(String filedName, String text) {
		if (text == null || text.length() == 0) {
			// return filedName + " はNull禁止項目です";
			return filedName + " is not null.";
		}
		return null;
	}

	/**
	 * 文字列の長さが指定したバイト数以内であるかどうかチェックします。
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @param maxBytes
	 *            最大バイト数
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String length_Check(String filedName, String text, int maxBytes) {

		if (text == null || text.equals(""))
			return null;

		int cnt = 0;
		for (int i = 0; i < text.length(); i++) {
			String s = text.substring(i, i + 1);
			cnt = cnt + s.getBytes().length; // 1文字あたりのバイト数を加算
		}

		if (cnt > maxBytes) {
			// return filedName + "は" + maxBytes + "バイト以内で入力してください";
			return filedName + " is " + maxBytes + "byte limit.";
		}
		return null;
	}

	/**
	 * 文字列が整数として正しいかチェックします。（負の値はNG)
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String numeric_Check(String filedName, String text) {
		if (text == null || text.equals(""))
			return null;

		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			if (!(chr >= '0' && chr <= '9')) { // 0～9以外はエラー
				// return filedName + "は半角数字のみ入力してください";
				return filedName + " is numeric only,";
			}
		}
		return null;
	}

	/**
	 * 文字列が小数を含む数値として正しいかチェックします。
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String decimal_Check(String filedName, String text) {
		try {
			new BigDecimal(text); // 数値チェック用にBigDecimalを生成

		} catch (NumberFormatException ex) {
			// return (filedName + "は数値を入力してください");
			return (filedName + " is numeric only.");
		}
		return null;
	}

	/**
	 * 文字列が日付として適当かどうかチェックします。
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String date_Check(String filedName, String text) {
		final String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);
		} catch (ParseException e) {
			// return filedName + "は、" + pattern + " 形式で入力してください";
			return filedName + " is the format of " + pattern + ".";
		}

		return null;

	}

	/**
	 * 文字列が日付として適当かどうかチェックします。
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String timestamp_Check(String filedName, String text) {
		final String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {
			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	/**
	 * 文字列が日付として適当かどうかチェックします。
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String timestamp2_Check(String filedName, String text) {
		final String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {
			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	/**
	 * 文字列が日付として適当かどうかチェックします。
	 * 
	 * @param filedName
	 *            フィールド名
	 * @param text
	 *            チェックする文字列
	 * @return 正常時："" 、異常時：エラーメッセージ
	 */
	public static final String time_Check(String filedName, String text) {
		final String pattern = "HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {

			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	public static final String boolean_Check(String filedName, String text) {
		String str = text.toLowerCase();
		if ("true".equals(str) || "false".equals(str)) {
			return null;
		} else {
			// return filedName + "は、true または false で入力してください";
			return filedName + " is true or false";
		}

	}

	public static final String tinyint_Check(String filedName, String text) {
		// final String msg = "は、-128～127の範囲で で入力してください";
		final String msg = "  is range from -128 to 127. ";

		try {
			if (decimal_Check(filedName, text) == null) {
				int value = Integer.parseInt(text);
				if (-128 <= value && value <= 127) {
					return null;
				} else {
					return filedName + msg;
				}
			} else {
				return filedName + msg;
			}
		} catch (NumberFormatException e) {
			return filedName + msg;
		}

	}

}
