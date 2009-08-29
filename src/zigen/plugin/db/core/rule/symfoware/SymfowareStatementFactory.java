/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.symfoware;

import java.sql.SQLException;

import zigen.plugin.db.core.rule.DefaultStatementFactory;

/**
 * 
 * OracleInsertFactory.javaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/07 ZIGEN create.
 * 
 */
public class SymfowareStatementFactory extends DefaultStatementFactory {
	
	public SymfowareStatementFactory(boolean convertUnicode) {
		super(convertUnicode);
	}
	
	// protected String getDate(Object value) throws SQLException {
	// if (value == null)
	// return NULL;
	// return "to_date('" + value + "','YYYY-MM-DD HH24:MI:SS')";
	// }
	
	protected String getTimestamp(Object value) throws SQLException {
		if (value == null)
			return NULL;
		
		return "CAST('" + value + "' AS TIMESTAMP)";
	}
}
