/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.exceptions;

public class NotFoundColumnInfoException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public NotFoundColumnInfoException(String message) {
		super(message);
	}
	
}
