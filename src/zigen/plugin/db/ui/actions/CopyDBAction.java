/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SameDbNameException;
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
public class CopyDBAction extends Action implements Runnable {

	public class DBNameValidator implements IInputValidator {
		final String name = Messages.getString("CopyDBAction.0"); //$NON-NLS-1$

		public String isValid(String str) {
			if (str == null || str.trim().equals("")) { //$NON-NLS-1$
				return name + Messages.getString("CopyDBAction.2"); //$NON-NLS-1$
			}
			if (DBConfigManager.hasSection(str)) {
				return Messages.getString("CopyDBAction.3") + name + Messages.getString("CopyDBAction.4"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return null;
		}
	}

	TreeViewer viewer = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public CopyDBAction(TreeViewer viewer) {

		this.viewer = viewer;
		this.setText(Messages.getString("CopyDBAction.5")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CopyDBAction.6")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_COPY));

	}

	/**
	 * Action実行時の処理
	 */
	public void run() {

		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof DataBase) {
			DataBase db = (DataBase) element;
			IDBConfig config = db.getDbConfig();
			IDBConfig copyConfig = (IDBConfig) config.clone();

			Shell shell = DbPlugin.getDefault().getShell();
			String name = config.getDbName() + Messages.getString("CopyDBAction.7"); //$NON-NLS-1$

			InputDialog dialog = new InputDialog(shell, Messages.getString("CopyDBAction.8"), Messages.getString("CopyDBAction.9"), name, new DBNameValidator()); //$NON-NLS-1$ //$NON-NLS-2$

			int rc = dialog.open();
			if (rc == InputDialog.CANCEL) {

				return;
			} else {
				// 入力した文字列を設定
				copyConfig.setDbName(dialog.getValue());

				// IContentProviderを取得
				IContentProvider obj = viewer.getContentProvider();
				if (obj instanceof TreeContentProvider) {
					TreeContentProvider provider = (TreeContentProvider) obj;
					DataBase registDb = provider.addDataBase(copyConfig); // データベースを追加する
					
					viewer.refresh();
					
					// 追加したデータエースに選択を与える
					viewer.setSelection(new StructuredSelection(registDb), true);
					
					viewer.getControl().notifyListeners(SWT.Selection, null);
				}

				// 設定ファイルの保存
				try {
					DBConfigManager.save(copyConfig);
				} catch (SameDbNameException e) {
					DbPlugin.getDefault().showErrorDialog(e);
				}

				
				DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);

			}

		}

	}
}
