/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;

public class WindowAdapter implements IWindowListener {

	public void windowActivated(IWorkbenchWindow window) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void windowClosed(IWorkbenchWindow window) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(window.getClass().getName());
		
	}

	public void windowDeactivated(IWorkbenchWindow window) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void windowOpened(IWorkbenchWindow window) {
		// TODO 自動生成されたメソッド・スタブ
		
	}



}
