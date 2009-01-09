/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;

/**
 * お気に入りクラス. XMLに保存するためのJavaBeans
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/09/25 ZIGEN create.
 * 
 */
public class Bookmark extends TreeNode implements ITable {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_TABLE = 0;

	public static final int TYPE_VIEW = 1;

	public static final int TYPE_SYNONYM = 2;

	protected IDBConfig dbConfig;

	protected DataBase dataBase;

	protected Schema schema;

	protected Table table;

	protected Folder folder;

	protected int type;

	public Bookmark() {
		super();

	}

	public void copy(Table original) {
		name = new String(original.getName());

		// DataBase要素のコピー（DBConfigもコピーされる)
		dataBase = (DataBase) original.getDataBase().clone();

		// DBConfigのコピー先の参照を設定
		dbConfig = dataBase.getDbConfig();

		if (original.getSchema() != null) {
			schema = (Schema) original.getSchema().clone();
		}
		table = (Table) original.clone();

		folder = (Folder) original.getFolder().clone();

		if (original instanceof Synonym) {
			type = TYPE_SYNONYM;
		} else if (original instanceof View) {
			type = TYPE_VIEW;
		} else {
			type = TYPE_TABLE;
		}

	}

	public Bookmark(Table table) {
		super();
		copy(table);

		// 展開状態でExportされているため、ここで非展開にする 過去のバージョン用
		this.isEnabled = false;
	}

	public void update(Table table) {
		// this.dbConfig = node.dbConfig;
		// this.dataBase = node.dataBase;
		// this.schema = node.schema;
		// this.table = table;
		// this.folder = node.folder;
		// this.type = node.type;
		this.table.update(table);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	private Column[] convertColumns(TreeLeaf[] leafs) {
		List list = new ArrayList(leafs.length);
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Column) {
				list.add((Column) leafs[i]);
			}
		}
		return (Column[]) list.toArray(new Column[0]);

	}

	public Column[] getColumns() {
		return convertColumns(getChildrens());
	}

	public String getLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		if (table.getRemarks() != null && table.getRemarks().length() > 0) {
			sb.append(" [");
			sb.append(table.getRemarks());
			sb.append("]");
		}
		return sb.toString();

	}

	public String getRemarks() {
		if (table != null) {
			return table.getRemarks();
		} else {
			return null;
		}
	}

	public String getSchemaName() {
		if (schema != null) {
			return schema.getName();
		} else {
			return null;
		}
	}

	/**
	 * スキーマ対応の場合はスキーマ名.テーブル名を返す
	 */
	public String getSqlTableName() {
		StringBuffer sb = new StringBuffer();
		if (dataBase.isSchemaSupport()) {
			if (StringUtil.isNumeric(schema.getName())) {
				sb.append("\"");
				sb.append(schema.getName());
				sb.append("\"");
				sb.append(".");
				sb.append(name);
			} else {
				sb.append(schema.getName() + "." + name);
			}


		} else {
			sb.append(name);
		}
		return sb.toString();
	}

	public boolean isSchemaSupport() {
		return dataBase.isSchemaSupport();
	}

	public ITable getTable() {
		return (ITable) table;
	}

	public void setTable(ITable table) {
		if (table instanceof Table) {
			this.table = (Table) table;
		} else {
			this.table = null;
		}
	}

	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public IDBConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(IDBConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	/**
	 * 所属するBookmarkRootの取得
	 * 
	 * @return
	 */
	public BookmarkRoot getBookmarkRoot() {
		return getBookmarkRoot(this);
	}

	/**
	 * 所属するBookmarkFolderの取得
	 * 
	 * @return
	 */
	public BookmarkFolder getBookmarkFolder() {
		return getBookmarkFolder(this);
	}

	private BookmarkRoot getBookmarkRoot(TreeLeaf leaf) {
		if (leaf instanceof BookmarkRoot) {
			return (BookmarkRoot) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkRoot(leaf.getParent());
			} else {
				// return null;
				throw new IllegalStateException("BookmarkRoot要素が上位に存在しません");
			}
		}
	}

	private BookmarkFolder getBookmarkFolder(TreeLeaf leaf) {
		if (leaf instanceof BookmarkFolder) {
			return (BookmarkFolder) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkFolder(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		Bookmark castedObj = (Bookmark) o;

		// tableの等価を追記した
		return ((this.dbConfig == null ? castedObj.dbConfig == null : this.dbConfig.equals(castedObj.dbConfig))
				&& (this.dataBase == null ? castedObj.dataBase == null : this.dataBase.equals(castedObj.dataBase))
				&& (this.schema == null ? castedObj.schema == null : this.schema.equals(castedObj.schema)) && (this.table == null ? castedObj.table == null : this.table
				.equals(castedObj.table)));
	}

	public TableFKColumn[] getTableFKColumns() {
		if (table != null) {
			return table.getTableFKColumns();
		} else {
			return null;
		}
	}

	public TablePKColumn[] getTablePKColumns() {
		if (table != null) {
			return table.getTablePKColumns();
		} else {
			return null;
		}
	}

	public TableIDXColumn[] getTableUIDXColumns() {
		if (table != null) {
			return table.getTableUIDXColumns();
		} else {
			return null;
		}
	}

	public TableIDXColumn[] getTableNonUIDXColumns() {
		if (table != null) {
			return table.getTableNonUIDXColumns();
		} else {
			return null;
		}
	}

	public void setTableFKColumns(TableFKColumn[] tableFKColumns) {
		this.table.setTableFKColumns(tableFKColumns);
	}

	public void setTablePKColumns(TablePKColumn[] tablePKColumns) {
		this.table.setTablePKColumns(tablePKColumns);
	}

	public void setTableUIDXColumns(TableIDXColumn[] tableUIDXColumns) {
		this.table.setTableUIDXColumns(tableUIDXColumns);
	}

	public void setTableNonUIDXColumns(TableIDXColumn[] tableNonUIDXColumns) {
		this.table.setTableNonUIDXColumns(tableNonUIDXColumns);
	}

	public String getFolderName() {
		// 下位互換処理
		if (folder == null) {
			if (type == Bookmark.TYPE_TABLE) {
				return "TABLE";
			} else if (type == Bookmark.TYPE_VIEW) {
				return "VIEW";
			} else if (type == Bookmark.TYPE_SYNONYM) {
				return "SYNONYM";
			} else {
				return "TABLE";
			}
		}
		return folder.getName();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Bookmark:");
		buffer.append(" dbConfig: ");
		buffer.append(dbConfig);
		buffer.append(" dataBase: ");
		buffer.append(dataBase);
		buffer.append(" schema: ");
		buffer.append(schema);
		buffer.append(" table: ");
		buffer.append(table);
		buffer.append(" folder: ");
		buffer.append(folder);
		buffer.append(" type: ");
		buffer.append(type);
		buffer.append("]");
		return buffer.toString();
	}

	// boolean isEnabled = true;
	boolean isEnabled = false;

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean b) {
		this.isEnabled = b;
	}

	public boolean isSynonym() {
		return type == Bookmark.TYPE_SYNONYM;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setRemarks(String remarks) {
		if (table != null) {
			this.table.setRemarks(remarks);
		}
	}

	public TableConstraintColumn[] getTableConstraintColumns() {
		return table.getTableConstraintColumns();
	}

	public void setTableConstraintColumns(TableConstraintColumn[] tableConstraintColumns) {
		table.setTableConstraintColumns(tableConstraintColumns);
	}


}
