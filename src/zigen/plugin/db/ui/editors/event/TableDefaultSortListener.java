/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.ui.editors.ITableViewEditor;

/**
 * TableDefaultSortListener�N���X.
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
		// �N���b�N���ꂽ�J����
		TableColumn col = (TableColumn) e.widget;
		// �\�[�g�Ώۃe�[�u��
		Table table = col.getParent();

		// ��ɏ����ŕ��בւ���
		editor.getViewer().setSorter(new TableColumnSorter(columnIndex, false));
		// NULL�����̐F��ύX
		editor.changeColumnColor();

		// �\�[�g���ꂽ���Ƃ��Ӗ�����O�p����N���A
		try {
			table.setSortDirection(SWT.NONE);
		} catch (Throwable ex) {
			;// Eclipse3.1�ł�NoSuchMethodException������
		}
	}

}
