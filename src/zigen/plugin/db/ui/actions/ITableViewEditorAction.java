/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import zigen.plugin.db.ui.editors.ITableViewEditor;

public interface ITableViewEditorAction extends IAction {

	public void setActiveEditor(ITableViewEditor targetEditor);

	public void run();

	public void selectionChanged(ISelection selection);
}
