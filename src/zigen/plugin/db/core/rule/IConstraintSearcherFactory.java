/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.Connection;

import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;

/**
 * 
 * IColumnSearchFactory.java�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/06 ZIGEN create.
 * 
 */
public interface IConstraintSearcherFactory {

	// INDEX �� TYPE�J�����ŕԂ����萔
	public static final int TABLE_INDEX_STATISTIC = 0; // �C���f�b�N�X�̓��v�l�D

	public static final int TABLE_INDEX_CLSTERED = 1; // �N���X�^�[�C���f�b�N�X�D

	public static final int TABLE_INDEX_HASHED = 2; // �n�b�V���C���f�b�N�X�D

	public static final int TABLE_INDEX_OTHER = 3; // ��L�ȊO�C���f�b�N�X�D

	TablePKColumn[] getPKColumns(Connection con, String schemaPattern, String tableName) throws Exception;

	TableFKColumn[] getFKColumns(Connection con, String schemaPattern, String tableName) throws Exception;

	TableIDXColumn[] getUniqueIDXColumns(Connection con, String schemaPattern, String tableName, boolean unique) throws Exception;

	TableConstraintColumn[] getConstraintColumns(Connection con, String schemaPattern, String tableName) throws Exception;

}
