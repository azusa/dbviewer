/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.rule.AbstractValidatorFactory;
import zigen.plugin.db.core.rule.IValidatorFactory;
import zigen.plugin.db.core.rule.UnSupportedTypeException;

/**
 * CellEditorValidatorクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/24 ZIGEN create.
 * 
 */
public class CellEditorValidator implements ICellEditorValidator {

	private TableViewer viewer;

	private int columnIndex;

	private IDBConfig config;

	private IValidatorFactory factory = null;

	public CellEditorValidator(IDBConfig config, TableViewer viewer, int columnIndex) {
		this.config = config;
		this.viewer = viewer;
		this.columnIndex = columnIndex;
		factory = AbstractValidatorFactory.getFactory(config);
	}

	public String isValid(Object value) {
		if (value != null) {
			return validate(String.valueOf(value));
		} else {
			return null;
		}
	}

	private String validate(Object value) {
		String msg = null;
		try {
			Table table = viewer.getTable();
			int currentRow = table.getSelectionIndex();// 行番号取得
			Object obj = viewer.getElementAt(currentRow);
			if (obj instanceof TableElement) {
				TableElement element = (TableElement) (obj);
				zigen.plugin.db.core.TableColumn column = element.getColumns()[columnIndex - 1];
				msg = factory.validate(column, value);
			}
		} catch (UnSupportedTypeException e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

		return msg;

	}
}
