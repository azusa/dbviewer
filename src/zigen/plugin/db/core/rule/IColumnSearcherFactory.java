/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.Connection;

import zigen.plugin.db.core.TableColumn;

/**
 * 
 * IColumnSearchFactory.java�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/06 ZIGEN create.
 * 
 */
public interface IColumnSearcherFactory {
	public static final int COLUMN_NAME = 4; // �J������

	public static final int DATA_TYPE = 5; // java.sql.Types �ł� SQL �f�[�^�^

	public static final int TYPE_NAME = 6; // DB ���i�Ɉˑ������̃f�[�^�^��

	public static final int COLUMN_SIZE = 7; // ��T�C�Y�Bchar/date
												// �^�C�v�ɂ��Ă͍ő啶�����Anumeric ��
												// decimal �^�C�v�ɂ��Ă͌���

	public static final int DECIMAL_DIGITS = 9; // �����_�ȉ��̌���

	public static final int NULLABLE = 11; // NULL �g�p���̉�

	public static final int REMARKS = 12; // �R�����g�L�q�� (null �̏ꍇ������)

	public static final int IS_NULLABLE = 18; // NULL �l�g�p�̉�

	abstract TableColumn[] execute(Connection con, String schemaPattern, String tableName) throws Exception;

	abstract void setConvertUnicode(boolean b);

}
