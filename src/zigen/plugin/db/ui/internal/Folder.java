/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

/**
 * Tableクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class Folder extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Folder(String name) {
		super(name);
	}
	
	public Folder() {
		super();
	}
	
	/**
	 * 要素を更新
	 * 
	 * @param name
	 */
	public void update(String name) {
		this.name = name;
	}
	
	public Object clone() {
		Folder inst = new Folder();
		inst.name = this.name == null ? null : new String(this.name);
		return inst;
	}
	
	// equals メソッドはオーバライドしない
	
}
