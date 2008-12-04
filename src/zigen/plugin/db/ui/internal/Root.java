/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

/**
 * Rootクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class Root extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Root() {
		super();
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Root(String name) {
		super(name);
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 * @param isRoot
	 */
	public Root(String name, boolean isRoot) {
		super(name, isRoot);
	}
}
