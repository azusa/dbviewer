/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class ChangeColorRecord implements Runnable {

	protected Table table;

	protected Color blue;

	protected Color black;

	protected Color glay;

	protected Color white;

	protected Color lightblue;

	protected int rowIndex;

	protected int columnSize;

	protected String nullSymbol;

	protected ITable tableNode;

	protected Column selectedColumn;

	public ChangeColorRecord(Table table, int rowIndex, int columnSize) {
		this(table, rowIndex, columnSize, null);
	}

	public ChangeColorRecord(Table table, int rowIndex, int columnSize, ITable tableNode) {
		this.table = table;
		this.rowIndex = rowIndex;
		this.columnSize = columnSize;
		this.tableNode = tableNode;
	}

	public void run() {
		try {

			if (table.isDisposed())
				return;


			TableItem item = table.getItem(rowIndex);
			TableElement elem = (TableElement) item.getData();
			TableColumn[] tableColmns = elem.getColumns();

			Color bgcolor;
			if (rowIndex % 2 == 0) {
				bgcolor = white;
			} else {
				bgcolor = lightblue;
			}
			// 行番号の背景色を変更
			item.setBackground(0, bgcolor); // 背景色

			if (tableNode == null || selectedColumn == null) {
				// 通常
				for (int k = 0; k < columnSize - 1; k++) {
					TableColumn tCol = tableColmns[k];
					// カラムが一致しない場合
					if (nullSymbol.equals(item.getText(k + 1))) {
						item.setForeground(k + 1, blue); // // NULL文字色
					} else {
						item.setForeground(k + 1, black); // // NULL以外 文字色(黒)
					}
					item.setBackground(k + 1, bgcolor); // 背景色
				}
			} else {
				// カラム選択モード

				// <-- 2007/12/27 zigen
				// 1行しか無い場合は、カラム選択が見えないので、選択状態をクリアする
				if (table.getItemCount() <= 1) {
					table.setSelection(-1);
				}
				// -->

				ITable targetTable = selectedColumn.getTable();
				for (int k = 0; k < columnSize - 1; k++) {
					TableColumn tCol = tableColmns[k];
					if (targetTable.equals(tableNode) && tCol.getColumnName().equals(selectedColumn.getName())) {
						// カラムが一致する場合(選択状態にする)
						// item.setForeground(k + 1, white); // 文字色
						item.setForeground(k + 1, black); // 文字色
						item.setBackground(k + 1, glay); // 背景色
						table.showColumn(table.getColumn(k + 1)); // カラム表示(自動スクロール)


					} else {
						// カラムが一致しない場合
						if (nullSymbol.equals(item.getText(k + 1))) {
							item.setForeground(k + 1, blue); // // NULL文字色
						} else {
							item.setForeground(k + 1, black); // // NULL以外 文字色(黒)
						}
						item.setBackground(k + 1, bgcolor); // 背景色


					}
				}

			}
		} catch (Exception e) {
			// 以下のエラーはログ出力のみとする
			DbPlugin.log(e);
		}
	}

	public void setSelectedColumn(Column column) {
		this.selectedColumn = column;
	}

	public void setNullSymbol(String nullSymbol) {
		this.nullSymbol = nullSymbol;
	}

	public void setBlue(Color blue) {
		this.blue = blue;
	}

	public void setBlack(Color black) {
		this.black = black;
	}

	public void setGlay(Color glay) {
		this.glay = glay;
	}

	public void setWhite(Color white) {
		this.white = white;
	}

	public void setLightblue(Color lightblue) {
		this.lightblue = lightblue;
	}

}
