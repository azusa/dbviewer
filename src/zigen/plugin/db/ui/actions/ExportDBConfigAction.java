/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.wizard.ExpDBConfigWizard;

/**
 * 
 * ExportDBConfigAction.javaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/04/12 ZIGEN create.
 * 
 */
public class ExportDBConfigAction extends Action {

	Root root = null;

	/**
	 * コンストラクタ
	 * 
	 * @param viewer
	 */
	public ExportDBConfigAction(Root root) {
		this.root = root;
		this.setText(Messages.getString("ExportDBConfigAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ExportDBConfigAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_EXPORT));

	}

	/**
	 * Action実行時の処理
	 */
	public void run() {

		try {
			Shell shell = DbPlugin.getDefault().getShell();
			ExpDBConfigWizard wizard = new ExpDBConfigWizard(root);
			WizardDialog dialog = new WizardDialog(shell, wizard);
			dialog.open();

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}

}
