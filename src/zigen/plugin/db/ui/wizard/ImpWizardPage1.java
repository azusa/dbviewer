/*
 * Copyright (c) 2007－2009 ZIGEN
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
 * SelectTableWizardPageクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/21 ZIGEN create.
 * 
 */
public class ImpWizardPage1 extends DefaultWizardPage {

	private final String LINE_SEP = System.getProperty("line.separator");

	public static final String MSG = "インポート対象を選択し、終了ボタンを押下してください";

	private String[] headers = {"データベース論理名", "接続文字列"};

	private IDBConfig[] configs;

	public ImpWizardPage1(IDBConfig[] configs) {
		super("wizardPage");
		this.configs = configs;
		setTitle("データベース定義のインポート");
		setDescription(MSG);
		setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		// 1分割、false:均等にしない
		container.setLayout(new GridLayout(1, false));

		Label label = new Label(container, SWT.NULL);
		label.setText("選択可能なデータベース定義一覧(&T):");

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
		table.setHeaderVisible(true);// ヘッダを可視にする
		table.setLinesVisible(true); // ラインを表示

		// テーブルヘッダの設定
		setHeaderColumn(table, headers);

		gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getControl().setLayoutData(gridData);

		String[] properties = new String[] {"check", ""};
		// カラム・プロパティの設定
		tableViewer.setColumnProperties(properties);

		// 各カラムに設定するセル・エディタの配列
		CellEditor[] editors = new CellEditor[] {new CheckboxCellEditor(table), null};

		// セル・エディタの設定
		tableViewer.setCellEditors(editors);

		tableViewer.setCellModifier(new ImpWizardPage1CellModifier(this));
		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		// ---------------------------
		// テーブルItemの作成
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
