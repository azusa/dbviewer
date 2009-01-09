/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
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
			// �s�ԍ��̔w�i�F��ύX
			item.setBackground(0, bgcolor); // �w�i�F

			if (tableNode == null || selectedColumn == null) {
				// �ʏ�
				for (int k = 0; k < columnSize - 1; k++) {
					TableColumn tCol = tableColmns[k];
					// �J��������v���Ȃ��ꍇ
					if (nullSymbol.equals(item.getText(k + 1))) {
						item.setForeground(k + 1, blue); // // NULL�����F
					} else {
						item.setForeground(k + 1, black); // // NULL�ȊO �����F(��)
					}
					item.setBackground(k + 1, bgcolor); // �w�i�F
				}
			} else {
				// �J�����I�����[�h

				// <-- 2007/12/27 zigen
				// 1�s���������ꍇ�́A�J�����I���������Ȃ��̂ŁA�I����Ԃ��N���A����
				if (table.getItemCount() <= 1) {
					table.setSelection(-1);
				}
				// -->

				ITable targetTable = selectedColumn.getTable();
				for (int k = 0; k < columnSize - 1; k++) {
					TableColumn tCol = tableColmns[k];
					if (targetTable.equals(tableNode) && tCol.getColumnName().equals(selectedColumn.getName())) {
						// �J��������v����ꍇ(�I����Ԃɂ���)
						// item.setForeground(k + 1, white); // �����F
						item.setForeground(k + 1, black); // �����F
						item.setBackground(k + 1, glay); // �w�i�F
						table.showColumn(table.getColumn(k + 1)); // �J�����\��(�����X�N���[��)


					} else {
						// �J��������v���Ȃ��ꍇ
						if (nullSymbol.equals(item.getText(k + 1))) {
							item.setForeground(k + 1, blue); // // NULL�����F
						} else {
							item.setForeground(k + 1, black); // // NULL�ȊO �����F(��)
						}
						item.setBackground(k + 1, bgcolor); // �w�i�F


					}
				}

			}
		} catch (Exception e) {
			// �ȉ��̃G���[�̓��O�o�݂͂̂Ƃ���
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
