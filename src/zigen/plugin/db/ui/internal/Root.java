/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

/**
 * Root�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class Root extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public Root() {
		super();
	}
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 */
	public Root(String name) {
		super(name);
	}
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 * @param isRoot
	 */
	public Root(String name, boolean isRoot) {
		super(name, isRoot);
	}
}
