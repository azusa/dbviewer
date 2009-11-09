/*
 * Copyright (c) 2007�|2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.wizard;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;

/**
 * SelectTableWizardPage�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/21 ZIGEN create.
 * 
 */
public class ImpWizardPage1 extends DefaultWizardPage {

	private final String LINE_SEP = System.getProperty("line.separator");

	public static final String MSG = "�C���|�[�g�Ώۂ�I�����A�I���{�^�����������Ă�������";

	private String[] headers = {"�f�[�^�x�[�X�_����", "�ڑ�������"};

	private IDBConfig[] configs;

	public ImpWizardPage1(IDBConfig[] configs) {
		super("wizardPage");
		this.configs = configs;
		setTitle("�f�[�^�x�[�X��`�̃C���|�[�g");
		setDescription(MSG);
		setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		// 1�����Afalse:�ϓ��ɂ��Ȃ�
		container.setLayout(new GridLayout(1, false));

		Label label = new Label(container, SWT.NULL);
		label.setText("�I���\�ȃf�[�^�x�[�X��`�ꗗ(&T):");

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
		Table table = tableViewer.getTable();
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);// �w�b�_�����ɂ���
		table.setLinesVisible(true); // ���C����\��

		// �e�[�u���w�b�_�̐ݒ�
		setHeaderColumn(table, headers);

		gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getControl().setLayoutData(gridData);

		String[] properties = new String[] {"check", ""};
		// �J�����E�v���p�e�B�̐ݒ�
		tableViewer.setColumnProperties(properties);

		// �e�J�����ɐݒ肷��Z���E�G�f�B�^�̔z��
		CellEditor[] editors = new CellEditor[] {new CheckboxCellEditor(table), null};

		// �Z���E�G�f�B�^�̐ݒ�
		tableViewer.setCellEditors(editors);

		tableViewer.setCellModifier(new ImpWizardPage1CellModifier(this));
		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		// ---------------------------
		// �e�[�u��Item�̍쐬
		// ---------------------------
		createTableItems();

		if (tableItems != null) {
			tableViewer.setInput(tableItems);
			columnsPack(table);
		}

		createSelectBtn(composite);
	}

	private void createTableItems() {
		try {
			tableItems = new TableItem[configs.length];
			for (int i = 0; i < configs.length; i++) {
				tableItems[i] = new TableItem(configs[i], false);
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
