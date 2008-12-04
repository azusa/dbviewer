/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
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
public class BookmarkFolder extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	public BookmarkFolder() {
		super();
	}
	
	public BookmarkFolder(String name) {
		super(name);
	}
	
	/**
	 * 所属するBookmarkRootの取得
	 * 
	 * @return
	 */
	public BookmarkRoot getBookmarkRoot() {
		return getBookmarkRoot(this);
	}
	
	/**
	 * 所属するBookmarkFolderの取得
	 * 
	 * @return
	 */
	public BookmarkFolder getBookmarkFolder() {
		return getBookmarkFolder(this);
	}
	
	private BookmarkRoot getBookmarkRoot(TreeLeaf leaf) {
		if (leaf instanceof BookmarkRoot) {
			return (BookmarkRoot) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkRoot(leaf.getParent());
			} else {
				// return null;
				throw new IllegalStateException("BookmarkRoot要素が上位に存在しません");
			}
		}
	}
	
	private BookmarkFolder getBookmarkFolder(TreeLeaf leaf) {
		if (leaf instanceof BookmarkFolder) {
			return (BookmarkFolder) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkFolder(leaf.getParent());
			} else {
				return null;
			}
		}
	}
	
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
		BookmarkFolder castedObj = (BookmarkFolder) o;
		return ((this.name == null ? castedObj.name == null : this.name.equals(castedObj.name)));
	}
	
}
