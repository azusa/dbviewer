/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace;

/**
 * CalcTableSpaceExceptionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/01 ZIGEN create.
 * 
 */
public class CalcTableSpaceException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param msg
	 */
	public CalcTableSpaceException(String message) {
		super(message);
	}

	public CalcTableSpaceException(String message, Throwable cause) {
		super(message, cause);
	}

}
