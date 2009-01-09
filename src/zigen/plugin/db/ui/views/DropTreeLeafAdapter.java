/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
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
 * DropTreeLeafAdapterクラス.
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
			// Dropするオブジェクトは、e.dataを使用すること
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

			// 現在のカーソル位置は
			int offset = viewer.getTextWidget().getCaretOffset();
			viewer.getTextWidget().insert(str);
			// ドラックした文字列の後ろにカーソル移動
			viewer.getTextWidget().setCaretOffset(offset + str.length());
			viewer.activatePlugins();
			viewer.getControl().forceFocus();
			viewer.getTextWidget().setFocus();


		} else if (e.data instanceof String[]) {

			String[] strs = (String[]) e.data;
			// FileTransfer用の実装

			if (strs.length == 1) {
				File file = new File(strs[0]);
				if (file.exists() && FileUtil.isSqlFile(file)) {

					String content = FileUtil.getContents(file);

					// 内容の上書き確認を行う
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
