/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ext.oracle.internal.OracleDbBlockSizeSearcher;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;

/**
 * SelectTableWizardPage�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/21 ZIGEN create.
 * 
 */
public class WizardPage2 extends DefaultWizardPage {

	private DbBlockSizeVerifyListener verifyListener = new DbBlockSizeVerifyListener();

	public static final String HEADER_TABLENAME = Messages.getString("WizardPage2.0"); //$NON-NLS-1$

	public static final String HEADER_BLOCKSIZE = Messages.getString("WizardPage2.1"); //$NON-NLS-1$

	public static final String HEADER_PCTFREE = Messages.getString("WizardPage2.2"); //$NON-NLS-1$

	public static final String HEADER_RECORD = Messages.getString("WizardPage2.3"); //$NON-NLS-1$

	public static final int COLUMN_TABLENAME = 0;

	public static final int COLUMN_PCTFREE = 1;

	public static final int COLUMN_RECORD = 2;

	Text dbBlockSizeText;

	public WizardPage2() {
		super("wizardPage"); //$NON-NLS-1$

		setTitle(Messages.getString("WizardPage2.5")); //$NON-NLS-1$
		setDescription(Messages.getString("WizardPage2.6")); //$NON-NLS-1$
		setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		// 1�����Afalse:�ϓ��ɂ��Ȃ�
		container.setLayout(new GridLayout(1, false));

		Composite composite = createDefaultComposite(container);
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("WizardPage2.7")); //$NON-NLS-1$
		// nameLabel.setLayoutData(getGridData(LEVEL_FIELD_WIDTH));
		dbBlockSizeText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		dbBlockSizeText.addFocusListener(new TextSelectionListener());
		dbBlockSizeText.addVerifyListener(verifyListener);
		dbBlockSizeText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		// dbBlockSizeText.setLayoutData(getGridData(TEXT_FIELD_WIDTH));
		//		
		//        

		createTable(container);
		setControl(container);
	}

	private void createTable(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = HEIGHT_HINT;
		gridData.widthHint = WIDTH_HINT;

		composite.setLayout(layout);
		composite.setLayoutData(gridData);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getControl().setLayoutData(gridData);

		Table table = tableViewer.getTable();
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);// �w�b�_�����ɂ���
		table.setLinesVisible(true); // ���C����\��

		// �e�[�u���w�b�_�̐ݒ�
		String[] headers = {HEADER_TABLENAME, HEADER_PCTFREE, HEADER_RECORD};
		setHeaderColumn(table, headers);

		// String[] properties = new String[] { "1", "2", "3" };
		// �J�����E�v���p�e�B�̐ݒ�
		tableViewer.setColumnProperties(headers);

		// �e�J�����ɐݒ肷��Z���E�G�f�B�^�̔z��
		CellEditor[] editors = new CellEditor[] {null, new TextCellEditor(table), new TextCellEditor(table)};

		for (int i = 0; i < editors.length; i++) {
			if (editors[i] != null) {
				editors[i].setValidator(new WizardPage2CellEditorValidator(i));
			}
		}
		// �Z���E�G�f�B�^�̐ݒ�
		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new WizardPage2CellModifier(this));

		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		if (tableItems != null) {
			tableViewer.setInput(tableItems);
			columnsPack(table);
		}

		// tableViewer.addSelectionChangedListener(new
		// ISelectionChangedListener() {
		// public void selectionChanged(SelectionChangedEvent event) {
		// tableSelectionChangedHandler(event);
		// }
		// });
		//		
		// // doubleClick �ɂ��ҏW�͖�����
		// tableViewer.addDoubleClickListener(new IDoubleClickListener() {
		// public void doubleClick(DoubleClickEvent event) {
		// if (!tableViewer.getSelection().isEmpty()) {
		// editButtonPressedHandler();
		// }
		// }
		// });

	}

	private void dialogChanged() {
		if (this.dbBlockSizeText.getText().length() == 0) {
			updateStatus(Messages.getString("WizardPage2.8")); //$NON-NLS-1$
			return;
		}
		updateStatus(null);
	}

	/**
	 * �e�[�u���w�b�_�[��ݒ肷��
	 * 
	 * @param meta
	 * @param table
	 * @throws SQLException
	 */
	private void setHeaderColumn(Table table, String[] headers) {

		for (int i = 0; i < headers.length; i++) {
			TableColumn col;
			if (headers[i].equals(WizardPage2.HEADER_TABLENAME)) {
				col = new TableColumn(table, SWT.NONE, i);

			} else {
				col = new TableColumn(table, SWT.RIGHT, i);
			}

			col.setText(headers[i]);
			col.setResizable(false);
			// column.setWidth(200);
			col.pack();

		}
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			CalcTableSpaceWizard wiz = (CalcTableSpaceWizard) getWizard();
			WizardPage1 page = (WizardPage1) wiz.getPreviousPage(this);
			tableItems = page.tableItems;
			if (tableItems != null) {
				if (dbBlockSizeText.getText() == null || "".equals(dbBlockSizeText.getText())) { //$NON-NLS-1$
					try {
						int dbBlockSize = OracleDbBlockSizeSearcher.execute(page.schemaNode.getDbConfig());
						dbBlockSizeText.removeVerifyListener(verifyListener);
						dbBlockSizeText.setText(String.valueOf(dbBlockSize));
						dbBlockSizeText.addVerifyListener(verifyListener);
					} catch (Exception e) {
						DbPlugin.getDefault().showErrorDialog(e);
					}
				}

				tableViewer.setInput(tableItems);
				columnsPack(tableViewer.getTable());
			}
		}

	}

	private class TableContentProvider implements IStructuredContentProvider {

		private List contents = null;

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IItem[]) {
				List checkedList = getCheckedList((TableItem[]) inputElement);
				return (TableItem[]) checkedList.toArray(new TableItem[0]);
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			contents = null;
		}

		public void dispose() {
			contents = null;
		}

		/**
		 * �I������Ă���e�[�u���ɍi�荞��
		 * 
		 * @param items
		 * @return
		 */
		private List getCheckedList(TableItem[] items) {
			List checkedList = new ArrayList();
			for (int i = 0; i < items.length; i++) {
				if (items[i].isChecked()) {
					checkedList.add(items[i]);
				}
			}
			return checkedList;

		}
	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			TableItem item = (TableItem) element;
			switch (columnIndex) {
			case COLUMN_TABLENAME:
				result = item.table.getName();
				break;

			case COLUMN_PCTFREE:
				result = String.valueOf(item.getPctFree());
				break;
			case COLUMN_RECORD:
				result = String.valueOf(item.getRecordSize());
				break;

			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	private class DbBlockSizeVerifyListener implements VerifyListener {

		private String acceptableChar = "1234567890"; //$NON-NLS-1$

		public void verifyText(VerifyEvent e) {
			char chr = e.character;
			// Backspace��Delete�������ꂽ�Ƃ��́A�L���ɂ���
			if (e.character == SWT.BS || e.character == SWT.DEL) {
				return;
			}
			// �啶���ȊO�̃A���t�@�x�b�g�͖����ɂ���
			if (acceptableChar.indexOf(Character.toString(e.character)) == -1) {
				e.doit = false;
			}

		}
	}

}
