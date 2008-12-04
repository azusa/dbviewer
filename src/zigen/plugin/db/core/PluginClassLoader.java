/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import zigen.plugin.db.DbPlugin;

/**
 * PluginClassLoader�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/16 ZIGEN create.
 * 
 */
public class PluginClassLoader extends URLClassLoader {

	/**
	 * �R���X�g���N�^
	 * 
	 * @param urls
	 * @param parent
	 */
	private PluginClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * Plugin�p�N���X���[�_�̎擾
	 * 
	 * @param classpaths
	 * @return
	 */
	public static PluginClassLoader getClassLoader(String[] classpaths, ClassLoader parent) {
		PluginClassLoader loader = new PluginClassLoader(convert(classpaths), parent);

		return loader;
	}

	/**
	 * String[]����URL[]�ɕϊ�. URL�ɂł��Ȃ�CLASSPATH�͏���
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
				wk[i] = url; // �ۑ�����
				cnt++; // �G���[�ɂȂ�Ȃ��������J�E���gUP
			} catch (Exception e) {
				DbPlugin.log(e);
			}
		}

		URL[] url = new URL[cnt]; // �߂�l�p��URL[]�I�u�W�F�N�g
		System.arraycopy(wk, 0, url, 0, cnt); // url�ɃR�s�[
		return url;
	}
}
