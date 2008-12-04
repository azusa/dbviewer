/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.ui.editors.ITableViewEditor;

/**
 * TableDefaultSortListenerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/24 ZIGEN create.
 * 
 */
public class TableDefaultSortListener extends TableSortListener {

	public TableDefaultSortListener(ITableViewEditor editor, int columnIndex) {
		super(editor, columnIndex);
	}

	public void widgetSelected(SelectionEvent e) {
		// クリックされたカラム
		TableColumn col = (TableColumn) e.widget;
		// ソート対象テーブル
		Table table = col.getParent();

		// 常に昇順で並べ替える
		editor.getViewer().setSorter(new TableColumnSorter(columnIndex, false));
		// NULL文字の色を変更
		editor.changeColumnColor();

		// ソートされたことを意味する三角印をクリア
		try {
			table.setSortDirection(SWT.NONE);
		} catch (Throwable ex) {
			;// Eclipse3.1ではNoSuchMethodExceptionが発生
		}
	}

}
