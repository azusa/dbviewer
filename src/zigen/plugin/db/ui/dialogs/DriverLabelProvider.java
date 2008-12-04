/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.dialogs;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;

/**
 * DDLLabelProviderクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class DriverLabelProvider extends LabelProvider {

	ImageCacher ic = ImageCacher.getInstance();

	/**
	 * 表示するラベルの取得
	 */
	public String getText(Object obj) {
		System.out.println(obj.toString());
		return obj.toString();
	}

	/**
	 * 表示する画像の取得
	 */
	public Image getImage(Object obj) {

		String imageKey = ISharedImages.IMG_OBJ_FILE;

		if (obj instanceof DataBase) {
			return ic.getImage(DbPlugin.IMG_CODE_DB);
			
		} else if (obj instanceof Folder) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}
