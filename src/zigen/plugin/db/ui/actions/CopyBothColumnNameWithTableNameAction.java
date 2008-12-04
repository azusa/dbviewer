/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

/**
 * CopyColumnNameAction.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/12/04 ZIGEN create.
 * 
 */
public class CopyBothColumnNameWithTableNameAction extends AbstractCopyAction {

	public void run(IAction action) {
		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof Column) {
					Column col = (Column) obj;
					ITable table = col.getTable();

					String tableName = table.getName();
					String tableRemarks = table.getRemarks();

					String columnName = col.getName();
					String remarks = col.getRemarks();

					if (index > 0) {
						sb.append(", ");//$NON-NLS-1$
					}
						if (tableRemarks == null || "".equals(tableRemarks.trim())) {
							sb.append(tableName);
						} else {
							sb.append(tableName);
							sb.append("(");
							sb.append(tableRemarks);
							sb.append(")");
						}
						sb.append(".");//$NON-NLS-1$
						if (remarks == null || "".equals(remarks.trim())) {
							sb.append(columnName);
						} else {
							sb.append(columnName);
							sb.append("(");
							sb.append(remarks);
							sb.append(")");
						}

					index++;
				}

			}

			clipboard.setContents(new Object[] { sb.toString() }, new Transfer[] { TextTransfer.getInstance() });

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

}
