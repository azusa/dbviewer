/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.ITable;

/**
 * UpdateSQLInvoker�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 * 
 */

public class UpdateSQLInvoker {

	public static int invoke(IDBConfig config, ITable table, TableColumn[] updateColumns, Object[] updateItems, TableColumn[] uniqueColumns, Object[] uniqueItems) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return invoke(con, table, updateColumns, updateItems, uniqueColumns, uniqueItems);

		} catch (Exception e) {
			throw e;
		}
	}

	public static int invoke(Connection con, ITable table, TableColumn[] updateColumns, Object[] updateItems, TableColumn[] uniqueColumns, Object[] uniqueItems) throws Exception {

		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		StringBuffer sb = new StringBuffer();

		int rowAffected;
		PreparedStatement pst = null;

		try {

			IMappingFactory factory = AbstractMappingFactory.getFactory(table.getDbConfig());

			sb.append("UPDATE ");
			sb.append(table.getSqlTableName());
			for (int i = 0; i < updateColumns.length; i++) {
				if (i == 0) {
					sb.append(" SET ");
				} else {
					sb.append(", ");
				}
				sb.append(updateColumns[i].getColumnName() + "= ? ");
			}
			for (int i = 0; i < uniqueColumns.length; i++) {
				if (i == 0) {
					sb.append("WHERE ");
					sb.append(uniqueColumns[i].getColumnName());

					if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
						sb.append(" is null ");
					} else {
						sb.append(" = ? ");
					}
				} else {
					sb.append("AND ");
					sb.append(uniqueColumns[i].getColumnName());
					if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
						sb.append(" is null ");
					} else {
						sb.append(" = ? ");
					}
				}
			}
			pst = con.prepareStatement(sb.toString());

			int index = 0;
			// �X�V�p
			for (int i = 0; i < updateItems.length; i++) {
				index++;
				factory.setObject(pst, index, updateColumns[i], updateItems[i]);
			}

			// Where��p
			for (int i = 0; i < uniqueItems.length; i++) {
				if (uniqueItems[i] != null && !nullSymbol.equals(uniqueItems[i])) {
					index++;
					factory.setObject(pst, index, uniqueColumns[i], uniqueItems[i]);
				}

			}

			// <- [001] 2005/12/12 add zigen
			// �X�V����(Where�j�ɁALong�^������ƁA
			// �ujava.sql.SQLException: ORA-00997: LONG�f�[�^�^�͎g�p�ł��܂���v���������܂��B
			// DBViewerPlugin�ł�PK�������ꍇ�́A�S���ڂ��X�V�L�[�ɂȂ邽�߁A
			// PK�� AND Long�^������ꍇ�͍X�V�G���[���������܂��B

			rowAffected = pst.executeUpdate();

			if (rowAffected == 0) {
				// �X�V�ł��Ȃ��ꍇ�̓��O�ɏo�͂���
				DbPlugin.log("[UpdateSQLInvoker#invoke]" + sb.toString());
			}

			return rowAffected;

		} catch (SQLException e) {
			DbPlugin.log(e);
			String msg = e.getLocalizedMessage();
			switch (table.getDbConfig().getDbType()) {
			case DBType.DB_TYPE_ORACLE:
				if (msg.startsWith("ORA-00997:")) {
					StringBuffer s = new StringBuffer();
					s.append("[LONG]�f�[�^�^���܂�[PrimaryKey]�̖����e�[�u���͍X�V�ł��܂���\n");
					s.append("�G���[�F" + e.getLocalizedMessage());
					throw new IllegalArgumentException(s.toString());
				}
				break;
			default:
				break;
			}
			throw e;

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(pst);
		}

	}

}
