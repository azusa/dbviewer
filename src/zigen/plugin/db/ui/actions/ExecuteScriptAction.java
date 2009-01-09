/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorInfo;
import zigen.plugin.db.ui.editors.sql.IPlsqlEditor;
import zigen.plugin.db.ui.jobs.ScriptExecJob;

public class ExecuteScriptAction extends Action implements Runnable {

	private boolean editorMode = false;

	private IDBConfig config;

	private String secondaryId;

	private IDocument doc;

	public ExecuteScriptAction(IDBConfig config, IDocument doc, String secondaryId) {
		super.setText(zigen.plugin.db.ui.actions.Messages.getString("ExecuteScriptAction.0")); //$NON-NLS-1$
		super.setToolTipText(zigen.plugin.db.ui.actions.Messages.getString("ExecuteScriptAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_SCRIPT));
		this.config = config;
		this.doc = doc;
		this.secondaryId = secondaryId;
	}

	private IPlsqlEditor plsqlEditor;

	public ExecuteScriptAction(IPlsqlEditor plsqlEditor) {
		super.setText(zigen.plugin.db.ui.actions.Messages.getString("ExecuteScriptAction.0")); //$NON-NLS-1$
		super.setToolTipText(zigen.plugin.db.ui.actions.Messages.getString("ExecuteScriptAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_SCRIPT));
		this.config = plsqlEditor.getConfig();
		this.editorMode = true;
		this.doc = plsqlEditor.getPLSQLSourceViewer().getDocument();
		this.secondaryId = null;
		this.plsqlEditor = plsqlEditor;
	}

	public void run() {
		try {
			if (config != null) {
				String sql = doc.get();
				if (sql != null && sql.trim().length() > 0) {

					// �g�����U�N�V�����擾
					Transaction trans = Transaction.getInstance(config);
					ScriptExecJob job = new ScriptExecJob(trans, sql, secondaryId);
					// job.setPriority(Job.SHORT);
					job.setUser(false); // �_�C�A���O���o���Ȃ�
					job.schedule();

					// RefreshFolderJob���I������܂őҋ@
					if (editorMode) {
						try {
							job.join();
							plsqlEditor.clearError();

							OracleSourceErrorInfo[] errs = job.getOracleSourceErrorInfos();
							if (errs != null) {
								plsqlEditor.setError(errs);
							}

						} catch (InterruptedException e) {
							DbPlugin.log(e);
						}

					}
				} else {
					DbPlugin.getDefault().showInformationMessage(zigen.plugin.db.ui.actions.Messages.getString("ExecuteScriptAction.2")); //$NON-NLS-1$
				}
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

}
