/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.preference;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;

/**
 * PreferencePageクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class SQLEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String DESC = Messages.getString("SQLEditorPreferencePage.0"); //$NON-NLS-1$

	public static final String P_COLOR_KEYWORD = "SQLEditorPreferencePage.ColorKeyword"; //$NON-NLS-1$

	public static final String P_COLOR_FUNCTION = "SQLEditorPreferencePage.ColorFunction"; //$NON-NLS-1$

	public static final String P_COLOR_COMMENT = "SQLEditorPreferencePage.ColorComment"; //$NON-NLS-1$

	public static final String P_COLOR_STRING = "SQLEditorPreferencePage.ColorString"; //$NON-NLS-1$

	public static final String P_COLOR_DEFAULT = "SQLEditorPreferencePage.ColorDefailt"; //$NON-NLS-1$

	public static final String P_COLOR_BACK = "SQLEditorPreferencePage.ColorBackGround"; //$NON-NLS-1$

	public static final String P_COLOR_SELECT_FORE = "SQLEditorPreferencePage.ColorSelectFore"; //$NON-NLS-1$

	public static final String P_COLOR_SELECT_BACK = "SQLEditorPreferencePage.ColorSelectBack"; //$NON-NLS-1$

	public static final String P_COLOR_MATCHING = "SQLEditorPreferencePage.ColorMatching"; //$NON-NLS-1$

	public static final String P_COLOR_FIND_SCOPE = "SQLEditorPreferencePage.ColorFindScope"; //$NON-NLS-1$

	public static final String P_COLOR_CURSOR_LINE = "SQLEditorPreferencePage.CursorLine"; //$NON-NLS-1$

	// public static final String P_LINE_DEMILITER =
	// "SQLEditorPreferencePage.LineDemiliter";

	public static final String P_SQL_DEMILITER = "SQLEditorPreferencePage.SqlDemiliter"; //$NON-NLS-1$

	// public static final String P_FORMAT_PATCH = "SQLEditorPreferencePage.FormatPatch"; //$NON-NLS-1$

	public void init(IWorkbench workbench) {}

	public SQLEditorPreferencePage() {
		super(GRID);
		super.setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());
		super.setDescription(DESC);
	}

	// 改行コードの扱い
	private String[][] radioLine = new String[][] {new String[] {"CR+LF (Windows)", "\r\n" //$NON-NLS-1$ //$NON-NLS-2$
	}, new String[] {"LF (Unix)", "\n" //$NON-NLS-1$ //$NON-NLS-2$
	}, new String[] {"CR (Mac)", "\r" //$NON-NLS-1$ //$NON-NLS-2$
	},};

	// SQL区切り
	private String[][] radioSql = new String[][] {new String[] {Messages.getString("SQLEditorPreferencePage.18"), "/" //$NON-NLS-1$ //$NON-NLS-2$
	}, new String[] {Messages.getString("SQLEditorPreferencePage.20"), ";" //$NON-NLS-1$ //$NON-NLS-2$
	}};

	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		addOption1(parent);
		addOption2(parent);
		// addOption3(parent);
	}

	private void addOption1(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("SQLEditorPreferencePage.22")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		addField(new ColorFieldEditor(P_COLOR_KEYWORD, Messages.getString("SQLEditorPreferencePage.23"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_FUNCTION, Messages.getString("SQLEditorPreferencePage.38"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_STRING, Messages.getString("SQLEditorPreferencePage.24"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_COMMENT, Messages.getString("SQLEditorPreferencePage.25"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_DEFAULT, Messages.getString("SQLEditorPreferencePage.26"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_BACK, Messages.getString("SQLEditorPreferencePage.27"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_SELECT_FORE, Messages.getString("SQLEditorPreferencePage.28"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_SELECT_BACK, Messages.getString("SQLEditorPreferencePage.29"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_MATCHING, Messages.getString("SQLEditorPreferencePage.30"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_FIND_SCOPE, Messages.getString("SQLEditorPreferencePage.34"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_CURSOR_LINE, Messages.getString("SQLEditorPreferencePage.37"), grp)); //$NON-NLS-1$

	}

	private void addOption2(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("SQLEditorPreferencePage.33")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		// 改行コードの設定
		// addField(new RadioGroupFieldEditor(P_LINE_DEMILITER,
		// "SQL実行時の改行コード(&D)", radioLine.length, radioLine, grp));

		// SQL区切りの設定
		addField(new RadioGroupFieldEditor(P_SQL_DEMILITER, Messages.getString("SQLEditorPreferencePage.32"), radioSql.length, radioSql, grp)); //$NON-NLS-1$

	}

	// private void addOption3(Composite parent) {
	// Group group = new Group(parent, SWT.NONE);
	// FillLayout layout = new FillLayout(SWT.HORIZONTAL);
	// layout.marginHeight = 4;
	// layout.marginWidth = 4;
	// group.setLayout(layout);
	// group.setText(Messages.getString("SQLEditorPreferencePage.31")); //$NON-NLS-1$
	// GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	// gd.horizontalSpan = 2;
	// group.setLayoutData(gd);
	// Composite grp = new Composite(group, SWT.NONE);
	// grp.setLayout(new GridLayout(2, false));
	//
	// StringBuffer sb = new StringBuffer();
	// sb.append(Messages.getString("SQLEditorPreferencePage.36")); //$NON-NLS-1$
	// addField(new BooleanFieldEditor(P_FORMAT_PATCH, sb.toString(), grp));
	//
	// Label label = new Label(grp, SWT.NONE);
	// label.setText(Messages.getString("SQLEditorPreferencePage.35")); //$NON-NLS-1$
	// }

}
