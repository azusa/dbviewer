/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.SQLException;
import java.sql.Types;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.preference.PreferencePage;

/**
 *
 * DefaultStatementFactory.java�N���X.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/06 ZIGEN create.
 *
 */
public class DefaultStatementFactory extends AbstractStatementFactory implements IStatementFactory {
	protected String nullSymbol;

	protected DefaultStatementFactory(boolean convertUnicode) {
		this.nullSymbol = SQLUtil.getNullSymbol();
		this.convertUnicode = convertUnicode;
	}

	public String getString(int DataType, Object value) throws SQLException {

		if (value.equals(nullSymbol)) {
			value = null;
		}

		String data = null;
		switch (DataType) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			data = getString(value);
			break;

		case Types.TINYINT:
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.BIGINT:
		case Types.REAL:
		case Types.FLOAT:
		case Types.DOUBLE:
		case Types.NUMERIC:
		case Types.DECIMAL:
			data = getNumeric(value);
			break;

		case Types.DATE:
			data = getDate(value);
			break;

		case Types.TIMESTAMP:
			data = getTimestamp(value);
			break;

		case Types.BIT:
		case Types.BOOLEAN:
			data = getBoolean(value);
			break;

		case Types.BINARY: // -2
		case Types.VARBINARY: // -3
		case Types.LONGVARBINARY: // -4
			if (value == null) {
				return NULL;
			}
			data = "<<BINARY>>";
			break;

		case Types.CLOB:
			data = "<<CLOB>>";
			break;
		case Types.BLOB:
			data = "<<BLOB>>";
			break;

		case Types.OTHER:
			data = "<<OTHER>>";
			break;

		default:
			data = "<<UNKNOWN>>";
			break;
		}
		return data;
	}

	protected String getString(Object value) throws SQLException {
		if (value == null)
			return NULL;

		String data = String.valueOf(value);
		if (convertUnicode) {
			data = JDBCUnicodeConvertor.convert(data);
		}
//		�_�u���N�H�[�g�ł͂Ȃ��V���O���N�H�[�g���G�X�P�[�v����悤�ɏC�� 2009/02/01 ZIGEN
//		data = data.replaceAll("\"", "\"\""); // "������΁A""�ɒu��
		data = data.replaceAll("'", "''"); // '������΁A''�ɒu��
		return "'" + data + "'"; // ������̑O���'��t����

	}

	protected String getNumeric(Object value) throws SQLException {
		if (value == null)
			return NULL;
		return String.valueOf(value);
	}

	protected String getBoolean(Object value) throws SQLException {
		if (value == null)
			return NULL;
		return String.valueOf(value);
	}

	protected String getDate(Object value) throws SQLException {
		if (value == null)
			return NULL;
		return "'" + String.valueOf(value) + "'";
	}

	protected String getTimestamp(Object value) throws SQLException {
		if (value == null)
			return NULL;
		return "'" + String.valueOf(value) + "'";
	}

	public static void main(String[] args){
		String 		data = "12'3";
		data = data.replaceAll("'", "''"); // "������΁A""�ɒu��
		System.out.println(data);
	}

	public char getEncloseChar(){
		return '"';	// �f�t�H���g�̓_�u���N�H�[�g
	}
}
