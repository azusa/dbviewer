/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.ui.internal.TreeNode;

/**
 * DDLLabelProvider�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class DDLLabelProvider extends LabelProvider {

	ImageCacher ic = ImageCacher.getInstance();

	/**
	 * �\�����郉�x���̎擾
	 */
	public String getText(Object obj) {
		if (obj instanceof DDLDiff) {
			IDDLDiff diff = (IDDLDiff) obj;
			return diff.getName();

		} else {
			return obj.toString();
		}
	}

	/**
	 * �\������摜�̎擾
	 */
	public Image getImage(Object obj) {

		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;

		if (obj instanceof IDDLDiff) {

			IDDLDiff diff = (IDDLDiff) obj;
			if ("TABLE".equals(diff.getType())) {
				return ic.getImage(DbPlugin.IMG_CODE_TABLE);
			} else if ("VIEW".equals(diff.getType())) {
				return ic.getImage(DbPlugin.IMG_CODE_VIEW);
			} else if ("SYNONYM".equals(diff.getType()) || "ALIAS".equals(diff.getType())) {
				return ic.getImage(DbPlugin.IMG_CODE_SYNONYM);
			} else {
				imageKey = ISharedImages.IMG_OBJ_FILE; // Folder�A�C�R��
			}
		} else if (obj instanceof TreeNode) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER; // Folder�A�C�R��
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}
