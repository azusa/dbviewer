/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
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
 * TableSortListener�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/24 ZIGEN create.
 * 
 */
public class TableSortListener extends SelectionAdapter {

	protected ImageCacher ic = ImageCacher.getInstance();

	protected ITableViewEditor editor = null;

	protected boolean desc = true;// �ŏ��̃N���b�N���̃\�[�g���w��

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

		// �N���b�N���ꂽ�J����
		TableColumn col = (TableColumn) e.widget;
		// �\�[�g�Ώۃe�[�u��
		Table table = col.getParent();

		// Table�̃w�b�_�[���N���b�N�����ꍇ�̃\�[�g�C�x���g
		if (!desc) {
			editor.getViewer().setSorter(new TableColumnSorter(columnIndex, desc));
			// col.setImage(ic.getImage(DbPlugin.IMG_CODE_ASC));
			desc = true;

			// �\�[�g���ꂽ���Ƃ��Ӗ�����O�p���\�� for 3.2
			try {
				table.setSortColumn(col);
				table.setSortDirection(SWT.UP);

			} catch (Throwable ex) {
				;// Eclipse3.1�ł�NoSuchMethodException������
			}

		} else {
			editor.getViewer().setSorter(new TableColumnSorter(columnIndex, desc));
			// col.setImage(ic.getImage(DbPlugin.IMG_CODE_DESC));
			desc = false;

			// �\�[�g���ꂽ���Ƃ��Ӗ�����O�p���\�� for 3.2
			try {
				table.setSortColumn(col);
				table.setSortDirection(SWT.DOWN);
			} catch (Throwable ex) {
				;// Eclipse3.1�ł�NoSuchMethodException������
			}

		}

		// NULL�����̐F��ύX
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
					// row �ԍ��ł̃\�[�g
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
					String v1 = (String) first.getItems()[index - 1]; // ��r����l�P
					String v2 = (String) second.getItems()[index - 1]; // ��r����l�Q
					try {
						// ���l�ϊ��ł���ꍇ�́A���l��r���s��
						BigDecimal d1 = new BigDecimal(v1); // ���l�ϊ�
						BigDecimal d2 = new BigDecimal(v2); // ���l�ϊ�

						if (isDesc) {
							return (d2.compareTo(d1)); // �~��
						} else {
							return (d1.compareTo(d2)); // ����
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
						// ���l�ϊ��ł��Ȃ��ꍇ�́A�������r���s��
						if (isDesc) {
							return (v2.compareTo(v1)); // �~��
						} else {
							return (v1.compareTo(v2)); // ����
						}

					}
				}

			}

		}

	}
}
