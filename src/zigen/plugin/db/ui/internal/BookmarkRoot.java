/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.io.Serializable;

public class BookmarkRoot extends BookmarkFolder implements Serializable {

	private static final long serialVersionUID = 1L;

	public BookmarkRoot() {
		super();
	}

	public BookmarkRoot(String name) {
		super(name);
	}

	//
	// /**
	// * ��������BookmarkRoot�̎擾
	// * @return
	// */
	// public BookmarkRoot getBookmarkRoot() {
	// return getBookmarkRoot(this);
	// }
	//
	// /**
	// * ��������BookmarkFolder�̎擾
	// * @return
	// */
	// public BookmarkFolder getBookmarkFolder() {
	// return getBookmarkFolder(this);
	// }
	//
	// private BookmarkRoot getBookmarkRoot(TreeLeaf leaf) {
	// if (leaf instanceof BookmarkRoot) {
	// return (BookmarkRoot) leaf;
	// } else {
	// if (leaf.getParent() != null) {
	// return getBookmarkRoot(leaf.getParent());
	// } else {
	// //return null;
	// throw new IllegalStateException("BookmarkRoot�v�f����ʂɑ��݂��܂���");
	// }
	// }
	// }
	//
	// private BookmarkFolder getBookmarkFolder(TreeLeaf leaf) {
	// if (leaf instanceof BookmarkFolder) {
	// return (BookmarkFolder) leaf;
	// } else {
	// if (leaf.getParent() != null) {
	// return getBookmarkFolder(leaf.getParent());
	// } else {
	// return null;
	// }
	// }
	// }

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

		BookmarkRoot castedObj = (BookmarkRoot) o;
		return ((this.name == null ? castedObj.name == null : this.name.equals(castedObj.name)));
	}
}
