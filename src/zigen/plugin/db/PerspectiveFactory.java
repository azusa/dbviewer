/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * PerspectiveFactory�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class PerspectiveFactory implements IPerspectiveFactory {

	public PerspectiveFactory() {
	}

	public void createInitialLayout(IPageLayout layout) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		// String editorArea = layout.getEditorArea();
		// // �t�H���_�̍쐬
		// IFolderLayout dbListArea =
		// layout.createFolder("TreeView",IPageLayout.LEFT,0.2f,editorArea);
		// // �r���[�̒ǉ�
		// dbListArea.addView("zigen.plugin.db.ui.views.TreeView");
		//	    
		// // �t�H���_�̍쐬
		// IFolderLayout tableArea =
		// layout.createFolder("SQLExecuteView",IPageLayout.BOTTOM,0.6f,editorArea);
		// // �r���[�̒ǉ�
		// tableArea.addView("zigen.plugin.db.ui.views.SQLExecuteView");
		//	    
		// layout.setEditorAreaVisible(false); // �G�f�B�^��\��

	}

}
