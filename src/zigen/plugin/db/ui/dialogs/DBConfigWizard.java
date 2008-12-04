/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.dialogs;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfig;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.DriverManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SameDbNameException;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.TreeLeaf;

/**
 * TestWizardクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/21 ZIGEN create.
 * 
 */
public class DBConfigWizard extends Wizard {

	IDBConfig oldConfig; // 旧DB接続定義

	IDBConfig newConfig; // XMLに登録するDBConfigオブジェクト

	private WizardPage1 page1;

	private WizardPage2 page2;

	private WizardPage3 page3;

	private ISelection selection;

	public DBConfigWizard(ISelection selection) {
		this(selection, null);
	}

	public DBConfigWizard(ISelection selection, IDBConfig oldConfig) {
		super();
		super.setWindowTitle(Messages.getString("DBConfigWizard.0")); //$NON-NLS-1$
		this.selection = selection;
		this.oldConfig = oldConfig;

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);

	}

	public void addPages() {

		page1 = new WizardPage1(selection);
		page2 = new WizardPage2(selection);
		page3 = new WizardPage3(selection);

		addPage(page1);
		addPage(page2);
		addPage(page3);


		// Filter用スキーマだけはここで設定しておく。
		if(oldConfig != null){
			page3.filterSchemas = oldConfig.getDisplayedSchemas();
			page3.checkFilterPattern = oldConfig.isCheckFilterPattern();
			page3.filterPattern = (oldConfig.getFilterPattern() == null) ? "" : oldConfig.getFilterPattern();
		}
		
	}

	public boolean performFinish() {
		try {

			newConfig = createNewConfig();

			// 新規か修正かを判定
			if (oldConfig == null) {
				// 新規保存
				DBConfigManager.save(newConfig);
			} else {

				if (selection instanceof StructuredSelection) {
					Object element = ((StructuredSelection) selection).getFirstElement();
					if (element instanceof DataBase) {
						DataBase _db = (DataBase) element;
						Root root = (Root) _db.getParent().getParent(); // invisible
																		// root
						List children = root.getChildren();
						for (Iterator iterator = children.iterator(); iterator.hasNext();) {
							Object obj = iterator.next();
							if (obj instanceof BookmarkRoot) {
								changeDataBase((BookmarkRoot) obj, _db, newConfig);
								break; // ブックマークのルートは１つ
							}
						}
					}
				}

				// 修正
				DBConfigManager.modify(oldConfig, newConfig);
			}

			// SQLExecuteViewのDBリスト（コンボボックス）の更新
			// updateComboOfSQLViewer(newConfig);

			// DriverManagerのキャッシュをクリアする
			DriverManager.getInstance().removeCach(newConfig);

			// 物理ファイルへの保存を行なうようにした。
			DbPlugin.getDefault().saveDBDialogSettings();
			
			return true;

		} catch (SameDbNameException e) {
			DbPlugin.getDefault().showWarningMessage(e.getMessage());

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return false;

	}

	/**
	 * 指定したDataBaseに一致するお気に入りのDBConfigも変更する
	 * 
	 * @param targetDataBase
	 */
	private void changeDataBase(BookmarkFolder folder, DataBase targetDataBase, IDBConfig newConfig) {
		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof Bookmark) {
				Bookmark bm = (Bookmark) leaf;
				if (bm.getDataBase().equals(targetDataBase)) {
					bm.setDbConfig(newConfig);
				}
			} else if (leaf instanceof BookmarkFolder) {
				changeDataBase((BookmarkFolder) leaf, targetDataBase, newConfig);
			}
		}
	}

	// オーバーライド
	public boolean canFinish() {
		if (page1.isPageComplete() && page2.isPageComplete() && page3.isPageComplete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * DB接続情報の保存
	 * 
	 * @return
	 */
	protected IDBConfig createNewConfig() {
		DBConfig config = new DBConfig();

		try {
			config.setDbName(page1.nameText.getText());
			config.setClassPaths((String[]) page1.classpathList.toArray(new String[0]));

			config.setDriverName(page2.driverCombox.getText());

			config.setUrl(page2.urlText.getText());
			config.setUserId(page2.userIdText.getText());
			config.setSchema(page2.schemaText.getText());
			config.setPassword(page2.passwordText.getText());

			if (page2.radio2.getSelection()) {
				config.setJdbcType(DBConfig.JDBC_DRIVER_TYPE_2);
			} else {
				config.setJdbcType(DBConfig.JDBC_DRIVER_TYPE_4);
			}

			config.setCharset(page3.charsetText.getText());
			config.setConvertUnicode(page3.unicodeCheck.getSelection());
			config.setAutoCommit(page3.commitModeCheck.getSelection());
			
			//config.setOnlyDefaultSchema(page3.schemaOnlyCheck.getSelection());
			config.setOnlyDefaultSchema(false); // この機能は廃止
			

			if (page3.symfowareOptionCheck != null){
				config.setNoLockMode(page3.symfowareOptionCheck.getSelection());
			}else{
				// 3ページを見なかった場合はtrueで設定する
				config.setNoLockMode(true);
				
			}
			if(page2.connectionModeCombox != null){
				int index = page2.connectionModeCombox.getSelectionIndex();
				switch (index) {
				case 0:
					config.setConnectAsSYSDBA(false);
					config.setConnectAsSYSOPER(false);
					break;
				case 1:
					config.setConnectAsSYSDBA(true);
					config.setConnectAsSYSOPER(false);
					break;
				case 2:
					config.setConnectAsSYSDBA(false);
					config.setConnectAsSYSOPER(true);
					break;
				}

			}
			if(page2.connectionModeCombox2 != null){
				int index = page2.connectionModeCombox2.getSelectionIndex();
				if(index == 0){
					config.setConnectAsInformationSchema(false);
				}else if(index == 1){
					config.setConnectAsInformationSchema(true);
				}
			}

			config.setDisplayedSchemas(page3.filterSchemas);
//			config.setCheckFilterPattern(page3.checkFilterPattern);
			config.setCheckFilterPattern(false); // 初回起動時はOFFにする
			config.setFilterPattern(page3.filterPattern);
			
			
			

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return config;
	}

	public IDBConfig getNewConfig() {
		return newConfig;
	}

}
