/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.jobs.OpenEditorJob;
import zigen.plugin.db.ui.jobs.OpenSourceEditorJob;
import zigen.plugin.db.ui.jobs.OracleSequeceSearchJob;
import zigen.plugin.db.ui.jobs.OracleSourceSearchJob;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;
import zigen.plugin.db.ui.jobs.TableTypeSearchJob;

/**
 * VerifyKeyAdapterクラス.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/03 ZIGEN create.
 *
 */
public class TreeDoubleClickHandler implements IDoubleClickListener {

	private boolean showDialog = false;

	public TreeDoubleClickHandler() {}

	public void doubleClick(DoubleClickEvent event) {

		try {

			Viewer view = event.getViewer();
			ISelection selection = event.getSelection();

			if (view instanceof TreeViewer && selection instanceof StructuredSelection) {
				TreeViewer viewer = (TreeViewer) view;
				Object element = ((StructuredSelection) selection).getFirstElement();

				if (element instanceof DataBase) {
					// ダブルクリックで接続する
					DataBase db = (DataBase) element;
					if (!db.isExpanded()) { // db.isConnect ではなく、isExpandで判定する
						db.setConnected(true);
						db.setExpanded(true);
						ConnectDBJob job = new ConnectDBJob(viewer, db);
						job.setPriority(ConnectDBJob.SHORT);
						job.setUser(false);
						job.setSystem(false);
						job.schedule(); // 接続に失敗すれば、db.setConnected(false);

					} else {
						// 接続済みであれば、展開状態のみ切り替え
						changeExpandedState(viewer, (TreeNode) element);
					}

				} else if (element instanceof ITable) {
					OpenEditorJob job = new OpenEditorJob(viewer, (ITable) element);
					job.setPriority(OpenEditorJob.SHORT);
					job.setUser(showDialog);
					job.schedule();

				} else if (element instanceof OracleSource || element instanceof OracleSequence) {
					// 展開済みの場合に、ダブルクリックで閉じない
					OpenSourceEditorJob job = new OpenSourceEditorJob(viewer);
					job.setPriority(OpenSourceEditorJob.SHORT);
					job.setUser(showDialog);
					job.schedule();

				} else if (element instanceof TreeNode) {
					// それ以外のノード要素は展開または非展開処理を行う
					changeExpandedState(viewer, (TreeNode) element);
				}

			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}

	}

	/**
	 * 展開・非展開の切り替え
	 *
	 * @param element
	 */
	private void changeExpandedState(TreeViewer viewer, TreeNode element) {

		// 要素が展開状態かどうか
		if (!viewer.getExpandedState(element)) {

			viewer.expandToLevel(element, 1); // 展開状態にする

			// 現在は「TABLE」フォルダ配下をあらかじめ検索しているため、以下の処理でOK

			if (element instanceof Schema) {

				Schema schema = (Schema) element;

				if (!schema.isExpanded()) {
					schema.setExpanded(true);
					// テーブル一覧を検索（非同期にて実行）
					TableTypeSearchJob job = new TableTypeSearchJob(viewer, schema);
					job.setPriority(TableTypeSearchJob.SHORT);
					job.setUser(showDialog); // ダイアログを出す
					job.schedule();
				}

			} else if (element instanceof Folder) {
				Folder folder = (Folder) element;
				if (!folder.isExpanded()) {
					folder.setExpanded(true);
					Schema schema = folder.getSchema();

					if (schema != null) {
						switch (DBType.getType(schema.getDbConfig())) {
						case DBType.DB_TYPE_ORACLE:
							if (schema != null) {
								if("TABLE".equals(folder.getName())){
									return;
								}else if ("SEQUENCE".equals(folder.getName())) { //$NON-NLS-1$
									OracleSequeceSearchJob job = new OracleSequeceSearchJob(viewer, folder);
									job.setPriority(OracleSequeceSearchJob.SHORT);
									job.setUser(showDialog);
									job.schedule();

									return;

								} else if ("VIEW".equals(folder.getName())) { //$NON-NLS-1$

									System.out.println("changeExpandedState!!");
									RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
									job.setPriority(OracleSequeceSearchJob.SHORT);
									job.setUser(showDialog);
									job.schedule();
									return;

								} else {
									String[] sTypes = schema.getSourceType();
									if (sTypes != null) {
										for (int i = 0; i < sTypes.length; i++) {
											String stype = sTypes[i];
											if (stype.equals(folder.getName())) {
												OracleSourceSearchJob job = new OracleSourceSearchJob(viewer, folder);
												job.setPriority(OracleSourceSearchJob.SHORT);
												job.setUser(showDialog);
												job.schedule();
												return;
											}
										}
									}
								}
							}
						default:
							// ↓ Oracle以外でフォルダをダブルクリックした場合の処理を追加
							RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
							job.setPriority(RefreshFolderJob.SHORT);
							job.setUser(showDialog);
							job.schedule();
							// ↑
							break;
						}
					}
				}

			}

		} else {
			viewer.collapseToLevel(element, 1); // 非展開状態にする
		}

	}
}
