/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
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
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.views.TreeContentProvider;

/**
 * RegistDBActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class RegistDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public RegistDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("RegistDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RegistDBAction.1")); //$NON-NLS-1$

		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_ADD));

	}

	/**
	 * Action実行時の処理
	 */
	public void run() {

		Shell shell = DbPlugin.getDefault().getShell();
		DBConfigWizard wizard = new DBConfigWizard(viewer.getSelection());
		WizardDialog dialog2 = new WizardDialog(shell, wizard);
		int ret = dialog2.open();

		if (ret == IDialogConstants.OK_ID) {
			// XMLに保存するDBConfigを取得
			IDBConfig newConfig = wizard.getNewConfig();

			// IContentProviderを取得
			IContentProvider obj = viewer.getContentProvider();
			if (obj instanceof TreeContentProvider) {
				TreeContentProvider provider = (TreeContentProvider) obj;
				DataBase registDb = provider.addDataBase(newConfig); // データベースを追加する

				// 追加 2007/11/22(登録した要素にフォーカスを与える)
				// Root root = provider.getRoot();
				// viewer.expandToLevel(root, 1);
				// TreeLeaf db = root.getChild(newConfig.getDbName());
				// viewer.reveal(db);

				viewer.refresh();

				// 追加したデータエースに選択を与える
				viewer.setSelection(new StructuredSelection(registDb), true);
			}

			// 選択状態を再度通知する
			viewer.getControl().notifyListeners(SWT.Selection, null);


			DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);
		}


	}

}
