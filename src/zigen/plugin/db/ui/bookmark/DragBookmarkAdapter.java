/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.bookmark;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

/**
 * DragBookmarkAdapter
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class DragBookmarkAdapter extends DragSourceAdapter {


	TreeViewer viewer;

	public DragBookmarkAdapter(TreeViewer viewer) {

		this.viewer = viewer;
	}

	/**
	 * ドラック可能かどうかの判定 「異なる階層(level)」を選択しているときは、ドラック不可とする
	 * 
	 * @param iter
	 * @return
	 */
	private boolean canDrag(Iterator iter) {
		int wk = -1;
		while (iter.hasNext()) {
			TreeLeaf leaf = (TreeLeaf) iter.next();
			if (wk == -1 || wk == leaf.getLevel()) {
				wk = leaf.getLevel();
				continue;
			} else {
				return false;
			}
		}
		return true;

	}

	public void dragStart(DragSourceEvent e) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		e.doit = canDrag(selection.iterator());
	}

	public void dragSetData(DragSourceEvent e) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		e.data = selection.toList().toArray(new TreeLeaf[0]);

	}

	public void dragFinished(DragSourceEvent e) {
		if (e.detail == DND.DROP_MOVE) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			Iterator iter = selection.iterator();
			while (iter.hasNext()) {
				TreeLeaf leaf = (TreeLeaf) iter.next();
				TreeNode parent = leaf.getParent();
				parent.removeChild(leaf);
				viewer.refresh(parent);

			}

		} else {
			;
		}
	}
}
