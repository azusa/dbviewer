/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
/*
 * �쐬��
 *
 * TODO ���̐������ꂽ�t�@�C���̃e���v���[�g��ύX����ɂ͎��փW�����v:
 * �E�B���h�E - �ݒ� - Java - �R�[�h�E�X�^�C�� - �R�[�h�E�e���v���[�g
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
	 * XML����Java�I�u�W�F�N�g�𕜌����� �����G���[���́A�����I�Ƀ��l�[������ ��DBViewer���̂̋N�����ł��Ȃ��Ȃ邽��
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object loadXml() throws Exception {
		Object obj = null;
		if (file.exists()) {
			try {
				int limit = 6400; // SAX�̃f�t�H���g
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
