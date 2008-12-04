/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.jobs;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.JobException;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.views.SQLExecuteView;

/**
 * AbstractOpenEditorJobクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public abstract class AbstractJob extends Job {

	protected IPreferenceStore store;

	public AbstractJob(String msg) {
		super(msg); // Title指定
		store = DbPlugin.getDefault().getPreferenceStore();
	}

	protected void showResults(Runnable action) {
		Display.getDefault().asyncExec(action);
	}

	protected void showSyncResults(Runnable action) {
		Display.getDefault().syncExec(action);
	}
	
	
	protected void showMessage(IStatusLineManager statusLineManager, String message) {
		Display.getDefault().asyncExec(new ShowInformationMessageAction(statusLineManager, message));
	}
	
	protected void showWarningMessage(IStatusLineManager statusLineManager, String message) {
		Display.getDefault().asyncExec(new ShowWarningMessageAction(statusLineManager, message));
	}

	protected void showErrorMessage(IStatusLineManager statusLineManager, String message, Throwable e) {
		Display.getDefault().asyncExec(new ShowErrorMessageAction(message, e));
	}
	
	
	protected void showWarningMessage(String message) {
		Display.getDefault().asyncExec(new ShowWarningMessageAction(message));
	}

	protected void showErrorMessage(String message, Throwable e) {
		Display.getDefault().asyncExec(new ShowErrorMessageAction(message, e));
	}

	protected void updateMessage(IDBConfig config, String message, String secondaryId) {
		Display.getDefault().asyncExec((Runnable) new UpdateStatusMessageAction(message, secondaryId));
	}

	// protected boolean isModal(Job job) {
	// Boolean isModal = (Boolean)
	// job.getProperty(IProgressConstants.PROPERTY_IN_DIALOG);
	// if (isModal == null)
	// return true; // ダイアログが無い場合は検索結果を即表示するためtrueにする
	// return isModal.booleanValue();
	// }
	//
	// protected void showResults(Runnable action) {
	// if (isModal(this)) {
	// Display.getDefault().asyncExec(action);
	// } else {
	// setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
	// setProperty(IProgressConstants.ACTION_PROPERTY, action);
	// }
	// }

	
	public class ShowInformationMessageAction implements Runnable {
		String msg = null;
		IStatusLineManager statusLineManager;
		
		public ShowInformationMessageAction(String msg) {
			this.msg = msg;
		}
		
		public ShowInformationMessageAction(IStatusLineManager statusLineManager, String msg) {
			this.msg = msg;
			this.statusLineManager = statusLineManager;
		}

		public void run() {
			try {
				if(statusLineManager == null){
					DbPlugin.getDefault().showInformationMessage(msg);
				}else{
					statusLineManager.setMessage(msg);
				}
				
			} catch (Exception e) {
				DbPlugin.log(e);
			}
		}
	}
	public class ShowWarningMessageAction implements Runnable {
		String msg = null;
		IStatusLineManager statusLineManager;

		public ShowWarningMessageAction(String msg) {
			this.msg = msg;
		}
		
		
		public ShowWarningMessageAction(IStatusLineManager statusLineManager, String msg) {
			this.msg = msg;
			this.statusLineManager = statusLineManager;
		}

		public void run() {
			try {
				if(statusLineManager == null){
					DbPlugin.getDefault().showWarningMessage(msg);
				}else{
					statusLineManager.setErrorMessage(msg);
				}
			} catch (Exception e) {
				DbPlugin.log(e);
			}
		}
	}

	public class ShowErrorMessageAction implements Runnable {
		Throwable e;

		String msg;

		
		public ShowErrorMessageAction(String msg, Throwable e) {
			this.msg = msg;
			this.e = e;
		}

		
		public void run() {
			try {
				JobException je = new JobException(msg, e);
				DbPlugin.getDefault().showErrorDialog(je);
			} catch (Exception e) {
				DbPlugin.log(e);
			}
		}
	}

	// public class UpdateResponseTimeAction implements Runnable {
	// String msg = null;
	// public UpdateResponseTimeAction(String msg) {
	// this.msg = msg;
	// }
	// public void run() {
	// try {
	// IViewPart part = DbPlugin.findView(DbPluginConstant.VIEW_ID_SQLExecute);
	// // if (part instanceof SQLExecuteView) {
	// // SQLExecuteView view = (SQLExecuteView) part;
	// // view.setResponseTime(msg);
	// // }
	// SQLExecuteView view = DbPlugin.getDefault().getActiveSQLExecuteView();
	// if (view != null) {
	// view.setResponseTime(msg);
	// }
	//
	// } catch (Exception e) {
	// DbPlugin.log(e);
	// }
	//
	// }
	//
	// }

	public class UpdateStatusMessageAction implements Runnable {

		private String message;

		private String secondaryId;

		public UpdateStatusMessageAction(String message, String secondaryId) {
			this.message = message;
			this.secondaryId = secondaryId;
		}

		public void run() {
			try {
				SQLExecuteView view = null;
				if (secondaryId == null) {
					view = (SQLExecuteView) DbPlugin.findView(DbPluginConstant.VIEW_ID_SQLExecute);
				} else {
					view = (SQLExecuteView) DbPlugin.findView(DbPluginConstant.VIEW_ID_SQLExecute, secondaryId);
				}
				if (view != null) {
					view.setStatusMessage(message);
				}

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

	}
}
