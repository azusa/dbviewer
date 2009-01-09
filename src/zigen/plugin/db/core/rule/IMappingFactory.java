/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import zigen.plugin.db.core.TableColumn;

/**
 * AbstractMappingRuleクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/22 ZIGEN create.
 * 
 */
public interface IMappingFactory {

	abstract public boolean canModifyDataType(int dataType);

	abstract public Object getObject(ResultSet rs, int icol) throws Exception;

	// abstract public void setObject(PreparedStatement pst, int icol, int type,
	// Object value) throws Exception;

	abstract public void setObject(PreparedStatement pst, int icol, TableColumn column, Object value) throws Exception;

	abstract public String getNullSymbol();

	abstract public void setNullSymbol(String nullSymbol);

	abstract public void setConvertUnicode(boolean convertUnicode);

}
