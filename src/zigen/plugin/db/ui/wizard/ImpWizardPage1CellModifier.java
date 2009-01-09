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
public class ImpWizardPage1CellModifier implements ICellModifier {

	private ImpWizardPage1 page;

	public ImpWizardPage1CellModifier(ImpWizardPage1 page) {
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

			// �� �S�đI��/�S�ĉ����̃{�^���ɑΉ����邽�߁A�������O�ł����Ă����l�[�����ăR�s�[�����
			/*
			 * // �f�[�^�x�[�X��`�������ɓo�^����Ă��邩�`�F�b�N String dbName = item.getDbName(); if (item.checked && DBConfigManager.hasSection(dbName)) { item.checked = false; // �I������ page.setErrorMessage(dbName +
			 * "�́A���ɒ�`����Ă���׃C���|�[�g�ł��܂���"); } else { page.setErrorMessage(null); }
			 */
			// ��
			// �P�ȏ�I����Ԃł��邩�`�F�b�N����
			if (page.isSelected()) {
				page.setPageComplete(true);
			} else {
				page.setPageComplete(false);
			}

		}

		// �e�[�u���E�r���[�����X�V
		page.tableViewer.update(element, null);

	}

}
