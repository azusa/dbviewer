/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;

public class WindowAdapter implements IWindowListener {

	public void windowActivated(IWorkbenchWindow window) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}

	public void windowClosed(IWorkbenchWindow window) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		System.out.println(window.getClass().getName());
		
	}

	public void windowDeactivated(IWorkbenchWindow window) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}

	public void windowOpened(IWorkbenchWindow window) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}



}
