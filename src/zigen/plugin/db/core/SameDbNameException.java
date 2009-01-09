/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

/**
 * 
 * SameDbNameException.javaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/04/12 ZIGEN create.
 * 
 */
public class SameDbNameException extends Throwable {

	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param message
	 */
	public SameDbNameException(String message) {
		super(message);
	}
}
