/*
 * Copyright (c) 2007�|2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.ui.internal.ITable;

/**
 * CopyTableNameAction.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/12/04 ZIGEN create.
 * 
 */
public class CopyTableNameWithRemarksAction extends Action implements Runnable {

	// private final String LINE_SEP = System.getProperty("line.separator");

	StructuredViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public CopyTableNameWithRemarksAction(StructuredViewer viewer) {
		this.viewer = viewer;

		this.setText(Messages.getString("CopyTableNameWithRemarksAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CopyTableNameWithRemarksAction.1")); //$NON-NLS-1$

	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof ITable) {
					ITable table = (ITable) obj;
					if (index == 0) {
						sb.append(table.getName());
					} else {
						sb.append(", " + table.getName()); //$NON-NLS-1$
					}

					String remarks = table.getRemarks();
					if (remarks != null && !"".equals(remarks.trim())) { //$NON-NLS-1$
						sb.append("("); //$NON-NLS-1$
						sb.append(remarks);
						sb.append(")"); //$NON-NLS-1$
					}

					index++;
				}

			}

			clipboard.setContents(new Object[] {sb.toString()}, new Transfer[] {TextTransfer.getInstance()});

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
