/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import zigen.plugin.db.DbPlugin;

/**
 * PluginClassLoaderクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/16 ZIGEN create.
 * 
 */
public class PluginClassLoader extends URLClassLoader {

	/**
	 * コンストラクタ
	 * 
	 * @param urls
	 * @param parent
	 */
	private PluginClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * Plugin用クラスローダの取得
	 * 
	 * @param classpaths
	 * @return
	 */
	public static PluginClassLoader getClassLoader(String[] classpaths, ClassLoader parent) {
		PluginClassLoader loader = new PluginClassLoader(convert(classpaths), parent);

		return loader;
	}

	/**
	 * String[]からURL[]に変換. URLにできないCLASSPATHは除く
	 * 
	 * @param classpaths
	 * @return
	 */
	private static URL[] convert(String[] classpaths) {
		URL[] wk = new URL[classpaths.length];
		int cnt = 0;
		for (int i = 0; i < classpaths.length; i++) {
			String classpath = classpaths[i];
			try {
				URL url = new File(classpath).toURL();
				wk[i] = url; // 保存する
				cnt++; // エラーにならない件数をカウントUP
			} catch (Exception e) {
				DbPlugin.log(e);
			}
		}

		URL[] url = new URL[cnt]; // 戻り値用のURL[]オブジェクト
		System.arraycopy(wk, 0, url, 0, cnt); // urlにコピー
		return url;
	}
}
