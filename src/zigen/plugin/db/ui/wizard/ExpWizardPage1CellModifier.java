/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.wizard;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;

/**
 * MyCellModifier�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/21 ZIGEN create.
 * 
 */
public class ExpWizardPage1CellModifier implements ICellModifier {

	private ExpWizardPage1 page;

	public ExpWizardPage1CellModifier(ExpWizardPage1 page) {
		this.page = page;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		TableItem item = (TableItem) element;
		// return item.getText();

		if (property == "check") {
			return new Boolean(item.isChecked());
		} else {
			return item.getDbName();
		}
	}

	public void modify(Object element, String property, Object value) {
		if (element instanceof Item) {
			element = ((Item) element).getData();
		}
		TableItem item = (TableItem) element;

		if (property == "check") {
			item.setChecked(((Boolean) value).booleanValue());

			// �P�ȏ�I����Ԃł��邩�`�F�b�N����
			if (page.isSelected()) {
				page.updateStatus(null);
			} else {
				page.updateStatus("�G�N�X�|�[�g�Ώۂ�I�����Ă�������");
			}
		}

		// �e�[�u���E�r���[�����X�V
		page.tableViewer.update(element, null);

	}

}
