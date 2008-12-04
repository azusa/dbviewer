/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.oracle;

import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.DefaultValidatorFactory;
import zigen.plugin.db.core.rule.IValidatorFactory;
import zigen.plugin.db.core.rule.UnSupportedTypeException;
import zigen.plugin.db.core.rule.Validator;

/**
 * OracleValidatorFactory.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/26 ZIGEN create.
 * 
 */
public class OracleValidatorFactory extends DefaultValidatorFactory implements IValidatorFactory {

	public String validateDataType(TableColumn column, Object value) throws UnSupportedTypeException {

		int type = column.getDataType();
		String columnName = column.getColumnName();
		switch (type) {
		// case Types.DATE:
		// return validate_DATE(columnName, (String) value);
		// case Types.TIMESTAMP:
		// return validate_TIMESTAMP(columnName, (String) value);

		default:
			return super.validateDataType(column, value);
		}

	}

	protected String validate_DATE(String columnName, String value) {
		return Validator.timestamp_Check(columnName, value);
	}

	protected String validate_TIMESTAMP(String columnName, String value) {
		return Validator.timestamp2_Check(columnName, value);
	}

}
