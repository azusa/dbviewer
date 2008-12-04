/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.sql.Connection;
import java.util.Iterator;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DeleteSQLInvoker;
import zigen.plugin.db.core.SQLCreator;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.RecordCountForTableJob;

/**
 * DeleteRecordAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class DeleteRecordAction extends TableViewEditorAction {

	protected IStructuredSelection selection;

	public DeleteRecordAction() {
		setEnabled(false);
		setImage(ITextOperationTarget.DELETE);
	}

	/**
	 * Enable���[�h��ݒ肷��
	 * 
	 */
	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (editor.getViewer() == null) {
			setEnabled(false);
		} else {
			selection = (IStructuredSelection) editor.getViewer().getSelection();
			if (selection.size() > 0) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	public void run() {
		// Connection con = null;
		int rowAffected = 0;
		try {

			TimeWatcher tw = new TimeWatcher();
			tw.start();
			ITable table = editor.getTableNode();

			TransactionForTableEditor trans = TransactionForTableEditor.getInstance(table.getDbConfig());
			Connection con = trans.getConnection();

			IStructuredSelection selection = (IStructuredSelection) editor.getViewer().getSelection();

			Iterator iter = selection.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TableElement) {
					TableElement elem = (TableElement) obj;

					if (elem.isNew()) {
						rowAffected++; // �V�K���R�[�h�p���J�E���g�ɒǉ����Ă݂�

					} else {

						TableColumn[] uniqueColumns = elem.getUniqueColumns();
						Object[] uniqueItems = elem.getUniqueItems();

						// rowAffected += DeleteSQLInvoker.invoke(con, table,
						// uniqueColumns, uniqueItems);
						// 2006/11/24 ZIGEN modify start
						String sql = SQLCreator.createDeleteSql(table, uniqueColumns, uniqueItems);

						int row = DeleteSQLInvoker.invoke(con, table, uniqueColumns, uniqueItems);
						// ���X�|���X�D��̂��߁A���O�ɋL�^���Ȃ��B
						/*
						if (row > 0) {
							TableEditorLogUtil.successLog(sql);
						} else {
							TableEditorLogUtil.failureLog(sql);
						}*/
						
						rowAffected += row;
						// 2006/11/24 ZIGEN modify start end;

					}
				}
			}
			tw.stop();
			
			if (DbPlugin.getDefault().confirmDialog(rowAffected + Messages.getString("DeleteRecordAction.0"))) { //$NON-NLS-1$
				// �R�~�b�g
				trans.commit();

				// TableViewer����폜����
				removeElement(selection);
				// �Č���
				// new OpenEditorAction(null).openTableEditor(table);

			} else {
				// ���[���o�b�N
				trans.rollback();
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}

	private void removeElement(IStructuredSelection selection) {
		ITableViewEditor editor = DbPlugin.getActiveTableViewEditor();
		if (editor != null) {
			TableViewer viewer = editor.getViewer();

			// �폜���X�|���X������
			TableViewerManager.remove(viewer, selection.toArray());
			
			TableElement[] elements = (TableElement[]) viewer.getInput();
			int dispCnt = elements.length - 1;
			editor.setTotalCount(dispCnt, -1); //$NON-NLS-1$
			ITable tableNode = editor.getTableNode();
			String condition = editor.getCondition();
			RecordCountForTableJob job2 = new RecordCountForTableJob(Transaction.getInstance(config), tableNode, condition, dispCnt, true);
			job2.setUser(false);
			job2.schedule();

		} else {
			throw new IllegalStateException("TableViewer��������܂���"); //$NON-NLS-1$
		}

	}

}
