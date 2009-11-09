/*
 * Copyright (c) 2007�|2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace.wizard;

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
public class WizardPage1CellModifier implements ICellModifier {

	private WizardPage1 page;

	public WizardPage1CellModifier(WizardPage1 page) {
		this.page = page;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {
		TableItem item = (TableItem) element;
		// return item.getText();

		if (property == "check") { //$NON-NLS-1$
			return new Boolean(item.isChecked());
		} else {
			return item.getTableName();
		}
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item) {
			element = ((Item) element).getData();
		}
		TableItem item = (TableItem) element;

		if (property == "check") { //$NON-NLS-1$
			item.setChecked(((Boolean) value).booleanValue());

			if (page.isSelected()) {
				page.updateStatus(null);
			} else {
				page.updateStatus(Messages.getString("WizardPage1CellModifier.2")); //$NON-NLS-1$
			}
		}

		// �e�[�u���E�r���[�����X�V
		page.tableViewer.update(element, null);

	}

}
