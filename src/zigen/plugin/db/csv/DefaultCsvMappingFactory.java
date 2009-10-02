/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.csv;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.InputStreamUtil;
import zigen.plugin.db.core.JDBCUnicodeConvertor;

/**
 * DefaultMappingFactory.java.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/25 ZIGEN create.
 */
public class DefaultCsvMappingFactory extends AbstractCsvMappingFactory implements ICsvMappingFactory {

	public DefaultCsvMappingFactory(boolean convertUnicode, boolean nonDoubleQuate) {
		this.convertUnicode = convertUnicode;
		this.nonDoubleQuate = nonDoubleQuate;
	}

	public String getCsvValue(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();

		String obj = null;
		int type = rmd.getColumnType(icol); // 最初にカラムタイプをチェックする

		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR: // -1
			obj = getString(rs, icol);
			break;

		case Types.BIT: // 一般的にはboolean
		case Types.BOOLEAN:
			obj = getBoolean(rs, icol);
			break;

		case Types.TINYINT:
		case Types.INTEGER: // 一般的にはint
		case Types.SMALLINT: // 一般的にはshort
		case Types.BIGINT: // 一般的にはlong
			obj = getLong(rs, icol);
			break;

		case Types.REAL: // 一般的にはfloat
		case Types.FLOAT: // 一般的にはdouble
		case Types.DOUBLE: // 一般的にはdouble
			obj = getDouble(rs, icol);
			break;

		case Types.NUMERIC: // 一般的にはBigDecimal
		case Types.DECIMAL:// 一般的にはBigDecimal
		// if (rmd.getScale(icol) > 0) {
		// obj = getDouble(rs, icol);
		// } else {
		// // 整数として表示
		// obj = getLong(rs, icol);
		// }
			obj = getBigDecimal(rs, icol);
			break;

		case Types.DATE:
			obj = getDate(rs, icol);

			break;

		case Types.TIME:
			obj = getTime(rs, icol);
			break;

		case Types.TIMESTAMP:
			obj = getTimestamp(rs, icol);
			break;

		case Types.BINARY: // -2
		case Types.VARBINARY: // -3
		case Types.LONGVARBINARY: // -4
			obj = getBinary(rs, icol); // バイナリー表示
			break;

		case Types.CLOB:
			obj = getCLOB(rs, icol);
			break;
		case Types.BLOB:
			obj = getBLOB(rs, icol);
			break;

		case Types.OTHER:
			obj = getOTHER(rs, icol);
			break;

		default:
			obj = Messages.getString("DefaultCsvMappingFactory.0") + type + ")>>"; //$NON-NLS-1$ //$NON-NLS-2$
			break;
		}
		return obj;
	}

	protected String getString(ResultSet rs, int icol) throws SQLException {
		String value = rs.getString(icol);

		if (rs.wasNull())
			return NULL;

		if (convertUnicode) {
			value = JDBCUnicodeConvertor.convert(value);
		}

		value = convertLineSep(value); // 改行コードを\nに変換

		if (!nonDoubleQuate) {
			value = value.replaceAll("\"", "\"\""); // "があれば、""に置換 //$NON-NLS-1$
			// //$NON-NLS-2$
			return "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return value;
		}
	}

	protected String getBoolean(ResultSet rs, int icol) throws SQLException {

		boolean value = rs.getBoolean(icol);

		if (rs.wasNull())
			return NULL;


		if (!nonDoubleQuate) {
			return "\"" + String.valueOf(value) + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return String.valueOf(value);
		}
	}

	protected String getBigDecimal(ResultSet rs, int icol) throws SQLException {
		BigDecimal value = rs.getBigDecimal(icol);
		if (rs.wasNull())
			return NULL;

		return value.toString();
	}

	protected String getLong(ResultSet rs, int icol) throws SQLException {
		long value = rs.getLong(icol);

		if (rs.wasNull())
			return NULL;

		return String.valueOf(value);
	}

	protected String getDouble(ResultSet rs, int icol) throws SQLException {
		double value = rs.getDouble(icol);
		if (rs.wasNull())
			return NULL;

		return String.valueOf(value);
	}

	protected String getDate(ResultSet rs, int icol) throws SQLException {
		Date value = rs.getDate(icol);

		if (rs.wasNull())
			return NULL;

		// return dateFormat.format(value);

		String temp = dateFormat.format(value);
		if (!nonDoubleQuate) {
			return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return temp;
		}


	}

	protected String getTime(ResultSet rs, int icol) throws SQLException {
		Time value = rs.getTime(icol);
		if (rs.wasNull())
			return NULL;

		// return timeFormat.format(value);
		String temp = timeFormat.format(value);
		if (!nonDoubleQuate) {
			return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return temp;
		}
	}

	protected String getTimestamp(ResultSet rs, int icol) throws SQLException {
		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull())
			return NULL;

		// return timeStampFormat.format(new Date(value.getTime()));
		String temp = timeStampFormat.format(new Date(value.getTime()));
		if (!nonDoubleQuate) {
			return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return temp;
		}

	}

	protected String getBinary(ResultSet rs, int icol) throws SQLException {
		InputStream in = null;
		try {
			in = rs.getBinaryStream(icol);
			if (in == null) {
				return NULL;
			}

			// data = (byte[]) toByteArray(in); // byte[]で表示する
			// return toBinary(toByteArray(in)); // バイナリー表示

			String temp = toBinary(toByteArray(in)); // バイナリー表示
			if (!nonDoubleQuate) {
				return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				return temp;
			}


		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					DbPlugin.log(e);
				}
				in = null;
			}
		}

	}

	protected String getCLOB(ResultSet rs, int icol) throws SQLException {
//		Object value = rs.getObject(icol);
//
//		if (rs.wasNull())
//			return NULL;
//
//		return "<<CLOB>>"; //$NON-NLS-1$
//
		InputStream in = null;
		try {
			Clob clob = rs.getClob(icol);

			if (rs.wasNull())
				return NULL;

			String temp = InputStreamUtil.toString(clob.getCharacterStream()); // String
			if (!nonDoubleQuate) {
				return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				return temp;
			}

		}catch(IOException e){
			throw new SQLException("Clob#getCharacterStream()でIOExceptionが発生しました。 " + e.getMessage());

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					DbPlugin.log(e);
				}
				in = null;
			}
		}
	}

	protected String getBLOB(ResultSet rs, int icol) throws SQLException {
//		Object value = rs.getObject(icol);
//
//		if (rs.wasNull())
//			return NULL;
//		return "<<BLOB>>"; //$NON-NLS-1$

		InputStream in = null;
		try {
			Blob blob = rs.getBlob(icol);

			if (rs.wasNull())
				return NULL;

			in = new BufferedInputStream(blob.getBinaryStream());
			String temp = toBinary(toByteArray(in)); // バイナリー表示
			if (!nonDoubleQuate) {
				return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				return temp;
			}

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					DbPlugin.log(e);
				}
				in = null;
			}
		}
	}

	protected String getOTHER(ResultSet rs, int icol) throws SQLException {
		Object value = rs.getObject(icol);

		if (rs.wasNull())
			return NULL;
		return "<<OTHER>>"; //$NON-NLS-1$
	}
}
