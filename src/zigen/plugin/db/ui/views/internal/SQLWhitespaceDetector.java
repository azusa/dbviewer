/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * SQLWhitespaceDetectorクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/07 ZIGEN create.
 * 
 */
public class SQLWhitespaceDetector implements IWhitespaceDetector {

	/**
	 * コンストラク
	 */
	public SQLWhitespaceDetector() {
	}

	public boolean isWhitespace(char c) {
		// return Character.isWhitespace(c);
		// return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '(' || c == ')' || c == ',');

	}
}
