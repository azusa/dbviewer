/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import java.io.File;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.util.FileUtil;

/**
 * DropTreeLeafAdapter�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class DropTreeLeafAdapter extends DropTargetAdapter {

	SourceViewer viewer;

	public DropTreeLeafAdapter(SourceViewer viewer) {
		this.viewer = viewer;
	}

	public void dragEnter(DropTargetEvent e) {
		if (e.detail == DND.DROP_DEFAULT) {
			e.detail = DND.DROP_COPY;
		}
	}

	public void dragOperationChanged(DropTargetEvent e) {
		if (e.detail == DND.DROP_DEFAULT) {
			e.detail = DND.DROP_COPY;
		}
	}

	public void drop(DropTargetEvent e) {
		if (e.data == null) {
			e.detail = DND.DROP_NONE;
			return;
		}

		if (e.data instanceof TreeLeaf[]) {
			// Drop����I�u�W�F�N�g�́Ae.data���g�p���邱��
			TreeLeaf[] leafs = (TreeLeaf[]) e.data;
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < leafs.length; i++) {
				TreeLeaf leaf = leafs[i];
				if (i > 0) {
					sb.append(", "); //$NON-NLS-1$
				}
				sb.append(leaf.getName());

			}

			String str = sb.toString();

			// ���݂̃J�[�\���ʒu��
			int offset = viewer.getTextWidget().getCaretOffset();
			viewer.getTextWidget().insert(str);
			// �h���b�N����������̌��ɃJ�[�\���ړ�
			viewer.getTextWidget().setCaretOffset(offset + str.length());
			viewer.activatePlugins();
			viewer.getControl().forceFocus();
			viewer.getTextWidget().setFocus();


		} else if (e.data instanceof String[]) {

			String[] strs = (String[]) e.data;
			// FileTransfer�p�̎���

			if (strs.length == 1) {
				File file = new File(strs[0]);
				if (file.exists() && FileUtil.isSqlFile(file)) {

					String content = FileUtil.getContents(file);

					// ���e�̏㏑���m�F���s��
					String pre = viewer.getDocument().get().trim();
					if ("".equals(pre)) { //$NON-NLS-1$
						viewer.getDocument().set(content);
						viewer.activatePlugins();
						viewer.getControl().forceFocus();
						viewer.getTextWidget().setFocus();


					} else {
						String msg = Messages.getString("DropTreeLeafAdapter.2"); //$NON-NLS-1$
						if (DbPlugin.getDefault().confirmDialog(msg)) {
							viewer.getDocument().set(content);
							viewer.activatePlugins();
							viewer.getControl().forceFocus();
							viewer.getTextWidget().setFocus();

						}
					}

				}
			} else {
				DbPlugin.getDefault().showWarningMessage(Messages.getString("DropTreeLeafAdapter.3")); //$NON-NLS-1$
			}

		}

	}

}
