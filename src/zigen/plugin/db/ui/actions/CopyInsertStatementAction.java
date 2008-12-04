/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.rule.DefaultStatementFactory;
import zigen.plugin.db.core.rule.IStatementFactory;
import zigen.plugin.db.ui.internal.ITable;

/**
 * DeleteRecordActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class CopyInsertStatementAction extends TableViewEditorAction {
	private final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	IStructuredSelection selection;

	public CopyInsertStatementAction() {
		// テキストやツールチップ、アイコンの設定
		this.setText(Messages.getString("CopyInsertStatementAction.1")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CopyInsertStatementAction.2")); //$NON-NLS-1$
	}

	public void run() {

		try {
			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();
			TextTransfer text_transfer = TextTransfer.getInstance();
			ITable table = editor.getTableNode();

			Iterator iter = selection.iterator();
			int index = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TableElement) {
					TableElement elem = (TableElement) obj;
					TableColumn[] columns = elem.getColumns();

					sb.append("INSERT INTO "); //$NON-NLS-1$
					sb.append(table.getSqlTableName());
					sb.append(" ("); //$NON-NLS-1$

					// カラムを指定するように修正
					for (int i = 0; i < columns.length; i++) {
						TableColumn col = columns[i];

						if (i == 0) {
							sb.append(" "); //$NON-NLS-1$
						} else {

							sb.append(", "); //$NON-NLS-1$
						}
						sb.append(col.getColumnName());

					}
					sb.append(" )"); //$NON-NLS-1$
					sb.append(" VALUES ("); //$NON-NLS-1$

					for (int i = 0; i < columns.length; i++) {
						TableColumn col = columns[i];
						int type = col.getDataType();
						Object value = elem.getItem(col);

						IStatementFactory factory = DefaultStatementFactory.getFactory(table.getDbConfig());
						if (i == 0) {
							sb.append(factory.getString(type, value));
						} else {
							sb.append("," + factory.getString(type, value)); //$NON-NLS-1$
						}

					}
					sb.append(")" + LINE_SEP + "/" + LINE_SEP); //$NON-NLS-1$ //$NON-NLS-2$
					index++;

				}
			}
			clipboard.setContents(new Object[] {
				sb.toString()
			}, new Transfer[] {
				text_transfer
			});

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

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
			selection = (IStructuredSelection) editor.getViewer().getSelection();
			if (selection.size() > 0) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

}
