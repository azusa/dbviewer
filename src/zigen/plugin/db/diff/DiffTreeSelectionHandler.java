/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import zigen.plugin.db.DbPlugin;

/**
 * DiffTreeSelectionHandlerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/03 ZIGEN create.
 * 
 */
public class DiffTreeSelectionHandler implements ISelectionChangedListener {

	private DDLDiffEditor editor;

	public DiffTreeSelectionHandler(DDLDiffEditor editor) {
		this.editor = editor;

	}

	public void selectionChanged(SelectionChangedEvent event) {

		try {
			ISelection selection = event.getSelection();

			if (selection instanceof StructuredSelection) {
				Object element = ((StructuredSelection) selection).getFirstElement();

				if (element instanceof IDDLDiff) {
					IDDLDiff diff = (IDDLDiff) element;
					editor.getDiffviewer().setInput(diff);

				}

			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}
}
