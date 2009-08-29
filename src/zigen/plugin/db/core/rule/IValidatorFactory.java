/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import zigen.plugin.db.core.TableColumn;

/**
 * IValidatorクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/22 ZIGEN create.
 */
public interface IValidatorFactory {
	
	abstract public String validate(TableColumn column, Object value) throws UnSupportedTypeException;
	
	abstract public String getNullSymbol();
	
	abstract public void setNullSymbol(String nullSymbol);
}
