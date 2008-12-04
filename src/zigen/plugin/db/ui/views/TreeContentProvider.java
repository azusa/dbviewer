/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.bookmark.BookmarkManager;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

/**
 * ViewContentProviderクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class TreeContentProvider implements ITreeContentProvider {

	private BookmarkManager bookMarkMgr = DbPlugin.getDefault().getBookmarkManager();

	private Root invisibleRoot;

	private Root root;

	private BookmarkRoot bookmarkRoot;
	
	private IDBConfig[] dbConfigs;
	

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		if (invisibleRoot == null)
			initialize();

		return getChildren(invisibleRoot);
	}

	public Object getParent(Object element) {
		if (element instanceof TreeLeaf) {
			return ((TreeLeaf) element).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TreeNode) {
			return ((TreeNode) parentElement).getChildrens();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		if (element instanceof TreeNode)
			return ((TreeNode) element).hasChildren();
		return false;
	}

	public void initialize() {
		invisibleRoot = new Root("invisible", true); //$NON-NLS-1$

		root = new Root("DBViewerPlugin"); //$NON-NLS-1$
		invisibleRoot.addChild(root);

		// TODO:V0.1.0向け
		try {
			bookmarkRoot = bookMarkMgr.getBookmarkRoot();
		} catch (Exception e) {
			DbPlugin.log(e);
		}

		if (bookmarkRoot == null || bookmarkRoot.getName() == null) {
			bookmarkRoot = new BookmarkRoot(Messages.getString("TreeContentProvider.2")); //$NON-NLS-1$
		}

		bookMarkMgr.setBookmarkRoot(bookmarkRoot);

		invisibleRoot.addChild(bookmarkRoot);

		createDataBase();

	}

	/**
	 * ツリーを再構築する
	 */
	public void createDataBase() {
		// データベース定義情報からツリーを作成する
		dbConfigs = DBConfigManager.getDBConfigs();
		for (int i = 0; i < dbConfigs.length; i++) {
			IDBConfig config = dbConfigs[i];
			DataBase db = new DataBase(config);
			root.addChild(db);
		}

	}

	/**
	 * データベース定義を追加するメソッド
	 * 
	 * @param config
	 */
	public DataBase addDataBase(IDBConfig config) {
		// データベース定義情報からツリーを作成する
		DataBase db = new DataBase(config);
		root.addChild(db);
		return db;
	}

	public BookmarkRoot getBookmarkRoot() {
		return bookmarkRoot;
	}

	public void setBookmarkRoot(BookmarkRoot bookmarkRoot) {
		this.bookmarkRoot = bookmarkRoot;
	}

	// /**
	// * お気に入り(Root)にFolderを追加する
	// * @param config
	// */
	// public void addBookmarkFolder(BookmarkFolder folder) {
	// // データベース定義情報からツリーを作成する
	// bookmarkRoot.addChild(folder);
	// }
	//
	// /**
	// * お気に入り(Root)に追加する
	// * @param config
	// */
	// public void addBookmark(TreeNode node) {
	// // データベース定義情報からツリーを作成する
	// bookmarkRoot.addChild(node);
	// }
	//
	// /**
	// * お気に入り(Root)から外す
	// * @param config
	// */
	// public void removeBookmark(TreeNode node) {
	// // データベース定義情報からツリーを作成する
	// bookmarkRoot.removeChild(node);
	// }

	public DataBase findDataBase(Bookmark bookmark) {
		TreeLeaf[] leafs = root.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof DataBase) {
				DataBase db = (DataBase) leaf;

				// DBの定義名が同じであれば、一緒と見なす。
				if (db.getName().equals(bookmark.getDataBase().getName())) {
					return db;
				}
			}
		}
		return null;

	}

	public Root getRoot() {
		return root;
	}
	
	public DataBase[] getDataBases(){
		TreeLeaf[] leafs = root.getChildrens();
		List list = new ArrayList();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof DataBase) {
				list.add(leaf);
			}
		}
		return (DataBase[])list.toArray(new DataBase[0]);
	}
	
	public DataBase findDataBase(IDBConfig config){
		TreeLeaf[] leafs = root.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof DataBase) {
				DataBase db = (DataBase) leaf;
				if (db.getName().equals(config.getDbName())) {
					return db;
				}
			}
		}
		return null;
	}

	public IDBConfig[] getDBConfigs() {
		return dbConfigs;
	}

}
