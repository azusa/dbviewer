/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TimeWatcher�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/18 ZIGEN create.
 * 
 */
public class TimeWatcher {

	// private static TimeWatcher instance;

	private Date startTime = null;

	private Date stopTime = null;

	private boolean isStart = false; // �v�������ǂ����̔���

	// /**
	// * �C���X�^���X����
	// * @param<code>_instance</code>
	// */
	// public synchronized static TimeWatcher getInstance() {
	// if (instance == null) {
	// instance = new TimeWatcher();
	// }
	// return instance;
	//
	// }

	/**
	 * �R���X�g���N�^
	 */
	public TimeWatcher() {}

	/**
	 * ���Z�b�g���� ��finally�����ŌĂяo���Ă��������B
	 */
	public void reset() {
		isStart = false;
		startTime = null;
		stopTime = null;
	}

	/**
	 * ����J�n
	 */
	public void start() {
		if (isStart) {
			throw new IllegalStateException("����J�n���ł�"); //$NON-NLS-1$
		} else {
			isStart = true;
		}
		startTime = new Date();
	}

	/**
	 * ����I��
	 */
	public void stop() {
		if (!isStart) {
			throw new IllegalStateException("������J�n���Ă�������"); //$NON-NLS-1$
		} else {
			isStart = false;
		}
		stopTime = new Date();
	}

	/**
	 * �g�[�^�����Ԃ��擾
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTotalTime() {
		if (startTime == null) {
			throw new IllegalStateException("����J�n���Ă�������"); //$NON-NLS-1$
		}
		if (stopTime == null) {
			throw new IllegalStateException("����I�����Ă�������"); //$NON-NLS-1$
		}

		return getString((stopTime.getTime() - startTime.getTime()) / 1000.0);
	}

	private String getString(double second) {
		StringBuffer sb = new StringBuffer();
		if ((int) (second / 3600) > 0) { // int�Ŋۂ߂�
			sb.append((int) (second / 3600) + "h"); //$NON-NLS-1$
		}
		if ((int) ((second % 3600) / 60) > 0) { // int�Ŋۂ߂�
			sb.append((int) (second % 3600) / 60 + "min"); //$NON-NLS-1$
		}
		sb.append(formart(second % 60) + "sec"); //$NON-NLS-1$
		return sb.toString();
	}

	private static String formart(double time) {
		BigDecimal decimal = new BigDecimal(time);
		// decimal = decimal.movePointLeft(4);
		decimal = decimal.setScale(1, BigDecimal.ROUND_UP); // �؏グ
		// decimal = decimal.setScale(2, BigDecimal.ROUND_UP); // �؏グ (����2��)

		// decimal = decimal.setScale(1, BigDecimal.ROUND_DOWN); // �؎̂�
		// decimal = decimal.setScale(1, BigDecimal.ROUND_UP); // �l�̌ܓ�
		return decimal.toString();
	}

	/**
	 * �v�������ǂ����̔��胁�\�b�h
	 * 
	 * @return
	 */
	public boolean isStart() {
		return isStart;
	}

}
