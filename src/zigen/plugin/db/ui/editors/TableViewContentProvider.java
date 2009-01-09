/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * TableContentProviderクラス.
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
	 * (非 Javadoc)
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
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		// if (contents != null && contents == inputElement) {
		Object[] out = new Object[contents.length - 1]; // カラム専用データを除く
		System.arraycopy(contents, 1, out, 0, out.length);
		return out;

		// }
		// return new Object[0];
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ
		contents = null;
	}

	public Object[] getContents() {
		return contents;
	}

}
