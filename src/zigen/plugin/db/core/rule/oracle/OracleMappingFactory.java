/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.oracle;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.rule.DefaultMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;


/**
 * OracleMappingFactory.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 * �V����Oracle9i��JDBC Driver�ł�
 * 
 * 
 */
public class OracleMappingFactory extends DefaultMappingFactory implements IMappingFactory {

	/**
	 * Oracle9i�ŌÂ�JDBCDriver�́ATIMESTAMP�^��-100��Ԃ�
	 */
	public static final int ORACLE_TIMESTAMP = -100;

	public static final int ORACLE_XMLTYPE = 2007;

	public OracleMappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	public Object getObject(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();
		int type = rmd.getColumnType(icol);
		switch (type) {
		// ��Driver�ł��\����������Ȃ�A�ȉ��̃R�����g���O��
		// case ORACLE_TIMESTAMP: // -100
		// return getTimestamp(rs, icol);
		case Types.CHAR:
			return getChar(rs, icol); // char�^�����Ǝ��d�l

			// add start �p�����[�^�Ȃ���NUMBER�^�ɑΉ�(���������邱�Ƃ��\�j
		case Types.NUMERIC: // ��ʓI�ɂ�BigDecimal
		case Types.DECIMAL:// ��ʓI�ɂ�BigDecimal
			// modify start
			// int precision = rmd.getPrecision(icol);
			// int scale = rmd.getScale(icol);
			// if (precision == 0 && scale == 0) {
			// return getDouble(rs, icol);
			// } else if (scale == 0) {
			// return getLong(rs, icol);
			// } else {
			// return getDouble(rs, icol);
			// }
			return getBigDecimal(rs, icol);
			// add end.

			// case ORACLE_XMLTYPE:
			// return getXmlType(rs, icol);

		default:
			return super.getObject(rs, icol);
		}
	}

	// private String getXmlType(ResultSet rs, int icol) throws SQLException {
	// try {
	// oracle.xdb.XMLType xml = (oracle.xdb.XMLType) rs.getObject(icol);
	// return xml.getStringVal().trim();
	// } catch (java.lang.NoClassDefFoundError e) {
	// e.printStackTrace();
	// throw new SQLException(e.getMessage());
	// }
	//		
	// }


	// // -2147483648�`2147483647�𒴂���Ɛ������\������Ȃ����߁ADouble�ł͂Ȃ�BigDecimal�Ŏ󂯂�
	// protected String getDouble(ResultSet rs, int icol) throws SQLException {
	// BigDecimal value = rs.getBigDecimal(icol);
	//
	// if (rs.wasNull())
	// return nullSymbol;
	//
	// return String.valueOf(value);
	//
	// }
	//	
	// protected String getLong(ResultSet rs, int icol) throws SQLException {
	//
	// try {
	// long value = rs.getLong(icol);
	//
	// if (rs.wasNull())
	// return nullSymbol;
	//
	// return String.valueOf(value);
	//
	// } catch (SQLException e) {
	// if (e.getErrorCode() == 17026) {
	// // Oracle�̐��l�̃I�[�o�[�t���[�ł��B
	// return rs.getBigDecimal(icol).toString();
	// } else {
	// throw e;
	// }
	// }
	//
	// }

	/**
	 * DB��charset�ɂ���āA�󔒃p�f�B���O�̐��������������� ���͂Ţ�o�C�g���킹�v����悤�ɂ��܂��B JA16SJIS�̏ꍇ�͎��͂Ŏ�������
	 * 
	 */
	protected String getChar(ResultSet rs, int icol) throws SQLException {
		String value = rs.getString(icol);

		if (rs.wasNull())
			return nullSymbol;

		if (convertUnicode) {
			value = JDBCUnicodeConvertor.convert(value);
		}

		// �󔒂���菜���A���ȃp�f�B���O����
		if (value != null) {
			value = StringUtil.padding(value.trim(), rs.getMetaData().getColumnDisplaySize(icol));
		}
		return value;
	}

	protected String getDate(ResultSet rs, int icol) throws SQLException {
		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull()) {
			return nullSymbol;
		}

		return timeStampFormat.format(new Date(value.getTime()));

	}

	protected String getTimestamp(ResultSet rs, int icol) throws SQLException {

		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull()) {
			return nullSymbol;
		}

		return timeStampFormat2.format(new Date(value.getTime()));

	}

	/**
	 * Oracle��Date�^��Timestamp�^�Ƃ��Ĉ���
	 */
	protected void setDate(PreparedStatement pst, int icol, String str) throws Exception {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.TIMESTAMP);
		} else {
			pst.setTimestamp(icol, toTimestamp(str));

		}

	}

	protected void setTimestamp(PreparedStatement pst, int icol, String str) throws Exception {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.TIMESTAMP);
		} else {
			pst.setTimestamp(icol, toTimestamp2(str));
		}

	}

	/**
	 * BLOB�̊i�[
	 * 
	 * @param pst
	 * @param icol
	 * @param value
	 * @throws SQLException
	 */
	protected void setBlob(PreparedStatement pst, int icol, Object value) throws SQLException {
		if (value == null) {
			pst.setNull(icol, Types.BLOB);
			return;

		} else {
			int size = 0;
			try {
				if (value instanceof File) {
					File file = (File) value;
					size = (int) file.length();
					pst.setBinaryStream(icol, new FileInputStream(file), (int) size);

				} else if (value instanceof byte[]) {
					byte[] bytes = (byte[]) value;
					size = bytes.length;
					pst.setBinaryStream(icol, new ByteArrayInputStream(bytes), (int) size);

				} else if (value instanceof String) {
					String str = (String) value;
					if (nullSymbol.equals(str)) {
						pst.setNull(icol, Types.BLOB);
						return;
					} else {
						byte[] bytes = str.getBytes();
						size = bytes.length;
						pst.setBinaryStream(icol, new ByteArrayInputStream(bytes), (int) size);
					}
				}

			} catch (Exception e) {
				DbPlugin.log(e);

			}

		}
	}

	/**
	 * BLOB�̎擾�i���f�[�^�͎擾���܂���j
	 */
	protected Object getBlob(ResultSet rs, int icol) throws SQLException {
		Object obj = null;

		try {

			Blob blob = rs.getBlob(icol);

			if (rs.wasNull())
				return nullSymbol;

			obj = "<< BLOB >>";

		} catch (Exception e) {
			DbPlugin.log(e);
			throw new SQLException(e.getMessage());
		}
		return obj;

	}

	/**
	 * CLOB�̊i�[
	 * 
	 * @param pst
	 * @param icol
	 * @param value
	 * @throws SQLException
	 */
	protected void setClob(PreparedStatement pst, int icol, Object value) throws SQLException {
		if (value == null) {
			pst.setNull(icol, Types.CLOB);
			return;
		} else {
			int size = 0;
			try {
				if (value instanceof File) {
					File file = (File) value;
					size = (int) file.length();
					pst.setCharacterStream(icol, new FileReader(file), (int) size);
				} else if (value instanceof char[]) {
					char[] chars = (char[]) value;
					size = chars.length;
					pst.setCharacterStream(icol, new CharArrayReader(chars), (int) size);
				} else if (value instanceof String) {
					String str = (String) value;
					if (nullSymbol.equals(str)) {
						pst.setNull(icol, Types.CLOB);
						return;
					} else {
						size = str.getBytes().length;
						pst.setCharacterStream(icol, new StringReader(str), (int) size);
					}
				}

			} catch (Exception e) {
				DbPlugin.log(e);

			}

		}
	}

	/**
	 * CLOB�̎擾�i���f�[�^�͎擾���܂���j
	 */
	protected Object getClob(ResultSet rs, int icol) throws SQLException {
		Object obj = null;
		try {

			Clob clob = rs.getClob(icol);

			if (rs.wasNull())
				return nullSymbol;

			obj = "<< CLOB >>";

		} catch (Exception e) {
			DbPlugin.log(e);
			throw new SQLException(e.getMessage());
		}
		return obj;

	}

	// CLOB���擾�ł���悤�ɏC�� 2006/09/02
	protected boolean canModify_BLOB() {
		// return false;
		return true;
	}

	// CLOB���擾�ł���悤�ɏC�� 2006/09/02
	protected boolean canModify_CLOB() {
		// return false;
		return true;
	}

	/**
	 * Varchar2(4000)�̍��ڂɓ��{��666�����𒴂���ꍇ�� �uORA-17070: �f�[�^�E�T�C�Y�����̌^�̍ő�T�C�Y�𒴂��Ă��܂��B�v �Ƃ����G���[�ɂȂ邽�߁AsetString�ł͂Ȃ��AsetCharacterStream()���g���悤�ɏC������B
	 * 
	 */
	protected void setVarchar(PreparedStatement pst, int icol, String str) throws SQLException {
		setLonvarchar(pst, icol, str);
	}
}
