/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;

import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.Schema;

/**
 * 
 * OracleSourceSearchThreadクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/21 ZIGEN create.
 * 
 */
public class OracleSequeceSearchAction implements Runnable {

	private StructuredViewer viewer;

	private Folder folder;

	public OracleSequeceSearchAction(StructuredViewer viewer, Folder folder) {
		this.viewer = viewer;
		this.folder = folder;
	}

	public void run() {
		try {
			// "読み込み中"を削除
			// folder.removeChild(folder.getChild(DbPluginConstant.TREE_LEAF_LOADING));

			// if (viewer != null) {
			// viewer.refresh(folder);// 再描画
			// }

			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();

			// Folderの上位は、Schema前提
			Schema schema = (Schema) folder.getParent();

			String owner = schema.getName();

			OracleSequenceInfo[] infos = OracleSequenceSearcher.execute(con, owner);

			AddSequences(con, folder, infos);

			// 再描画
			if (viewer != null) {
				viewer.refresh(folder);// 再描画
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	/**
	 * ソース要素をフォルダ配下に追加する
	 * 
	 * @param con
	 * @param folder
	 * @param infos
	 * @throws Exception
	 */
	private static void AddSequences(Connection con, Folder folder, OracleSequenceInfo[] infos) throws Exception {
		for (int i = 0; i < infos.length; i++) {
			OracleSequence seq = new OracleSequence();
			seq.setOracleSequenceInfo(infos[i]);
			folder.addChild(seq);
		}
	}

}
