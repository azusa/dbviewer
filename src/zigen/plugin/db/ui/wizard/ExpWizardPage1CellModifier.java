/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.wizard;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;

/**
 * MyCellModifierクラス.
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

			// １つ以上選択状態であるかチェックする
			if (page.isSelected()) {
				page.updateStatus(null);
			} else {
				page.updateStatus("エクスポート対象を選択してください");
			}
		}

		// テーブル・ビューワを更新
		page.tableViewer.update(element, null);

	}

}
