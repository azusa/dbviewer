/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Validator.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class Validator {

	/**
	 * �����񂪖����͂��ǂ����`�F�b�N���܂��B
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String entry_Check(String filedName, String text) {
		if (text == null || text.length() == 0) {
			// return filedName + " ��Null�֎~���ڂł�";
			return filedName + " is not null.";
		}
		return null;
	}

	/**
	 * ������̒������w�肵���o�C�g���ȓ��ł��邩�ǂ����`�F�b�N���܂��B
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @param maxBytes
	 *            �ő�o�C�g��
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String length_Check(String filedName, String text, int maxBytes) {

		if (text == null || text.equals(""))
			return null;

		int cnt = 0;
		for (int i = 0; i < text.length(); i++) {
			String s = text.substring(i, i + 1);
			cnt = cnt + s.getBytes().length; // 1����������̃o�C�g�������Z
		}

		if (cnt > maxBytes) {
			// return filedName + "��" + maxBytes + "�o�C�g�ȓ��œ��͂��Ă�������";
			return filedName + " is " + maxBytes + "byte limit.";
		}
		return null;
	}

	/**
	 * �����񂪐����Ƃ��Đ��������`�F�b�N���܂��B�i���̒l��NG)
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String numeric_Check(String filedName, String text) {
		if (text == null || text.equals(""))
			return null;

		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			if (!(chr >= '0' && chr <= '9')) { // 0�`9�ȊO�̓G���[
				// return filedName + "�͔��p�����̂ݓ��͂��Ă�������";
				return filedName + " is numeric only,";
			}
		}
		return null;
	}

	/**
	 * �����񂪏������܂ސ��l�Ƃ��Đ��������`�F�b�N���܂��B
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String decimal_Check(String filedName, String text) {
		try {
			new BigDecimal(text); // ���l�`�F�b�N�p��BigDecimal�𐶐�

		} catch (NumberFormatException ex) {
			// return (filedName + "�͐��l����͂��Ă�������");
			return (filedName + " is numeric only.");
		}
		return null;
	}

	/**
	 * �����񂪓��t�Ƃ��ēK�����ǂ����`�F�b�N���܂��B
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String date_Check(String filedName, String text) {
		final String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);
		} catch (ParseException e) {
			// return filedName + "�́A" + pattern + " �`���œ��͂��Ă�������";
			return filedName + " is the format of " + pattern + ".";
		}

		return null;

	}

	/**
	 * �����񂪓��t�Ƃ��ēK�����ǂ����`�F�b�N���܂��B
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String timestamp_Check(String filedName, String text) {
		final String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {
			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	/**
	 * �����񂪓��t�Ƃ��ēK�����ǂ����`�F�b�N���܂��B
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String timestamp2_Check(String filedName, String text) {
		final String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {
			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	/**
	 * �����񂪓��t�Ƃ��ēK�����ǂ����`�F�b�N���܂��B
	 * 
	 * @param filedName
	 *            �t�B�[���h��
	 * @param text
	 *            �`�F�b�N���镶����
	 * @return ���펞�F"" �A�ُ펞�F�G���[���b�Z�[�W
	 */
	public static final String time_Check(String filedName, String text) {
		final String pattern = "HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {

			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	public static final String boolean_Check(String filedName, String text) {
		String str = text.toLowerCase();
		if ("true".equals(str) || "false".equals(str)) {
			return null;
		} else {
			// return filedName + "�́Atrue �܂��� false �œ��͂��Ă�������";
			return filedName + " is true or false";
		}

	}

	public static final String tinyint_Check(String filedName, String text) {
		// final String msg = "�́A-128�`127�͈̔͂� �œ��͂��Ă�������";
		final String msg = "  is range from -128 to 127. ";

		try {
			if (decimal_Check(filedName, text) == null) {
				int value = Integer.parseInt(text);
				if (-128 <= value && value <= 127) {
					return null;
				} else {
					return filedName + msg;
				}
			} else {
				return filedName + msg;
			}
		} catch (NumberFormatException e) {
			return filedName + msg;
		}

	}

}
