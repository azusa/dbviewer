/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import zigen.plugin.db.ui.views.TreeView;

/**
 * CopyColumnNameAction.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/12/04 ZIGEN create.
 * 
 */
abstract public class AbstractCopyAction implements IObjectActionDelegate {

	protected IAction action;

	protected IStructuredSelection selection;

	protected TreeView treeView;

	protected StructuredViewer structuredViewer;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		if (targetPart instanceof TreeView) {
			this.treeView = (TreeView)targetPart;
			this.structuredViewer = treeView.getTreeViewer();
		}else{
			throw new RuntimeException("Required TreeView"); //$NON-NLS-1$
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.action = action;
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
			this.action.setEnabled(true);
		} else {
			this.action.setEnabled(false);
			throw new RuntimeException("Required IStructuredSelection."); //$NON-NLS-1$
		}
	}
	
	abstract public void run(IAction action);

}
