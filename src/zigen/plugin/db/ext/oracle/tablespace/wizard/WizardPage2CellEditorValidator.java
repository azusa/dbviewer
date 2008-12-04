/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.tablespace.wizard;

import org.eclipse.jface.viewers.ICellEditorValidator;

import zigen.plugin.db.core.rule.Validator;

/**
 * WizardPage2CellEditorValidatorクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/24 ZIGEN create.
 * 
 */
public class WizardPage2CellEditorValidator implements ICellEditorValidator {

	int columnIndex;

	public WizardPage2CellEditorValidator(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String isValid(Object value) {
		String input = String.valueOf(value);
		// エラーの場合は、エラーメッセージを格納
		String msg = validate(input);
		return msg;

	}

	private String validate(String input) {
		String columnName;
		String msg = null;

		switch (columnIndex) {
		case 1:
			columnName = WizardPage2.HEADER_PCTFREE;
			if ((msg = Validator.entry_Check(columnName, input)) != null) {
				return msg;
			} else if ((msg = Validator.numeric_Check(columnName, input)) != null) {
				return msg;
			}
		case 2:
			columnName = WizardPage2.HEADER_RECORD;
			if ((msg = Validator.numeric_Check(columnName, input)) != null) {
				return msg;
			} else if ((msg = Validator.numeric_Check(columnName, input)) != null) {
				return msg;
			}

			break;

		default:
			break;
		}

		return msg;
	}

}
