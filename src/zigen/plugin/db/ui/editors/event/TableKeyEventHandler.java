/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import java.sql.Connection;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableElementSearcher;
import zigen.plugin.db.core.TableNewElement;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.core.rule.AbstractValidatorFactory;
import zigen.plugin.db.core.rule.IValidatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.InsertRecordAction;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.TextCellEditor;
import zigen.plugin.db.ui.editors.exceptions.UpdateException;
import zigen.plugin.db.ui.editors.exceptions.ZeroUpdateException;
import zigen.plugin.db.ui.editors.internal.RecordUpdateThread;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

/**
 * 
 * TableKeyEventHandlerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class TableKeyEventHandler {

	protected ITableViewEditor editor;

	protected TableViewer viewer;

	protected Table table;

	protected IDBConfig config;

	/**
	 * コンストラクタ
	 * 
	 */
	public TableKeyEventHandler(ITableViewEditor editor) {
		this.editor = editor;
		this.viewer = editor.getViewer();
		this.table = editor.getViewer().getTable();
		this.config = editor.getDBConfig();
	}

	/**
	 * 選択している行番号を取得
	 * 
	 * @return
	 */
	public int getSelectedRow() {
		return table.getSelectionIndex(); // 行（先頭は0から)
	}

	public void selectRow(int index) {
		table.select(index);
	}

	/**
	 * 選択している列番号を取得
	 * 
	 * @return
	 */
	public int getSelectedCellEditorIndex() {
		int defaultIndex = 0;
		CellEditor[] editors = viewer.getCellEditors();
		if (editors == null)
			return -1;
		for (int i = 0; i < editors.length; i++) {
			if (editors[i] != null && editors[i].isActivated()) {
				return i;
			}
		}
		return defaultIndex;
	}

	/**
	 * 編集可能な次の列番号を取得
	 * 
	 * @param cuurentCol
	 * @return
	 */
	public int getEditableNextColumn(int cuurentCol) {
		ICellModifier modifier = viewer.getCellModifier();
		int nextCol = (cuurentCol < table.getColumnCount() - 1) ? cuurentCol + 1 : 1;
		if (modifier.canModify(getHeaderTableElement(), String.valueOf(nextCol))) {
			return nextCol;
		} else {
			return getEditableNextColumn(nextCol);
		}
	}

	/**
	 * 編集可能な前の列番号を取得
	 * 
	 * @param CurrentCol
	 * @return
	 */
	public int getEditablePrevColumn(int CurrentCol) {
		ICellModifier modifier = viewer.getCellModifier();
		int nextCol = (CurrentCol == 1) ? table.getColumnCount() - 1 : CurrentCol - 1;
		if (modifier.canModify(getHeaderTableElement(), String.valueOf(nextCol))) {
			return nextCol;
		} else {
			return getEditablePrevColumn(nextCol);
		}
	}

	/**
	 * テーブルのヘッダ要素を取得
	 * 
	 * @return
	 */
	public TableElement getHeaderTableElement() {
		Object obj = viewer.getInput();
		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) obj;
			if (elements.length > 0) {
				return elements[0];
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 指定したカラムを編集モードにする
	 * 
	 * @param rowIndex
	 *            行番号
	 * @param columnIndex
	 *            列番号
	 */
	public void editTableElement(int rowIndex, int columnIndex) {
		Object element = viewer.getElementAt(rowIndex);
		if (element != null) {

			// うまく横スクロールして編集モードにならない
			// viewer.editElement(element, columnIndex);
			// table.showColumn(table.getColumn(columnIndex));

			// 順番入れかえることで、うまくスクロール＋編集モードになる
			if(columnIndex == 1){
				table.showColumn(table.getColumn(0)); // 行番号を見えるようにする
			}else{
				table.showColumn(table.getColumn(columnIndex));
			}
			viewer.editElement(element, columnIndex);

		}
	}

	/**
	 * 画面上のカラム文字列を更新する ※データベースへの更新は行わない（あくまでも見た目上）
	 * 
	 */
	public void updateColumn(TableElement element, int col, Object newValue) {
		
		// TableElementの値を更新し、編集済みリストに追加
		element.updateItems(col - 1, newValue);// row分-1する
		// テーブル・ビューワを更新
		viewer.update(element, null);
		columnsPack();

	}


	public void setMessage(String msg) {
		DbPlugin.getDefault().showWarningMessage(msg);
		// DbPlugin.getActiveTableViewEditor().setStatusErrorMessage(msg);
	}

	/**
	 * Row番号のカラムのPack
	 */
	private void columnsPack() {
/*
		table.setVisible(false);

		TableColumn[] cols = table.getColumns();
		cols[0].pack();

		table.setVisible(true);
*/
	}

	

	/**
	 * 入力チェックおよび更新データをTableElementに登録する
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean validate(int row, int col) {
		TableElement element = (TableElement) viewer.getElementAt(row);
		Object newValue = null;
		CellEditor editor = viewer.getCellEditors()[col];
		if (editor == null)
			throw new IllegalStateException("CellEditorが設定されていません"); //$NON-NLS-1$
		int columnIndex = -1;
		if (editor instanceof TextCellEditor) {
			TextCellEditor tce = (TextCellEditor)editor;
			newValue = tce.getInputValue();
			columnIndex = tce.getColumnIndex();
			
		}
		Object oldValue = element.getItems()[col - 1];// row分 index-1 したカラム番号
		if (!oldValue.equals(newValue)) {
			String msg = editor.getErrorMessage();
			if (msg == null) {
				// 見た目更新
				updateColumn(element, col, newValue);
				return true; // 成功

			} else {
				// 編集をキャンセル(modifyが呼ばれないようにする)
				viewer.cancelEditing();
				// 見た目を更新
				updateColumn(element, col, newValue);
				// エラーメッセージ表示
				setMessage(msg); //$NON-NLS-1$
				// 入力元のカラムを編集状態にする
				editTableElement(row, col);
				return false; // 失敗
			}

		}
		return true;

	}
	
	public boolean validateAll() {
		// log.debug("validateAll");
		try {
			int row = getSelectedRow();

			if (row == -1) {
				;// CTRL+Cの場合はこちら
			} else {
				TableElement element = (TableElement) viewer.getElementAt(row);
				IDBConfig config = element.getTable().getDbConfig();
				IValidatorFactory factory = AbstractValidatorFactory.getFactory(config);
				zigen.plugin.db.core.TableColumn[] columns = element.getColumns();
				String msg = null;
				for (int col = 0; col < columns.length; col++) {
//					
//					if(!validate(row, col+1)){
//						return false;
//					}
						
						
					
					Object[] items = element.getItems();
					msg = factory.validate(columns[col], items[col]);
					if (msg != null) {
						viewer.cancelEditing();
						setMessage(msg);
						editTableElement(row, col + 1); // row分追加 (ここでは col+1)
						return false;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return true;
	}

	public boolean updateDataBase(TableElement element) throws Exception {
		try {
			PasteRecordMonitor.isPasting();
			
			//TimeWatcher tw = new TimeWatcher();
			//tw.start();
			Display display = Display.getDefault();
			// データベース更新フラグをOFFにする
			element.setUpdatedDataBase(false);
			if (element.isNew()) {
				if (validateAll()) {
					// INSERTの場合のみ同一データが存在する場合は登録できないようにする
					if(hasSameRecord(element)){
						return false;
					}else{
						display.syncExec(new RecordUpdateThread(editor, element));
					}		
					// Row番号のカラム幅をPack
					columnsPack();
				} else {
					return false;
				}

			} else if (element.isModify()) {
				// log.debug("UPDATE発行");
				if (validateAll()) {
					display.syncExec(new RecordUpdateThread(editor, element));
				} else {
					return false;
				}
			} else {
				;
			}
			// データベース更新後、コピー可能にするために選択状態にし、Listenerへ通知する
			viewer.getTable().select(getSelectedRow());	// 修正行にフォーカスを当てる
			viewer.getControl().notifyListeners(SWT.Selection, null);

			//tw.stop();
			
			// データベース更新フラグをONにする
			element.setUpdatedDataBase(true);
			
			return true;	// 更新していない場合でもエラーでなければTrueを返す

		} catch (ZeroUpdateException e) {
			return false;
		} catch (UpdateException e) {
			return false;
		}

	}

	// 同じデータが登録済みかどうかチェックする
	public boolean hasSameRecord(TableElement element) throws Exception{
		Connection con = TransactionForTableEditor.getInstance(config).getConnection();
		TableElement registedElem = TableElementSearcher.findElement(con, element, true);
		return registedElem != null ? true : false;
	}
	
	/**
	 * テーブルに新規登録用のレコードを表示する
	 * 
	 */
	public void createNewRecord() {

		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		TableElement elem = getHeaderTableElement();
		ITable tbl = elem.getTable();
		int count = table.getItems().length;

		// NewRecordの初期値
		Object[] items = new Object[elem.getColumns().length];
		for (int i = 0; i < items.length; i++) {
			zigen.plugin.db.core.TableColumn column = elem.getColumns()[i];
			items[i] = InsertRecordAction.getDefaultValue(column);
		}

		TableElement newElement = new TableNewElement(tbl, count + 1, elem.getColumns(), items, elem.getUniqueColumns());

		// レコードの追加
		TableViewerManager.insert(viewer, newElement);

		// １カラム目(Rowを除いたもの）を編集
		editTableElement(count, 1); // 最終レコードの1カラム目を編集

	}

	/**
	 * 画面上の新規用レコードを削除する ※データベースへの削除は行わない（あくまでも見た目上）
	 * 
	 */
	public void removeRecord(TableElement element) {
		TableViewerManager.remove(viewer, element);
	}

	/**
	 * コネクションの開放
	 */
	public void dispose() {
		// trans.cloesConnection();
	}

//	public void createNewRecord(Object[] items) throws Exception {
//		// String nullSymbol =
//		// DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
//		TableElement elem = getHeaderTableElement();
//		ITable tbl = elem.getTable();
//		int count = table.getItems().length;
//		TableElement newElement = new TableNewElement(tbl, count + 1, elem.getColumns(), items, elem.getUniqueColumns());
//		// レコードの追加
//		TableViewerManager.insert(viewer, newElement);
//
//		// 修正したカラムとして設定する
//		for (int i = 0; i < items.length; i++) {
//			newElement.addMofiedColumn(i);
//		}
//
//		// 選択行を変更しておく 2007-09-06 ZIGEN
//		table.setSelection(count);
//
//		// 更新する 2007-06-19 ZIGEN
//		updateDataBase(newElement);
//
//		// １カラム目(Rowを除いたもの）を編集
//		editTableElement(count, 1); // 最終レコードの1カラム目を編集
//
//	}

}
