/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.parser.util.CurrentSql;
import zigen.plugin.db.preference.SQLEditorPreferencePage;


public class ExecuteCurrentSQLForEditorAction extends AbstractExecuteSQLForEditorAction {

	public ExecuteCurrentSQLForEditorAction(SqlEditor2 editor) {
		super(editor);
	}

	protected String targetSql(IDocument doc) {
		int offset = editor.getOffset();
		CurrentSql cs = createCurrentSql(doc, offset);
		return cs.getSql(); // �J�[�\���ʒu�̂���SQL�����s
	}

	private CurrentSql createCurrentSql(IDocument doc, int offset) {
		String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
		return new CurrentSql(doc, offset, demiliter);

	}
}
