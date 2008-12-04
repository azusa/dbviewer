/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.editors.text.TextEditorActionContributor;

import zigen.plugin.db.csv.CreateCSVForQueryAction;
import zigen.plugin.db.ui.actions.CopyRecordDataAction;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.actions.SelectAllRecordAction;
import zigen.plugin.db.ui.editors.IQueryViewEditor;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.views.ISQLOperationTarget;

/**
 * TableViewerContributorクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 * 
 */
// public class SqlEditorContributor extends MultiPageEditorActionBarContributor
// {
public class SqlEditorContributor extends TextEditorActionContributor {

	protected SourceViewer sqlViewer;

	protected SelectAllRecordAction selectAllRecordAction;

	protected CopyRecordDataAction copyAction;

	protected CreateCSVForQueryAction createCSVForQueryAction;

	/**
	 * コンストラクタ
	 * 
	 */
	public SqlEditorContributor() {
		selectAllRecordAction = new SelectAllRecordAction();
		copyAction = new CopyRecordDataAction();
		createCSVForQueryAction = new CreateCSVForQueryAction();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	// メニュー表示
	public void fillContextMenu(IMenuManager manager) {
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.UNDO));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.REDO));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.CUT));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.COPY));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.PASTE));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.DELETE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.LINE_DEL));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.ALL_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.CURRENT_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.SELECTED_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.SCRIPT_EXECUTE));

		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.FORMAT));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.UNFORMAT));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.COMMENT));
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	// メニュー表示
	public void fillContextMenuForResultView(IMenuManager manager) {
		reflesh();
		manager.add(copyAction);
		manager.add(selectAllRecordAction);
		manager.add(createCSVForQueryAction);
		//manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	// ツールバーを追加する場合にオーバーライドする
	public void contributeToToolBar(IToolBarManager manager) {
		super.contributeToToolBar(manager);

	}

	// public void contributeToMenu(IMenuManager menu) {
	// // Menuバーの更新(Enable/Disable)の方法が未定のため、実装しない
	// // super.contributeToMenu(menu);
	// // IMenuManager editMenu
	// // =menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
	// // if(editMenu!=null){
	// // editMenu.add(executeSQLAction);
	// // editMenu.add(executeCurrentSQLAction);
	// // editMenu.add(executeSelectedSQLAction);
	// // // deleteAction.refresh();
	// // // editMenu.add(new Separator());
	// // // editMenu.add(insertAction);
	// // // editMenu.add(deleteAction);
	// //
	// // }
	// }

	public void setActivePage(IEditorPart target) {
		if (target != null && target instanceof SqlEditor) {
			sqlViewer = ((SqlEditor) target).sqlViewer;
		}

	}

	// private void makeAction(SqlEditor editor) {
	// sqlViewer = editor.sqlViewer;
	// }

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		if (target instanceof SqlEditor) {
			SqlEditor editor = (SqlEditor) target;
			sqlViewer = editor.sqlViewer;
		}

		if (target != null && target instanceof ITableViewEditor) {
			copyAction.setActiveEditor((ITableViewEditor) target);
			selectAllRecordAction.setActiveEditor((ITableViewEditor) target);
		}
		if (target != null && target instanceof IQueryViewEditor) {
			createCSVForQueryAction.setActiveEditor((IQueryViewEditor) target);
		}

	}

	void reflesh() {
		copyAction.refresh();
	}

	public void dispose() {
		super.dispose();
	}

}
