/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class ImageCacher {

	private static ImageCacher instance;

	private Map map;

	/**
	 * インスタンス生成
	 * 
	 * @param<code>_instance</code>
	 */
	public synchronized static ImageCacher getInstance() {
		if (instance == null) {
			instance = new ImageCacher();
		}
		return instance;
	}

	/**
	 * コンストラクタ
	 */
	private ImageCacher() {
		this.map = new HashMap();
	}

	public Image getImage(String imageCode) {
		if (map.containsKey(imageCode)) {
			return (Image) map.get(imageCode);
		} else {
			createImage(imageCode);
			return getImage(imageCode);
			// returnull;
		}
	}

	private void createImage(String imageCode) {
		DbPlugin plugin = DbPlugin.getDefault();
		ImageDescriptor id = plugin.getImageDescriptor(imageCode);
		if (id != null) {
			map.put(imageCode, id.createImage());
		} else {
			DbPlugin.log(Messages.getString("ImageCacher.Error") + imageCode); //$NON-NLS-1$

		}
	}

	public void clear() {
		this.map = null;
	}

}
