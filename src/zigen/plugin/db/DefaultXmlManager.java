/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
/*
 * 作成日
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package zigen.plugin.db;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;

import zigen.plugin.db.core.XMLManager;

public class DefaultXmlManager {

	protected File file;

	public DefaultXmlManager(IPath path, String fileName) {
		String readWritePath = path.append(fileName).toOSString();
		file = new File(readWritePath);
	}

	public void saveXml(Object obj) throws IOException {
		XMLManager.save(file, obj);
	}

	/**
	 * XMLからJavaオブジェクトを復元する 復元エラー時は、強制的にリネームする ※DBViewer自体の起動ができなくなるため
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object loadXml() throws Exception {
		Object obj = null;
		if (file.exists()) {
			try {
				int limit = 6400; // SAXのデフォルト
				String _limit = System.getProperty("entityExpansionLimit"); //$NON-NLS-1$
				if (_limit != null) {
					limit = Integer.parseInt(_limit);
				}
				obj = XMLManager.load(file, limit);
			} catch (Exception e) {
				throw e;
			}
		}
		return obj;
	}


}
