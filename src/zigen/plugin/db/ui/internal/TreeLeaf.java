/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.io.Serializable;

import zigen.plugin.db.core.IDBConfig;

/**
 * TreeObjectクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class TreeLeaf implements INode, Serializable {
	private static final long serialVersionUID = 1L;
	
	protected int level = 0;

	protected String name;

	protected TreeNode parent;

	boolean isEnabled = true;

	public TreeLeaf() {
		this(null);
	}

	public TreeLeaf(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean b) {
		this.isEnabled = b;
	}

	/**
	 * 名前の設定
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 名前の取得
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 親オブジェクトの設定
	 * 
	 * @param parent
	 */
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	/**
	 * オブジェクトの取得
	 * 
	 * @return
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * toStringメソッド(LableProviderで使用）
	 */
	public String toString() {
		return getName();
	}

	/**
	 * 所属するスキーマの取得
	 * 
	 * @return
	 */
	public IDBConfig getDbConfig() {
		return getDbConfig(this);
	}

	/**
	 * 所属するスキーマの取得
	 * 
	 * @return
	 */
	public DataBase getDataBase() {
		return getDataBase(this);
	}

	/**
	 * 所属するスキーマの取得
	 * 
	 * @return
	 */
	public Schema getSchema() {
		return getSchema(this);
	}

	/**
	 * 所属するフォルダの取得
	 * 
	 * @return
	 */
	public Folder getFolder() {
		return getFolder(this);
	}

	/**
	 * 所属するテーブルの取得
	 * 
	 * @return
	 */
	public ITable getTable() {
		return getTable(this);
	}

	/**
	 * 所属データベースの取得 上位の要素をまで再帰的に呼び出す
	 * 
	 * @param leaf
	 * @return
	 */
	private DataBase getDataBase(TreeLeaf leaf) {
		if (leaf instanceof DataBase) {
			return (DataBase) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getDataBase(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	/**
	 * 所属スキーマの取得 上位の要素をまで再帰的に呼び出す
	 * 
	 * @param leaf
	 * @return
	 */
	private Schema getSchema(TreeLeaf leaf) {
		if (leaf instanceof Schema) {
			return (Schema) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getSchema(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	/**
	 * 所属フォルダの取得 上位の要素をまで再帰的に呼び出す
	 * 
	 * @param leaf
	 * @return
	 */
	private Folder getFolder(TreeLeaf leaf) {
		if (leaf instanceof Folder) {
			return (Folder) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getFolder(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	/**
	 * 所属テーブルの取得 上位の要素をまで再帰的に呼び出す
	 * 
	 * @param leaf
	 * @return
	 */
	private ITable getTable(TreeLeaf leaf) {
		if (leaf instanceof ITable) {
			return (ITable) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getTable(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	/**
	 * IDBConfigの取得 上位の要素をまで再帰的に呼び出す
	 * 
	 * @param leaf
	 * @return
	 */
	protected IDBConfig getDbConfig(TreeLeaf leaf) {

		if (leaf instanceof DataBase) {
			return ((DataBase) leaf).getDbConfig();
		} else {

			if (leaf.getParent() != null) {
				return getDbConfig(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	/**
	 * Adapterの取得
	 */
	public Object getAdapter(Class key) {
		return null;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
