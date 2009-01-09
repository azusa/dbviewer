/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.derby;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import zigen.plugin.db.core.rule.DefaultMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;

/**
 * OracleMappingFactory.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class DerbyMappingFactory extends DefaultMappingFactory implements IMappingFactory {

	public static final int TYPES_DERBY_ORG_APACHE_DERBY_CATALOG_ALISASINFO = -4;

	public DerbyMappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	public Object getObject(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();
		int type = rmd.getColumnType(icol);
		switch (type) {
		case Types.LONGVARBINARY: // -4
			return "<<未対応(org.apache.derby.catalog.aliasinfo)>>"; //$NON-NLS-1$

		default:
			return super.getObject(rs, icol);
		}
	}

}
