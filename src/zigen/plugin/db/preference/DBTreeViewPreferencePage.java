/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.preference;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;

/**
 * PreferencePageクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/03/09 ZIGEN create. [002] 2005/07/30 ZIGEN テーブルおよびカラムのコメント表示有無の追加
 * 
 */
public class DBTreeViewPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String DESC = Messages.getString("DBTreeViewPreferencePage.0"); //$NON-NLS-1$

	public static final String P_DISPLAY_TBL_COMMENT = "DBTreeViewPreferencePage.DisplayTableComment"; //$NON-NLS-1$

	public static final String P_DISPLAY_COL_COMMENT = "DBTreeViewPreferencePage.DisplayColumnComment"; //$NON-NLS-1$

	public void init(IWorkbench workbench) {}

	public DBTreeViewPreferencePage() {
		super(GRID);
		super.setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());
		super.setDescription(DESC);
	}

	public void createFieldEditors() {
		addField(new BooleanFieldEditor(P_DISPLAY_TBL_COMMENT, Messages.getString("DBTreeViewPreferencePage.3"), getFieldEditorParent())); //$NON-NLS-1$
		addField(new BooleanFieldEditor(P_DISPLAY_COL_COMMENT, Messages.getString("DBTreeViewPreferencePage.4"), getFieldEditorParent())); //$NON-NLS-1$
	}

}
