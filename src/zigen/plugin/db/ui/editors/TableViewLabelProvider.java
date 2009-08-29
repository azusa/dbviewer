/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableElement;

/**
 * TableLabelProviderクラス.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 *
 */
public class TableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	/**
	 * カラムデータ取得メソッド 第１引数のObject：レコード単位のオブジェクト（TableElement） 第２引数のindeex：カラム番号
	 */
	public String getColumnText(Object obj, int index) {
		try {
			if (obj instanceof TableElement) {
				TableElement element = (TableElement) obj;

				if (index == 0) {
					// 新規レコードは*で表示する
					if (element.isNew()) {
						return "*"; //$NON-NLS-1$

					} else {

						if (element.isModify()) {
							return String.valueOf("*" + element.getRecordNo()); //$NON-NLS-1$
							// return String.valueOf(element.getRecordNo()); //$NON-NLS-1$

						} else {
							return String.valueOf(" " + element.getRecordNo()); //$NON-NLS-1$
							// return String.valueOf(element.getRecordNo()); //$NON-NLS-1$

						}
					}

				} else {

					Object elem = element.getItems()[index - 1];
					// TableColumn colum = element.getColumns()[index-1];

					if (elem != null) {
						//return elem.toString();
						// 性能改善：表示用には、右側の半角空白をトリムする
						return StringUtil.rTrim(elem.toString(), ' ');

					} else {
						// return "";
						return null;

					}
				}

			} else {
				throw new RuntimeException("予期しない型です"); //$NON-NLS-1$
			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return null;

	}

	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}

	public Image getImage(Object obj) {
		// return PlatformUI.getWorkbench().
		// getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		return null;
	}
}
