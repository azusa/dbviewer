/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableElement;

/**
 * TableLabelProvider�N���X.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 *
 */
public class TableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	/**
	 * �J�����f�[�^�擾���\�b�h ��P������Object�F���R�[�h�P�ʂ̃I�u�W�F�N�g�iTableElement�j ��Q������indeex�F�J�����ԍ�
	 */
	public String getColumnText(Object obj, int index) {
		try {
			if (obj instanceof TableElement) {
				TableElement element = (TableElement) obj;

				if (index == 0) {
					// �V�K���R�[�h��*�ŕ\������
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
						// ���\���P�F�\���p�ɂ́A�E���̔��p�󔒂��g��������
						return StringUtil.rTrim(elem.toString(), ' ');

					} else {
						// return "";
						return null;

					}
				}

			} else {
				throw new RuntimeException("�\�����Ȃ��^�ł�"); //$NON-NLS-1$
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
