/*
 * 作成日: 2007/10/11
 * 著作権: Copyright (c) 2005 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.TreeLeaf;

public class DriverSelectDialog extends TitleAreaDialog {

	TreeViewer viewer;

	List targetNames;

	IContainer container;

	private String title = Messages.getString("DriverSelectDialog.0"); //$NON-NLS-1$

	public DriverSelectDialog(Shell shell) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE); // リサイズ可能
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected Control createDialogArea(Composite parent) {
		super.setTitle(title);
		super.setMessage(Messages.getString("DriverSelectDialog.1"), IMessageProvider.NONE); //$NON-NLS-1$

		Composite composite = (Composite) super.createDialogArea(parent);

		Composite composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayoutData(new GridData(GridData.FILL_BOTH));

		composite2.setLayout(new GridLayout(1, false));

		// ツリーの作成
		viewer = new TreeViewer(composite2, SWT.BORDER | SWT.MULTI);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite3 = new Composite(composite2, SWT.NONE);
		composite3.setLayout(new GridLayout(2, false));
		composite3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Label label = new Label(composite3, SWT.NONE);
		// label.setText("hogehoghoege");
		// Javaプロジェクト表示の設定
		viewer.setContentProvider(new DriverContentProvider());
		viewer.setLabelProvider(new DriverLabelProvider());
		viewer.setInput(this);
		// ラベルプロバイダーの設定
		// WorkbenchLabelProvider provider = new WorkbenchLabelProvider();
		// viewer.setLabelProvider(provider);

		// IWorkspace ws = ResourcesPlugin.getWorkspace();
		// viewer.setInput(ws);

		// ツリーを選択したときの処理
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selectionChangeHandler(event);
			}
		});

		viewer.expandAll();

		return composite;
	}

	protected Control createContents(Composite parent) {
		Control ctl = super.createContents(parent);
		// OKボタンを使用不可にする
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		return ctl;
	}

	protected void okPressed() {
		// saveFileName = fileText.getText().trim();
		super.okPressed();
	}

	private void selectionChangeHandler(SelectionChangedEvent event) {
		// 選択したものによって表示するメニューを変更

		StructuredSelection ss = (StructuredSelection) event.getSelection();

		targetNames = new ArrayList();
		boolean enabeld = false;

		for (Iterator iter = ss.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (!(element instanceof DataBase)) {
				enabeld = true;
				TreeLeaf leaf = (TreeLeaf) element;
				targetNames.add(leaf.getName());
			}
		}

		getButton(IDialogConstants.OK_ID).setEnabled(enabeld);

	}

	// private void validate(){
	// IContainer container = getContainer();
	// if(container != null){
	// IFile file = container.getFile(new Path(getSaveFileName()));
	// if(file.exists()){
	// super.setMessage(Messages.getString("ProjectSelectDialog.4"),
	// IMessageProvider.WARNING); //$NON-NLS-1$
	// }
	// }
	// }

	protected Point getInitialSize() {
		return new Point(480, 450);
	}

	public IContainer getContainer() {
		return container;
	}

	public List getTargetNames() {
		return targetNames;
	}

}
