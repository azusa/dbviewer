/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;

/**
 * TableViewerManagerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/09/17 ZIGEN create.
 * 
 */
public class TableViewerManager {

	/**
	 * 要素を追加する
	 * 
	 * @param viewer
	 * @param newElement
	 */
	public static void insert(TableViewer viewer, TableElement newElement) {

		Object obj = viewer.getInput();

		if (obj instanceof TableElement[]) {
			TableElement[] old = (TableElement[]) viewer.getInput();

			TableElement[] wk = new TableElement[old.length + 1];
			System.arraycopy(old, 0, wk, 0, old.length);

			// 最後に新規Elementを追加する
			wk[old.length] = newElement;

			// viewerに追加したElementをセットする
			viewer.setInput(wk);

		} else {
			throw new IllegalArgumentException("TableViewerに格納されているオブジェクトが異なります"); //$NON-NLS-1$
		}
	}

	/**
	 * 指定した要素を更新する ※複数行更新された場合でも、実際に更新した行のみ再描画される
	 * 
	 * @param viewer
	 * @param from
	 * @param to
	 */
	public static void update(TableViewer viewer, TableElement from, TableElement to) {

		Object obj = viewer.getInput();
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) viewer.getInput();
			for (int i = 1; i < elements.length; i++) {
				if (elements[i].equals(from)) {
					TableElement ele = elements[i];
					ele.copy(to);
					ele.clearMofiedColumn();

					// System.out.println(to.getRecordNo());
					// System.out.println(from.getRecordNo());
					// System.out.println();
					// ele.setRecordNo(i); // 1番目はヘッダー
					ele.setRecordNo(from.getRecordNo()); // 1番目はヘッダー

					ele.isNew(false);
					viewer.update(ele, null);// テーブル・ビューワを更新
					break; // 更新行を見つけたらbreakする
				} else {


				}
			}
		} else {
			throw new IllegalArgumentException("TableViewerに格納されているオブジェクトが異なります"); //$NON-NLS-1$
		}
		tw.stop();
	}

	/**
	 * 指定した要素を削除する
	 * 
	 * @param viewer
	 * @param from
	 * @param to
	 */
	public static void remove(TableViewer viewer, Object target) {
		Object obj = viewer.getInput();

		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) viewer.getInput();

			List newList = new ArrayList();
			for (int i = 0; i < elements.length; i++) {
				if (!elements[i].equals(target)) {
					// 不一致のElementのみ追加
					newList.add(elements[i]);
				}
			}
			// viewerに新しいElementをセットする
			viewer.setInput((TableElement[]) newList.toArray(new TableElement[0]));

			// TableViewerを再描画
			// viewer.refresh();

		} else {
			throw new IllegalArgumentException("TableViewerに格納されているオブジェクトが異なります"); //$NON-NLS-1$
		}
	}

	/**
	 * 指定した複数の要素を削除する
	 * 
	 * @param viewer
	 * @param target
	 */
	public static void remove(TableViewer viewer, Object[] target) {
		TableElement[] contents = (TableElement[]) viewer.getInput();

		LinkedList srcList = new LinkedList(Arrays.asList(contents));
		for (int i = 0; i < target.length; i++) {
			srcList.remove(target[i]);
		}
		contents = (TableElement[]) srcList.toArray(new TableElement[srcList.size()]);

		viewer.setInput(contents);
	}


}
