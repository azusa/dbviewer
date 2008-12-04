/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.contentassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.sql.ISqlEditor;
import zigen.plugin.db.ui.editors.sql.SqlEditor;
import zigen.plugin.db.ui.internal.ContentAssistTable;
import zigen.plugin.db.ui.views.ColumnSearchAction;
import zigen.plugin.db.ui.views.SQLExecuteView;
import zigen.plugin.db.ui.views.internal.SQLWhitespaceDetector;

/**
 * ContentAssistUtilクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class ContentAssistUtil {

	/**
	 * ピリオドで終了しているか
	 * 
	 * @param document
	 * @param documentOffset
	 * @return
	 */
	public static boolean isAfterPeriod(IDocument document, int documentOffset) {
		try {
			char c = document.getChar(documentOffset - 1);
			if (c == '.') {
				return true;
			} else {
				return false;
			}
		} catch (BadLocationException e) {
			return false;
		}
	}

	// 現在のオフセットから直前の空白までの文字列を返すメソッド
	public static String getPreviousWord(IDocument document, int documentOffset) {

		if (isAfterPeriod(document, documentOffset)) {
			// ピリオドで終わっている場合は、offsetを１つ前にしておく
			--documentOffset;
		}

		// modify 2005-05-06 zigen
		SQLWhitespaceDetector whiteSpace = new SQLWhitespaceDetector();

		// 直前の空白までの文字を追加する文字列バッファ
		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				// 直前の文字を取得
				char c = document.getChar(--documentOffset);
				// 空白かピリオドに到達
				// if (Character.isWhitespace(c) || c == '.') return
				// buf.reverse().toString();
				if (whiteSpace.isWhitespace(c) || c == '.')
					return buf.reverse().toString();
				// 文字を追加
				buf.append(c);

			} catch (BadLocationException e) {
				// ドキュメントの先頭まで到達した場合
				return buf.reverse().toString();
			}
		}
	}

	// 現在のオフセットから直前の空白までの文字列を返すメソッド
	public static String getPreviousWordGroup(IDocument document, int documentOffset) {
		if (isAfterPeriod(document, documentOffset)) {
			// ピリオドで終わっている場合は、offsetを１つ前にしておく
			--documentOffset;
		}

		// modify 2005-05-06 zigen
		SQLWhitespaceDetector whiteSpace = new SQLWhitespaceDetector();

		// 直前の空白までの文字を追加する文字列バッファ
		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				// 直前の文字を取得
				char c = document.getChar(--documentOffset);
				// 空白に到達
				// if (Character.isWhitespace(c)) return
				// buf.reverse().toString();
				if (whiteSpace.isWhitespace(c))
					return buf.reverse().toString();

				// 文字を追加
				buf.append(c);

			} catch (BadLocationException e) {
				// ドキュメントの先頭まで到達した場合
				return buf.reverse().toString();
			}
		}
	}

	/**
	 * 文字列を切り出す
	 * 
	 * @param modifier
	 * @param length
	 * @return
	 */
	public static String subString(String modifier, int length) {
		if (modifier == null)
			return null;
		if (modifier.length() <= length) {
			return modifier;
		} else {
			return modifier.substring(0, length);
		}
	}

	public static IDBConfig getIDBConfig() {
		try {
			IWorkbenchPage page = DbPlugin.getDefault().getPage();
			IWorkbenchPart part = page.getActivePart();

			if (part instanceof SqlEditor) {
				SqlEditor editorPart = (SqlEditor) part;
				//IEditorInput input = editorPart.getEditorInput();
				/*
				Object obj = input.getAdapter(IResource.class);
				if (obj instanceof IResource) {
					IResource rs = (IResource) obj;
					IDBConfig config = ResourceUtil.getDBConfig(rs);
					if (config == null) {
						DbPlugin.getDefault().showWarningMessage(Messages.getString("ContentAssistUtil.0")); //$NON-NLS-1$
					} else {
						return config;
					}
				}*/
				if(editorPart instanceof ISqlEditor){
					return ((ISqlEditor)editorPart).getConfig();
				}else{
					return null;
				}
				
				
			} else if (part instanceof SQLExecuteView) {
				SQLExecuteView viewPart = (SQLExecuteView) part;
				return viewPart.getConfig();

			} else if (part instanceof TableViewEditorFor31) {
				TableViewEditorFor31 editor = (TableViewEditorFor31) part;
				return editor.getTableNode().getDbConfig();

			} else {
				throw new IllegalStateException("予定していない処理です part = " + part.getClass().getName()); //$NON-NLS-1$
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return null;
	}

	/**
	 * ContentAssistTableを作成する(コード補完用）
	 * 
	 * @param tableName
	 * @return
	 */
	public static ContentAssistTable createContentAssistTable(String schemaName, String tableName) {
		Display display = Display.getDefault();
		IDBConfig config = getIDBConfig();

		ContentAssistTable contentTable;
		switch (config.getDbType()) {
		case DBType.DB_TYPE_ORACLE:
			// Oracleでは大文字で判定する
			contentTable = new ContentAssistTable(config, schemaName, tableName.toUpperCase());

			// カラム検索は非同期処理にしない(変更しないこと)
			display.syncExec((Runnable) new ColumnSearchAction(contentTable));
			

			break;
		default:
			// 1.入力した文字でカラムを検索
			contentTable = new ContentAssistTable(config, schemaName, tableName);
			// カラム検索は非同期処理にしない(変更しないこと)
			display.syncExec((Runnable) new ColumnSearchAction(contentTable));

			if (contentTable.getColumns() == null || contentTable.getColumns().length == 0) {
				// 2.テーブル名を大文字に変換して再検索
				contentTable = new ContentAssistTable(config, schemaName, tableName.toUpperCase());
				display.syncExec((Runnable) new ColumnSearchAction(contentTable));

				if (contentTable.getColumns() == null || contentTable.getColumns().length == 0) {
					// 3.テーブル名を小文字に変換して際検索
					contentTable = new ContentAssistTable(config, schemaName, tableName.toLowerCase());
					display.syncExec((Runnable) new ColumnSearchAction(contentTable));
				}
			}
			break;
		}

		// カラム付きのTableを返す
		return contentTable;
	}

}
