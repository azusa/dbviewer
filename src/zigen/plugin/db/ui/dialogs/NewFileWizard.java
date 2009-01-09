package zigen.plugin.db.ui.dialogs;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewFileWizard extends Wizard implements INewWizard {

	private WizardNewFileCreationPage page;

	private IStructuredSelection selection;

	public boolean performFinish() {
		page.createNewFile();
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO 自動生成されたメソッド・スタブ
		this.selection = selection;
	}

	public void addPages() {
		page = new WizardNewFileCreationPage("ファイルの保存", selection);
		addPage(page);
	}

}
