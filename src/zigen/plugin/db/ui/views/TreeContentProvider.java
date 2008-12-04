/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
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
 * ViewContentProvider�N���X.
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

		// TODO:V0.1.0����
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
	 * �c���[���č\�z����
	 */
	public void createDataBase() {
		// �f�[�^�x�[�X��`��񂩂�c���[���쐬����
		dbConfigs = DBConfigManager.getDBConfigs();
		for (int i = 0; i < dbConfigs.length; i++) {
			IDBConfig config = dbConfigs[i];
			DataBase db = new DataBase(config);
			root.addChild(db);
		}

	}

	/**
	 * �f�[�^�x�[�X��`��ǉ����郁�\�b�h
	 * 
	 * @param config
	 */
	public DataBase addDataBase(IDBConfig config) {
		// �f�[�^�x�[�X��`��񂩂�c���[���쐬����
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
	// * ���C�ɓ���(Root)��Folder��ǉ�����
	// * @param config
	// */
	// public void addBookmarkFolder(BookmarkFolder folder) {
	// // �f�[�^�x�[�X��`��񂩂�c���[���쐬����
	// bookmarkRoot.addChild(folder);
	// }
	//
	// /**
	// * ���C�ɓ���(Root)�ɒǉ�����
	// * @param config
	// */
	// public void addBookmark(TreeNode node) {
	// // �f�[�^�x�[�X��`��񂩂�c���[���쐬����
	// bookmarkRoot.addChild(node);
	// }
	//
	// /**
	// * ���C�ɓ���(Root)����O��
	// * @param config
	// */
	// public void removeBookmark(TreeNode node) {
	// // �f�[�^�x�[�X��`��񂩂�c���[���쐬����
	// bookmarkRoot.removeChild(node);
	// }

	public DataBase findDataBase(Bookmark bookmark) {
		TreeLeaf[] leafs = root.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof DataBase) {
				DataBase db = (DataBase) leaf;

				// DB�̒�`���������ł���΁A�ꏏ�ƌ��Ȃ��B
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
