/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

public class SQLAutoEditStrategy implements IAutoEditStrategy {

	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		if (command.text.equals("\"")) { //$NON-NLS-1$
			addText(command, "\""); //$NON-NLS-1$
		} else if (command.text.equals("'")) { //$NON-NLS-1$
			addText(command, "'"); //$NON-NLS-1$
		} else if (command.text.equals("(")) { //$NON-NLS-1$
			addText(command, ")"); //$NON-NLS-1$
		}
	}

	private void addText(DocumentCommand command, String addString) {
		command.text = command.text + addString;
		command.caretOffset = command.offset + 1; // �蓮��caret���P�ړ�����
		command.shiftsCaret = false; // �����I��caret���V�t�g���Ȃ�
		command.doit = false;
	}

}
