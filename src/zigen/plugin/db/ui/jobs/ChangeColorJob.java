/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.views.internal.ColorManager;

public class ChangeColorJob extends AbstractJob {

	protected IPreferenceStore store;

	protected Table table;

	protected ITable tableNode;

	protected int columnSize;

	protected int rowSize;

	protected Color blue;

	protected Color black;

	protected Color glay;

	protected Color white;

	protected Color lightblue;

	protected Column selectedColumn;

	public ChangeColorJob(Table table) {
		this(table, null);

	}

	public ChangeColorJob(Table table, ITable tableNode) {
		super("Changing ForgoundColor...");
		this.tableNode = tableNode;
		setTable(table);
	}

	public void setTable(Table table) {
		this.store = DbPlugin.getDefault().getPreferenceStore();
		this.table = table;
		this.columnSize = table.getColumnCount(); // �J�������擾
		this.rowSize = table.getItemCount(); // ���R�[�h���擾

		ColorManager colorManager = new ColorManager();
		Display display = Display.getDefault();
		this.blue = new Color(display, 0, 0, 255); // ��
		this.black = new Color(display, 0, 0, 0);
		this.glay = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		this.white = display.getSystemColor(SWT.COLOR_WHITE);
		this.lightblue = colorManager.getColor(PreferencePage.P_COLOR_BACKGROUND);
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Change Color...", rowSize);

			if (store.getBoolean(PreferencePage.P_CHANGE_NULL_COLOR)) {
				String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);


				for (int i = 0; i < rowSize; i++) {
					monitor.worked(1);

					ChangeColorRecord thread;
					if (tableNode != null) {
						thread = new ChangeColorRecord(table, i, columnSize, tableNode);
					} else {
						thread = new ChangeColorRecord(table, i, columnSize);
					}
					thread.setNullSymbol(nullSymbol);
					thread.setBlack(black);
					thread.setBlue(blue);
					thread.setGlay(glay);
					thread.setWhite(white);
					thread.setLightblue(lightblue);
					thread.setSelectedColumn(selectedColumn);

					showResults(thread);
				}
			}

			monitor.done();

		} catch (org.eclipse.swt.SWTException e) {
			// �A�������N�G���𔭍s����ƃG���[���������邱�Ƃ�����
			DbPlugin.log(e);

		} catch (Exception e) {
			showErrorMessage("", e);

		}
		return Status.OK_STATUS;
	}

	public void setSelectedColumn(Column selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

}
