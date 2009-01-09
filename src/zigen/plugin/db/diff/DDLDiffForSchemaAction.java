/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.Schema;

public class DDLDiffForSchemaAction extends Action implements Runnable {

	private boolean showDialog = true;

	private TreeViewer viewer = null;

	private Schema left = null;

	private Schema right = null;

	public DDLDiffForSchemaAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DDLDiffForSchemaAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DDLDiffForSchemaAction.1")); //$NON-NLS-1$
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
				if (obj instanceof Schema) {
					Schema schema = (Schema) obj;
					if (index == 0) {
						left = schema;
						index++;
					} else if (index == 1) {
						right = schema;
						index++;
					} else {
						break;
					}
				}
			}

			if (index == 2) {

				// �Q�I������Ă���ꍇ�̂ݎ��s
				DDLDiffForSchemaJob job = new DDLDiffForSchemaJob(viewer, left, right);
				// job.setPriority(DDLDiffJob.SHORT);
				job.setPriority(DDLDiffForSchemaJob.SHORT);
				// job.setPriority(DDLDiffJob.DECORATE); // �����Ƃ��y��

				job.setUser(showDialog); // �_�C�A���O���o��
				job.schedule();

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
