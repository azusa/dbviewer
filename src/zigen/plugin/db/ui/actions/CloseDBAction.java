/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.ui.editors.QueryViewEditorInput;
import zigen.plugin.db.ui.editors.TableViewEditorInput;
import zigen.plugin.db.ui.editors.sql.SequenceEditorInput;
import zigen.plugin.db.ui.editors.sql.SourceEditorInput;
import zigen.plugin.db.ui.internal.DataBase;

/**
 * CloseDBAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 */
public class CloseDBAction extends Action implements Runnable {
	TreeViewer viewer = null;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param viewer
	 */
	public CloseDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("CloseDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CloseDBAction.1")); //$NON-NLS-1$
		this.setEnabled(false);

	}

	/**
	 * Action���s���̏���
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (element instanceof DataBase) {
				DataBase db = (DataBase) element;
				closeDataBase(db);
			
				DbPlugin.fireStatusChangeListener(db.getDbConfig(), IStatusChangeListener.EVT_RemoveSchemaFilter);
				
			}
		}

	}

	private void closeDataBase(DataBase db) {
		try {
			Transaction trans = Transaction.getInstance(db.getDbConfig());
			trans.cloesConnection();

			TransactionForTableEditor trans2 = TransactionForTableEditor.getInstance(db.getDbConfig());
			trans2.cloesConnection();

			// �I����Ԃ��ēx�ʒm����
			viewer.getControl().notifyListeners(SWT.Selection, null);

			db.removeChildAll();
			db.setConnected(false); // �ؒf��ԂƂ���
			db.setExpanded(false);

			viewer.collapseToLevel(db, 1); // ��W�J��Ԃɂ���(2007/06/20)
			viewer.refresh(db);

			List target = new ArrayList();
			IEditorReference[] references = DbPlugin.getDefault().getPage().getEditorReferences();
			for (int i = 0; i < references.length; i++) {
				IEditorReference reference = references[i];
				IEditorInput input = reference.getEditorInput();

				if (input instanceof QueryViewEditorInput) {
					IDBConfig config = ((QueryViewEditorInput) input).getConfig();
					if (config.getDbName().equals(db.getName())) {
						target.add(reference);
					}

				} else if (input instanceof TableViewEditorInput) {
					IDBConfig config = ((TableViewEditorInput) input).getConfig();
					if (config.getDbName().equals(db.getName())) {
						target.add(reference);
					}

				} else if (input instanceof SequenceEditorInput) {
					IDBConfig config = ((SequenceEditorInput) input).getConfig();
					if (config.getDbName().equals(db.getName())) {
						target.add(reference);
					}
				} else if (input instanceof SourceEditorInput) {
					IDBConfig config = ((SourceEditorInput) input).getConfig();
					if (config.getDbName().equals(db.getName())) {
						target.add(reference);
					}
				}

			}
			if (target.size() > 0) {
				DbPlugin.getCloseEditors((IEditorReference[]) target.toArray(new IEditorReference[0]));
			}

			if (DBType.getType(db.getDbConfig()) == DBType.DB_TYPE_DERBY) {
				// Derby�̏ꍇ�́A�ؒf���ɃV���b�g�_�E������i�g�ݍ��݃��[�h�̂݁j
				shutdown(db.getDbConfig());
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private void shutdown(IDBConfig config) {
		try {
			ConnectionManager.shutdown(config);

		} catch (SQLException e) {

			if (DBType.getType(config) == DBType.DB_TYPE_DERBY) {
				if (e.getErrorCode() == 50000) {// Derby�̐���V���b�g�_�E���́A�G���[�R�[�h��50000
					DbPlugin.getDefault().showInformationMessage(e.getMessage());
					return;
				}
			}
			DbPlugin.getDefault().showErrorDialog(e);

		} catch (Exception e) {

			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

}
