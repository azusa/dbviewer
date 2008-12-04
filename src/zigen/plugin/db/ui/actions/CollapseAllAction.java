/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;

/**
 * CollapseAllAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 */
public class CollapseAllAction extends Action implements Runnable {
	
	TreeViewer viewer = null;
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public CollapseAllAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText("Collapse All");
		this.setToolTipText("Collapse All");
		this.setEnabled(true);
		
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_COLLAPSE_ALL));
	}
	
	/**
	 * Action���s���̏���
	 */
	public void run() {
		viewer.collapseAll();
	}
	
}
