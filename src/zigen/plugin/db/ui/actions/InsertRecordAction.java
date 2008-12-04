/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableNewElement;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

/**
 * InsertRecordActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class InsertRecordAction extends TableViewEditorAction {

	public InsertRecordAction() {
		// テキストやツールチップ、アイコンの設定
		this.setText(Messages.getString("InsertRecordAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("InsertRecordAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.InsertRecordCommand"); //$NON-NLS-1$
//		this.setImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_ADD));

	}

	public void run() {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		ITable table = editor.getTableNode();

		TableViewer viewer = editor.getViewer();
		TableElement elem = editor.getHeaderTableElement();
		int count = viewer.getTable().getItems().length;

		// NewRecordの初期値
		Object[] items = new Object[elem.getColumns().length];
		for (int i = 0; i < items.length; i++) {
			TableColumn column = elem.getColumns()[i];
			items[i] = getDefaultValue(column);
		}

		// NewRecord
		// log.debug(elem.getUniqueColumns());
		TableElement newElement = new TableNewElement(table, count + 1, elem.getColumns(), items, elem.getUniqueColumns());

		// レコードの追加
		TableViewerManager.insert(viewer, newElement);

		// １カラム目(Rowを除いたもの）を編集
		editor.editTableElement(newElement, 1);

	}

	/**
	 * 新規レコード作成時の初期値を設定するメソッド
	 * 
	 * @param column
	 * @return
	 */
	public static String getDefaultValue(TableColumn column) {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
        
		// 初期値を設定するように修正
		String defaultValue = column.getDefaultValue();
		
		if (defaultValue != null && !"".equals(defaultValue)) { //$NON-NLS-1$

			if (defaultValue.matches("^'.*'$")) { //$NON-NLS-1$
				defaultValue = defaultValue.replaceAll("^'|'$", "");// 前後の'を外す //$NON-NLS-1$ //$NON-NLS-2$
				defaultValue = defaultValue.replaceAll("''", "'");// '' → //$NON-NLS-1$ //$NON-NLS-2$
																																				// ' 変換
				return defaultValue;
			} else if (defaultValue.matches("^\\(.*\\)$")) { //$NON-NLS-1$
				defaultValue = defaultValue.replaceAll("^\\(|\\)$", "");// 前後の()を外す //$NON-NLS-1$ //$NON-NLS-2$
				
				// 括弧を外した後に、前後の'を外す
				if (defaultValue.matches("^'.*'$")) { //$NON-NLS-1$
					defaultValue = defaultValue.replaceAll("^'|'$", "");// 前後の'を外す //$NON-NLS-1$ //$NON-NLS-2$
					defaultValue = defaultValue.replaceAll("''", "'");// '' → //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				return defaultValue;
				
			} else {
				if (defaultValue.equalsIgnoreCase("NULL")) { //$NON-NLS-1$
				    return nullSymbol; // NULLならばNULL文字を設定
				} else if (StringUtil.isNumeric(defaultValue.trim())) {
				    return defaultValue.trim();
				} else {
					; // 設定しない(SYSDATEなどの関数を初期値に設定している場合)
					return ""; //$NON-NLS-1$
				}
			}

		} else {
			if (!column.isNotNull()) {
			    return nullSymbol; // NULLならばNULL文字を設定
			} else {
			    return ""; //$NON-NLS-1$
			}
		}
	}

}
