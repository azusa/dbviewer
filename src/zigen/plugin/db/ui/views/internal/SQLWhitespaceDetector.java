/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * SQLWhitespaceDetector�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/07 ZIGEN create.
 * 
 */
public class SQLWhitespaceDetector implements IWhitespaceDetector {

	/**
	 * �R���X�g���N
	 */
	public SQLWhitespaceDetector() {
	}

	public boolean isWhitespace(char c) {
		// return Character.isWhitespace(c);
		// return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '(' || c == ')' || c == ',');

	}
}
