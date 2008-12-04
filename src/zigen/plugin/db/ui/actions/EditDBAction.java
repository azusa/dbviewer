/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.dialogs.DBConfigWizard;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.views.TreeContentProvider;

/**
 * EditDBAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/03/21 ZIGEN create.
 *        [002] 2005/07/16 ZIGEN �ꕔ�C��.
 * 
 */
public class EditDBAction extends Action implements Runnable {
	TreeViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public EditDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("EditDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("EditDBAction.1")); //$NON-NLS-1$
		this.setEnabled(true);
        this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_EDIT));
	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof DataBase) {

			DataBase db = (DataBase) element;

			DataBase oldDB = (DataBase) db.clone();

			/*
			 * // DB�ڑ���`�p�_�C�A���O�̃I�[�v�� DBConfigDialog dialog = new
			 * DBConfigDialog(DbPlugin.getDefault().getShell(),
			 * db.getDbConfig()); int ret = dialog.open();
			 * 
			 * if (ret == IDialogConstants.OK_ID) { // XML�ɕۑ�����DBConfig���擾
			 * IDBConfig newConfig = dialog.getNewConfig(); // <!-- [002] �C��
			 * ZIGEN 2005/07/16 // db.setName(newConfig.getDBName());
			 * db.setDbConfig(newConfig); // 2005/08/05 �C�� ZIGEN
			 * viewer.refresh(db); // [002] �C�� ZIGEN 2005/07/16 --> }
			 */

			Shell shell = DbPlugin.getDefault().getShell();
			DBConfigWizard wizard = new DBConfigWizard(viewer.getSelection(), db.getDbConfig());
			WizardDialog dialog2 = new WizardDialog(shell, wizard);
			int ret = dialog2.open();

			if (ret == IDialogConstants.OK_ID) {
				// XML�ɕۑ�����DBConfig���擾
				IDBConfig newConfig = wizard.getNewConfig();
				
				db.setDbConfig(newConfig); // 2005/08/05 �C�� ZIGEN
				viewer.refresh(db);

				IContentProvider cp = viewer.getContentProvider();
				if (cp instanceof TreeContentProvider) {
					TreeContentProvider tcp = (TreeContentProvider) cp;
					BookmarkRoot bmroot = tcp.getBookmarkRoot();

					updateBookmark(bmroot, oldDB, db);

				}

				// �I����Ԃ��ēx�ʒm����
				viewer.getControl().notifyListeners(SWT.Selection, null);
				
				// Filter�ʒm
				DbPlugin.fireStatusChangeListener(newConfig, IStatusChangeListener.EVT_AddSchemaFilter);

				// �f�[�^�x�[�X��`�ҏC��ʒm����
				DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);
				

			}

		}
	}

	/**
	 * �w�肵��DataBase�Ɉ�v���邨�C�ɓ����DataBase�����X�V����B
	 * 
	 * @param targetDataBase
	 */
	private void updateBookmark(BookmarkFolder folder, DataBase targetDataBase, DataBase newDataBase) {
		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof Bookmark) {
				Bookmark bm = (Bookmark) leaf;
				if (bm.getDataBase().equals(targetDataBase)) {
					bm.setDataBase(newDataBase);
				}
			} else if (leaf instanceof BookmarkFolder) {
				updateBookmark((BookmarkFolder) leaf, targetDataBase, newDataBase);
			}
		}
	}

}
