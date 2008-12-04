/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLTokenizer;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.preference.SQLFormatPreferencePage;

//public class SQLFormattingStrategy implements IFormattingStrategy, IFormattingStrategyExtension {
public class SQLFormattingStrategy implements IFormattingStrategy{


	int maxfomatCnt = 10;

	SQLSourceViewer viewer;

	IPreferenceStore ps;

	public SQLFormattingStrategy(ISourceViewer sourceViewer) {
		this.ps = DbPlugin.getDefault().getPreferenceStore();
		if (sourceViewer instanceof SQLSourceViewer) {
			this.viewer = (SQLSourceViewer) sourceViewer;
		}
	}

	public String format(String content, boolean isLineStart, String indentation, int[] positions) {
		if (viewer != null) {

			IDocument doc = viewer.getDocument();
			TextSelection ts = (TextSelection) viewer.getSelection();
			int firstPosition = calcFirstWordPosition(doc, ts);
			boolean selectionMode = (ts.getText().length() > 0) ? true : false;
			return innerformat(content, selectionMode, firstPosition);
			
			/*
			if(selectionMode){
				return innerformat(content, selectionMode, firstPosition);
			}else{
				return innerformat(doc.get(), selectionMode, firstPosition);
			}*/

		} else {
			return content;
		}
		// return SQLFormatter.format(content, type, onPatch, formattOffset);

	}

	// ここではこの処理は無効になります
	void setSelection(ISourceViewer viewer, ISelection selection, boolean reveal) {
		if (selection instanceof ITextSelection) {
			ITextSelection s = (ITextSelection) selection;
			viewer.setSelectedRange(s.getOffset(), s.getLength());

			if (reveal)
				viewer.revealRange(s.getOffset(), s.getLength());
		}
	}
	
	private int calcFirstWordPosition(IDocument doc, TextSelection selection) {
		int formattOffset = 0;
		try {
			if (selection.getText().length() > 0) {
				int sOffset = doc.getLineOffset(selection.getStartLine());
				int firstWordPosition = StringUtil.firstWordPosition(selection.getText());
				formattOffset = selection.getOffset() - sOffset + firstWordPosition;
			}else{
				;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return formattOffset;
	}

	private String innerformat(String content, boolean selectionMode, int firstPosition) {
		
		String demiliter = ps.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
		boolean onPatch = ps.getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
		int type = ps.getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);


		StringBuffer sb = new StringBuffer();
		SQLTokenizer st = new SQLTokenizer(content, demiliter);

		
		if (st.getTokenCount() <= maxfomatCnt) {
			while (st.hasMoreElements()) {
				String sql = (String) st.nextElement();
				if (sql != null && sql.length() > 0) {
					sb.append(SQLFormatter.format(sql, type, onPatch, firstPosition));

					if (!selectionMode) {
						if ("/".equals(demiliter)) { //$NON-NLS-1$
							sb.append(DbPluginConstant.LINE_SEP);
						}
						sb.append(demiliter);
						sb.append(DbPluginConstant.LINE_SEP);
					}else{
						// 選択モードの場合は改行を追加
						//sb.append(DbPluginConstant.LINE_SEP);
					}
				}
			}

		} else {
			return content; // MAX以上のSQL文がある場合はフォーマットしない
		}

		return sb.toString();
	}

	public void formatterStarts(String initialIndentation) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public void formatterStops() {
		// TODO 自動生成されたメソッド・スタブ

	}

	/*
	public void format() {
		// TODO 自動生成されたメソッド・スタブ
		if (viewer != null) {

			IDocument doc = viewer.getDocument();
			TextSelection ts = (TextSelection) viewer.getSelection();

			int firstPosition = calcFirstWordPosition(doc, ts);
			
			boolean selectionMode = (ts.getText().length() > 0) ? true : false;
			
			String formatted = null;
			
			if(selectionMode){
				formatted = innerformat(ts.getText(), selectionMode, firstPosition);
			}else{
				String all = doc.get();
				ts = new TextSelection(doc, 0, all.length());
				setSelection(viewer, ts, true);
				formatted = innerformat(ts.getText(), selectionMode, firstPosition);
			}
			
            try {
				MultiTextEdit multiTextEdit = new MultiTextEdit();
				multiTextEdit.addChild(new ReplaceEdit(ts.getOffset(), ts.getLength(), formatted));
				multiTextEdit.apply(doc);
			} catch (MalformedTreeException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			
		}		
	}

	public void formatterStarts(IFormattingContext context) {
		// TODO 自動生成されたメソッド・スタブ

	}*/
}
