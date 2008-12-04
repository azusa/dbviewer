/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * DBConfigSorter�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/05/23 ZIGEN create.
 * 
 */
public class DBConfigSorter implements Comparator {
	boolean isDesc = false;

	public DBConfigSorter() {
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		IDBConfig first = (IDBConfig) o1;
		IDBConfig second = (IDBConfig) o2;

		// ----------------------------------------------------
		// ��r����t�B�[���h���擾����
		// ----------------------------------------------------
		String v1 = first.getDbName(); // ��r����l�P
		String v2 = second.getDbName(); // ��r����l�Q

		try {
			// ���l�ϊ��ł���ꍇ�́A���l��r���s��
			BigDecimal d1 = new BigDecimal(v1); // ���l�ϊ�
			BigDecimal d2 = new BigDecimal(v2); // ���l�ϊ�

			if (d1.doubleValue() < d2.doubleValue()) {
				if (isDesc) {
					return (1);
				} else {
					return (-1);
				}
			} else if (d2.doubleValue() < d1.doubleValue()) {
				if (isDesc) {
					return (-1);
				} else {
					return (1);
				}
			} else {
				return (0);
			}

		} catch (NumberFormatException ex) {
			// ���l�ϊ��ł��Ȃ��ꍇ�́A�������r���s��
			if (isDesc) {
				return (v2.compareTo(v1)); // �~��
			} else {
				return (v1.compareTo(v2)); // ����
			}

		}
	}

}
