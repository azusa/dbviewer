/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.bookmark;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.TreeItem;

import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

/**
 * DropBookmarkAdapter
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class DropBookmarkAdapter extends DropTargetAdapter {


	TreeViewer viewer;

	IStructuredSelection selection;

	public DropBookmarkAdapter(TreeViewer viewer) {
		this.viewer = viewer;
	}

	public void dragOver(DropTargetEvent e) {
		setEventDetail(e);
		e.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
	}

	public void dropAccept(DropTargetEvent e) {
		if (e.item == null) {
			e.detail = DND.DROP_NONE;
		} else {

			// Drop対象のオブジェクトを取得する
			Object obj = ((TreeItem) e.item).getData();
			if (obj instanceof TreeLeaf) {
				if (obj instanceof BookmarkRoot || obj instanceof BookmarkFolder) {
					e.detail = DND.DROP_MOVE;
				} else {
					e.detail = DND.DROP_NONE;
				}
			} else {
				e.detail = DND.DROP_NONE;
			}

			// DragOverでも実装しているが念のため
			setEventDetail(e);

		}
	}

	private void setEventDetail(DropTargetEvent e) {
		if (e.item == null) {
			e.detail = DND.DROP_NONE;
			return;
		}

		if (!(e.item instanceof TreeItem)) {
			e.detail = DND.DROP_NONE;
			return;
		}

		if (!(e.item.getData() instanceof TreeNode)) {
			e.detail = DND.DROP_NONE;
			return;
		}

		// Drop対象のオブジェクトを取得する
		Object target = ((TreeItem) e.item).getData();

		// 自分の子に移動すること禁止する
		// ※ e.dataは、オリジナルのコピーなので、
		// Viewerで選択しているオブジェクトを使って判定すること

		// viewerからselectionを取得するのは、Eclipse3.1系でしか取得できない
		// # Eclipse3.0 ではselection.size()が0になる。
		// 回避方法：dragEnterの時点で、selectionを取得しておく
		// IStructuredSelection selection = (IStructuredSelection)
		// viewer.getSelection(); // コメント

		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			TreeLeaf leaf = (TreeLeaf) iter.next();

			// BookmarkFolder or Bookmark以外はDROP不可
			if (!(leaf instanceof Bookmark) && !(leaf instanceof BookmarkFolder)) {
				e.detail = DND.DROP_NONE;
				return;
			}

			// 自分自身かどうかの判定
			if (leaf.equals((TreeLeaf) target)) {
				e.detail = DND.DROP_NONE;
				return;
			}
			// 自分の子かどうかの判定
			if (isMyChild(leaf, (TreeLeaf) target)) {
				e.detail = DND.DROP_NONE;
				return; // 自分の子に移動するときは抜ける
			}

		}

		if (target instanceof BookmarkRoot || target instanceof BookmarkFolder) {
			e.detail = DND.DROP_MOVE;
		} else {
			e.detail = DND.DROP_NONE;
		}
	}

	/**
	 * targetが自分の子かどうか
	 * 
	 * @param parent
	 * @param target
	 * @return
	 */
	private boolean isMyChild(TreeLeaf parent, TreeLeaf target) {
		if (target.getParent() == null) {
			return false;
		} else {
			TreeNode wkParent = target.getParent();
			if (parent.equals(wkParent)) {
				return true;
			} else {
				return isMyChild(parent, wkParent);
			}

		}
	}

	public void dragEnter(DropTargetEvent e) {
		// Eclipse3.0用にDragEnterの時点でIStructuredSelectionを取得
		selection = (IStructuredSelection) viewer.getSelection();

		if (e.detail == DND.DROP_DEFAULT) {
			e.detail = DND.DROP_MOVE;
		}
	}

	public void dragOperationChanged(DropTargetEvent e) {
		if (e.detail == DND.DROP_DEFAULT) {
			e.detail = DND.DROP_MOVE;
		}
	}

	public void drop(DropTargetEvent e) {

		if (e.data == null) {
			e.detail = DND.DROP_NONE;
			return;
		}

		Object obj = ((TreeItem) e.item).getData();
		if (obj instanceof TreeNode) {
			TreeNode target = (TreeNode) obj;

			if (TreeLeafListTransfer.getInstance().isSupportedType(e.currentDataType)) {

				// Dropするオブジェクトは、e.dataを使用すること
				TreeLeaf[] leafs = (TreeLeaf[]) e.data;

				for (int i = 0; i < leafs.length; i++) {
					TreeLeaf leaf = leafs[i];
					target.addChild(leaf);
					viewer.expandToLevel(leaf, 1);

				}

				viewer.refresh(target);
			} else {
				throw new RuntimeException("予期しないエラー"); //$NON-NLS-1$
			}

		} else {
			throw new IllegalArgumentException("予期しないオブジェクト" + obj.getClass().getName()); //$NON-NLS-1$
		}

	}
}
