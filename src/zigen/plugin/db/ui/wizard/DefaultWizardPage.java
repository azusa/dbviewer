/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.wizard;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * DefaultWizardPage�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/02 ZIGEN create.
 * 
 */
abstract class DefaultWizardPage extends WizardPage {
	protected int LEVEL_FIELD_WIDTH = 20;

	protected int TEXT_FIELD_WIDTH = 50;

	protected int HEIGHT_HINT = 150;

	protected int WIDTH_HINT = 450;

	public static final String CHECKED_IMAGE = "checked"; //$NON-NLS-1$

	public static final String UNCHECKED_IMAGE = "unchecked"; //$NON-NLS-1$

	protected static ImageRegistry imageRegistry = new ImageRegistry();

	static {
		String iconPath = ""; //$NON-NLS-1$
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(ImpWizardPage1.class, iconPath + CHECKED_IMAGE + ".gif")); //$NON-NLS-1$
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(ImpWizardPage1.class, iconPath + UNCHECKED_IMAGE + ".gif")); //$NON-NLS-1$
	}

	protected class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		private Image getImage(boolean isSelected) {
			String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
			return imageRegistry.get(key);
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			TableItem item = (TableItem) element;
			switch (columnIndex) {
			case 0:
				result = item.getDbName();
				break;
			case 1:
				result = item.getUrl();
				break;
			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return (columnIndex == 0) ? getImage(((TableItem) element).isChecked()) : null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	protected class TableContentProvider implements IStructuredContentProvider {

		private List contents = null;

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IItem[]) {
				return (TableItem[]) inputElement;
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// log.debug("�������ł�");
			contents = null;
		}

		public void dispose() {
			contents = null;
		}

	}

	protected TableItem[] tableItems = null;

	protected TableViewer tableViewer = null;;

	public DefaultWizardPage(String pageName) {
		super(pageName);
	}

	abstract public void createControl(Composite parent);

	/**
	 * �e�[�u���w�b�_�[��ݒ肷��
	 * 
	 * @param meta
	 * @param table
	 * @throws SQLException
	 */
	protected void setHeaderColumn(Table table, String[] headers) {

		for (int i = 0; i < headers.length; i++) {
			TableColumn col = new TableColumn(table, SWT.NONE, i);
			col.setText(headers[i]);
			col.setResizable(true);
			col.pack();

		}
	}

	/**
	 * �e�J�����̕��̎����v�Z
	 */
	protected void columnsPack(Table table) {
		table.setVisible(false);
		TableColumn[] cols = table.getColumns();
		for (int i = 0; i < cols.length; i++) {
			cols[i].pack();
		}
		table.setVisible(true);
	}

	/**
	 * Status�o�[���X�V
	 * 
	 * @param message
	 */
	void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	protected void createLine(Composite parent, int ncol) {
		Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BOLD);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = ncol;
		line.setLayoutData(gridData);
	}

	/**
	 * ���ʂ�Composite�쐬���\�b�h
	 * 
	 * @param parent
	 * @return
	 */
	protected Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;

		composite.setLayout(gridLayout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;

		composite.setLayoutData(data);

		return composite;
	}

	/**
	 * ���ʂ�GridData�擾���\�b�h GridData�̎g���܂킵NG�ׁ̈A�K��New�������̂�Ԃ�
	 * 
	 * @param width
	 * @return
	 */
	protected GridData getGridData(int width) {
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(width);
		return gd;
	}

	/**
	 * �P�ł��I����Ԃ������True
	 * 
	 * @return
	 */
	protected final boolean isSelected() {
		for (int i = 0; i < tableItems.length; i++) {
			IItem item = tableItems[i];
			if (item.isChecked()) {
				return true;
			}
		}
		return false;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		// ����̍쐬
		// createButton(parent, BUTTON_ID_EXP, "�t�@�C���ɕۑ�", false);
		// createButton(parent, BUTTON_ID_IMP, "�f�[�^�̕ύX", false);
		// createButton(parent, BUTTON_ID_DEL, "�f�[�^�̍폜", false);
		// createButton(parent, IDialogConstants.CLOSE_ID,
		// IDialogConstants.CLOSE_LABEL, true);

	}
	
	protected void createSelectBtn(Composite composite) {
		Composite c = new Composite(composite, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		c.setLayout(layout);

		final Button selectBtn = new Button(c, SWT.PUSH);
		selectBtn.setText(Messages.getString("ExpWizardPage1.8")); //$NON-NLS-1$
		selectBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (tableItems != null) {
					for (int i = 0; i < tableItems.length; i++) {
						tableItems[i].setChecked(true);
					}
					tableViewer.update(tableItems, new String[] {
						"check"}); //$NON-NLS-1$
					setPageComplete(true);
				}
			}
		});

		final Button unSelectBtn = new Button(c, SWT.PUSH);
		unSelectBtn.setText(Messages.getString("ExpWizardPage1.10")); //$NON-NLS-1$
		unSelectBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (tableItems != null) {
					for (int i = 0; i < tableItems.length; i++) {
						tableItems[i].setChecked(false);
					}
					tableViewer.update(tableItems, new String[] {
						"check"}); //$NON-NLS-1$
					setPageComplete(false);
				}
			}
		});
	}

}
