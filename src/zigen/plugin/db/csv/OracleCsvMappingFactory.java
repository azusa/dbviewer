/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.csv;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * OracleMappingFactory.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class OracleCsvMappingFactory extends DefaultCsvMappingFactory implements ICsvMappingFactory {
	/**
	 * Oracle9iで古いJDBCDriverは、TIMESTAMP型は-100を返す
	 */
	public static final int ORACLE_TIMESTAMP = -100;

	public OracleCsvMappingFactory(boolean convertUnicode, boolean nonDoubleQuate) {
		super(convertUnicode, nonDoubleQuate);
	}

	public String getCsvValue(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();
		int type = rmd.getColumnType(icol);
		switch (type) {
		// 旧Driverでも表示だけするなら、以下のコメントを外す
		// case ORACLE_TIMESTAMP: // -100
		// return getTimestamp(rs, icol);
		default:
			return super.getCsvValue(rs, icol);
		}
	}

	protected String getDate(ResultSet rs, int icol) throws SQLException {
		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull()) {
			return NULL;
		}

//		return timeStampFormat.format(new Date(value.getTime()));
        String temp = timeStampFormat.format(new Date(value.getTime()));
        
        if (!nonDoubleQuate) {
            return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
        }else{
            return temp;
        }
	}

	protected String getTimestamp(ResultSet rs, int icol) throws SQLException {

		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull()) {
			return NULL;
		}

		//return timeStampFormat2.format(new Date(value.getTime()));
		
        String temp = timeStampFormat2.format(new Date(value.getTime()));
        if (!nonDoubleQuate) {
            return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
        }else{
            return temp;
        }
        

	}

}
