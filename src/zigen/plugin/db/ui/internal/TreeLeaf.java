/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.io.Serializable;

import zigen.plugin.db.core.IDBConfig;

/**
 * TreeObject�N���X.
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
	 * ���O�̐ݒ�
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ���O�̎擾
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * �e�I�u�W�F�N�g�̐ݒ�
	 * 
	 * @param parent
	 */
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	/**
	 * �I�u�W�F�N�g�̎擾
	 * 
	 * @return
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * toString���\�b�h(LableProvider�Ŏg�p�j
	 */
	public String toString() {
		return getName();
	}

	/**
	 * ��������X�L�[�}�̎擾
	 * 
	 * @return
	 */
	public IDBConfig getDbConfig() {
		return getDbConfig(this);
	}

	/**
	 * ��������X�L�[�}�̎擾
	 * 
	 * @return
	 */
	public DataBase getDataBase() {
		return getDataBase(this);
	}

	/**
	 * ��������X�L�[�}�̎擾
	 * 
	 * @return
	 */
	public Schema getSchema() {
		return getSchema(this);
	}

	/**
	 * ��������t�H���_�̎擾
	 * 
	 * @return
	 */
	public Folder getFolder() {
		return getFolder(this);
	}

	/**
	 * ��������e�[�u���̎擾
	 * 
	 * @return
	 */
	public ITable getTable() {
		return getTable(this);
	}

	/**
	 * �����f�[�^�x�[�X�̎擾 ��ʂ̗v�f���܂ōċA�I�ɌĂяo��
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
	 * �����X�L�[�}�̎擾 ��ʂ̗v�f���܂ōċA�I�ɌĂяo��
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
	 * �����t�H���_�̎擾 ��ʂ̗v�f���܂ōċA�I�ɌĂяo��
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
	 * �����e�[�u���̎擾 ��ʂ̗v�f���܂ōċA�I�ɌĂяo��
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
	 * IDBConfig�̎擾 ��ʂ̗v�f���܂ōċA�I�ɌĂяo��
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
	 * Adapter�̎擾
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
