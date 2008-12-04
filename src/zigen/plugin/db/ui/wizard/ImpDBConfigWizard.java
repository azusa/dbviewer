/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.wizard;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SameDbNameException;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.views.TreeContentProvider;

/**
 * TestWizardクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/21 ZIGEN create.
 * 
 */
public class ImpDBConfigWizard extends Wizard {

	private ImpWizardPage1 page1;

	private TreeViewer viewer;

	private IDBConfig[] configs;

	public ImpDBConfigWizard(TreeViewer viewer, IDBConfig[] configs) {
		super();
		this.viewer = viewer;
		this.configs = configs;
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page1 = new ImpWizardPage1(configs);
		addPage(page1);
	}

	public boolean performFinish() {
		try {
			TableItem[] tableItems = this.page1.tableItems;
			for (int i = 0; i < tableItems.length; i++) {
				TableItem item = tableItems[i];
				if (item.isChecked()) {
					// 設定を保存する
					saveDBConfig(item.getConfig());
					
				}
			}

			DbPlugin.getDefault().saveDBDialogSettings();

			return true;
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
			return false;
		}

	}

	// オーバーライド
	public boolean canFinish() {
		if (page1.isPageComplete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 同じ名前があると「のコピー」と付ける
	 * 
	 * @param config
	 * @param saveName
	 */
	private void saveDBConfig(IDBConfig config) {
		try {
			DBConfigManager.save(config);

			IContentProvider obj = viewer.getContentProvider();
			if (obj instanceof TreeContentProvider) {
				TreeContentProvider provider = (TreeContentProvider) obj;
				DataBase registDb = provider.addDataBase(config); // データベースを追加する
				// 追加 2007/11/22
				viewer.expandToLevel(provider.getRoot(), 1);
				viewer.refresh();
				
				// 追加したデータエースに選択を与える
				viewer.setSelection(new StructuredSelection(registDb), true);
			}
		} catch (SameDbNameException e) {
			config.setDbName(config.getDbName() + "のコピー");
			saveDBConfig(config);
		}
	}

}
