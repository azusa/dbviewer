/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.ui.IEditorPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.actions.LockDataBaseAction;
import zigen.plugin.db.ui.actions.OpenSQLAction;
import zigen.plugin.db.ui.actions.SaveSQLAction;
import zigen.plugin.db.ui.actions.ShowHistoryViewAction;
import zigen.plugin.db.ui.editors.sql.PlsqlEditor;
import zigen.plugin.db.ui.editors.sql.SqlEditor;
import zigen.plugin.db.ui.views.CommitModeAction;
import zigen.plugin.db.ui.views.FormatModeAction;
import zigen.plugin.db.ui.views.ISQLOperationTarget;

public class CopyOfSQLToolBar {

	protected PluginSettingsManager pluginMgr = DbPlugin.getDefault().getPluginSettingsManager();

	protected SQLHistoryManager historyManager = DbPlugin.getDefault().getHistoryManager();

	protected IDBConfig[] configs;

	protected SQLSourceViewer fSourceViewer;

	protected CoolBar coolBar;

	protected Combo selectCombo;

	protected ComboContributionItem comboContributionItem = new ComboContributionItem("SelectDataBase"); //$NON-NLS-1$

	protected GlobalAction allExecAction = new GlobalAction(null, ISQLOperationTarget.ALL_EXECUTE);

	protected GlobalAction currExecAction = new GlobalAction(null, ISQLOperationTarget.CURRENT_EXECUTE);

	protected GlobalAction selectExecAction = new GlobalAction(null, ISQLOperationTarget.SELECTED_EXECUTE);

	protected GlobalAction scriptExecAction = new GlobalAction(null, ISQLOperationTarget.SCRIPT_EXECUTE);

	protected GlobalAction allClearAction = new GlobalAction(null, ISQLOperationTarget.ALL_CLEAR);

	protected GlobalAction nextSqlAction = new GlobalAction(null, ISQLOperationTarget.NEXT_SQL);

	protected GlobalAction backSqlAction = new GlobalAction(null, ISQLOperationTarget.BACK_SQL);

	protected GlobalAction formatSqlAction = new GlobalAction(null, ISQLOperationTarget.FORMAT);

	protected GlobalAction commitAction = new GlobalAction(null, ISQLOperationTarget.COMMIT);

	protected GlobalAction rollbackAction = new GlobalAction(null, ISQLOperationTarget.ROLLBACK);

	protected OpenSQLAction openAction = new OpenSQLAction(null);

	protected SaveSQLAction saveAction = new SaveSQLAction(null);

	protected CommitModeAction commitModeAction = new CommitModeAction(null);

	protected FormatModeAction formatModeAction = new FormatModeAction(null);

	protected ShowHistoryViewAction showHistoryViewAction = new ShowHistoryViewAction();

	protected LockDataBaseAction lockDataBaseAction = new LockDataBaseAction(null);

	IEditorPart fEditor;

	String lastSelectedDB;

	boolean lastAutoFormatMode;

	public static int TYPE_SQL_EXECUTE_VIEW = 0;

	public static int TYPE_SQL_EDITOR = 1;

	public static int TYPE_PLSQL_EDITOR = 2;

	private int type = TYPE_SQL_EXECUTE_VIEW;

	public CopyOfSQLToolBar(final Composite parent, IEditorPart editor) {

		this.fEditor = editor;

		if (editor instanceof PlsqlEditor) {
			this.type = TYPE_PLSQL_EDITOR;
		} else if (editor instanceof SqlEditor) {
			this.type = TYPE_SQL_EDITOR;
		}

//		coolBar = new CoolBar(parent, SWT.FLAT);
		coolBar = new CoolBar(parent, SWT.NONE);

		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(data);

		CoolBarManager coolBarMgr = new CoolBarManager(coolBar);

		ToolBarManager toolBarMgr1 = new ToolBarManager(SWT.FLAT);

		if (type == TYPE_SQL_EDITOR || type == TYPE_SQL_EXECUTE_VIEW) {
			toolBarMgr1.add(allExecAction);
		}
		if (type == TYPE_PLSQL_EDITOR || type == TYPE_SQL_EXECUTE_VIEW) {
			toolBarMgr1.add(scriptExecAction);
		}
		
		toolBarMgr1.add(allClearAction);

		ToolBarManager toolBarMgr2 = new ToolBarManager(SWT.FLAT);

		if (type == TYPE_SQL_EXECUTE_VIEW) {
			toolBarMgr2.add(backSqlAction);
			toolBarMgr2.add(nextSqlAction);
		}

		ToolBarManager toolBarMgr3 = new ToolBarManager(SWT.FLAT);
		if (type == TYPE_SQL_EDITOR || type == TYPE_SQL_EXECUTE_VIEW) {
			toolBarMgr3.add(commitModeAction);
			toolBarMgr3.add(commitAction);
			toolBarMgr3.add(rollbackAction);
		}

		ToolBarManager toolBarMgr4 = new ToolBarManager(SWT.FLAT);
		if (type == TYPE_SQL_EDITOR || type == TYPE_SQL_EXECUTE_VIEW) {
			toolBarMgr4.add(formatModeAction);
		}

		ToolBarManager toolBarMgr6 = new ToolBarManager(SWT.FLAT);

		if (type == TYPE_SQL_EXECUTE_VIEW) {
			toolBarMgr6.add(openAction);
			toolBarMgr6.add(saveAction);
		} else {
			// エディターとしての保存
			toolBarMgr6.add(new SaveAction());
		}
		
		ToolBarManager toolBarMgr5 = new ToolBarManager(SWT.FLAT);
		toolBarMgr5.add(comboContributionItem); //$NON-NLS-1$

		if (type == TYPE_SQL_EXECUTE_VIEW) {
			toolBarMgr5.add(lockDataBaseAction);
		}

		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr6));
		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr1));

		if (type == TYPE_SQL_EXECUTE_VIEW) {
			coolBarMgr.add(new ToolBarContributionItem(toolBarMgr2));
		}
		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr3));
		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr4));
		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr5));
		coolBarMgr.update(true);

		coolBar.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}

			public void controlResized(ControlEvent e) {
				parent.getParent().layout(true);
				parent.layout(true);
			}
		});

	}

	public CopyOfSQLToolBar(final Composite parent) {
		this(parent, null);
	}

	public void setSQLSourceViewer(SQLSourceViewer sqlSourceViewer) {
		fSourceViewer = sqlSourceViewer;

		openAction.setSQLSourceViewer(sqlSourceViewer);
		saveAction.setSQLSourceViewer(sqlSourceViewer);

		nextSqlAction.setTextViewer(sqlSourceViewer);
		backSqlAction.setTextViewer(sqlSourceViewer);

		allExecAction.setTextViewer(sqlSourceViewer);
		currExecAction.setTextViewer(sqlSourceViewer);
		selectExecAction.setTextViewer(sqlSourceViewer);
		scriptExecAction.setTextViewer(sqlSourceViewer);
		allClearAction.setTextViewer(sqlSourceViewer);
		formatModeAction.setSQLSourceViewer(sqlSourceViewer);
		commitModeAction.setSQLSourceViewer(sqlSourceViewer);
		commitAction.setTextViewer(sqlSourceViewer);
		rollbackAction.setTextViewer(sqlSourceViewer);
	}

	/**
	 * 履歴ボタンの有効設定 外部のActionから呼ばれます
	 */
	public final void updateHistoryButton() {
		// 前ボタン
		if (historyManager.hasPrevHistory()) {
			if (backSqlAction != null)
				backSqlAction.setEnabled(true);
		} else {
			if (backSqlAction != null)
				backSqlAction.setEnabled(false);
		}
		// 次ボタン
		if (historyManager.hasNextHistory()) {
			if (nextSqlAction != null)
				nextSqlAction.setEnabled(true);
		} else {
			if (nextSqlAction != null)
				nextSqlAction.setEnabled(false);
		}
	}

	public void updateCombo(IDBConfig config) {
		if(!lockDataBaseAction.isChecked()){
			comboContributionItem.updateCombo(config);	
		}
	}

	public void initializeSelectCombo() {
		comboContributionItem.initializeSelectCombo();
	}

	public void setCommitMode(IDBConfig targetConfig, boolean autoCommit) {
		comboContributionItem.setCommitMode(targetConfig, autoCommit);
	}

	class ComboContributionItem extends ControlContribution {
		public ComboContributionItem(String id) {
			super(id);

		}

		protected Control createControl(Composite parent) {
			selectCombo = new Combo(parent, SWT.READ_ONLY);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = 200;
			selectCombo.setLayoutData(data);

			if (type == TYPE_SQL_EXECUTE_VIEW) {
				// Viewの場合のみ
				lastSelectedDB = getLastSelectedDBName();
			}

			initializeSelectCombo();

			selectCombo.addSelectionListener(new SelectionAdapter() {
				// 接続先を変更した場合
				public void widgetSelected(SelectionEvent e) {
					IDBConfig config = getConfig();

					if (fSourceViewer != null) {
						updateCombo(config);
					}

					if (fEditor == null) {
						// Viewの場合のみ
						lastSelectedDB = config.getDbName();
						setLastSelectedDBName(lastSelectedDB);
					}

					DbPlugin.fireStatusChangeListener(config, IStatusChangeListener.EVT_ChangeDataBase);
				}
			});

			return selectCombo;
		}

		String getLastSelectedDBName() {
			Object obj = pluginMgr.getValue(PluginSettingsManager.KEY_DEFAULT_DB);
			if (obj != null) {
				return (String) obj;
			} else {
				return null;
			}
		}

		void setLastSelectedDBName(String dbName) {
			pluginMgr.setValue(PluginSettingsManager.KEY_DEFAULT_DB, dbName);
		}

		void initializeSelectCombo() {
			IDBConfig config = getConfig();
			selectCombo.removeAll();
			configs = DBConfigManager.getDBConfigs();
			for (int i = 0; i < configs.length; i++) {
				IDBConfig w_config = configs[i];
				selectCombo.add(w_config.getSchema() + " : " + w_config.getDbName() + "  "); //$NON-NLS-1$ //$NON-NLS-2$

				if (lastSelectedDB != null && lastSelectedDB.equals(w_config.getDbName())) {
					// この時点では見た目は変わるが、SourceViewerへの反映ができていない
					selectCombo.select(i);
				}

				if (config != null && config.getDbName().equals(w_config.getDbName())) {
					// この時点では見た目は変わるが、SourceViewerへの反映ができていない
					selectCombo.select(i);
				}
			}
		}

		void updateCombo(IDBConfig newConfig) {
			if (newConfig != null) {
				for (int i = 0; i < configs.length; i++) {
					IDBConfig w_config = configs[i];
					if (newConfig != null) {
						if (newConfig.getDbName().equals(w_config.getDbName())) {
							selectCombo.select(i);
						}
					}
				}
				if (fSourceViewer != null) {
					fSourceViewer.setDbConfig(newConfig);
					commitModeAction.setSQLSourceViewer(fSourceViewer);
					comboContributionItem.setCommitMode(newConfig, newConfig.isAutoCommit());
				}
			} else {
				selectCombo.select(-1);
			}
		}

		private void setCommitMode(IDBConfig targetConfig, boolean isAutoCommit) {
			IDBConfig config = getConfig();
			if (config == null || config.getDbName().equals(targetConfig.getDbName())) {
				commitAction.setEnabled(!isAutoCommit);
				rollbackAction.setEnabled(!isAutoCommit);
				commitModeAction.setCommitMode(isAutoCommit);

			}

		}

		public IDBConfig selectedConfig() {
			int index = selectCombo.getSelectionIndex();
			if (index >= 0) {
				return configs[index];
			} else {
				return null;
			}
		}
	}

	public ComboContributionItem getComboContributionItem() {
		return comboContributionItem;
	}

	public IDBConfig getConfig() {
		return comboContributionItem.selectedConfig();
	}

	public IDBConfig[] getConfigs() {
		return configs;
	}

	public CoolBar getCoolBar() {
		return coolBar;
	}

	public SQLSourceViewer getFSourceViewer() {
		return fSourceViewer;
	}

	public Combo getSelectCombo() {
		return selectCombo;
	}

	/**
	 * エディター保存用
	 * 
	 */
	private class SaveAction extends Action {

		public SaveAction() {
			this.setToolTipText(Messages.getString("SQLToolBar.3")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_SAVE));
		}

		public void run() {
			fEditor.doSave(null);
		}

	}
	
	public boolean isLockedDataBase(){
		return lockDataBaseAction.isChecked();
	}
	public void setLockedDataBase(boolean isLocked){
		lockDataBaseAction.setChecked(isLocked);
	}
	
}
