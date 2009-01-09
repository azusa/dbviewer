/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
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
 * 新しいOracle9iのJDBC Driverでも
 * 
 * 
 */
public class OracleMappingFactory extends DefaultMappingFactory implements IMappingFactory {

	/**
	 * Oracle9iで古いJDBCDriverは、TIMESTAMP型は-100を返す
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
		// 旧Driverでも表示だけするなら、以下のコメントを外す
		// case ORACLE_TIMESTAMP: // -100
		// return getTimestamp(rs, icol);
		case Types.CHAR:
			return getChar(rs, icol); // char型だけ独自仕様

			// add start パラメータなしのNUMBER型に対応(小数を入れることが可能）
		case Types.NUMERIC: // 一般的にはBigDecimal
		case Types.DECIMAL:// 一般的にはBigDecimal
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


	// // -2147483648～2147483647を超えると正しく表示されないため、DoubleではなくBigDecimalで受ける
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
	// // Oracleの数値のオーバーフローです。
	// return rs.getBigDecimal(icol).toString();
	// } else {
	// throw e;
	// }
	// }
	//
	// }

	/**
	 * DBのcharsetによって、空白パディングの数がおかしいため 自力で｢バイト合わせ」するようにします。 JA16SJISの場合は自力で実装する
	 * 
	 */
	protected String getChar(ResultSet rs, int icol) throws SQLException {
		String value = rs.getString(icol);

		if (rs.wasNull())
			return nullSymbol;

		if (convertUnicode) {
			value = JDBCUnicodeConvertor.convert(value);
		}

		// 空白を取り除き、自己パディングする
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
	 * OracleはDate型⇒Timestamp型として扱う
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
	 * BLOBの格納
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
	 * BLOBの取得（実データは取得しません）
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
	 * CLOBの格納
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
	 * CLOBの取得（実データは取得しません）
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

	// CLOBが取得できるように修正 2006/09/02
	protected boolean canModify_BLOB() {
		// return false;
		return true;
	}

	// CLOBが取得できるように修正 2006/09/02
	protected boolean canModify_CLOB() {
		// return false;
		return true;
	}

	/**
	 * Varchar2(4000)の項目に日本語666文字を超える場合と 「ORA-17070: データ・サイズがこの型の最大サイズを超えています。」 というエラーになるため、setStringではなく、setCharacterStream()を使うように修正する。
	 * 
	 */
	protected void setVarchar(PreparedStatement pst, int icol, String str) throws SQLException {
		setLonvarchar(pst, icol, str);
	}
}
