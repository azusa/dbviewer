/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
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

			// Drop�Ώۂ̃I�u�W�F�N�g���擾����
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

			// DragOver�ł��������Ă��邪�O�̂���
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

		// Drop�Ώۂ̃I�u�W�F�N�g���擾����
		Object target = ((TreeItem) e.item).getData();

		// �����̎q�Ɉړ����邱�Ƌ֎~����
		// �� e.data�́A�I���W�i���̃R�s�[�Ȃ̂ŁA
		// Viewer�őI�����Ă���I�u�W�F�N�g���g���Ĕ��肷�邱��

		// viewer����selection���擾����̂́AEclipse3.1�n�ł����擾�ł��Ȃ�
		// # Eclipse3.0 �ł�selection.size()��0�ɂȂ�B
		// �����@�FdragEnter�̎��_�ŁAselection���擾���Ă���
		// IStructuredSelection selection = (IStructuredSelection)
		// viewer.getSelection(); // �R�����g

		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			TreeLeaf leaf = (TreeLeaf) iter.next();

			// BookmarkFolder or Bookmark�ȊO��DROP�s��
			if (!(leaf instanceof Bookmark) && !(leaf instanceof BookmarkFolder)) {
				e.detail = DND.DROP_NONE;
				return;
			}

			// �������g���ǂ����̔���
			if (leaf.equals((TreeLeaf) target)) {
				e.detail = DND.DROP_NONE;
				return;
			}
			// �����̎q���ǂ����̔���
			if (isMyChild(leaf, (TreeLeaf) target)) {
				e.detail = DND.DROP_NONE;
				return; // �����̎q�Ɉړ�����Ƃ��͔�����
			}

		}

		if (target instanceof BookmarkRoot || target instanceof BookmarkFolder) {
			e.detail = DND.DROP_MOVE;
		} else {
			e.detail = DND.DROP_NONE;
		}
	}

	/**
	 * target�������̎q���ǂ���
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
		// Eclipse3.0�p��DragEnter�̎��_��IStructuredSelection���擾
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

				// Drop����I�u�W�F�N�g�́Ae.data���g�p���邱��
				TreeLeaf[] leafs = (TreeLeaf[]) e.data;

				for (int i = 0; i < leafs.length; i++) {
					TreeLeaf leaf = leafs[i];
					target.addChild(leaf);
					viewer.expandToLevel(leaf, 1);

				}

				viewer.refresh(target);
			} else {
				throw new RuntimeException("�\�����Ȃ��G���["); //$NON-NLS-1$
			}

		} else {
			throw new IllegalArgumentException("�\�����Ȃ��I�u�W�F�N�g" + obj.getClass().getName()); //$NON-NLS-1$
		}

	}
}
