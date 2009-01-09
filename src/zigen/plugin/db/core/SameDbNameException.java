/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

/**
 * 
 * SameDbNameException.java�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/04/12 ZIGEN create.
 * 
 */
public class SameDbNameException extends Throwable {

	private static final long serialVersionUID = 1L;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param message
	 */
	public SameDbNameException(String message) {
		super(message);
	}
}
