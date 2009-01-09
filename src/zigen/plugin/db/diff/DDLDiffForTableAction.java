/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.views.ColumnSearchAction;

public class DDLDiffForTableAction extends Action implements Runnable {

	private StructuredViewer viewer = null;

	private ITable left = null;

	private ITable right = null;

	public DDLDiffForTableAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DDLDiffForTableAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DDLDiffForTableAction.1")); //$NON-NLS-1$
	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {
			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();

				if (obj instanceof ITable) {
					ITable table = (ITable) obj;

					if (index == 0) {
						left = table;
						index++;
					} else if (index == 1) {
						right = table;
						index++;
					} else {
						break;
					}

				}

			}

			if (index == 2) {
				// �Q�I������Ă���ꍇ�̂ݎ��s
				loadColumnInfo(left);
				loadColumnInfo(right);
				showDDLDiff();
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private void showDDLDiff() throws Exception {

		IDDLDiff diff = new DDLDiff(new DDL(left), new DDL(right));
		DDLDiffEditorInput input = new DDLDiffEditorInput(new IDDLDiff[] {diff}, true);
		IWorkbenchPage page = DbPlugin.getDefault().getPage();
		IDE.openEditor(page, input, DDLDiffEditor.ID, true);

	}

	private void loadColumnInfo(ITable table) {
		if (!table.isExpanded()) {
			table.setExpanded(true);

			// -----------------------------------------------
			// �J���������͔񓯊������ɂ��Ȃ�(�ύX���Ȃ�����)
			// -----------------------------------------------
			Display display = Display.getDefault();
			display.syncExec((Runnable) new ColumnSearchAction(viewer, table));

		}
	}

}
