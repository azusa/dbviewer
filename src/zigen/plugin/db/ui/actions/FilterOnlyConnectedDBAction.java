/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.ui.dialogs.MessageDialogWithToggle2;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Schema;

public class FilterOnlyConnectedDBAction extends Action {
	TreeViewer viewer;
	DataBaseFilter filter;

	public FilterOnlyConnectedDBAction(TreeViewer viewer) {

		super("", IAction.AS_CHECK_BOX); //$NON-NLS-1$
		this.viewer = viewer;
		this.setText("�e�X�g");
		this.setToolTipText("�ڑ����̃f�[�^�ް��̂ݕ\������"); //$NON-NLS-1$
//		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(
//				DbPlugin.IMG_CODE_CONNECTED_DB));
	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		try {
			Shell shell = DbPlugin.getDefault().getShell();
//			DropTableConfirmDialog dialog = new DropTableConfirmDialog(shell);
//			if (dialog.open() != Window.OK) {
//				return;
//			}
			
			String message = "�폜���܂����H";
			String toggleMessage = "CASCADE CONSTRAINT �I�v�V����";
			boolean toggleStatus = false;
			String toggleMessage2 = "PURGE �I�v�V����";
			boolean toggleStatus2 = false;
			
			MessageDialogWithToggle2.open(shell, DbPluginConstant.TITLE, message, toggleMessage, toggleStatus, toggleMessage2, toggleStatus2);
			
//	        MessageDialogWithToggle2 dialog = new MessageDialogWithToggle2(shell,
//	        		TITLE, null, // accept the default window icon
//	                message, QUESTION, new String[] { IDialogConstants.YES_LABEL,
//	                        IDialogConstants.NO_LABEL }, 0, // yes is the default
//	                toggleMessage, toggleState);
//	        dialog.prefStore = store;
//	        dialog.prefKey = key;
//	        dialog.open();
//	        return dialog;
	        
	        

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}
	


	class DataBaseFilter extends ViewerFilter {

		public DataBaseFilter() {
		}

		public boolean select(Viewer viewer, Object parent, Object node) {
			return filterDataBase(node);
		}

		private boolean filterDataBase(Object element) {
			if (element instanceof DataBase) {
				DataBase db = (DataBase) element;
				return db.isConnected();
			} else if (element instanceof Schema) {
				return true;
			} else {
				return true;
			}
		}

	}


	class DropTableConfirmDialog extends Dialog {
		protected Button checkCascadeConstraint;
		protected Button checkPurgeOption;

		public DropTableConfirmDialog(Shell parent) {
			super(parent);
			setShellStyle(getShellStyle() | SWT.RESIZE); // ���T�C�Y�\
		}

		protected void okPressed() {
			if (dropTable()) {
				super.okPressed();
			}
		}

		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText("�m�F�_�C�A���O"); //$NON-NLS-1$
		}

		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			// GridLayout layout = new GridLayout();
			// layout.verticalSpacing = 5;
			// composite.setLayout(layout);
			FormLayout layout = new FormLayout();
			composite.setLayout(layout);

			Label label = new Label(composite, SWT.FLAT);
			label.setText("�폜���܂����H");
			FormData data = new FormData();
			data.top = new FormAttachment(0, 10);
			data.left = new FormAttachment(0, 10);
			data.right = new FormAttachment(100, 10);
			label.setLayoutData(data);

			checkCascadeConstraint = new Button(composite, SWT.CHECK);
			checkCascadeConstraint.setText("CASCADE CONSTRAINT �I�v�V����");
			data = new FormData();
			data.top = new FormAttachment(label, 10);
			data.left = new FormAttachment(0, 10);
			data.right = new FormAttachment(100, 10);
			checkCascadeConstraint.setLayoutData(data);

			checkPurgeOption = new Button(composite, SWT.CHECK);
			checkPurgeOption.setText("PURGE �I�v�V����");
			data = new FormData();
			data.top = new FormAttachment(checkCascadeConstraint, 10);
			data.left = new FormAttachment(0, 10);
			data.right = new FormAttachment(100, 10);
			checkPurgeOption.setLayoutData(data);

			return composite;
		}

		/**
		 * DB�ڑ����̕ۑ�
		 * 
		 * @return
		 */
		private boolean dropTable() {
			return true;
			// }
			//
			// private void addDriverSection(Composite parent) {
			// Composite composite = createDefaultComposite(parent);
			// Label nameLabel = new Label(composite, SWT.NONE);
			// nameLabel.setText(Messages.getString("URLInputDialog.9"));
			// //$NON-NLS-1$
			// nameLabel.setLayoutData(getGridData(LEVEL_FIELD_WIDTH));
			// driverText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			// driverText.setLayoutData(getGridData(TEXT_FIELD_WIDTH));
			// // �t�H�[�J�X���ɓ��͕������I����ԂƂ���
			// driverText.addFocusListener(new TextSelectionListener());
			//
			// driverText.addVerifyListener(new VerifyListener() {
			// public void verifyText(VerifyEvent e) {
			// verifyHandler(e);
			// }
			// });
			//
			// if (valueMap.containsKey(KEY_DRIVER)) {
			// driverText.setText(getStringValue(KEY_DRIVER));
			// }
			// }
			//
			// private void addURLSection(Composite parent) {
			// Composite composite = createDefaultComposite(parent);
			// Label nameLabel = new Label(composite, SWT.NONE);
			// nameLabel.setText(Messages.getString("URLInputDialog.10"));
			// //$NON-NLS-1$
			// nameLabel.setLayoutData(getGridData(LEVEL_FIELD_WIDTH));
			// urlText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			// urlText.setLayoutData(getGridData(TEXT_FIELD_WIDTH));
			// // �t�H�[�J�X���ɓ��͕������I����ԂƂ���
			// urlText.addFocusListener(new TextSelectionListener());
			// urlText.addVerifyListener(new VerifyListener() {
			// public void verifyText(VerifyEvent e) {
			// verifyHandler(e);
			// }
			// });
			// if (valueMap.containsKey(KEY_URL)) {
			// urlText.setText(getStringValue(KEY_URL));
			// }
			// }

			/**
			 * �_�C�A���O�T�C�Y
			 */
			// protected Point getInitialSize() {
			// return new Point(400, 150);
			// }
		}
	}

}
