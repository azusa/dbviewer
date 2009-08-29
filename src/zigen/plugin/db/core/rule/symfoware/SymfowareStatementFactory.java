/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.symfoware;

import java.sql.SQLException;

import zigen.plugin.db.core.rule.DefaultStatementFactory;

/**
 * 
 * OracleInsertFactory.java�N���X.
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
