/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.FolderInfo;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.csv.CreateCSVAction;
import zigen.plugin.db.diff.DDLDiffForFolderAction;
import zigen.plugin.db.diff.DDLDiffForSchemaAction;
import zigen.plugin.db.diff.DDLDiffForSourceAction;
import zigen.plugin.db.diff.DDLDiffForTableAction;
import zigen.plugin.db.ext.oracle.internal.OpenSourceEdirotAction;
import zigen.plugin.db.ext.oracle.tablespace.CalcTableSpaceWizardAction;
import zigen.plugin.db.ui.actions.CloseDBAction;
import zigen.plugin.db.ui.actions.CollapseAllAction;
import zigen.plugin.db.ui.actions.ConnectDBAction;
import zigen.plugin.db.ui.actions.CopyColumnNameAction;
import zigen.plugin.db.ui.actions.CopyDBAction;
import zigen.plugin.db.ui.actions.CopySchemaNameAction;
import zigen.plugin.db.ui.actions.CopyTableNameAction;
import zigen.plugin.db.ui.actions.CopyTableNameWithRemarksAction;
import zigen.plugin.db.ui.actions.DeleteFromTableAction;
import zigen.plugin.db.ui.actions.DropTreeNodeAction;
import zigen.plugin.db.ui.actions.EditDBAction;
import zigen.plugin.db.ui.actions.ExportDBConfigAction;
import zigen.plugin.db.ui.actions.ImportDBConfigAction;
import zigen.plugin.db.ui.actions.LoadColumnAction;
import zigen.plugin.db.ui.actions.OpenEditorAction;
import zigen.plugin.db.ui.actions.PurgeRecyclebinAction;
import zigen.plugin.db.ui.actions.RefreshAction;
import zigen.plugin.db.ui.actions.RegistBookmarkAction;
import zigen.plugin.db.ui.actions.RegistBookmarkFolderAction;
import zigen.plugin.db.ui.actions.RegistDBAction;
import zigen.plugin.db.ui.actions.RemoveBookmarkAction;
import zigen.plugin.db.ui.actions.RemoveBookmarkFolderAction;
import zigen.plugin.db.ui.actions.RemoveDBAction;
import zigen.plugin.db.ui.actions.RenameBookmarkFolderAction;
import zigen.plugin.db.ui.actions.RenameTableAction;
import zigen.plugin.db.ui.actions.ShowDriverVersionAction;
import zigen.plugin.db.ui.actions.ShowPluginVersionAction;
import zigen.plugin.db.ui.actions.ToggleLinkingAction;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;
import zigen.plugin.db.ui.jobs.RefreshTableJob;
import zigen.plugin.db.ui.views.internal.ColumnFilter;
import zigen.plugin.db.ui.views.internal.ElementFilter;
import zigen.plugin.db.ui.views.internal.ElementFilterDialog;
import zigen.plugin.db.ui.views.internal.FolderFilter;

/**
 * TreeViewクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/03/09 ZIGEN create. [002] 2005/07/15 ZIGEN VO、CSV作成機能。テーブルリネーム機能追加 [003] 2005/10/01 ZIGEN Oracle用表領域見積もり機能の追加
 * 
 */
public class TreeView extends AbstractTreeView {

	PluginSettingsManager pluginMgr = DbPlugin.getDefault().getPluginSettingsManager();

	public static Object lock = new Object();

	private RegistDBAction registDBAction;

	private RemoveDBAction removeDBAction;

	private EditDBAction editDBAction;

	private ConnectDBAction connectDBAction;

	private CloseDBAction closeDBAction;

	private RefreshAction refreshAction;

	private OpenEditorAction openEditorAction;

	private RenameTableAction renameTableAction;

	private CreateCSVAction createCSVAction;

	// private GenerateVOAction generateVOAction;

	private CalcTableSpaceWizardAction calcTableSpaceWizardAction;;

	private CopyDBAction copyDBAction;

	private ShowDriverVersionAction showDriverVersionAction;

	private ShowPluginVersionAction showPluginVersionAction;

	private RegistBookmarkFolderAction registBookmarkFolderAction;

	private RenameBookmarkFolderAction renameBookmarkFolderAction;

	private RemoveBookmarkFolderAction removeBookmarkFolderAction;

	private RegistBookmarkAction registBookmarkAction;

	private RemoveBookmarkAction removeBookmarkAction;

	private CopyTableNameAction copyTableNameAction;

	private CopyTableNameWithRemarksAction copyTableNameWithRemarksAction;

	private CopySchemaNameAction copySchemaNameAction;

	private CopyColumnNameAction copyColumnNameAction;

	// private CopyColumnNameWithRemarksAction copyColumnNameWithRemarksAction;

	private ExportDBConfigAction exportDBConfigAction;

	private ImportDBConfigAction importDBConfigAction;

	private PurgeRecyclebinAction purgeRecyclebinAction;

	private DeleteFromTableAction deleteFromTableAction;

	private DDLDiffForTableAction diffAction;

	private DDLDiffForSchemaAction diffForSchemaAction;

	private DDLDiffForFolderAction diffForFolderAction;

	private DDLDiffForSourceAction diffForSourceAction;

	private OpenSourceEdirotAction openEdirotForSourceAction;

	private DropTreeNodeAction dropTreeNodeAction;

	private ToggleLinkingAction toggleLinkingAction;

	// private TableDefineEditAction tableDefineEditAction;

	// private EditorLinkContiribution editorLinkContiribution;

	// private FilterOnlyConnectedDBAction filterOnlyConnectedDBAction;

	private LoadColumnAction loadColumnAction;

	private CollapseAllAction collapseAllAction;

	ElementFilterAction setColumnFilterAction;

	public void setFocus() {}

	void makeActions() {
		registDBAction = new RegistDBAction(viewer);
		removeDBAction = new RemoveDBAction(viewer);
		removeDBAction.setEnabled(false); // 初期状態False
		editDBAction = new EditDBAction(viewer);
		connectDBAction = new ConnectDBAction(viewer);
		closeDBAction = new CloseDBAction(viewer);
		openEditorAction = new OpenEditorAction(viewer);
		refreshAction = new RefreshAction(viewer);
		// generateVOAction = new GenerateVOAction(viewer);
		renameTableAction = new RenameTableAction(viewer);
		createCSVAction = new CreateCSVAction(viewer);
		calcTableSpaceWizardAction = new CalcTableSpaceWizardAction(viewer);
		showDriverVersionAction = new ShowDriverVersionAction(viewer);
		showPluginVersionAction = new ShowPluginVersionAction();
		copyDBAction = new CopyDBAction(viewer);
		exportDBConfigAction = new ExportDBConfigAction(contentProvider.getRoot());
		importDBConfigAction = new ImportDBConfigAction(viewer);
		purgeRecyclebinAction = new PurgeRecyclebinAction(viewer);
		deleteFromTableAction = new DeleteFromTableAction(viewer);
		diffAction = new DDLDiffForTableAction(viewer);
		diffForSchemaAction = new DDLDiffForSchemaAction(viewer);
		openEdirotForSourceAction = new OpenSourceEdirotAction(viewer);
		dropTreeNodeAction = new DropTreeNodeAction(viewer);
		// tableDefineEditAction = new TableDefineEditAction(viewer);
		toggleLinkingAction = new ToggleLinkingAction(this);
		diffForSourceAction = new DDLDiffForSourceAction(viewer);

		diffForFolderAction = new DDLDiffForFolderAction(viewer);

		// filterOnlyConnectedDBAction = new
		// FilterOnlyConnectedDBAction(viewer);

		setColumnFilterAction = new ElementFilterAction(this);

		bookMarkActions();

		clipboardActions();

	}

	/**
	 * お気に入り用Action作成
	 */
	void bookMarkActions() {
		registBookmarkFolderAction = new RegistBookmarkFolderAction(viewer);
		renameBookmarkFolderAction = new RenameBookmarkFolderAction(viewer);
		removeBookmarkFolderAction = new RemoveBookmarkFolderAction(viewer);
		registBookmarkAction = new RegistBookmarkAction(viewer);
		removeBookmarkAction = new RemoveBookmarkAction(viewer);
	}

	/**
	 * クリップボード用Action作成
	 */
	void clipboardActions() {

		copySchemaNameAction = new CopySchemaNameAction(viewer);
		copyTableNameAction = new CopyTableNameAction(viewer);
		copyColumnNameAction = new CopyColumnNameAction(viewer);
		copyTableNameWithRemarksAction = new CopyTableNameWithRemarksAction(viewer);
		// copyColumnNameWithRemarksAction = new
		// CopyColumnNameWithRemarksAction(viewer);

		loadColumnAction = new LoadColumnAction(viewer);
		collapseAllAction = new CollapseAllAction(viewer);
	}

	void fillContextMenu(IMenuManager manager) {
		// 選択したものによって表示するメニューを変更
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();

		if (obj instanceof Root) {
			removeDBAction.setEnabled(false); // 削除不可能
			manager.add(registDBAction);
			manager.add(removeDBAction);
			manager.add(new Separator());
			manager.add(importDBConfigAction);
			manager.add(exportDBConfigAction);
			manager.add(new Separator());
			manager.add(showPluginVersionAction);

		} else if (obj instanceof BookmarkRoot) {
			manager.add(registBookmarkFolderAction);

		} else if (obj instanceof BookmarkFolder) {
			manager.add(registBookmarkFolderAction);
			manager.add(renameBookmarkFolderAction);
			manager.add(removeBookmarkFolderAction);

		} else if (obj instanceof DataBase) {
			DataBase db = (DataBase) obj;
			removeDBAction.setEnabled(true); // 削除可能
			manager.add(connectDBAction); // 接続
			manager.add(closeDBAction); // 切断
			// manager.add(refreshAction); // 更新（再接続）
			manager.add(new Separator());

			manager.add(registDBAction); // DB定義登録
			manager.add(editDBAction); // DB定義編集
			manager.add(removeDBAction); // DB定義削除
			manager.add(copyDBAction); // DB定義複写
			// manager.add(new Separator());
			// manager.add(importDBConfigAction);
			// manager.add(exportDBConfigAction);
			manager.add(new Separator());
			manager.add(showDriverVersionAction); // Driver Version表示

			// 選択したDataBase要素に応じて、接続/切断のsetEnabledを行う
			if (db.isConnected()) {
				// refreshAction.setEnabled(true);
				connectDBAction.setEnabled(false);
				closeDBAction.setEnabled(true);
				showDriverVersionAction.setEnabled(true);
			} else {
				// refreshAction.setEnabled(false);
				connectDBAction.setEnabled(true);
				closeDBAction.setEnabled(false);
				showDriverVersionAction.setEnabled(false);
			}

		} else if (obj instanceof Schema) {
			// refreshAction.setEnabled(true);
			manager.add(refreshAction); // 更新（再接続）
			manager.add(copySchemaNameAction); // クリップボードにテーブル名を貼り付け

			// <!-- [003] 追加 ZIGEN 2005/10/01
			// Oracle Only
			Schema schema = (Schema) obj;
			switch (DBType.getType(schema.getDbConfig())) {
			case DBType.DB_TYPE_ORACLE:
				manager.add(new Separator());
				manager.add(calcTableSpaceWizardAction); // 表領域の見積

				if (schema.getName().compareToIgnoreCase(schema.getDbConfig().getUserId()) == 0) {
					manager.add(new Separator());
					manager.add(purgeRecyclebinAction);
				}

				break;

			default:
				break;
			}
			// [003] 追加 ZIGEN 2005/10/01 -->

			manager.add(diffForSchemaAction);

			// <!-- [003] 追加 ZIGEN 2005/06/25
		} else if (obj instanceof Folder) {
			// refreshAction.setEnabled(true);
			manager.add(refreshAction); // 更新（再接続）

			Folder folder = (Folder) obj;
			if (folder.getName().equalsIgnoreCase("TABLE")) { //$NON-NLS-1$
				manager.add(loadColumnAction);
			}

			manager.add(diffForFolderAction);

		} else if (obj instanceof OracleSource || obj instanceof OracleSequence) {
			// Source source = (Source)obj;
			manager.add(openEdirotForSourceAction);
			manager.add(dropTreeNodeAction);
			manager.add(diffForSourceAction);

		}

		// [003] 追加 ZIGEN 2005/06/25 -->

		else if (obj instanceof ITable) {

			try {
				ITable table = (ITable) obj;

				manager.add(openEditorAction);
				// manager.add(tableDefineEditAction);

				manager.add(refreshAction); // 更新（再接続）

				manager.add(new Separator());
				manager.add(copyTableNameAction); // クリップボードにテーブル名を貼り付け
				manager.add(copyTableNameWithRemarksAction); // クリップボードにテーブル名を貼り付け
				manager.add(new GroupMarker("group.copy.table"));
				manager.add(new Separator());
				manager.add(new GroupMarker("group.copy.statement"));


				// <!-- [002] 追加 ZIGEN 2005/07/15
				manager.add(new Separator());
				manager.add(renameTableAction);
				manager.add(deleteFromTableAction);

				// Other plug-ins can contribute there actions here
				manager.add(new Separator("Generate")); //$NON-NLS-1$
				// manager.add(generateVOAction);
				manager.add(createCSVAction);

				manager.add(diffAction);

				// お気に入りに追加
				manager.add(new Separator());

				// お気に入りに追加はTableのみ
				if (obj instanceof Table) {
					manager.add(registBookmarkAction);
				} else if (obj instanceof Bookmark) {
					manager.add(removeBookmarkAction);
				}

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		} else if (obj instanceof Column) {
			manager.add(new Separator());
			manager.add(copyColumnNameAction);
			manager.add(new GroupMarker("group.copy.column"));
			manager.add(new Separator());
			manager.add(new GroupMarker("group.copy.statement"));
			manager.add(new Separator());


			// manager.add(copyColumnNameWithRemarksAction);
		}

		// 「お気に入り」用のContextMenu作成
		bookMarkFillContextMenu(manager, obj);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	void bookMarkFillContextMenu(IMenuManager manager, Object obj) {
	// if (obj instanceof BookmarkRoot) {
	// manager.add(refreshAction); // 更新
	// } else if (obj instanceof BookmarkFolder) {
	// manager.add(refreshAction); // 更新
	// } else if (obj instanceof Bookmark) {
	// manager.add(refreshAction); // 更新
	// }
	}

	protected void fillLocalPullDown(IMenuManager manager) {
		// manager.add(registDBAction); // DB定義登録
		// manager.add(removeDBAction); // DB定義登録
		manager.add(new Separator());
		manager.add(importDBConfigAction);
		manager.add(exportDBConfigAction);
		manager.add(new Separator());
		// manager.add(new GroupMarker("ColumnFilter"));
		// manager.appendToGroup("ColumnFilter", setColumnFilterAction);
		manager.add(setColumnFilterAction);
		manager.add(new Separator());
		manager.add(toggleLinkingAction);

	}

	// for test
	protected void addColumnFilterAction(IAction action) {
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().appendToGroup("ColumnFilter", action); //$NON-NLS-1$
	}

	void fillLocalToolBar(IToolBarManager manager) {
		manager.add(registDBAction); // DB定義登録
		// manager.add(removeDBAction); // DB定義登録
		manager.add(new Separator());
		manager.add(importDBConfigAction);
		manager.add(exportDBConfigAction);
		manager.add(new Separator());
		manager.add(collapseAllAction);
		manager.add(toggleLinkingAction);
		// manager.add(filterOnlyConnectedDBAction);

	}

	private Column selectedColumn;

	public Column getSelectedColumn() {
		return selectedColumn;
	}

	/**
	 * 選択した要素によってメニューの活性・非活性を制御
	 */
	void selectionChangeHandler(SelectionChangedEvent event) {

		selectedColumn = null;// 選択カラムを初期化

		removeDBAction.setEnabled(false);
		renameTableAction.setEnabled(false);
		diffForSchemaAction.setEnabled(false);
		diffForFolderAction.setEnabled(false);
		diffAction.setEnabled(false);
		createCSVAction.setEnabled(false);
		deleteFromTableAction.setEnabled(false);
		refreshAction.setEnabled(false);
		// tableDefineEditAction.setEnabled(false);

		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		if (selection.size() == 1) {
			Object element = (Object) (selection).getFirstElement();
			if (element instanceof DataBase) {
				removeDBAction.setEnabled(true);

			} else if (element instanceof ITable) {
				renameTableAction.setEnabled(true);
				createCSVAction.setEnabled(true);
				deleteFromTableAction.setEnabled(true);
				// tableDefineEditAction.setEnabled(true);
			} else if (element instanceof Column) {
				selectedColumn = (Column) element;
			}

			refreshAction.setEnabled(true);

			// status messeage clear
			if (element instanceof TreeNode) {
				IDBConfig selectedConfig = ((TreeNode) element).getDbConfig();
				if (currentIDBConfig != null) {
					if (!currentIDBConfig.equals(selectedConfig)) {
						setStatusMessage(null, ""); //$NON-NLS-1$
					}
				}
			}

			if (element instanceof OracleSource || element instanceof OracleSequence) {
				diffForSourceAction.setEnabled(false);
			}

		} else if (selection.size() == 2) {
			Object ele1 = selection.iterator().next();
			Object ele2 = selection.iterator().next();

			if (ele1 instanceof ITable && ele2 instanceof ITable) {
				// 現時点ではOracleのみDiff機能を提供
				// ITable table1 = (ITable) ele1;
				// ITable table2 = (ITable) ele2;
				/*
				 * if (DBType.DB_TYPE_ORACLE == table1.getDbConfig().getDbType() && DBType.DB_TYPE_ORACLE == table2.getDbConfig().getDbType()) { // Oracleの場合はDDLのDiffメニュー表示
				 * ddlDiffAction.setEnabled(true); } else { ddlDiffAction.setEnabled(false); }
				 */
				diffAction.setEnabled(true);

			} else if (ele1 instanceof Schema && ele2 instanceof Schema) {
				diffForSchemaAction.setEnabled(true);

			} else if (ele1 instanceof Folder && ele2 instanceof Folder) {
				diffForFolderAction.setEnabled(true);

			} else if (ele1 instanceof OracleSource || ele2 instanceof OracleSequence) {
				diffForSourceAction.setEnabled(true);
			}
		}

		setGlobalAction(selection);

	}

	void setGlobalAction(IStructuredSelection selection) {
		IActionBars bars = getViewSite().getActionBars();
		bars.clearGlobalActionHandlers();
		if (selection.size() == 1) {
			Object element = (Object) (selection).getFirstElement();
			if (element instanceof Schema) {
				bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), refreshAction);
			} else if (element instanceof Folder) {
				bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), refreshAction);

			} else if (element instanceof Synonym) {
				bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), refreshAction);

			} else if (element instanceof View) {
				bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), refreshAction);

			} else if (element instanceof Table) {
				bars.setGlobalActionHandler(ActionFactory.RENAME.getId(), renameTableAction);
				bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), refreshAction);

			} else if (element instanceof Bookmark) {
				bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), refreshAction);

			} else if (element instanceof Column) {

			}
		}

		// 選択した要素数に関係なく
		Object element = (Object) (selection).getFirstElement();
		if (element instanceof Schema) {
			bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copySchemaNameAction);

		} else if (element instanceof ITable) {
			bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableNameAction);

		} else if (element instanceof Column) {
			bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyColumnNameAction);
		}
		bars.updateActionBars();
	}

	public void statusChanged(Object obj, int status) {
		if (status == IStatusChangeListener.EVT_ModifyTableDefine) {
			if (obj instanceof ITable) {
				ITable table = (ITable) obj;
				RefreshTableJob job = new RefreshTableJob(viewer, table);
				job.setPriority(RefreshTableJob.SHORT);
				job.setUser(true);
				job.schedule();
			}
		} else if (status == IStatusChangeListener.EVT_LinkTable) {
			if (isLinkingEnabled()) {
				if (obj instanceof ISelection) {
					ISelection selection = (ISelection) obj;
					viewer.setSelection(selection, true);
					// viewer.reveal(table);

				}
			}
		} else if (status == IStatusChangeListener.EVT_ChangeTransactionMode) {

			if (obj instanceof CommitModeAction) {
				// コミットモードを設定する
				IDBConfig config = ((CommitModeAction) obj).getDbConfig();
				DataBase db = contentProvider.findDataBase(config);
				db.getDbConfig().setAutoCommit(config.isAutoCommit());
			}

		} else if (status == IStatusChangeListener.EVT_AddSchemaFilter) {
			if (obj instanceof IDBConfig) {
				IDBConfig config = (IDBConfig) obj;
				setSchemaFilter(config, config.getDisplayedSchemas());
			}

		} else if (status == IStatusChangeListener.EVT_RemoveSchemaFilter) {
			if (obj instanceof IDBConfig) {
				IDBConfig config = (IDBConfig) obj;
				removeSchemaFilter(config, config.getDisplayedSchemas());
			}
		}

	}

	ElementFilter elementFilter;

	FolderFilter folderFilter;

	class ElementFilterAction extends Action {

		String keyword;

		ColumnFilter fColumnFilter;

		TreeView treeView;

		public ElementFilterAction(TreeView view) {
			super(Messages.getString("TreeView.4")); //$NON-NLS-1$
			super.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_FILTER));
			this.treeView = view;
		}

		public void run() {
			Shell shell = DbPlugin.getDefault().getShell();

			ElementFilterDialog dialog = new ElementFilterDialog(shell, treeView);
			int ret = dialog.open();
			if (ret == IDialogConstants.OK_ID) {
				// Filter実行

				FolderInfo[] filterFolders = (FolderInfo[]) pluginMgr.getValue(PluginSettingsManager.KEY_ELEM_FILTER_FOLDER_LIST);
				if (folderFilter != null)
					treeView.getTreeViewer().removeFilter(folderFilter);
				folderFilter = new FolderFilter(filterFolders);
				treeView.getTreeViewer().addFilter(folderFilter);

				boolean isVisible = pluginMgr.getValueBoolean(PluginSettingsManager.KEY_ELEM_FILTER_VISIBLE);
				boolean isRegularExp = pluginMgr.getValueBoolean(PluginSettingsManager.KEY_ELEM_FILTER_REGULAREXP);
				boolean isCaseSensitive = pluginMgr.getValueBoolean(PluginSettingsManager.KEY_ELEM_FILTER_CASESENSITIVE);

				String pattern = pluginMgr.getValueString(PluginSettingsManager.KEY_ELEM_FILTER_PATTERN);

				if (elementFilter != null)
					treeView.getTreeViewer().removeFilter(elementFilter);

				if (isVisible) {
					elementFilter = new ElementFilter(pattern, isRegularExp, isCaseSensitive);
					treeView.getTreeViewer().addFilter(elementFilter);
				}

			}

		}


	}


}
