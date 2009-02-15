/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.SQLException;

/**
 *
 * IInsertMappingFactory.javaクラス.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/06 ZIGEN create.
 *
 */
public interface IStatementFactory {

	public String getString(int DataType, Object value) throws SQLException;

	public char getEncloseChar();
}
