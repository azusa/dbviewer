/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * TableContentProvider�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 * 
 */
public class TableViewContentProvider implements IStructuredContentProvider {

	Object[] contents;

	// TableViewer viewer;

	public TableViewContentProvider() {}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// this.viewer = (TableViewer) viewer;
		if (newInput instanceof Object[]) {
			contents = (Object[]) newInput;
		} else {
			contents = null;
		}
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		// if (contents != null && contents == inputElement) {
		Object[] out = new Object[contents.length - 1]; // �J������p�f�[�^������
		System.arraycopy(contents, 1, out, 0, out.length);
		return out;

		// }
		// return new Object[0];
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		contents = null;
	}

	public Object[] getContents() {
		return contents;
	}

}
