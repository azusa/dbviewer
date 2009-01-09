/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class DDLDiffContributor extends MultiPageEditorActionBarContributor {

	private CopyNodeNameAction copyNodeNameAction;


	public DDLDiffContributor() {
		copyNodeNameAction = new CopyNodeNameAction();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	// ���j���[�\��
	public void fillContextMenu(IMenuManager manager) {
		copyNodeNameAction.refresh();
		manager.add(copyNodeNameAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	// �c�[���o�[��ǉ�����ꍇ�ɃI�[�o�[���C�h����
	public void contributeToToolBar(IToolBarManager toolBarManager) {
	// �c�[���o�[���쐬
	}

	public void setActivePage(IEditorPart target) {
		makeActions(target);
	}

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		makeActions(target);
	}

	private void makeActions(IEditorPart target) {
		if (target instanceof DDLDiffEditor) {
			DDLDiffEditor editor = (DDLDiffEditor) target;
			copyNodeNameAction.setActiveEditor(editor);
		}
	}

	public void dispose() {
		super.dispose();
		copyNodeNameAction.setActiveEditor(null);

	}
}
