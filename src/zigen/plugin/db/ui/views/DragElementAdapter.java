/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

import zigen.plugin.db.ui.internal.TreeLeaf;

/**
 * DragElementAdapter�N���X.
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
	// // viewer����ResultElement�����o���B
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
	 * �h���b�O�����f�[�^����event�ɒl���Z�b�g���邽�߂̃��\�b�h Method declared on DragSourceListener
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
