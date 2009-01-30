/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.internal;

import java.util.Iterator;
import java.util.List;

/**
 * Table�N���X.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 *
 */
public class Folder extends TreeNode {

	private static final long serialVersionUID = 1L;

	/**
	 * �R���X�g���N�^
	 *
	 * @param name
	 */
	public Folder(String name) {
		super(name);
	}

	public Folder() {
		super();
	}


	/**
	 * �v�f���X�V
	 *
	 * @param name
	 */
	public void update(String name) {
		this.name = name;
	}

	public Object clone() {
		Folder inst = new Folder();
		inst.name = this.name == null ? null : new String(this.name);

		return inst;
	}

	public boolean hasError() {
		List list =getChildren();
		if(list == null || list.size() == 0){
			return false;
		}else{
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				TreeLeaf leaf = (TreeLeaf) iterator.next();
				if(leaf instanceof OracleSource){
					OracleSource source = (OracleSource)leaf;
					if(source.hasError()){
						return true;
					}
				}
			}
		}
		return false;

	}


}
