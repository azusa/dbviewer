/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import zigen.plugin.db.csv.CreateCSVForQueryAction;
import zigen.plugin.db.ui.actions.CopyRecordDataAction;
import zigen.plugin.db.ui.actions.SelectAllRecordAction;

/**
 * TableViewerContributor�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 * 
 */
public class QueryViewerContributor extends MultiPageEditorActionBarContributor {

	private SelectAllRecordAction selectAllAction;

	private CopyRecordDataAction copyRecordDataAction;

	private CreateCSVForQueryAction createCSVForQueryAction;

	/**
	 * �R���X�g���N�^
	 * 
	 */
	public QueryViewerContributor() {
		copyRecordDataAction = new CopyRecordDataAction();
		createCSVForQueryAction = new CreateCSVForQueryAction();
		selectAllAction = new SelectAllRecordAction();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	// ���j���[�\��
	// public void fillContextMenu(IMenuManager manager, IStructuredSelection
	// selection, String query, TableColumn[] columns) {
	public void fillContextMenu(IMenuManager manager) {
		reflesh();

		manager.add(copyRecordDataAction);
		manager.add(selectAllAction);
//		manager.add(new Separator());
		manager.add(createCSVForQueryAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	// �c�[���o�[��ǉ�����ꍇ�ɃI�[�o�[���C�h����
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		// �c�[���o�[���쐬
		// toolBarManager.add(new MyAction());
	}

	public void setActivePage(IEditorPart target) {
		makeActions(target);
	}

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		makeActions(target);
	}

	private void makeActions(IEditorPart target) {
		if (target instanceof QueryViewEditor2) {
			QueryViewEditor2 editor = (QueryViewEditor2) target;
			selectAllAction.setActiveEditor(editor);
			copyRecordDataAction.setActiveEditor(editor);
			createCSVForQueryAction.setActiveEditor(editor);
		}
	}

	void reflesh() {
		copyRecordDataAction.refresh();
	}

	public void dispose() {
		super.dispose();
		copyRecordDataAction.setActiveEditor(null);
		createCSVForQueryAction.setActiveEditor(null);

	}
}
