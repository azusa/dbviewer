/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

public interface IConfirmDDLWizard {

	public String[] createSQL() throws Exception;

	public ConfirmDDLWizardPage getConfirmDDLWizardPage();
}
