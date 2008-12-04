/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

import zigen.plugin.db.ui.internal.TreeLeaf;

/**
 * DragElementAdapterクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class DragElementAdapter extends DragSourceAdapter {

	StructuredViewer viewer;

	public DragElementAdapter(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	// public void dragStart(DragSourceEvent event) {
	// log.debug("dragStart");
	//
	// if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
	// // viewerからResultElementを取り出す。
	// IStructuredSelection selection = (IStructuredSelection)
	// viewer.getSelection();
	//            
	// Iterator iter = selection.iterator();
	// int i=0;
	//            
	// StringBuffer sb = new StringBuffer();
	// while (iter.hasNext()) {
	// Object obj = iter.next();
	// if (obj instanceof TreeLeaf) {
	// if(i > 0){
	// sb.append(", ");
	// }
	// sb.append(((TreeLeaf)obj).getName());
	// i++;
	//                    
	// }
	// }
	//            
	// event.data= sb.toString();
	//            
	// }
	// }

	/**
	 * ドラッグしたデータからeventに値をセットするためのメソッド Method declared on DragSourceListener
	 */
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		Iterator iter = selection.iterator();
		int i = 0;

		StringBuffer sb = new StringBuffer();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof TreeLeaf) {
				if (i > 0) {
					sb.append(", "); //$NON-NLS-1$
				}
				sb.append(((TreeLeaf) obj).getName());
				i++;

			}
		}

		event.data = sb.toString();

	}

}
