/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.StringTokenizer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.StringUtil;

/**
 * CopySQLForStringBuilderContextActionクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/08/28 ZIGEN create.
 * 
 * 
 */
public class CopySQLForStringBuilderContextAction implements IViewActionDelegate {
	private final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	private ISelection selection;

	private IViewPart viewPart;

	public void init(IViewPart view) {
		// TODO 自動生成されたメソッド・スタブ
		this.viewPart = view;

	}

	public void run(IAction action) {
		// TODO 自動生成されたメソッド・スタブ
		StringBuffer sb = new StringBuffer();
		Clipboard clipboard = ClipboardUtils.getInstance();
		TextTransfer text_transfer = TextTransfer.getInstance();

		if (selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) selection;
			String text = textSelection.getText();

			if (text.length() > 0) {
				sb.append("StringBuilder sb = new StringBuilder();"); //$NON-NLS-1$
				sb.append(LINE_SEP);

				StringTokenizer st = new StringTokenizer(text, LINE_SEP);
				while (st.hasMoreTokens()) {
					String token = st.nextToken();

					sb.append("sb.Append(\"" + StringUtil.encodeDoubleQuotation(token) + "\");" + LINE_SEP); //$NON-NLS-1$ //$NON-NLS-2$
				}

				clipboard.setContents(new Object[] {
					sb.toString()
				}, new Transfer[] {
					text_transfer
				});
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO 自動生成されたメソッド・スタブ
		this.selection = selection;

	}

}
