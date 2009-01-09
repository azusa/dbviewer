/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.ui.bookmark.BookmarkDialog;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.views.TreeContentProvider;

/**
 * RegistDBAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class RegistBookmarkAction extends Action implements Runnable {

	// private BookmarkManager bookMarkMgr =
	// DbPlugin.getDefault().getBookmarkManager();

	TreeViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public RegistBookmarkAction(TreeViewer viewer) {

		this.viewer = viewer;
		this.setText(Messages.getString("RegistBookmarkAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RegistBookmarkAction.1")); //$NON-NLS-1$

	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		IContentProvider obj = viewer.getContentProvider();

		if (obj instanceof TreeContentProvider) {
			TreeContentProvider provider = (TreeContentProvider) obj;
			BookmarkDialog dialog = new BookmarkDialog(DbPlugin.getDefault().getShell(), provider);
			int ret = dialog.open();

			if (ret == Dialog.OK) {
				TreeNode selectedNode = dialog.getSelectedNode();

				// if (selectedNode instanceof BookmarkRoot) {
				// BookmarkRoot root = (BookmarkRoot) selectedNode;
				// } else if (selectedNode instanceof BookmarkFolder) {
				// BookmarkFolder folder = (BookmarkFolder) selectedNode;
				//
				// }

				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				setBookmark(selection, selectedNode);

			}
			// cancel�̏ꍇ�ł����t���b�V������

		}

		viewer.refresh();

	}

	private void setBookmark(IStructuredSelection selection, TreeNode node) {
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object object = iter.next();
			// ���C�ɓ���̓o�^�Ȃ̂�Table�̂ݑΉ�
			if (object instanceof Table) {
				Table table = (Table) object;
				Bookmark bm = new Bookmark(table);
				node.addChild(bm);

				// �_�~�[�J������ǉ�
				if (bm.getChildren().size() == 0) {
					TableColumn tColumn = new TableColumn();
					tColumn.setColumnName(DbPluginConstant.TREE_LEAF_LOADING);
					bm.addChild(new Column(tColumn));
				}

				// viewer.expandToLevel(bm, 1);
			}
		}

	}

	// /**
	// * Action���s���̏���
	// */
	// public void run() {
	//
	// IStructuredSelection selection = (IStructuredSelection)
	// viewer.getSelection();
	//
	// Iterator iter = selection.iterator();
	//
	// IContentProvider obj = viewer.getContentProvider();
	// if (obj instanceof TreeContentProvider) {
	// TreeContentProvider provider = (TreeContentProvider) obj;
	//
	// while (iter.hasNext()) {
	// Object object = iter.next();
	//
	// // ���C�ɓ���̓o�^�Ȃ̂�Table�̂ݑΉ�
	// if (object instanceof Table) {
	// Table table = (Table) object;
	//
	// //IDBConfig config = table.getDbConfig();
	// Bookmark bm = new Bookmark(table);
	//
	// // ���݃`�F�b�N
	// if (!bookMarkMgr.contains(bm)) {
	//
	// // DB�ڑ���`�p�_�C�A���O�̃I�[�v��
	// BookmarkDialog dialog = new
	// BookmarkDialog(DbPlugin.getDefault().getShell(), provider);
	// int ret = dialog.open();
	//
	// // if (ret == Dialog.OK) {
	// // provider.addBookmark(bm);
	// //
	// // /**
	// // * ���C�ɓ���ɒǉ�
	// // */
	// // bookMarkMgr.addBookmark(bm);
	// // }
	//
	// } else {
	// DbPlugin.getDefault().showWaringMessage(table.getName() +
	// "�͊��ɂ��C�ɓ���ɓo�^����Ă��܂�");
	// }
	//
	// }
	// }
	//
	// viewer.refresh();
	// }
	//
	// }
}
