/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.util;

import org.eclipse.swt.custom.StyledText;

import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.views.internal.ColorManager;

public class StyledTextUtil {

	public static void changeColor(ColorManager manager, StyledText text) {

		if (text != null) {
			// 背景色
			text.setBackground(manager.getColor(SQLEditorPreferencePage.P_COLOR_BACK));

			// 選択の前景色
			text.setSelectionForeground(manager.getColor(SQLEditorPreferencePage.P_COLOR_SELECT_FORE));

			// 選択の背景色
			text.setSelectionBackground(manager.getColor(SQLEditorPreferencePage.P_COLOR_SELECT_BACK));

		}
	}

}
