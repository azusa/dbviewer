/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * SelectIntoChecker.java�N���X. MySQL��SELECT FROM INTO �����ǂ������肷�邽�߂̃`�F�b�J�[
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/07/29 ZIGEN create.
 * 
 */
public class SelectIntoChecker {

	public static boolean check(String text) {
		// ���s�R�[�h���󔒂ɕϊ�����
		String str = StringUtil.convertLineSep(text, " ");

		StringTokenizer t = new StringTokenizer(str, " ");
		String token = null;
		List wk = new ArrayList();

		if ((token = t.nextToken()).equalsIgnoreCase("SELECT")) { //$NON-NLS-1$
			wk.add(token);

			// 2�߈ȍ~������
			while ((token = t.nextToken()) != null) {
				if ("FROM".equals(token) || "INTO".equals(token)) { //$NON-NLS-1$ //$NON-NLS-2$
					wk.add(token);
				}
				// ���[�v���Ƀ��X�g��4���ȏ�ɂȂ�����Afalse
				if (wk.size() == 3) {
					break;
				}
			}

			if (wk.size() == 3) {
				String[] s = (String[]) wk.toArray(new String[3]);
				if (s[0].equals("SELECT") && s[1].equals("FROM") && s[2].equals("INTO")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					return true;
				}
			}
		}

		return false;
	}

}
/*
 * class SelectIntoTokenizer implements Enumeration {
 * 
 * private String demiliter = " "; //$NON-NLS-1$
 * 
 * private String text;
 * 
 * private int currentPosition;
 * 
 * private int maxPosition;
 * 
 * public SelectIntoTokenizer(String str) { // ���s�R�[�h�𔼊p�󔒂̈����ɕύX str = str.replaceAll(DbPluginConstant.LINE_SEP, " "); //$NON-NLS-1$ str = str.replaceAll("\r", " "); //$NON-NLS-1$ //$NON-NLS-2$ str =
 * str.replaceAll("\n", " "); //$NON-NLS-1$ //$NON-NLS-2$ str = str.replaceAll("\t", " "); //$NON-NLS-1$ //$NON-NLS-2$
 * 
 * this.text = str; currentPosition = 0; maxPosition = this.text.length();
 *  }
 * 
 * private int nextDemiliter(int i) { boolean flg = false;
 * 
 * while (i < maxPosition) { char ch = text.charAt(i);
 * 
 * int pos = text.indexOf(demiliter, i);
 * 
 * if (!flg && pos == i) { break; } else if ('\'' == ch) { flg = !flg; } else if ('"' == ch) { flg = !flg; }
 * 
 * i++; } return i; }
 * 
 * public int getTokenCount() { int i = 0; int ret = 1; while ((i = nextDemiliter(i)) < maxPosition) { i++; ret++; } return ret; }
 * 
 * public String nextToken() { if (currentPosition > maxPosition) { return null; }
 * 
 * int start = currentPosition; currentPosition = nextDemiliter(currentPosition);
 * 
 * StringBuffer sb = new StringBuffer(); while (start < currentPosition) { char ch = text.charAt(start++); sb.append(ch); } currentPosition++;
 * 
 * return sb.toString(); }
 * 
 * public Object nextElement() { return nextToken(); }
 * 
 * public boolean hasMoreElements() { return (nextDemiliter(currentPosition) <= maxPosition); }
 *  }
 */
