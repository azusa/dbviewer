/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.contentassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.sql.ISqlEditor;
import zigen.plugin.db.ui.editors.sql.SqlEditor;
import zigen.plugin.db.ui.internal.ContentAssistTable;
import zigen.plugin.db.ui.views.ColumnSearchAction;
import zigen.plugin.db.ui.views.SQLExecuteView;
import zigen.plugin.db.ui.views.internal.SQLWhitespaceDetector;

/**
 * ContentAssistUtil�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class ContentAssistUtil {

	/**
	 * �s���I�h�ŏI�����Ă��邩
	 * 
	 * @param document
	 * @param documentOffset
	 * @return
	 */
	public static boolean isAfterPeriod(IDocument document, int documentOffset) {
		try {
			char c = document.getChar(documentOffset - 1);
			if (c == '.') {
				return true;
			} else {
				return false;
			}
		} catch (BadLocationException e) {
			return false;
		}
	}

	// ���݂̃I�t�Z�b�g���璼�O�̋󔒂܂ł̕������Ԃ����\�b�h
	public static String getPreviousWord(IDocument document, int documentOffset) {

		if (isAfterPeriod(document, documentOffset)) {
			// �s���I�h�ŏI����Ă���ꍇ�́Aoffset���P�O�ɂ��Ă���
			--documentOffset;
		}

		// modify 2005-05-06 zigen
		SQLWhitespaceDetector whiteSpace = new SQLWhitespaceDetector();

		// ���O�̋󔒂܂ł̕�����ǉ����镶����o�b�t�@
		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				// ���O�̕������擾
				char c = document.getChar(--documentOffset);
				// �󔒂��s���I�h�ɓ��B
				// if (Character.isWhitespace(c) || c == '.') return
				// buf.reverse().toString();
				if (whiteSpace.isWhitespace(c) || c == '.')
					return buf.reverse().toString();
				// ������ǉ�
				buf.append(c);

			} catch (BadLocationException e) {
				// �h�L�������g�̐擪�܂œ��B�����ꍇ
				return buf.reverse().toString();
			}
		}
	}

	// ���݂̃I�t�Z�b�g���璼�O�̋󔒂܂ł̕������Ԃ����\�b�h
	public static String getPreviousWordGroup(IDocument document, int documentOffset) {
		if (isAfterPeriod(document, documentOffset)) {
			// �s���I�h�ŏI����Ă���ꍇ�́Aoffset���P�O�ɂ��Ă���
			--documentOffset;
		}

		// modify 2005-05-06 zigen
		SQLWhitespaceDetector whiteSpace = new SQLWhitespaceDetector();

		// ���O�̋󔒂܂ł̕�����ǉ����镶����o�b�t�@
		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				// ���O�̕������擾
				char c = document.getChar(--documentOffset);
				// �󔒂ɓ��B
				// if (Character.isWhitespace(c)) return
				// buf.reverse().toString();
				if (whiteSpace.isWhitespace(c))
					return buf.reverse().toString();

				// ������ǉ�
				buf.append(c);

			} catch (BadLocationException e) {
				// �h�L�������g�̐擪�܂œ��B�����ꍇ
				return buf.reverse().toString();
			}
		}
	}

	/**
	 * �������؂�o��
	 * 
	 * @param modifier
	 * @param length
	 * @return
	 */
	public static String subString(String modifier, int length) {
		if (modifier == null)
			return null;
		if (modifier.length() <= length) {
			return modifier;
		} else {
			return modifier.substring(0, length);
		}
	}

	public static IDBConfig getIDBConfig() {
		try {
			IWorkbenchPage page = DbPlugin.getDefault().getPage();
			IWorkbenchPart part = page.getActivePart();

			if (part instanceof SqlEditor) {
				SqlEditor editorPart = (SqlEditor) part;
				//IEditorInput input = editorPart.getEditorInput();
				/*
				Object obj = input.getAdapter(IResource.class);
				if (obj instanceof IResource) {
					IResource rs = (IResource) obj;
					IDBConfig config = ResourceUtil.getDBConfig(rs);
					if (config == null) {
						DbPlugin.getDefault().showWarningMessage(Messages.getString("ContentAssistUtil.0")); //$NON-NLS-1$
					} else {
						return config;
					}
				}*/
				if(editorPart instanceof ISqlEditor){
					return ((ISqlEditor)editorPart).getConfig();
				}else{
					return null;
				}
				
				
			} else if (part instanceof SQLExecuteView) {
				SQLExecuteView viewPart = (SQLExecuteView) part;
				return viewPart.getConfig();

			} else if (part instanceof TableViewEditorFor31) {
				TableViewEditorFor31 editor = (TableViewEditorFor31) part;
				return editor.getTableNode().getDbConfig();

			} else {
				throw new IllegalStateException("�\�肵�Ă��Ȃ������ł� part = " + part.getClass().getName()); //$NON-NLS-1$
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return null;
	}

	/**
	 * ContentAssistTable���쐬����(�R�[�h�⊮�p�j
	 * 
	 * @param tableName
	 * @return
	 */
	public static ContentAssistTable createContentAssistTable(String schemaName, String tableName) {
		Display display = Display.getDefault();
		IDBConfig config = getIDBConfig();

		ContentAssistTable contentTable;
		switch (config.getDbType()) {
		case DBType.DB_TYPE_ORACLE:
			// Oracle�ł͑啶���Ŕ��肷��
			contentTable = new ContentAssistTable(config, schemaName, tableName.toUpperCase());

			// �J���������͔񓯊������ɂ��Ȃ�(�ύX���Ȃ�����)
			display.syncExec((Runnable) new ColumnSearchAction(contentTable));
			

			break;
		default:
			// 1.���͂��������ŃJ����������
			contentTable = new ContentAssistTable(config, schemaName, tableName);
			// �J���������͔񓯊������ɂ��Ȃ�(�ύX���Ȃ�����)
			display.syncExec((Runnable) new ColumnSearchAction(contentTable));

			if (contentTable.getColumns() == null || contentTable.getColumns().length == 0) {
				// 2.�e�[�u������啶���ɕϊ����čČ���
				contentTable = new ContentAssistTable(config, schemaName, tableName.toUpperCase());
				display.syncExec((Runnable) new ColumnSearchAction(contentTable));

				if (contentTable.getColumns() == null || contentTable.getColumns().length == 0) {
					// 3.�e�[�u�������������ɕϊ����čی���
					contentTable = new ContentAssistTable(config, schemaName, tableName.toLowerCase());
					display.syncExec((Runnable) new ColumnSearchAction(contentTable));
				}
			}
			break;
		}

		// �J�����t����Table��Ԃ�
		return contentTable;
	}

}
