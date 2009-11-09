/*
 * Copyright (c) 2007Å|2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

public class JDBCUnicodeConvertor {

	public static final String convert(String str) {

		if (str == null) {
			return null;
		}

		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '\u301c': // 'Å`'
				chars[i] = '\uff5e';
				break;
			case '\u2016': // 'Åa'
				chars[i] = '\u2225';
				break;
			case '\u2212': // 'Å|'
				chars[i] = '\uff0d';
				break;
			case '\u00a2': // 'Åë'
				chars[i] = '\uffe0';
				break;
			case '\u00a3': // 'Åí'
				chars[i] = '\uffe1';
				break;
			case '\u00ac': // 'Å '
				chars[i] = '\uffe2';
				break;
			case '\u00a6': // '˙U'
				chars[i] = '\uffe4';
				break;
			case '\u2032': // '˙V'
				chars[i] = '\uff07';
				break;
			case '\u2033': // '˙W'
				chars[i] = '\uff02';
				break;
			default:
				break;
			}
		}
		return new String(chars);
	}

}
