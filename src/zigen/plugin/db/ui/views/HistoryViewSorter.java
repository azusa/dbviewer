/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.History;

/**
 * TreeViewerSorterクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class HistoryViewSorter extends ViewerSorter {

	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof Folder && e2 instanceof Folder) {
			String a = e1.toString();
			String b = e2.toString();
			return a.compareTo(b);

		} else if (e1 instanceof History && e2 instanceof History) {
			History c1 = (History) e1;
			History c2 = (History) e2;

			return c1.getSqlHistory().getDate().compareTo(c2.getSqlHistory().getDate());

		} else {
			// 上記以外は名前でソート
			String a = e1.toString();
			String b = e2.toString();
			return a.compareTo(b);
		}

	}
}
