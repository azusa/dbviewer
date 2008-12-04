/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;

/**
 * Columnクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 */
public class Column extends TreeLeaf {
	
	private static final long serialVersionUID = 1L;
	
	protected TableColumn column;
	
	protected TablePKColumn pkColumn = null;
	
	protected TableFKColumn[] fkColumns = null;
	
	public boolean isNotNull() {
		return column.isNotNull();
	}
	
	/**
	 * コンストラクタ ※読み込み中用のコンストラクタ
	 * 
	 * @param name
	 */
	public Column(TableColumn column) {
		super(column.getColumnName());
		this.column = column;
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Column(TableColumn column, TablePKColumn pkColumn, TableFKColumn[] fkColumns) {
		this(column);
		this.pkColumn = pkColumn;
		this.fkColumns = fkColumns;
	}
	
	public void update(Column node) {
		this.column = node.column;
		this.pkColumn = node.pkColumn;
		this.fkColumns = node.fkColumns;
	}
	
	/**
	 * 要素を更新します
	 * 
	 * @param column
	 * @param pkColumn
	 * @param fkColumns
	 */
	public void update(TableColumn column, TablePKColumn pkColumn, TableFKColumn[] fkColumns) {
		this.name = column.getColumnName();
		this.column = column;
		this.pkColumn = pkColumn;
		this.fkColumns = fkColumns;
	}
	
	/**
	 * @return column を戻します。
	 */
	public TableColumn getColumn() {
		return column;
	}
	
	/**
	 * 名前の取得
	 * 
	 * @return
	 */
	public String getName() {
		return column.getColumnName();
	}
	
	/**
	 * 名前の取得
	 * 
	 * @return
	 */
	public String getColumnLabel() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(column.getColumnName());
		sb.append(" ");
		sb.append(column.getTypeName().toLowerCase());
		
		if (isVisibleColumnSize()) {
			if (column.getDecimalDigits() == 0) {
				sb.append("(" + column.getColumnSize() + ")");
			} else {
				sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
			}
		} else {
			;
		}
		
		if (pkColumn != null) {
			sb.append(" PK");
		}
		if (fkColumns != null && fkColumns.length > 0) {
			sb.append(" FK");
		}
		
		// カラムのコメントON
		if (DbPlugin.getDefault().getPreferenceStore().getBoolean(DBTreeViewPreferencePage.P_DISPLAY_COL_COMMENT)) {
			
			// remarksに値があれば追加する
			if (column.getRemarks() != null && column.getRemarks().length() > 0) {
				sb.append(" [");
				sb.append(column.getRemarks());
				sb.append("]");
			}
			
		}
		
		return sb.toString();
		
	}
	
	public boolean isVisibleColumnSize() {
		
		// 下位互換の処理
		IDBConfig config = null;
		ITable table = null;
		
		if (getParent() instanceof Bookmark) {
			// お気に入り配下のカラムの場合
			Bookmark bk = (Bookmark) getParent();
			config = bk.getDbConfig();
			table = bk;
		} else if (getParent() instanceof ContentAssistTable) {
			// コード補完用の場合
			ContentAssistTable cat = (ContentAssistTable) getParent();
			config = cat.getDbConfig();
			table = cat.getTable();
		} else {
			// 通常の場合
			config = getDbConfig(); // 上位階層で定義しているDBConfigを取得
			table = getTable(); // 上位階層のTABLE要素を取得
		}
		
		ISQLCreatorFactory factory;
		if (config != null && table != null) {
			factory = AbstractSQLCreatorFactory.getFactory(config, table);
			return factory.isVisibleColumnSize(column.getTypeName());
			
		} else {
			// SQLコードアシストの場合は、現在は常にTrue
			// TreeNodeではなく、単体でColumnオブジェクトを検索しているため
			return true;
		}
	}
	
	/**
	 * 名前の取得
	 * 
	 * @return
	 */
	public String getLogicalColumnLabel() {
		StringBuffer sb = new StringBuffer();
		
		if (column.getRemarks() != null && column.getRemarks().length() > 0) {
			sb.append(column.getRemarks());
		} else {
			sb.append(column.getColumnName());
		}
		sb.append("：");
		sb.append(column.getTypeName().toLowerCase());
		
		if (column.getDecimalDigits() == 0) {
			sb.append("(" + column.getColumnSize() + ")");
		} else {
			sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
		}
		
		if (pkColumn != null) {
			sb.append(" <Primary Key>");
		}
		// if(fkColumns != null && fkColumns.length>0){ sb.append(" <Foreign
		// Key>");}
		
		return sb.toString();
		
	}
	
	/**
	 * @return fkColumns を戻します。
	 */
	public TableFKColumn[] getFkColumns() {
		return fkColumns;
	}
	
	/**
	 * @return pkColumns を戻します。
	 */
	public TablePKColumn getPkColumn() {
		return pkColumn;
	}
	
	/**
	 * プライマリーキーを持っているか取得
	 * 
	 * @return
	 */
	public boolean hasPrimaryKey() {
		return (pkColumn != null);
	}
	
	/**
	 * 外部キーを持っているか取得
	 * 
	 * @return
	 */
	public boolean hasForeignKey() {
		return (fkColumns != null && fkColumns.length > 0);
	}
	
	public Column() {
		super();
	}
	
	public void setColumn(TableColumn column) {
		this.column = column;
	}
	
	public void setFkColumns(TableFKColumn[] fkColumns) {
		this.fkColumns = fkColumns;
	}
	
	public void setPkColumn(TablePKColumn pkColumn) {
		this.pkColumn = pkColumn;
	}
	
	public String getSize() {
		StringBuffer sb = new StringBuffer();
		if (isVisibleColumnSize() && !column.isWithoutParam()) {
			if (column.getDecimalDigits() == 0) {
				sb.append(column.getColumnSize());
			} else {
				sb.append(column.getColumnSize());
				sb.append(",");
				sb.append(column.getDecimalDigits());
			}
		}
		return sb.toString();
	}
	
	public int getDataType() {
		return column.getDataType();
		
	}
	
	public int getDecimalDigits() {
		return column.getDecimalDigits();
	}
	
	public String getDefaultValue() {
		return column.getDefaultValue();
	}
	
	public String getRemarks() {
		return column.getRemarks();
	}
	
	public int getSeq() {
		return column.getSeq();
	}
	
	public String getTypeName() {
		// return column.getTypeName();
		return column.getTypeName().toUpperCase();
		
	}
	
	// Table定義情報を変更するためのメソッド //
	
	public void setName(String columnName) {
		this.column.setColumnName(columnName);
	}
	
	public void setSize(String size) {
		try {
			if (size != null && !"".equals(size) && isVisibleColumnSize()) {
				int comma = size.indexOf(',');
				if (comma > 0) {
					// 整数部＋小数部
					int _size = Integer.parseInt(size.substring(0, comma));
					int _degits = Integer.parseInt(size.substring(comma + 1));
					column.setColumnSize(_size);
					column.setDecimalDigits(_degits);
					
				} else {
					// 整数部のみ
					column.setColumnSize(Integer.parseInt(size));
					column.setDecimalDigits(0);
					
				}
			}
		} catch (NumberFormatException e) {
			;
		}
		
	}
	
	public void setTypeName(String typeName) {
		this.column.setTypeName(typeName);
	}
	
	public void setDefaultValue(String defaultValue) {
		this.column.setDefaultValue(defaultValue);
	}
	
	public void setRemark(String remarks) {
		this.column.setRemarks(remarks);
	}
	
	public void setNotNull(boolean notNull) {
		this.column.setNotNull(notNull);
	}
	

}
