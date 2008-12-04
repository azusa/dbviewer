/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.diff;

import java.io.Serializable;
import java.util.ArrayList;

import zigen.plugin.db.ui.internal.TreeNode;


abstract public class DDLNode extends TreeNode implements IDDLDiff, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * �f�t�H���g�R���X�g���N�^.
	 * 
	 * @param name
	 */
	public DDLNode() {
		this(null, false);
	}

	/**
	 * �R���X�g���N�^. ���̃R���X�g���N�^���g�p�����isRoot��false�Őݒ肳��܂�
	 * 
	 * @param name
	 */
	public DDLNode(String name) {
		this(name, false);
	}

	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 * @param isRoot
	 */
	public DDLNode(String name, boolean isRoot) {
		this.name = name;
		this.isRoot = isRoot;
		this.children = new ArrayList();
	}

}
