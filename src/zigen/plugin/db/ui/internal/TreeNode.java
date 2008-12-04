/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TreeParentクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create. [2] 2005/03/17 ZIGEN 指定した子を取得するメソッドの追加
 * 
 */
public class TreeNode extends TreeLeaf {
	
	private static final long serialVersionUID = 1L;
	
	protected List children;
	
	protected boolean isRoot;
	
	protected boolean isExpanded = false; // 展開済みかどうか（キャッシュの判定用）
	
	/**
	 * デフォルトコンストラクタ.
	 * 
	 * @param name
	 */
	public TreeNode() {
		this(null, false);
	}
	
	/**
	 * コンストラクタ. このコンストラクタを使用するとisRootがfalseで設定されます
	 * 
	 * @param name
	 */
	public TreeNode(String name) {
		this(name, false);
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 * @param isRoot
	 */
	public TreeNode(String name, boolean isRoot) {
		super(name);
		children = new ArrayList();
		this.isRoot = isRoot;
	}
	
	/**
	 * 子の追加
	 * 
	 * @param child
	 */
	public void addChild(TreeLeaf child) {
		children.add(child);
		child.setParent(this);
		child.setLevel(level + 1);
	}
	
	/**
	 * 子の削除
	 * 
	 * @param child
	 */
	public void removeChild(TreeLeaf child) {
		children.remove(child);
		if (child != null) {
			child.setParent(null);
		}
	}
	
	/**
	 * 子をすべて削除
	 */
	public void removeChildAll() {
		TreeLeaf[] elements = getChildrens();
		for (int i = 0; i < elements.length; i++) {
			TreeLeaf elem = (TreeLeaf) elements[i];
			removeChild(elem);
		}
	}
	
	/**
	 * 子を取得
	 * 
	 * @return
	 */
	public TreeLeaf[] getChildrens() {
		return (TreeLeaf[]) children.toArray(new TreeLeaf[children.size()]);
	}
	
	// 追加メソッド 2003.
	public TreeLeaf getChild(String name) {
		TreeLeaf[] elements = getChildrens();
		for (int i = 0; i < elements.length; i++) {
			TreeLeaf elem = (TreeLeaf) elements[i];
			if (elem.getName().equals(name)) {
				return elem;
			}
		}
		return null;
	}
	
	/**
	 * 子を持っているかを返す
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	/**
	 * invisibleRootか返す
	 * 
	 * @return isRoot を戻します。
	 */
	public boolean isRoot() {
		return isRoot;
	}
	
	/**
	 * 展開されているかどうかを返す
	 * 
	 * @return
	 */
	public boolean isExpanded() {
		return isExpanded;
	}
	
	/**
	 * 展開状態かどうかを設定する
	 * 
	 * @param isExpanded
	 */
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	
	public void setChildren(List children) {
		this.children = children;
		
		// parentを再設定
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			TreeNode node = (TreeNode) iter.next();
			node.setParent(this);
			node.setLevel(level + 1);
		}
		
	}
	
	public List getChildren() {
		return this.children;
	}
	
	/**
	 * Returns <code>true</code> if this <code>TreeNode</code> is the same as the o argument.
	 * 
	 * @return <code>true</code> if this <code>TreeNode</code> is the same as the o argument.
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		TreeNode castedObj = (TreeNode) o;
		return ((this.children == null ? castedObj.children == null : this.children.equals(castedObj.children)) && (this.isRoot == castedObj.isRoot));
	}
	
}
