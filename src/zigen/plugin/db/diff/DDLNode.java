/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import java.io.Serializable;
import java.util.ArrayList;

import zigen.plugin.db.ui.internal.TreeNode;


abstract public class DDLNode extends TreeNode implements IDDLDiff, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * デフォルトコンストラクタ.
	 * 
	 * @param name
	 */
	public DDLNode() {
		this(null, false);
	}

	/**
	 * コンストラクタ. このコンストラクタを使用するとisRootがfalseで設定されます
	 * 
	 * @param name
	 */
	public DDLNode(String name) {
		this(name, false);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 * @param isRoot
	 */
	public DDLNode(String name, boolean isRoot) {
		this.name = name;
		this.isRoot = isRoot;
		this.children = new ArrayList();
	}

}
