/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;

/**
 * Tableクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class Table extends TreeNode implements ITable {

	private static final long serialVersionUID = 1L;

	String remarks;

	TablePKColumn[] tablePKColumns = null;

	TableFKColumn[] tableFKColumns = null;

	TableConstraintColumn[] tableConstraintColumns = null;

	TableIDXColumn[] tableUIDXColumns = null;

	TableIDXColumn[] tableNonUIDXColumns = null;

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Table(String name, String remarks) {
		super(name);
		this.remarks = remarks;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Table(String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Table() {
		super();
	}

	public void update(Table node) {
		this.remarks = node.remarks;
		this.tablePKColumns = node.tablePKColumns;
		this.tableFKColumns = node.tableFKColumns;
		this.tableUIDXColumns = node.tableUIDXColumns;
		this.tableNonUIDXColumns = node.tableNonUIDXColumns;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see zigen.plugin.db.ui.internal.ITable#getLabel()
	 */
	public String getLabel() {
		StringBuffer sb = new StringBuffer();

		sb.append(this.getName());
		if (remarks != null && remarks.length() > 0) {
			sb.append(" [");
			sb.append(remarks);
			sb.append("]");
		}
		return sb.toString();

	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see zigen.plugin.db.ui.internal.ITable#getRemarks()
	 */
	public String getRemarks() {
		return (remarks == null) ? "" : remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	protected Column[] convertColumns(TreeLeaf[] leafs) {
		List list = new ArrayList(leafs.length);
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Column) {
				list.add((Column) leafs[i]);
			}
		}
		return (Column[]) list.toArray(new Column[0]);
	}

	/**
	 * テーブルが所有しているカラムを取得する
	 * 
	 * @return
	 */
	public Column[] getColumns() {
		return convertColumns(getChildrens());
	}

	public String getSqlTableName() {
		StringBuffer sb = new StringBuffer();
		StringBuffer wkTable = new StringBuffer();
		if (SQLUtil.isBinTableForOracle(name) || name.indexOf("-") > 0) {
			wkTable.append("\"");
			wkTable.append(name);
			wkTable.append("\"");
		} else {
			wkTable.append(name);
		}

		String tableName = wkTable.toString();
		if (getDataBase().isSchemaSupport()) {
			String schemaName = getSchema().getName();
			if (SQLUtil.requireDoubleQuote(schemaName)) {
				sb.append("\"");
				sb.append(schemaName);
				sb.append("\"");
				sb.append(".");
				sb.append(tableName);
			} else {
				sb.append(schemaName);
				sb.append(".");
				sb.append(tableName);
			}

		} else {
			sb.append(tableName);
		}
		return sb.toString();
	}

	public String getSchemaName() {
		if (getSchema() != null) {
			return getSchema().getName();
		} else {
			return null;
		}
	}

	public boolean isSchemaSupport() {
		return getDataBase().isSchemaSupport;
	}

	/**
	 * 以下のequalsメソッドは変更しないこと
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}

		Table castedObj = (Table) o;
		IDBConfig config = castedObj.getDbConfig();
		Schema schema = castedObj.getSchema();

		// 追加 2007/08/20
		if (config == null) {
			System.err.println("Table#equals() DBConfigを取得できませんでした。");
			return false;
		}

		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig()) && schema.equals(getSchema())) {
			return true;
		} else {
			return false;
		}

	}

	public Object clone() {
		Table inst = new Table();
		inst.name = this.name == null ? null : new String(this.name);
		inst.remarks = this.remarks == null ? null : new String(this.remarks);
		return inst;
	}

	public TableFKColumn[] getTableFKColumns() {
		return this.tableFKColumns;
	}

	public TablePKColumn[] getTablePKColumns() {
		return this.tablePKColumns;
	}

	public void setTableFKColumns(TableFKColumn[] tableFKColumns) {
		this.tableFKColumns = tableFKColumns;
	}

	public void setTablePKColumns(TablePKColumn[] tablePKColumns) {
		this.tablePKColumns = tablePKColumns;
	}

	public TableIDXColumn[] getTableUIDXColumns() {
		return tableUIDXColumns;
	}

	public void setTableUIDXColumns(TableIDXColumn[] tableUIDXColumns) {
		this.tableUIDXColumns = tableUIDXColumns;
	}

	public TableIDXColumn[] getTableNonUIDXColumns() {
		return tableNonUIDXColumns;
	}

	public void setTableNonUIDXColumns(TableIDXColumn[] tableNonUIDXColumns) {
		this.tableNonUIDXColumns = tableNonUIDXColumns;
	}

	public String getFolderName() {
		if (getFolder() != null) {
			return getFolder().getName();
		} else {
			return null;
		}
	}

	public TableConstraintColumn[] getTableConstraintColumns() {
		return tableConstraintColumns;
	}

	public void setTableConstraintColumns(TableConstraintColumn[] tableConstraintColumns) {
		this.tableConstraintColumns = tableConstraintColumns;
	}


}
