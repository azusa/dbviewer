/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;

import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;

/**
 * 
 * TableTypeSearchThreadクラス. スキーマ下にTable-Typeを検索する
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/21 ZIGEN create.
 * 
 */
public class SourceTypeSearchAction implements Runnable {

	StructuredViewer viewer;

	Schema schema;

	public SourceTypeSearchAction(StructuredViewer viewer, Schema schema) {
		this.viewer = viewer;
		this.schema = schema;
	}

	public void run() {
		try {
			// "読み込み中"を削除
			// schema.removeChild((Table)schema.getChild(DbPluginConstant.TREE_LEAF_LOADING));
			// schema.removeChild(schema.getChild(DbPluginConstant.TREE_LEAF_LOADING));
			// 再描画
			// viewer.refresh(schema);

			Connection con = Transaction.getInstance(schema.getDbConfig()).getConnection();

			String owner = schema.getName();
			String[] sourceTypes = OracleSourceTypeSearcher.execute(con, owner);
			schema.setSourceType(sourceTypes);

			for (int i = 0; i < sourceTypes.length; i++) {
				String stype = sourceTypes[i];
				Folder folder = new Folder(stype);

				OracleSource source = new OracleSource();
				source.setName(DbPluginConstant.TREE_LEAF_LOADING);
				folder.addChild(source);
				schema.addChild(folder);
			}

			// 再描画
			viewer.refresh(schema);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}
}
