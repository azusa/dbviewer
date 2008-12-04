/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.dialogs.DBConfigWizard;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.views.TreeContentProvider;

/**
 * EditDBActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/03/21 ZIGEN create.
 *        [002] 2005/07/16 ZIGEN 一部修正.
 * 
 */
public class EditDBAction extends Action implements Runnable {
	TreeViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public EditDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("EditDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("EditDBAction.1")); //$NON-NLS-1$
		this.setEnabled(true);
        this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_EDIT));
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof DataBase) {

			DataBase db = (DataBase) element;

			DataBase oldDB = (DataBase) db.clone();

			/*
			 * // DB接続定義用ダイアログのオープン DBConfigDialog dialog = new
			 * DBConfigDialog(DbPlugin.getDefault().getShell(),
			 * db.getDbConfig()); int ret = dialog.open();
			 * 
			 * if (ret == IDialogConstants.OK_ID) { // XMLに保存するDBConfigを取得
			 * IDBConfig newConfig = dialog.getNewConfig(); // <!-- [002] 修正
			 * ZIGEN 2005/07/16 // db.setName(newConfig.getDBName());
			 * db.setDbConfig(newConfig); // 2005/08/05 修正 ZIGEN
			 * viewer.refresh(db); // [002] 修正 ZIGEN 2005/07/16 --> }
			 */

			Shell shell = DbPlugin.getDefault().getShell();
			DBConfigWizard wizard = new DBConfigWizard(viewer.getSelection(), db.getDbConfig());
			WizardDialog dialog2 = new WizardDialog(shell, wizard);
			int ret = dialog2.open();

			if (ret == IDialogConstants.OK_ID) {
				// XMLに保存するDBConfigを取得
				IDBConfig newConfig = wizard.getNewConfig();
				
				db.setDbConfig(newConfig); // 2005/08/05 修正 ZIGEN
				viewer.refresh(db);

				IContentProvider cp = viewer.getContentProvider();
				if (cp instanceof TreeContentProvider) {
					TreeContentProvider tcp = (TreeContentProvider) cp;
					BookmarkRoot bmroot = tcp.getBookmarkRoot();

					updateBookmark(bmroot, oldDB, db);

				}

				// 選択状態を再度通知する
				viewer.getControl().notifyListeners(SWT.Selection, null);
				
				// Filter通知
				DbPlugin.fireStatusChangeListener(newConfig, IStatusChangeListener.EVT_AddSchemaFilter);

				// データベース定義編修後通知する
				DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);
				

			}

		}
	}

	/**
	 * 指定したDataBaseに一致するお気に入りのDataBase情報を更新する。
	 * 
	 * @param targetDataBase
	 */
	private void updateBookmark(BookmarkFolder folder, DataBase targetDataBase, DataBase newDataBase) {
		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof Bookmark) {
				Bookmark bm = (Bookmark) leaf;
				if (bm.getDataBase().equals(targetDataBase)) {
					bm.setDataBase(newDataBase);
				}
			} else if (leaf instanceof BookmarkFolder) {
				updateBookmark((BookmarkFolder) leaf, targetDataBase, newDataBase);
			}
		}
	}

}
