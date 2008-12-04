/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.DefaultMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;

/**
 * PostgreSQLMappingFactory.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 * 
 */
public class PostgreSQLMappingFactory extends DefaultMappingFactory implements IMappingFactory {

	public PostgreSQLMappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	public Object getObject(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();
		int type = rmd.getColumnType(icol);
		switch (type) {
		// 旧Driverでも表示だけするなら、以下のコメントを外す
		// case ORACLE_TIMESTAMP: // -100
		// return getTimestamp(rs, icol);
		case Types.REAL: // 一般的にはfloat   PostgreSQLのfloat4
			return getFloat(rs, icol);
			//return getBigDecimal(rs, icol);
		default:
			return super.getObject(rs, icol);
		}
	}

	public void setObject(PreparedStatement pst, int icol, TableColumn column, Object value) throws Exception {

		try {
			int type = column.getDataType();

			String str = String.valueOf(value); // Stringへ変換

			switch (type) {
			case Types.REAL:
				setFloat(pst, icol, str);
				//setBigDecimal(pst, icol, str);
				break;
			default:
				super.setObject(pst, icol, column, value);
			}
		} catch (SQLException e) {
			DbPlugin.log(e);
			throw new Exception(e);
		}
	}

	protected String getFloat(ResultSet rs, int icol) throws SQLException {
		float value = rs.getFloat(icol);

		if (rs.wasNull())
			return nullSymbol;

		return String.valueOf(value);
	}

	protected void setFloat(PreparedStatement pst, int icol, String str) throws SQLException {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.FLOAT);
		} else {
			float a = Float.parseFloat(str);
			pst.setFloat(icol, Float.parseFloat(str));

		}
	}

}
