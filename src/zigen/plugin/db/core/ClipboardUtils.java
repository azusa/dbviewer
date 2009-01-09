/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

/**
 * ClipboardUtils.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/12/04 ZIGEN create.
 * 
 */
public class ClipboardUtils {

	private static Clipboard clipboard;

	public static Clipboard getInstance() {
		if (clipboard == null) {
			clipboard = new Clipboard(Display.getCurrent());
		}
		return clipboard;
	}
}
