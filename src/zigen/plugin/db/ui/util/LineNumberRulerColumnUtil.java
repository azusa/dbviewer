/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.util;

import org.eclipse.jface.text.source.LineNumberRulerColumn;

import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.views.internal.ColorManager;

public class LineNumberRulerColumnUtil {

	public static void changeColor(ColorManager manager,
			LineNumberRulerColumn rulerColumn) {

		if (rulerColumn != null) {
			// 背景色
			rulerColumn.setBackground(manager
					.getColor(SQLEditorPreferencePage.P_COLOR_BACK));

			rulerColumn.setForeground(manager
					.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT));
		}
	}
}
