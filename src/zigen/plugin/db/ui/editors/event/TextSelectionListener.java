/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.event;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Text;

public class TextSelectionListener implements FocusListener {

	public void focusGained(FocusEvent e) {
		if (e.widget instanceof Text) {
			Text text = (Text) e.widget;
			text.selectAll();
		}
	}

	public void focusLost(FocusEvent e) {
		;
	}
}
