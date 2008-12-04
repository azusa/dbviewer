/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TreeParent�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create. [2] 2005/03/17 ZIGEN �w�肵���q���擾���郁�\�b�h�̒ǉ�
 * 
 */
public class TreeNode extends TreeLeaf {
	
	private static final long serialVersionUID = 1L;
	
	protected List children;
	
	protected boolean isRoot;
	
	protected boolean isExpanded = false; // �W�J�ς݂��ǂ����i�L���b�V���̔���p�j
	
	/**
	 * �f�t�H���g�R���X�g���N�^.
	 * 
	 * @param name
	 */
	public TreeNode() {
		this(null, false);
	}
	
	/**
	 * �R���X�g���N�^. ���̃R���X�g���N�^���g�p�����isRoot��false�Őݒ肳��܂�
	 * 
	 * @param name
	 */
	public TreeNode(String name) {
		this(name, false);
	}
	
	/**
	 * �R���X�g���N�^
	 * 
	 * @param name
	 * @param isRoot
	 */
	public TreeNode(String name, boolean isRoot) {
		super(name);
		children = new ArrayList();
		this.isRoot = isRoot;
	}
	
	/**
	 * �q�̒ǉ�
	 * 
	 * @param child
	 */
	public void addChild(TreeLeaf child) {
		children.add(child);
		child.setParent(this);
		child.setLevel(level + 1);
	}
	
	/**
	 * �q�̍폜
	 * 
	 * @param child
	 */
	public void removeChild(TreeLeaf child) {
		children.remove(child);
		if (child != null) {
			child.setParent(null);
		}
	}
	
	/**
	 * �q�����ׂč폜
	 */
	public void removeChildAll() {
		TreeLeaf[] elements = getChildrens();
		for (int i = 0; i < elements.length; i++) {
			TreeLeaf elem = (TreeLeaf) elements[i];
			removeChild(elem);
		}
	}
	
	/**
	 * �q���擾
	 * 
	 * @return
	 */
	public TreeLeaf[] getChildrens() {
		return (TreeLeaf[]) children.toArray(new TreeLeaf[children.size()]);
	}
	
	// �ǉ����\�b�h 2003.
	public TreeLeaf getChild(String name) {
		TreeLeaf[] elements = getChildrens();
		for (int i = 0; i < elements.length; i++) {
			TreeLeaf elem = (TreeLeaf) elements[i];
			if (elem.getName().equals(name)) {
				return elem;
			}
		}
		return null;
	}
	
	/**
	 * �q�������Ă��邩��Ԃ�
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	/**
	 * invisibleRoot���Ԃ�
	 * 
	 * @return isRoot ��߂��܂��B
	 */
	public boolean isRoot() {
		return isRoot;
	}
	
	/**
	 * �W�J����Ă��邩�ǂ�����Ԃ�
	 * 
	 * @return
	 */
	public boolean isExpanded() {
		return isExpanded;
	}
	
	/**
	 * �W�J��Ԃ��ǂ�����ݒ肷��
	 * 
	 * @param isExpanded
	 */
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	
	public void setChildren(List children) {
		this.children = children;
		
		// parent���Đݒ�
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			TreeNode node = (TreeNode) iter.next();
			node.setParent(this);
			node.setLevel(level + 1);
		}
		
	}
	
	public List getChildren() {
		return this.children;
	}
	
	/**
	 * Returns <code>true</code> if this <code>TreeNode</code> is the same as the o argument.
	 * 
	 * @return <code>true</code> if this <code>TreeNode</code> is the same as the o argument.
	 */
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
		TreeNode castedObj = (TreeNode) o;
		return ((this.children == null ? castedObj.children == null : this.children.equals(castedObj.children)) && (this.isRoot == castedObj.isRoot));
	}
	
}
