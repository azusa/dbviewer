/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

/**
 * LockDataBaseAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class LockDataBaseAction extends SQLSourceViewerAction {

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public LockDataBaseAction(SQLSourceViewer viewer) {
		super(viewer, Messages.getString("LockDataBaseAction.0"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("LockDataBaseAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_PIN));
	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
	// fSQLSourceViewer.setLockedDataBase(isChecked());
	}

	public void setChecked(boolean checked) {
		super.setChecked(checked);
		// �A�C�R����ύX����

	}

}
