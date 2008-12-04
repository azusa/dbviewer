/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.ITable;

/**
 * TableColumnクラス. １レコード分のデータを保持するクラス
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/24 ZIGEN create.
 * 
 */
public class TableElement implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	protected ITable table; // テーブル名（テーブルを結合している場合はNULL)

	protected int recordNo; // レコード番号

	protected TableColumn[] columns; // カラム配列

	protected Object[] items; // データ配列

	protected TableColumn[] uniqueColumns; // Uniqueカラム配列

	protected Object[] uniqueItems; // Uniqueデータ配列

	// オリジナルデータ保存用
	protected Object[] orgItems;

	// オプション属性
	protected List modifiedList = new ArrayList(); // 編集しているカラム

	protected boolean isNew = false; // 新規レコードかどうか

	protected boolean canModify = true; // 編集可能かどうか

	protected TablePKColumn[] pks;

	protected List fks;

	protected boolean updatedDataBase;

	public void copy(TableElement src) {
		this.items = src.items;
		this.orgItems = src.orgItems;
	}

	public boolean hasTablePKColumn() {
		return (pks != null);
	}

	public boolean hasTableFKColumn() {
		return (fks != null);
	}

	public void setTablePKColumn(TablePKColumn[] pks) {
		this.pks = pks;
	}

	public void setTableFKColumn(TableFKColumn[] fks) {
		this.fks = convertTableFKColumn(fks);
	}

	// public String getConstraintPKStr() {
	// StringBuffer sb = new StringBuffer();
	//
	// if (pks == null || pks.length == 0)
	// return null;
	//
	// int i = 0;
	// for (i = 0; i < pks.length; i++) {
	//
	// TablePKColumn pkc = pks[i];
	// if (i == 0) {
	// sb.append("CONSTRAINT ");
	// sb.append(pkc.getName());
	// sb.append(" PRIMARY KEY ");
	// sb.append("(");
	// sb.append(pkc.getColumnName());
	// } else {
	// sb.append(", " + pkc.getColumnName());
	// }
	//
	// }
	// sb.append(")");
	// return sb.toString();
	// }

	public List convertTableFKColumn(TableFKColumn[] fks) {

		List result = new ArrayList();

		String temp = "";
		for (int i = 0; i < fks.length; i++) {

			TableFKColumn fkc = fks[i];
			temp = fkc.getName();
			List list = new ArrayList();

			for (int k = i; k < fks.length; k++) {
				TableFKColumn _fkc = fks[k];
				if (!temp.equals(_fkc.getName())) {
					break;
				} else {
					list.add(_fkc);
					i += k;
				}
				temp = _fkc.getName();
			}
			result.add((TableFKColumn[]) list.toArray(new TableFKColumn[0]));
		}

		return result;
	}

	// public String[] getConstraintFKStr() {
	//
	// if (fks == null)
	// return null;
	// List result = new ArrayList();
	// for (Iterator iter = fks.iterator(); iter.hasNext();) {
	// TableFKColumn[] _fks = (TableFKColumn[]) iter.next();
	//
	// StringBuffer sb = new StringBuffer();
	// StringBuffer sb2 = new StringBuffer();
	// boolean cascade = false;
	// for (int i = 0; i < _fks.length; i++) {
	// TableFKColumn column = _fks[i];
	// cascade = column.isCasucade();
	//
	// if (i == 0) {
	// sb.append("CONSTRAINT ");
	// sb.append(column.getName());
	// sb.append(" FOREIGN KEY ");
	// sb.append("(");
	// sb.append(column.getColumnName());
	//
	// // Reference
	// sb2.append(" REFERENCES ");
	// if (column.getPkSchema() != null) {
	// sb2.append(column.getPkSchema());
	// sb2.append(".");
	// }
	// sb2.append(column.getPkTableName());
	// sb2.append(" ");
	// sb2.append("(");
	// sb2.append(column.getPkColumnName());
	//
	// } else {
	//
	// sb.append(", " + column.getColumnName());
	// sb2.append(", " + column.getColumnName());
	// }
	//
	// }
	// sb.append(")");
	// sb2.append(")");
	// if (cascade) {
	// sb2.append(" ON DELETE CASCADE");
	// }
	// result.add(sb.toString() + sb2.toString());
	// }
	//
	// return (String[]) result.toArray(new String[0]);
	//
	// }

	public void addMofiedColumn(int colIndex) {
		if (columns != null || colIndex <= columns.length - 1)
			if (!modifiedList.contains(columns[colIndex])) {
				this.modifiedList.add(columns[colIndex]);
			}
	}

	public boolean isModify() {
		return (modifiedList.size() > 0);
	}

	public boolean isNew() {
		return this.isNew;
	}

	public TableColumn[] getModifiedColumns() {
		return (TableColumn[]) this.modifiedList.toArray(new TableColumn[0]);
	}

	public Object[] getModifiedItems() {
		Object[] modifiedValues = new Object[modifiedList.size()];
		for (int i = 0; i < modifiedList.size(); i++) {
			modifiedValues[i] = getItem((TableColumn) modifiedList.get(i));
		}
		return modifiedValues;
	}

	/**
	 * 指定したTableColumnからItemを取得する
	 * Selectの結果の場合、同名のカラムが存在するため、
	 * その場合はgetItem(int index)を使用する必要があります
	 * @param column
	 * @return
	 */
	public Object getItem(TableColumn column) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals(column)) {
				return items[i];
			}
		}
		return null;
	}

	/**
	 * 指定したIndexからItemを取得する
	 * 
	 * @param column
	 * @return
	 */
	public Object getItem(int index) {
		return items[index];
	}

	/**
	 * Query用用
	 */
	public TableElement(int recordNo, TableColumn[] columns, Object[] items) {
		this.recordNo = recordNo;
		this.columns = columns;
		this.items = items;
	}

	/**
	 * テーブル編集用
	 */
	public TableElement(ITable table, int recordNo, TableColumn[] columns, Object[] items, TableColumn[] uniqueColumns, Object[] uniqueItems) {
		this.table = table;
		this.recordNo = recordNo;
		this.columns = columns;

		this.uniqueColumns = uniqueColumns;
		this.uniqueItems = uniqueItems;

		this.items = items;

		// オリジナルデータをコピーしておく
		if (items != null) {
			this.orgItems = new Object[items.length];
			System.arraycopy(items, 0, this.orgItems, 0, items.length);
		}
	}

	/**
	 * @return columNo を戻します。
	 */
	public int getRecordNo() {
		return recordNo;
	}

	/**
	 * @return items を戻します。
	 */
	public Object[] getItems() {
		return items;
	}

	/**
	 * @return columns を戻します。
	 */
	public TableColumn[] getColumns() {
		return columns;
	}

	/**
	 * @return uniqueColumns を戻します。
	 */
	public TableColumn[] getUniqueColumns() {
		return uniqueColumns;
	}

	/**
	 * @return uniqueItems を戻します。
	 */
	public Object[] getUniqueItems() {
		return uniqueItems;
	}

	protected Object padding(int index, Object obj) {

		if (obj instanceof String) {
			String value = String.valueOf(obj);

			// NULL文字列であれば、以下の処理は行わない
			String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
			if (!value.equals(nullSymbol)) {
				TableColumn column = columns[index];
				int type = column.getDataType();
				int size = column.getColumnSize();
				switch (type) {
				case Types.CHAR:
					return StringUtil.padding(value, size);
				}
			}
		}

		return obj;
	}

	/**
	 * 指定カラムのオブジェクトを更新する
	 * 
	 * @param index
	 * @param obj
	 */
	public void updateItems(int index, Object obj) {

		// items[index] = obj;
		items[index] = padding(index, obj); // CHAR型用に空白パディング

		// 変更リストに登録する（登録済みの場合は登録されない）
		addMofiedColumn(index);
	}

	public boolean isCanModify() {
		return canModify;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableElement:");
		buffer.append(" table: ");
		buffer.append(table);
		buffer.append(" recordNo: ");
		buffer.append(recordNo);
		buffer.append(" { ");
		for (int i0 = 0; columns != null && i0 < columns.length; i0++) {
			buffer.append(" columns[" + i0 + "]: ");
			buffer.append(columns[i0]);
		}
		buffer.append(" } ");
		buffer.append(" { ");
		for (int i0 = 0; items != null && i0 < items.length; i0++) {
			buffer.append(" items[" + i0 + "]: ");
			buffer.append(items[i0]);
		}
		buffer.append(" } ");
		buffer.append(" { ");
		for (int i0 = 0; uniqueColumns != null && i0 < uniqueColumns.length; i0++) {
			buffer.append(" uniqueColumns[" + i0 + "]: ");
			buffer.append(uniqueColumns[i0]);
		}
		buffer.append(" } ");
		buffer.append(" { ");
		for (int i0 = 0; uniqueItems != null && i0 < uniqueItems.length; i0++) {
			buffer.append(" uniqueItems[" + i0 + "]: ");
			buffer.append(uniqueItems[i0]);
		}
		buffer.append(" } ");
		buffer.append(" modifiedList: ");
		buffer.append(modifiedList);
		buffer.append(" isNew: ");
		buffer.append(isNew);
		buffer.append(" canModify: ");
		buffer.append(canModify);
		buffer.append("]");
		return buffer.toString();
	}

	public Object[] getOrgItems() {
		return orgItems;
	}

	/**
	 * ユニークキーに対応する値を更新します
	 * 
	 */
	public void modifyUniqueItems() {
		Object[] uniequeItems = new Object[uniqueColumns.length];

		for (int i = 0; i < uniqueColumns.length; i++) {
			TableColumn uCol = uniqueColumns[i];

			for (int k = 0; k < columns.length; k++) {
				TableColumn col = columns[k];

				if (col.getColumnName().equals(uCol.getColumnName())) {
					uniequeItems[i] = items[k];
					break;
				}
			}
		}

		this.uniqueItems = uniequeItems;
	}

	/**
	 * isModifyをリセットするためのメソッド
	 * 
	 */
	public void clearMofiedColumn() {
		modifiedList = new ArrayList();
	}

	public boolean isUpdatedDataBase() {
		return updatedDataBase;
	}

	public void setUpdatedDataBase(boolean updatedDataBase) {
		this.updatedDataBase = updatedDataBase;
	}

	public void setRecordNo(int recordNo) {
		this.recordNo = recordNo;
	}

	public void isNew(boolean isNew) {
		this.isNew = isNew;
	}

}
