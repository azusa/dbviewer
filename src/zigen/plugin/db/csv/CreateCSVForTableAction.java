/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.csv;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableManager;
import zigen.plugin.db.preference.CSVPreferencePage;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;

/**
 * CreateCSVActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/07/08 ZIGEN create.
 * 
 */
public class CreateCSVForTableAction extends Action {

	protected TableViewEditorFor31 editor;

	private IPreferenceStore store;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public CreateCSVForTableAction() {
		this.setText(Messages.getString("CreateCSVForTableAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CreateCSVForTableAction.1")); //$NON-NLS-1$

		this.store = DbPlugin.getDefault().getPreferenceStore();
	}

	public void setActiveEditor(TableViewEditorFor31 editor) {
		this.editor = editor;
	}

	/**
	 * Action実行時の処理
	 */
	public void run() {
		// 1件ずつしか処理しない場合
		invoke();

	}

	private void invoke() {

		try {
			Shell shell = DbPlugin.getDefault().getShell();
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFileName(editor.getTableNode().getName());
			dialog.setFilterExtensions(new String[] {
					"*.csv", "*.*" //$NON-NLS-1$ //$NON-NLS-2$
			});
			dialog.setFilterNames(new String[] {
					Messages.getString("CreateCSVForTableAction.4"), Messages.getString("CreateCSVForTableAction.5") //$NON-NLS-1$ //$NON-NLS-2$
					});
			String fileName = dialog.open();

			// キャンセル時の処理
			if (fileName == null)
				return;

			File csvFile = new File(fileName);
			if (csvFile.exists()) {
				// Shell shell = DbPlugin.getDefault().getShell();
				MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				msg.setMessage(fileName + Messages.getString("CreateCSVForTableAction.6")); //$NON-NLS-1$
				msg.setText(Messages.getString("CreateCSVForTableAction.7")); //$NON-NLS-1$
				int res2 = msg.open();
				if (res2 == SWT.NO)
					return;
			}

			CSVConfig config = new CSVConfig();

			String encoding = store.getString(CSVPreferencePage.P_ENCODING);
            String separator = store.getString(CSVPreferencePage.P_DEMILITER);
            boolean nonHeader = store.getBoolean(CSVPreferencePage.P_NON_HEADER);
            boolean nonDoubleQuate = store.getBoolean(CSVPreferencePage.P_NON_DOUBLE_QUATE);
            
			
			String condition = editor.getCondition();
			if(condition == null || condition.length() == 0){
				config.setQuery(TableManager.getSQLForCSV(editor.getTableNode()));
			}else{
				config.setQuery(TableManager.getSQLForCSV(editor.getTableNode(), condition));	
			}
			
			config.setCsvEncoding(encoding);
            config.setSeparator(separator);
            config.setNonHeader(nonHeader);
            config.setNonDoubleQuate(nonDoubleQuate);
            

			config.setCsvFile(fileName);
			CSVWriter writer = new CSVWriter(editor.getDBConfig(), config);
			writer.execute();

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
