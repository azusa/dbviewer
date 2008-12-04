/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.event;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.TableColumn;

public class TableColumnContorolListener extends ControlAdapter {
	private TableViewer viewer = null;

	private int columnIndex;

	private int columnWidth;

	public TableColumnContorolListener(TableViewer viewer, int columnIndex) {
		this.viewer = viewer;
		this.columnIndex = columnIndex;
	}

	public void controlResized(ControlEvent e) {
		if (e.widget instanceof TableColumn) {
			TableColumn c = (TableColumn) e.widget;
			columnWidth = c.getWidth();
		}
	}
}
