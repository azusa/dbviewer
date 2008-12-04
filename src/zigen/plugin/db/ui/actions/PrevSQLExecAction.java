/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLHistory;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.views.SQLExecuteView;

/**
 * CommitAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class PrevSQLExecAction extends Action implements Runnable {
	SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();
	private SQLExecuteView view;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public PrevSQLExecAction(SQLExecuteView view) {

		this.setText(Messages.getString("PrevSQLExecAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("PrevSQLExecAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLBackCommand"); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_BACK));
		// this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPluginConstant.IMG_CODE_BACK));
		// this.setDisabledImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPluginConstant.IMG_CODE_E_BACK));
		this.view = view;
	}

	/**
	 * Action���s���̏���
	 */
	public void run() {

		SQLHistory history = mgr.prevHisotry();

		if (history != null) {
			String sql = mgr.loadContents(history);
			view.getSqlViewer().getDocument().set(sql);
			//view.getSqlViewer().setSelectedRange(sql.length(), 0);
			view.getSqlViewer().invalidateTextPresentation(); // �e�L�X�g�G�f�B�^���ĕ`��
		} else {
			view.getSqlViewer().getDocument().set(""); //$NON-NLS-1$
		}
		view.updateHistoryButton();

	}
}
