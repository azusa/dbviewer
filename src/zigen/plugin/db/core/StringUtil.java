/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * 最後の単語の開始オフセットを取得する
	 * 
	 * @param text
	 * @return
	 */
	public static int endWordPosition(String text) {
		boolean flg = false;

		for (int i = text.length() - 1; i >= 0; i--) {
			char chr = text.charAt(i);
			switch (chr) {
			case ' ':
			case ',':
			case '\t':
			case '\r':
			case '\n':
				if (flg) {
					return i + 1;
				} else {
					break;
				}

			default:
				if (!flg)
					flg = true;
				break;
			}

		}
		return 0;
	}

	/**
	 * 最初に見つかる文字(空白、改行コード以外）の位置を取得する
	 * 
	 * @param text
	 * @return
	 */
	public static int firstWordPosition(String text) {
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			switch (chr) {
			case ' ':
				// case ' ':
			case '\t':
			case '\r':
			case '\n':
				break;
			default:
				return i;
			}

		}
		return 0;
	}

	/**
	 * 配列から配列を削除する
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public static Object[] remove(Object[] src, Object[] target) {
		LinkedList srcList = new LinkedList(Arrays.asList(src));
		for (int i = 0; i < target.length; i++) {
			srcList.remove(target[i]);
		}
		return srcList.toArray(new Object[srcList.size()]);
	}

	/*
	 * 配列に配列を追加
	 */
	public static Object[] add(Object[] array1, Object[] array2) {
		if (array1 != null && array2 == null)
			return array1;
		if (array1 == null && array2 != null)
			return array2;

		TreeSet tree = new TreeSet();

		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != null)
				tree.add(array1[i]);
		}
		for (int i = 0; i < array2.length; i++) {
			if (!tree.contains(array2[i]) && array2[i] != null)
				tree.add(array2[i]);
		}

		return tree.toArray();
	}

	/**
	 * 数値かどうか
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNumeric(String text) {
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			if (!(chr >= '0' && chr <= '9')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 空白パディング(バイト合わせ）
	 * 
	 * @param value
	 * @param digit
	 * @return
	 */
	public static String padding(String value, int digit) {
		StringBuffer sb = new StringBuffer(value);

		// int len = digit - value.length();
		int len = digit - value.getBytes().length;
		for (int i = 0; i < len; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * インデント
	 * 
	 * @param value
	 * @param digit
	 * @return
	 */
	public static String indent(String value, int digit) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digit; i++) {
			sb.append(" ");
		}
		sb.append(value);
		return sb.toString();
	}

	/**
	 * 右側の連続する文字列を削除
	 * 
	 * @param str
	 * @param trimChar
	 * @return
	 */
	public static String rTrim(String str, char trimChar) {
		int cnt = 0;
		for (int i = str.length() - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if (c != trimChar) {
				cnt = i + 1;
				break;
			}
		}
		return str.substring(0, cnt);
	}

	/**
	 * 右側の全角空白を削除
	 * 
	 * @param str
	 * @return
	 */
	public static String rTrimFullSpace(String str) {
		return rTrim(str, '　'); // デフォルトは半角空白
	}

	/**
	 * 左側の全角空白を削除
	 * 
	 * @param str
	 * @return
	 */
	public static String lTrimFullSpace(String str) {
		return lTrim(str, '　');
	}

	/**
	 * 左側の連続する文字を削除する
	 * 
	 * @param str
	 * @param trimChar
	 * @return
	 */
	public static String lTrim(String str, char trimChar) {
		int cnt = str.length();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c != trimChar) {
				cnt = i;
				break;
			}
		}
		return str.substring(cnt);
	}

	/**
	 * 文字列中の「"」をエスケープする。
	 * 
	 * @param strSrc
	 *            元の文字列
	 * @return 変換後の文字列
	 */
	public static final String encodeDoubleQuotation(String str) {
		return str.replaceAll("\"", "\\\\\"");
	}

	private static Pattern commentPattern = Pattern.compile("/\\*.*?(\\*/|$)", Pattern.DOTALL);

	private static Pattern lineCommentPattern = Pattern.compile("--.*?(\r|\n|$)");

	private static Pattern leftTrimPattern = Pattern.compile("^(　| )+");

	private static Pattern rightTrimPattern = Pattern.compile("(　| )+$");

	public static String removeComment(String s) {
		return commentPattern.matcher(s).replaceAll("");
	}

	public static String removeLineComment(String s) {
		return lineCommentPattern.matcher(s).replaceAll("");
	}

	public static String removeLeftFullSpace(String s) {
		return leftTrimPattern.matcher(s).replaceAll("");
	}

	public static String removerightFullSpace(String s) {
		return rightTrimPattern.matcher(s).replaceAll("");
	}

	/**
	 * 改行コードをLF(\n)に統一する
	 * 
	 * @param text
	 * @return
	 */
	public static String convertLineSep(String text) {
		return text.replaceAll("\\r\\n|\\r|\\n", "\n");

	}

	/**
	 * 改行コードを任意のコードに統一する
	 * 
	 * @param text
	 * @return
	 */
	public static String convertLineSep(String text, String demiliter) {
		return text.replaceAll("\\r\\n|\\r|\\n", demiliter);

	}

}
