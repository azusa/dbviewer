/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;

/**
 * Schemaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class Schema extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * ソースタイプ(Oracle用) ※FunctionやPROCEDUREなど
	 */
	private String[] sourceTypes;
	
	public Schema() {
		super();
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 */
	public Schema(String name) {
		super(name);
	}
	
	public void update(Schema node) {
		this.sourceTypes = node.sourceTypes;
	}
	
	private Schema getNewSchema(Schema schema) {
		String schemaName = schema.getName();
		DataBase db = schema.getDataBase();
		TreeLeaf[] leafs = db.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Schema) {
				Schema newSchema = (Schema) leafs[i];
				if (schemaName.equals(newSchema.getName())) {
					return newSchema;
				}
			}
		}
		return null;
	}
	
	private Table[] convertTables(TreeLeaf[] leafs) {
		List list = new ArrayList(leafs.length);
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Table) {
				list.add((Table) leafs[i]);
			}
		}
		return (Table[]) list.toArray(new Table[0]);
	}
	
	/**
	 * スキーマが所有しているテーブルを取得する
	 * 
	 * @return
	 */
	public Table[] getTables() {
		TreeLeaf[] leafs = getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Folder) {
				Folder folder = (Folder) leafs[i];
				if ("TABLE".equals(folder.getName())) {
					return convertTables(folder.getChildrens());
				}
			}
			
		}
		return null;
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
		
		/**
		 * スキーマ名だけでなくDBConfigが同じかどうか確認すること
		 */
		Schema castedObj = (Schema) o;
		IDBConfig config = castedObj.getDbConfig();
		/*
		 * if (castedObj.getName().equals(getName()) && config.equals(getDbConfig())) { return true; } else { return false; }
		 */

		// 追加 2007/08/20
		if (config == null) {
			System.err.println("Schema#equals() DBConfigを取得できませんでした。");
			return false;
		}
		
		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig())) {
			return true;
		} else {
			return false;
		}
		

	}
	
	public Object clone() {
		Schema inst = new Schema();
		inst.name = this.name == null ? null : new String(this.name);
		return inst;
	}
	
	public String[] getSourceType() {
		return sourceTypes;
	}
	
	public void setSourceType(String[] sourceTypes) {
		this.sourceTypes = sourceTypes;
	}
	
}
