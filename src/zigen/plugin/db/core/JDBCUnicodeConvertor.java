/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

/**
 * JDBCUnicodeConvertor�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/09/27 ZIGEN create.
 * 
 */
public class JDBCUnicodeConvertor {

	public static final String convert(String str) {

		/*
		 * if (str == null || str.length() == 0) { //return new String(str);
		 * return null; }
		 */

		// �󕶎��̏ꍇ��NULL��Ԃ��Ȃ��悤�ɏC��
		if (str == null) {
			return null;
		}

		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '\u301c': // '�`'
				chars[i] = '\uff5e';
				break;
			case '\u2016': // '�a'
				chars[i] = '\u2225';
				break;
			case '\u2212': // '�|'
				chars[i] = '\uff0d';
				break;
			case '\u00a2': // '��'
				chars[i] = '\uffe0';
				break;
			case '\u00a3': // '��'
				chars[i] = '\uffe1';
				break;
			case '\u00ac': // '��'
				chars[i] = '\uffe2';
				break;
			case '\u00a6': // '�U'
				chars[i] = '\uffe4';
				break;
			case '\u2032': // '�V'
				chars[i] = '\uff07';
				break;
			case '\u2033': // '�W'
				chars[i] = '\uff02';
				break;
			default:
				break;
			}
		}
		return new String(chars);
	}

}
