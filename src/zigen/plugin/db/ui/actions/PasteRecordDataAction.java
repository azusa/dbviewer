/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.event.TableKeyAdapter;
import zigen.plugin.db.ui.editors.event.TableKeyEventHandler;

/**
 * CopyRecordDataAction.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/12/04 ZIGEN create.
 * 
 */
public class PasteRecordDataAction extends TableViewEditorAction implements IWorkbenchWindowActionDelegate {

	public PasteRecordDataAction() {
		// テキストやツールチップ、アイコンの設定
		// this.setText("クリップボードデータを貼り付け(&P)");
		// this.setToolTipText("クリップボードデータを貼り付け");
		setImage(ITextOperationTarget.PASTE);
		setEnabled(false);

	}

	public void run() {
		try {

			// TableKeyEventHandler handler = new
			// TableKeyEventHandler(editor.getViewer());
			TableKeyEventHandler handler = new TableKeyEventHandler((TableViewEditorFor31) editor);

			TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);
			keyAdapter.createNewElement();

			//editor.getViewer().getTable().notifyListeners(SWT.Selection, null); // 選択状態を通知

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {

			// 選択状態を再度通知する
			editor.getViewer().getControl().notifyListeners(SWT.Selection, null);
			/*
			 * if (editor instanceof TableViewEditorFor31) { //
			 * 貼り付け後、ActionをRefleshする ((TableViewEditorFor31)
			 * editor).refleshAction(); }
			 */
		}

	}

	/**
	 * Enableモードを設定する
	 * 
	 */
	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (editor.getViewer() == null) {
			setEnabled(false);
		} else {
			// クリップボードに適切なデータがあればTrueを設定する

			// TableKeyEventHandler handler = new
			// TableKeyEventHandler(editor.getViewer());
			TableKeyEventHandler handler = new TableKeyEventHandler((TableViewEditorFor31) editor);
			TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);

			boolean canPaste = keyAdapter.canCreateNewElement();
			setEnabled(canPaste);

		}
	}

	protected IWorkbenchWindow window;

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void run(IAction action) {
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
