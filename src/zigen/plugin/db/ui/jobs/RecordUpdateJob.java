/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.InsertSQLInvoker;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableElementSearcher;
import zigen.plugin.db.core.TableManager;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.core.UpdateSQLInvoker;
import zigen.plugin.db.ui.actions.MaxRecordException;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.event.PasteRecordMonitor;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

public class RecordUpdateJob extends AbstractJob {

	public static final String JOB_NAME = RecordUpdateJob.class.getName();

	private ITableViewEditor editor;

	private TableViewer viewer;

	private TableElement element;

	private ITable table;

	private TransactionForTableEditor trans = null;

	public RecordUpdateJob(ITableViewEditor editor) {
		super(Messages.getString("RecordSearchJob.0")); //$NON-NLS-1$
		this.editor = editor;
		this.viewer = editor.getViewer();
		this.table = editor.getTableNode();
		this.trans = TransactionForTableEditor.getInstance(table.getDbConfig());
	}

	public void setTargetTableElement(TableElement element) {
		this.element = element;
	}

	protected IStatus run(IProgressMonitor monitor) {
		Connection con;

		if (element.isModify()) {
			int rowAffected = 0;
			try {
				TimeWatcher tw = new TimeWatcher();
				tw.start();
				con = trans.getConnection();

				if (element.isNew()) {
					rowAffected = InsertSQLInvoker.invoke(con, table, element.getColumns(), element.getItems());
				} else {
					rowAffected = UpdateSQLInvoker.invoke(con, table, element.getModifiedColumns(), element.getModifiedItems(), element.getUniqueColumns(), element
							.getUniqueItems());
				}
				tw.stop();


				trans.commit();
				tw.start();

				if (rowAffected < 1) {
					showWarningMessage(("更新に失敗しました"));
				} else if (rowAffected == 1) {
					// showResults(new RefreshElementAction(con, true));
					Display.getDefault().syncExec(new RefreshElementAction(con, true));
				} else {
					// showResults(new RefreshAllAction(con, element.getTable()));
					Display.getDefault().syncExec(new RefreshAllAction(con, element.getTable()));
				}

				tw.stop();

			} catch (SQLException e) {
				showWarningMessage(e.getMessage());
			} catch (Exception e) {
				if (trans != null) {
					trans.rollback();
				}
				if (!PasteRecordMonitor.isPasting()) {
					DbPlugin.getDefault().showErrorDialog(e);
				}
				showErrorMessage(Messages.getString("RecordSearchJob.1"), e); //$NON-NLS-1$
			}

		}

		return Status.OK_STATUS;
	}

	// private int insert(Connection con) throws Exception {
	// String sql = SQLCreator.createInsertSql(table, element.getColumns(), element.getItems());
	//
	// int rowAffected = 0;
	// try {
	// rowAffected = InsertSQLInvoker.invoke(con, table, element.getColumns(), element.getItems());
	//
	// if (rowAffected > 0) {
	// // TableEditorLogUtil.successLog(sql);
	// } else {
	// // TableEditorLogUtil.failureLog(sql);
	// }
	//
	// // 登録したレコードを検索
	// TableElement updatedElem = TableElementSearcher.findElement(con, element, true);
	//
	// // element ⇒ updatedElem に置き換える
	// TableViewerManager.update(viewer, element, updatedElem);
	// } catch (Exception e) {
	// e.printStackTrace();
	// // TableEditorLogUtil.failureLog(sql);
	// throw e;
	// }
	//
	// return rowAffected;
	// }
	//
	// private int update(Connection con) throws Exception {
	//
	// String sql = SQLCreator.createUpdateSql(table, element.getModifiedColumns(), element.getModifiedItems(), element.getUniqueColumns(), element.getUniqueItems());
	//
	// int rowAffected = 0;
	//
	// try {
	// rowAffected = UpdateSQLInvoker.invoke(con, table, element.getModifiedColumns(), element.getModifiedItems(), element.getUniqueColumns(), element.getUniqueItems());
	//
	// StringBuffer sb = new StringBuffer();
	//
	// if (rowAffected == 0) {
	// // TableEditorLogUtil.failureLog(sql);
	// sb.append(Messages.getString("RecordUpdateThread.3")); //$NON-NLS-1$
	// sb.append(Messages.getString("RecordUpdateThread.4")); //$NON-NLS-1$
	// DbPlugin.getDefault().showWarningMessage(sb.toString());
	//
	// } else if (rowAffected == 1) {
	// // TableEditorLogUtil.successLog(sql);
	// TableElement updatedElem = TableElementSearcher.findElement(con, element, true);
	// if (updatedElem != null) {
	// TableViewerManager.update(viewer, element, updatedElem);
	// } else {
	// sb.append(Messages.getString("RecordUpdateThread.5")); //$NON-NLS-1$
	// sb.append(Messages.getString("RecordUpdateThread.6")); //$NON-NLS-1$
	// DbPlugin.getDefault().showWarningMessage(sb.toString());
	// }
	//
	// } else {
	// // TableEditorLogUtil.successLog(sql);
	// sb.append(Messages.getString("RecordUpdateThread.7") + rowAffected + Messages.getString("RecordUpdateThread.8")); //$NON-NLS-1$ //$NON-NLS-2$
	// DbPlugin.getDefault().showWarningMessage(sb.toString());
	// // reload(con, table);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// // TableEditorLogUtil.failureLog(sql);
	// throw e;
	// }
	// return rowAffected;
	// }


	class RefreshElementAction implements Runnable {

		Connection con;

		boolean isNew;

		public RefreshElementAction(Connection con, boolean isNew) {
			this.con = con;
			this.isNew = isNew;
		}

		public void run() {
			try {

				// 登録したレコードを検索
				TableElement updatedElem = TableElementSearcher.findElement(con, element, isNew);

				if (updatedElem != null) {
					// element ⇒ updatedElem に置き換える
					TableViewerManager.update(viewer, element, updatedElem);
				}

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}

	class RefreshAllAction implements Runnable {

		Connection con;

		ITable table;

		public RefreshAllAction(Connection con, ITable table) {
			this.con = con;
			this.table = table;
		}

		public void run() {
			try {

				TableElement[] elements;
				try {
					elements = TableManager.invoke(con, table, null);
					viewer.setInput(elements);

				} catch (MaxRecordException e) {
					// 最大件数を超えた場合の処理
					viewer.setInput(e.getTableElements());
					DbPlugin.getDefault().showWarningMessage(e.getMessage());
				}

				viewer.refresh();

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}

	// protected class WriteErrorMessageAction implements Runnable {
	// Connection con;
	// ITable table;
	//		
	//
	// public WriteErrorMessageAction(Connection con, ITable table) {
	// this.con = con;
	// this.table = table;
	// }
	// public void run() {
	// try {
	// TimeWatcher tw = new TimeWatcher();
	// tw.start();
	//
	// TableElement[] elements;
	// try {
	// elements = TableManager.invoke(con, table);
	// viewer.setInput(elements);
	//
	// } catch (MaxRecordException e) {
	// // 最大件数を超えた場合の処理
	// viewer.setInput(e.getTableElements());
	// DbPlugin.getDefault().showWarningMessage(e.getMessage());
	// }
	//
	// viewer.refresh();
	// tw.stop();
	//				
	//				
	// } catch (Exception e) {
	// DbPlugin.log(e);
	// }
	//
	// }
	// }

}
