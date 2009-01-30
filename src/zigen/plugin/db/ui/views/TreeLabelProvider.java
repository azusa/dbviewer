/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.diff.DDLDiff;
import zigen.plugin.db.diff.IDDLDiff;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.internal.HistoryFolder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleFunction;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;

/**
 * ViewLabelProviderクラス.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 *
 */
public class TreeLabelProvider extends LabelProvider {

	ImageCacher ic = ImageCacher.getInstance();

	/**
	 * 表示するラベルの取得
	 */
	public String getText(Object obj) {
		if (obj instanceof Column) {
			Column column = (Column) obj;
			// 読み込み中の場合があるのでいきなり、
			// getColumnLabel()を実行しない
			// return column.getColumnLabel(); // << 呼ばない

			// TableColumnのタイプを判定して表示
			TableColumn _column = column.getColumn();
			if (_column.getTypeName() != null) {
				return ((Column) obj).getColumnLabel();
			} else {
				return obj.toString();
			}

			// <!-- [002] 修正 ZIGEN 2005/07/30
		} else if (obj instanceof ITable) {
			ITable table = (ITable) obj;

			// テーブルのコメントON
			if (DbPlugin.getDefault().getPreferenceStore().getBoolean(DBTreeViewPreferencePage.P_DISPLAY_TBL_COMMENT)) {
				return table.getLabel();

			} else {
				return table.getName();
			}

			// [002] 修正 ZIGEN 2005/07/30 -->

		} else if (obj instanceof Schema) {
			Schema schema = (Schema) obj;
			return schema.getName();

		} else if (obj instanceof History) {
			History history = (History) obj;
			return history.getName();

		} else if (obj instanceof DDLDiff) {
			IDDLDiff diff = (IDDLDiff) obj;
			return diff.getName();

		} else {
			return obj.toString();
		}
	}

	/**
	 * 表示する画像の取得
	 */
	public Image getImage(Object obj) {

		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof Root) {
			return ic.getImage(DbPlugin.IMG_CODE_DB);

		} else if (obj instanceof DataBase) {
			DataBase db = (DataBase) obj;
			if (db.isConnected()) {
				return ic.getImage(DbPlugin.IMG_CODE_CONNECTED_DB);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_DB);
				// return DbPlugin.getDefault().getImage(DbPlugin.IMG_CODE_DB);
			}

		} else if (obj instanceof Schema) {
			return ic.getImage(DbPlugin.IMG_CODE_SCHEMA);

		} else if (obj instanceof BookmarkRoot) {
			return ic.getImage(DbPlugin.IMG_CODE_BOOKMARK);

		} else if (obj instanceof Synonym) {
			Synonym synonym = (Synonym) obj;
			if (synonym.isEnabled()) {
				return ic.getImage(DbPlugin.IMG_CODE_SYNONYM);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_DISABLED_SYNONYM);
			}
		} else if (obj instanceof View) {
			return ic.getImage(DbPlugin.IMG_CODE_VIEW);

		} else if (obj instanceof Bookmark) {
			Bookmark bm = (Bookmark) obj;
			if (Bookmark.TYPE_TABLE == bm.getType()) {
				return ic.getImage(DbPlugin.IMG_CODE_TABLE);

			} else if (Bookmark.TYPE_VIEW == bm.getType()) {
				return ic.getImage(DbPlugin.IMG_CODE_VIEW);

			} else if (Bookmark.TYPE_SYNONYM == bm.getType()) {
				return ic.getImage(DbPlugin.IMG_CODE_SYNONYM);

			} else {
				imageKey = ISharedImages.IMG_OBJ_FILE;
			}
		} else if (obj instanceof ITable) {
			ITable table = (ITable) obj;
			if (table.isEnabled()) {
				return ic.getImage(DbPlugin.IMG_CODE_TABLE);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_DISABLED_TABLE);
			}
		} else if (obj instanceof Column) {
			Column col = (Column) obj;
			if (col.hasPrimaryKey()) {
				return ic.getImage(DbPlugin.IMG_CODE_PK_COLUMN);
			} else {
				if (col.isNotNull()) {
					return ic.getImage(DbPlugin.IMG_CODE_NOTNULL_COLUMN);
				} else {
					return ic.getImage(DbPlugin.IMG_CODE_COLUMN);
				}
			}
		} else if (obj instanceof History) {
			return ic.getImage(DbPlugin.IMG_CODE_CLOCK);

		} else if (obj instanceof HistoryFolder) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER; // Folderアイコン

		} else if (obj instanceof DDLDiff) {
			return ic.getImage(DbPlugin.IMG_CODE_TABLE);

		} else if (obj instanceof OracleFunction) {
			OracleFunction function = (OracleFunction)obj;
			if(function.hasError()){
				return ic.getImage(DbPlugin.IMG_CODE_FUNCTION_ERR);
			}else{
				return ic.getImage(DbPlugin.IMG_CODE_FUNCTION);
			}

		} else if (obj instanceof OracleSource) {
			OracleSource source = (OracleSource)obj;
			if(source.hasError()){
				return ic.getImage(DbPlugin.IMG_CODE_FILE_ERR);// Folder エラーアイコン
			}else{
				imageKey = ISharedImages.IMG_OBJ_FILE; // Folderアイコン
			}


		} else if (obj instanceof OracleSequence) {
			return ic.getImage(DbPlugin.IMG_CODE_SEQUENCE);


		} else if(obj instanceof Folder){
			Folder folder = (Folder)obj;
			if(folder.hasError()){
				return ic.getImage(DbPlugin.IMG_CODE_FOLDER_ERR);// Folder エラーアイコン
			}else{
				imageKey = ISharedImages.IMG_OBJ_FOLDER; // Folderアイコン
			}
		} else if (obj instanceof TreeNode) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER; // Folderアイコン
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);

	}
}
