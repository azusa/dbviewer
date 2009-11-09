/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.views.TreeContentProvider;

/**
 * RemoveDBActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class RemoveDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public RemoveDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("RemoveDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RemoveDBAction.1")); //$NON-NLS-1$
		this.setEnabled(true);

		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_DELETE));;
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {

		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		StringBuffer sb = new StringBuffer();
		sb.append(Messages.getString("RemoveDBAction.2")); //$NON-NLS-1$
		sb.append(Messages.getString("RemoveDBAction.3")); //$NON-NLS-1$

		if (DbPlugin.getDefault().confirmDialog(sb.toString())) {

			IContentProvider cp = viewer.getContentProvider();
			if (cp instanceof TreeContentProvider) {
				TreeContentProvider tcp = (TreeContentProvider) cp;
				BookmarkRoot bmroot = tcp.getBookmarkRoot();

				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof DataBase) {
						DataBase db = (DataBase) obj;

						// お気に入りも削除する
						removeBookmark(bmroot, db);

						// DBConfigから削除する
						DBConfigManager.remove(db.getDbConfig());
						// 自信を要素から削除する
						db.getParent().removeChild(db);
						// Viewerの再描画
						viewer.refresh();
						// SQL実行Viewのコンボボックスを更新
						// updateComboOfSQLViewer(null);
						// 選択状態を再度通知する
						viewer.getControl().notifyListeners(SWT.Selection, null);

						DbPlugin.fireStatusChangeListener(db.getDbConfig(), IStatusChangeListener.EVT_RemoveSchemaFilter);

					}
				}


				DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);


			}

		}

	}

	/**
	 * 指定したDataBaseに一致するお気に入りを削除します。
	 * 
	 * @param targetDataBase
	 */
	private void removeBookmark(BookmarkFolder folder, DataBase targetDataBase) {
		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof Bookmark) {
				Bookmark bm = (Bookmark) leaf;
				if (bm.getDataBase().equals(targetDataBase)) {
					folder.removeChild(bm);
				}
			} else if (leaf instanceof BookmarkFolder) {
				removeBookmark((BookmarkFolder) leaf, targetDataBase);
			}
		}
	}

}
