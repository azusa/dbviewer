/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.DbPlugin;

public class TableColumnFilterAction extends Action implements Runnable {

	protected TableViewer viewer;

	protected ColumnFilterInfo[] columnSelectInfos;

	public TableColumnFilterAction(TableViewer viewer, ColumnFilterInfo[] columnSelectInfos) {
		this.viewer = viewer;
		this.columnSelectInfos = columnSelectInfos;
	}

	public void run() {

		Table table = viewer.getTable();
		TableColumn[] cols = viewer.getTable().getColumns();

		// 一時的に描画をとめる
		table.setVisible(false);

		try {
			for (int i = 1; i < cols.length; i++) {
				TableColumn column = cols[i];
				ColumnFilterInfo info = columnSelectInfos[i - 1];

				if (info.isChecked()) {
					// column.pack();
					// column.setResizable(true);

					if (!column.getResizable()) {
						// サイズ変更不可ということは非表示と判定する
						column.pack();
						column.setResizable(true);
					}
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}

			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		// 描画を再開する
		table.setVisible(true);
	}

}
