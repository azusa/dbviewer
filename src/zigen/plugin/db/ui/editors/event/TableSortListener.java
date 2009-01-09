/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.ui.editors.ITableViewEditor;

/**
 * TableSortListenerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/24 ZIGEN create.
 * 
 */
public class TableSortListener extends SelectionAdapter {

	protected ImageCacher ic = ImageCacher.getInstance();

	protected ITableViewEditor editor = null;

	protected boolean desc = true;// 最初のクリック時のソート順指定

	protected int columnIndex;

	protected TableColumn col;

	public TableSortListener(ITableViewEditor editor, int columnIndex) {
		this.editor = editor;
		this.columnIndex = columnIndex;
		this.col = editor.getViewer().getTable().getColumn(columnIndex);
	}

	// private void resetImage() {
	// TableColumn[] cols = viewer.getTable().getColumns();
	// for (int i = 0; i < cols.length; i++) {
	// TableColumn column = cols[i];
	// column.setImage(ic.getImage(DbPlugin.IMG_CODE_BLANK));
	// column.pack();
	// int width = column.getWidth();
	// column.setWidth(width + 14);
	// }
	//
	// }

	public void widgetSelected(SelectionEvent e) {
		// resetImage();

		// クリックされたカラム
		TableColumn col = (TableColumn) e.widget;
		// ソート対象テーブル
		Table table = col.getParent();

		// Tableのヘッダーをクリックした場合のソートイベント
		if (!desc) {
			editor.getViewer().setSorter(new TableColumnSorter(columnIndex, desc));
			// col.setImage(ic.getImage(DbPlugin.IMG_CODE_ASC));
			desc = true;

			// ソートされたことを意味する三角印を表示 for 3.2
			try {
				table.setSortColumn(col);
				table.setSortDirection(SWT.UP);

			} catch (Throwable ex) {
				;// Eclipse3.1ではNoSuchMethodExceptionが発生
			}

		} else {
			editor.getViewer().setSorter(new TableColumnSorter(columnIndex, desc));
			// col.setImage(ic.getImage(DbPlugin.IMG_CODE_DESC));
			desc = false;

			// ソートされたことを意味する三角印を表示 for 3.2
			try {
				table.setSortColumn(col);
				table.setSortDirection(SWT.DOWN);
			} catch (Throwable ex) {
				;// Eclipse3.1ではNoSuchMethodExceptionが発生
			}

		}

		// NULL文字の色を変更
		editor.changeColumnColor();
	}

	protected class TableColumnSorter extends ViewerSorter {

		boolean isDesc = false;

		int index;

		public TableColumnSorter(int index, boolean isDesc) {
			this.index = index;
			this.isDesc = isDesc;
		}

		public int compare(Viewer viewer, Object o1, Object o2) {

			TableElement first = (TableElement) o1;
			TableElement second = (TableElement) o2;

			if (first.isNew() && second.isNew()) {
				return 0;
			} else if (first.isNew()) {
				return 1;
			} else if (second.isNew()) {
				return -1;
			} else {

				if (index == 0) {
					// row 番号でのソート
					int no1 = first.getRecordNo();
					int no2 = second.getRecordNo();

					if (no1 < no2) {
						if (isDesc) {
							return (1);
						} else {
							return (-1);
						}
					} else if (no1 > no2) {
						if (isDesc) {
							return (-1);
						} else {
							return (1);
						}
					} else {
						return (0);
					}

				} else {
					String v1 = (String) first.getItems()[index - 1]; // 比較する値１
					String v2 = (String) second.getItems()[index - 1]; // 比較する値２
					try {
						// 数値変換できる場合は、数値比較を行う
						BigDecimal d1 = new BigDecimal(v1); // 数値変換
						BigDecimal d2 = new BigDecimal(v2); // 数値変換

						if (isDesc) {
							return (d2.compareTo(d1)); // 降順
						} else {
							return (d1.compareTo(d2)); // 昇順
						}

						// if (d1.doubleValue() < d2.doubleValue()) {
						// if (isDesc) {
						// return (1);
						// } else {
						// return (-1);
						// }
						// } else if (d2.doubleValue() < d1.doubleValue()) {
						// if (isDesc) {
						// return (-1);
						// } else {
						// return (1);
						// }
						// } else {
						// return (0);
						// }

					} catch (NumberFormatException ex) {
						// 数値変換できない場合は、文字列比較を行う
						if (isDesc) {
							return (v2.compareTo(v1)); // 降順
						} else {
							return (v1.compareTo(v2)); // 昇順
						}

					}
				}

			}

		}

	}
}
