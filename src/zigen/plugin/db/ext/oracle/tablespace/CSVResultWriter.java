/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import zigen.plugin.db.DbPlugin;

/**
 * �\�̈�̌��ς��茋�ʂ�CSV�ɏo�͂���N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/10/01 ZIGEN create.
 * 
 */
public class CSVResultWriter {

	private char demiliter = ',';

	private boolean append = false;

	private String encording = Messages.getString("CSVResultWriter.0"); //$NON-NLS-1$

	// private List csvList = null;
	private String[] headers = null;

	public CSVResultWriter() {}

	public CSVResultWriter(char demiliter) {
		this.demiliter = demiliter;
	}

	public CSVResultWriter(char demiliter, boolean append) {
		this.demiliter = demiliter;
		this.append = append;
	}

	public CSVResultWriter(char demiliter, boolean append, String encording) {
		this.demiliter = demiliter;
		this.append = append;
		this.encording = encording;
	}

	/**
	 * �R�l�N�V�������ێ����Ȃ���SQL��A�����s����ꍇ�͒��ڋN������. ���̏ꍇ�́A������Connection��Close���s������.
	 * 
	 * @param con
	 * @param query
	 * @throws Exception
	 */
	public void execute(File csvFile, List csvList) throws Exception {
		PrintStream pout = null;

		try {
			pout = new PrintStream(new FileOutputStream(csvFile, append), true, // flashMode
					encording); // encording

			// �J�������x���̏�������
			if (!append) {
				// �ǉ����[�h�łȂ���΃w�b�_�[���o��
				writeHeader(pout);
			}

			// �J�����l�̏�������
			writeValue(pout, csvList);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			if (pout != null)
				pout.close();
		}

	}

	// �J�����f�[�^�̏�������
	private void writeHeader(PrintStream pout) throws SQLException {

		for (int i = 0; i < headers.length; i++) {
			String header = encode(headers[i]);
			if (i == headers.length - 1) {
				// �Ō�͉��s�t
				pout.println(header);
			} else {
				pout.print(header + demiliter);
			}

		}
	}

	// �J�����f�[�^�̏�������
	private void writeValue(PrintStream pout, List csvList) throws SQLException {

		for (Iterator iter = csvList.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof List) {
				List list = (List) obj;

				for (int i = 0; i < list.size(); i++) {
					String value = encode(list.get(i).toString());
					if (i == list.size() - 1) {
						// �Ō�͉��s�t
						pout.println(value);
					} else {
						pout.print(value + demiliter);
					}

				}
			} else {
				throw new IllegalArgumentException("csvList�̒��g��List�^�ł���K�v������܂�"); //$NON-NLS-1$
			}

		}

	}

	private String encode(String value) {
		// " �� "" ��
		value = value.replaceAll("\"", "\"\""); //$NON-NLS-1$ //$NON-NLS-2$

		// �J���}���܂ރf�[�^�̏ꍇ�́A�O���"�ň͂�
		if (value.indexOf("\"") > 0 || value.indexOf(",") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			value = "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return value;
	}

	/**
	 * @return append ��߂��܂��B
	 */
	public boolean isAppend() {
		return append;
	}

	/**
	 * @param append
	 *            append ��ݒ�B
	 */
	public void setAppend(boolean append) {
		this.append = append;
	}

	/**
	 * @return demiliter ��߂��܂��B
	 */
	public char getDemiliter() {
		return demiliter;
	}

	/**
	 * @param demiliter
	 *            demiliter ��ݒ�B
	 */
	public void setDemiliter(char demiliter) {
		this.demiliter = demiliter;
	}

	/**
	 * @return encording ��߂��܂��B
	 */
	public String getEncording() {
		return encording;
	}

	/**
	 * @param encording
	 *            encording ��ݒ�B
	 */
	public void setEncording(String encording) {
		this.encording = encording;
	}

	/**
	 * @return header ��߂��܂��B
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * @param header
	 *            header ��ݒ�B
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
}
