/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternUtil {

	/**
	 * 正規表現ではなく、A や A, B のような検索をするためのPatterを取得 ※複数キーワードはカンマで区切ります。 ※前後方一致検索になります。
	 * 
	 * @param key
	 * @param caseSensitive
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static final Pattern getPattern(String key, boolean caseSensitive) throws PatternSyntaxException {
		Pattern pattern = null;
		String[] keys = key.split(",| "); //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		for (int i = 0; i < keys.length; i++) {
			String keywd = keys[i];
			if (keywd != null && keywd.trim().length() > 0) {
				// キーワードの前後の空白は削除する
				// keywd = keywd.trim().replaceAll("\\*", "\\.\\*"); //$NON-NLS-1$ //$NON-NLS-2$

				// 前後方一致検索
				StringBuffer w = new StringBuffer();
				w.append(".*");
				w.append(keywd.trim());
				w.append(".*");

				if (cnt == 0) {
					sb.append(w.toString());
				} else {
					sb.append("|"); //$NON-NLS-1$
					sb.append(w.toString());
				}
				cnt++;
			}
		}
		if (!caseSensitive) {
			pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(sb.toString());
		}
		return pattern;

	}

	/**
	 * 正規表現ではなく、A* や A, B のような検索をするためのPatterを取得 ※複数キーワードはカンマで区切ります。 ※一致する文字列には*を入れます。(入れない場合は、完全一致になります）
	 * 
	 * @param key
	 * @param caseSensitive
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static final Pattern getPattern2(String key, boolean caseSensitive) throws PatternSyntaxException {
		Pattern pattern = null;
		String[] keys = key.split(","); //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		for (int i = 0; i < keys.length; i++) {
			String keywd = keys[i];
			if (keywd != null && keywd.trim().length() > 0) {
				// キーワードの前後の空白は削除する
				keywd = keywd.trim().replaceAll("\\*", "\\.\\*"); //$NON-NLS-1$ //$NON-NLS-2$
				if (cnt == 0) {
					sb.append(keywd);
				} else {
					sb.append("|"); //$NON-NLS-1$
					sb.append(keywd);
				}
				cnt++;
			}
		}
		if (!caseSensitive) {
			pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(sb.toString());
		}
		return pattern;

	}
}
