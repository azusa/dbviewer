/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * PerspectiveFactoryクラス.
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
		// TODO 自動生成されたメソッド・スタブ
		// String editorArea = layout.getEditorArea();
		// // フォルダの作成
		// IFolderLayout dbListArea =
		// layout.createFolder("TreeView",IPageLayout.LEFT,0.2f,editorArea);
		// // ビューの追加
		// dbListArea.addView("zigen.plugin.db.ui.views.TreeView");
		//	    
		// // フォルダの作成
		// IFolderLayout tableArea =
		// layout.createFolder("SQLExecuteView",IPageLayout.BOTTOM,0.6f,editorArea);
		// // ビューの追加
		// tableArea.addView("zigen.plugin.db.ui.views.SQLExecuteView");
		//	    
		// layout.setEditorAreaVisible(false); // エディタ非表示

	}

}
