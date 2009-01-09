/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TabTokenizer;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableNewElement;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.QueryViewEditor2;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.AbstractJob;
import zigen.plugin.db.ui.jobs.OpenEditorJob;
import zigen.plugin.db.ui.jobs.RecordCountForTableJob;

/**
 * 
 * TableKeyAdapter�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class TableKeyAdapter implements KeyListener, TraverseListener {

	private TableKeyEventHandler handler;

	private ITableViewEditor editor;

	TableColumn[] columns;

	int columnCount;

	TableElement headerTableElement;

	Table table;

	/**
	 * �R���X�g���N�^
	 * 
	 */
	public TableKeyAdapter(TableKeyEventHandler handler) {
		this.handler = handler;
		this.editor = handler.editor;
		this.table = handler.table;

		this.headerTableElement = handler.getHeaderTableElement();
		if (this.headerTableElement != null) {
			this.columns = headerTableElement.getColumns();
			this.columnCount = columns.length;
		}

	}

	public void keyTraversed(TraverseEvent e) {
		int row = handler.getSelectedRow(); // �s�i�擪��0����)
		int col = handler.getSelectedCellEditorIndex(); // ���݂̃J����

		if (e.character == SWT.TAB) {
			if ((e.stateMask & SWT.SHIFT) != 0) {
				if (handler.validate(row, col)) {
					int prevCol = handler.getEditablePrevColumn(col);
					handler.editTableElement(row, prevCol);
				}
			} else {
				if (handler.validate(row, col)) {
					int nextCol = handler.getEditableNextColumn(col);
					handler.editTableElement(row, nextCol);
				}
			}
			e.doit = false; // TAB�͏��false
		}
	}

	/**
	 * Enter�C�x���g
	 * 
	 * @param e
	 * @throws Exception
	 */
	private void enterEvent(KeyEvent e) throws Exception {
		int row = handler.getSelectedRow(); // �s�i�擪��0����)
		int col = handler.getSelectedCellEditorIndex(); // ���݂̃J����

		TableElement element = (TableElement) handler.viewer.getElementAt(row);
		if (!handler.validate(row, col)) {
			e.doit = false;
		} else {
			if (handler.updateDataBase(element)) {
				editor.changeColumnColor();
				handler.selectRow(row);// �X�V��I����Ԃɂ���
				e.doit = true;
			} else {
				// handler.editTableElement(row, col); // �����ŕҏW����ƁA�G���[�ӏ��ł̕ҏW����������Ă��܂��B
				e.doit = false;
			}
		}

	}

	/**
	 * ���C�x���g
	 * 
	 * @param e
	 * @param text
	 * @throws Exception
	 */
	private void arrowEvent(KeyEvent e, Text text) throws Exception {
		int row = handler.getSelectedRow(); // �s�i�擪��0����)
		int col = handler.getSelectedCellEditorIndex(); // ���݂̃J����
		int prevCol = handler.getEditablePrevColumn(col);
		int nextCol = handler.getEditableNextColumn(col);
		int maxRow = handler.table.getItemCount();
		int maxCol = handler.table.getColumnCount();
		int caretPostion = text.getCaretPosition();
		int carCount = text.getCharCount();
		int selectionCount = text.getSelectionCount();
		TableElement element = (TableElement) handler.viewer.getElementAt(row);
		switch (e.keyCode) {
		case SWT.ARROW_UP:
			// if (row > 0) {
			if (row >= 0) {
				if (handler.validate(row, col)) { // validate���Ȃ���isModify���L���ɂȂ�Ȃ�
					if (element.isNew() && !element.isModify()) {
						handler.removeRecord(element);
						handler.editTableElement(row - 1, col);
					} else {
						if (handler.updateDataBase(element)) {
							editor.changeColumnColor();

							handler.editTableElement(row - 1, col);
						} else {
							handler.editTableElement(row, col);
						}
					}
				}
				e.doit = false;
			}
			break;
		case SWT.ARROW_DOWN:
			if (row < maxRow - 1) {
				if (handler.validate(row, col)) {
					if (handler.updateDataBase(element)) {
						handler.editTableElement(row + 1, col);
					} else {
						handler.editTableElement(row, col);
					}
				}
				e.doit = false;
			} else {
				if (handler.validate(row, col)) {
					if (!element.isNew()) {
						if (handler.updateDataBase(element)) {
							if (editor instanceof QueryViewEditor2) {
								;// �������Ȃ�
								// handler.editTableElement(0, col); // �擪��
							} else {
								handler.createNewRecord();
							}
						} else {
							handler.editTableElement(row, col);
						}
					} else {
						if (element.isModify()) {
							if (handler.updateDataBase(element)) {
								handler.createNewRecord();
							} else {
								handler.editTableElement(row, col);
							}
						}
					}
				}
				e.doit = false;
			}
			break;
		case SWT.ARROW_LEFT:
			if (col == 1) {
				if (selectionCount == carCount) {
					e.doit = false;

				} else {
					e.doit = true;
				}
			} else if (col > 1) {
				if ((selectionCount == 0 && caretPostion == 0) || selectionCount == carCount) {
					handler.editTableElement(row, prevCol);
					e.doit = false;
				}
			} else {
				e.doit = false;
			}
			break;
		case SWT.ARROW_RIGHT:
			if (col == maxCol - 1) {
				if (selectionCount == carCount) {
					e.doit = false;

				} else {
					e.doit = true;
				}
			} else if (col < maxCol - 1) {
				if (selectionCount == carCount || caretPostion == carCount) {
					handler.editTableElement(row, nextCol);
					e.doit = false;
				}
			} else {
				e.doit = false;
			}
			break;
		default:
			break;
		}

	}

	public void keyReleased(KeyEvent e) {
		;
	}

	public void keyPressed(KeyEvent e) {
		Text text = null;
		try {
			if (e.widget instanceof Text) {
				text = (Text) e.widget;

				// ENTER
				if (e.character == SWT.CR) {
					enterEvent(e);

					// F2
				} else if (e.keyCode == SWT.F2) {
					text.clearSelection();// �I���e�L�X�g����������

					// ���L�[�C�x���g
				} else if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT) {
					arrowEvent(e, text);
				}

				/*
				 * } else if (e.stateMask == SWT.CTRL && e.keyCode == 97) { // CTL+A �ŕ����I�� text.selectAll(); }
				 */

				// CTRL+V
				if (e.stateMask == SWT.CTRL && e.keyCode == 118) {
					if (createNewElement()) {
						e.doit = false;
					}
				}

			} else if (e.widget instanceof Button) {
				if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT) {
					arrowEvent(e);
				}


			}
		} catch (Exception e1) {
			DbPlugin.getDefault().showErrorDialog(e1);
		}
	}

	private boolean isHeaderData(TableColumn[] colums, String[] items) {
		boolean isHeader = true;
		if (colums.length != items.length)
			return false;

		for (int i = 0; i < colums.length; i++) {
			TableColumn col = colums[i];

			if (!col.getColumnName().equals(items[i])) {
				isHeader = false;
			}

		}
		return isHeader;

	}

	private boolean isRecordData(String target, int columnCount) {

		if (target == null || "".equals(target))return false; //$NON-NLS-1$

		StringTokenizer tokenizer = new StringTokenizer(target, DbPluginConstant.LINE_SEP);
		while (tokenizer.hasMoreTokens()) {
			StringBuffer sb = new StringBuffer();
			// \t\t�ŏI���ꍇ���l�����ĉ��s������
			sb.append(tokenizer.nextToken()).append(DbPluginConstant.LINE_SEP);
			TabTokenizer t = new TabTokenizer(sb.toString());
			if (columnCount != t.getTokenCount()) {
				return false;
			}
		}

		return true;

	}

	private String getCurrentClipboard() {
		String result = null;

		Clipboard cp = ClipboardUtils.getInstance();
		TextTransfer transfer = TextTransfer.getInstance();
		// RTFTransfer rtransfer = RTFTransfer.getInstance();
		String v[] = cp.getAvailableTypeNames();
		for (int i = 0; i < v.length; i++) {
			if (v[i].equals("CF_TEXT")) { //$NON-NLS-1$
				result = (String) cp.getContents(transfer);
				if (result != null)
					return result;

			}
		}
		return result;
	}

	public boolean canCreateNewElement() {
		String str = getCurrentClipboard();

		if (isRecordData(str, columnCount)) {
			if (str == null)
				return false;

			StringTokenizer tokenizer = new StringTokenizer(str, DbPluginConstant.LINE_SEP);

			// �R�s�[�����f�[�^�̃J�������S�ē������`�F�b�N����
			while (tokenizer.hasMoreTokens()) {
				String record = tokenizer.nextToken();
				String[] items = record.split("\t"); //$NON-NLS-1$
				if (!isHeaderData(columns, items)) {
					return true; // �\��t���\���
				}
			}
		}
		return false;
	}

	public boolean createNewElement() throws Exception {
		String str = getCurrentClipboard();

		if (isRecordData(str, columnCount)) {
			final StringTokenizer tokenizer = new StringTokenizer(str, DbPluginConstant.LINE_SEP);

			int position = editor.getRecordOffset() + table.getItemCount();
			PasteRecordJob job = new PasteRecordJob(tokenizer, position);
			job.setPriority(OpenEditorJob.LONG);
			job.setUser(true);
			job.schedule();

			return true;

		} else {
			return false;
		}
	}

	class CalcTotalCountAction implements Runnable {

		public void run() {
			TableElement[] elements = (TableElement[]) handler.viewer.getInput();
			int dispCnt = 0;
			for (int i = 1; i < elements.length; i++) { // �w�b�_�[���̏������߁Ai = 1
				if (!elements[i].isNew())
					dispCnt++;
			}

			editor.setTotalCount(dispCnt, -1); //$NON-NLS-1$
			IDBConfig config = editor.getDBConfig();
			ITable tableNode = editor.getTableNode();
			String condition = editor.getCondition();
			RecordCountForTableJob job2 = new RecordCountForTableJob(Transaction.getInstance(config), tableNode, condition, dispCnt, true);
			job2.setUser(false);
			job2.schedule();
		}

	}

	class UpdateTableAction implements Runnable {

		int recordNo;

		Object[] items;

		public UpdateTableAction(int recordNo, Object[] items) {
			this.recordNo = recordNo;
			this.items = items;
		}

		public void run() {
			// ThreadLocal�ŃG���[�_�C�A���O�̘A���\���𐧌䂷�邱��
			PasteRecordMonitor.begin();
			try {
				createNewRecord(recordNo, items);
			} catch (Exception e) {
				DbPlugin.log(e);
			} finally {
				PasteRecordMonitor.end();
			}
		}

		private void createNewRecord(int recordNo, Object[] items) throws Exception {
			ITable tbl = headerTableElement.getTable();
			int count = table.getItems().length;

			// �\�����R�[�h�����{�P��RecordNo��ݒ�
			// TableElement newElement = new TableNewElement(tbl, count + 1, headerTableElement.getColumns(), items, headerTableElement.getUniqueColumns());

			// System.out.println("create " + recordNo);
			TableElement newElement = new TableNewElement(tbl, recordNo, headerTableElement.getColumns(), items, headerTableElement.getUniqueColumns());

			// �������R�[�h���o�^����Ă���ꍇ�͔�X�V���(*)�ɂ���


			// ���R�[�h�̒ǉ�
			TableViewerManager.insert(handler.viewer, newElement);

			// �C�������J�����Ƃ��Đݒ肷��
			for (int i = 0; i < items.length; i++) {
				newElement.addMofiedColumn(i);
			}

			// �I���s��ύX���Ă��� 2007-09-06 ZIGEN
			table.setSelection(count);

			// �X�V���� 2007-06-19 ZIGEN
			handler.updateDataBase(newElement);

			// �P�J������(Row�����������́j��ҏW
			handler.editTableElement(count, 1); // �ŏI���R�[�h��1�J�����ڂ�ҏW

		}
	}

	class PasteRecordJob extends AbstractJob {

		StringTokenizer tokenizer;

		int position;

		public PasteRecordJob(StringTokenizer tokenizer, int position) {
			super("Paste Record Data");
			this.tokenizer = tokenizer;
			editor.setEnabled(false); // ��ҏW���[�h�ɂ���
			this.position = position;
		}

		protected IStatus run(IProgressMonitor monitor) {
			try {
				synchronized (table) {
					int totalWork = tokenizer.countTokens();
					monitor.beginTask("Now Pasting Records...", totalWork);
					int cnt = 0;
					int displayCount = 0;


					// �R�s�[�����f�[�^�̃J�������S�ē������`�F�b�N����
					while (tokenizer.hasMoreTokens()) {
						cnt++;
						displayCount++;
						if (monitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						} else {
							String record = tokenizer.nextToken();
							List itemList = new ArrayList();
							TabTokenizer t = new TabTokenizer(record);

							while (t.hasMoreElements()) {
								String token = convertToken(t.nextToken());
								itemList.add(token);
							}
							String[] items = (String[]) itemList.toArray(new String[0]);

							// 1�s�ڂ��w�b�_�[���ǂ����`�F�b�N����
							if (cnt == 1 && isHeaderData(columns, items)) {
								totalWork--; // �g�[�^����1���炷
								displayCount--; // �\���p�̃J�E���g���P���炷
								monitor.subTask((displayCount) + "/" + totalWork);
								monitor.worked(1);

							} else {
								monitor.subTask((displayCount) + "/" + totalWork);
								monitor.worked(1);

								Display.getDefault().syncExec(new UpdateTableAction(position++, items));
							}
						}

					}
				}
				monitor.done();
			} catch (Exception e) {
				showErrorMessage("The error occurred. ", e);

			} finally {
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						editor.changeColumnColor();// ���ׂ̔w�i�F��ύX
						editor.setEnabled(true);
					}
				});
				// �����̍Čv�Z�͂�����
				Display.getDefault().asyncExec(new CalcTotalCountAction());
			}

			return Status.OK_STATUS;
		}

		private String convertToken(String token) {
			if (token == null)
				token = ""; // 2008/01/30 ZIGEN NULL�̏ꍇ�́A""�ɕϊ�����

			if (token.startsWith("\"") && token.endsWith("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
				token = token.replaceAll("^\"|\"$", "");// �O���"���폜����
				// //$NON-NLS-1$
				// //$NON-NLS-2$
				token = token.replaceAll("\"\"", "\"");// "" ��
				// "
				// �ɕϊ�
				// //$NON-NLS-1$
				// //$NON-NLS-2$

			}
			return token;
		}
	}

	private void arrowEvent(KeyEvent e) throws Exception {
		int row = handler.getSelectedRow(); // �s�i�擪��0����)
		int col = handler.getSelectedCellEditorIndex(); // ���݂̃J����
		int prevCol = handler.getEditablePrevColumn(col);
		int nextCol = handler.getEditableNextColumn(col);
		int maxRow = handler.table.getItemCount();
		int maxCol = handler.table.getColumnCount();
		TableElement element = (TableElement) handler.viewer.getElementAt(row);
		switch (e.keyCode) {
		case SWT.ARROW_UP:
			// if (row > 0) {
			if (row >= 0) {
				if (handler.validate(row, col)) { // validate���Ȃ���isModify���L���ɂȂ�Ȃ�
					if (element.isNew() && !element.isModify()) {
						handler.removeRecord(element);
						handler.editTableElement(row - 1, col);
					} else {
						if (handler.updateDataBase(element)) {
							editor.changeColumnColor();

							handler.editTableElement(row - 1, col);
						} else {
							handler.editTableElement(row, col);
						}
					}
				}
				e.doit = false;
			}
			break;
		case SWT.ARROW_DOWN:
			if (row < maxRow - 1) {
				if (handler.validate(row, col)) {
					if (handler.updateDataBase(element)) {
						handler.editTableElement(row + 1, col);
					} else {
						handler.editTableElement(row, col);
					}
				}
				e.doit = false;
			} else {
				if (handler.validate(row, col)) {
					if (!element.isNew()) {
						if (handler.updateDataBase(element)) {
							if (editor instanceof QueryViewEditor2) {
								;// �������Ȃ�
								// handler.editTableElement(0, col); // �擪��
							} else {
								handler.createNewRecord();
							}
						} else {
							handler.editTableElement(row, col);
						}
					} else {
						if (element.isModify()) {
							if (handler.updateDataBase(element)) {
								handler.createNewRecord();
							} else {
								handler.editTableElement(row, col);
							}
						}
					}
				}
				e.doit = false;
			}
			break;
		case SWT.ARROW_LEFT:
			if (col == 1) {
				e.doit = true;
			} else if (col > 1) {
				handler.editTableElement(row, prevCol);
				e.doit = false;
			} else {
				e.doit = false;
			}
			break;
		case SWT.ARROW_RIGHT:
			if (col == maxCol - 1) {
				e.doit = true;
			} else if (col < maxCol - 1) {
				handler.editTableElement(row, nextCol);
				e.doit = false;
			} else {
				e.doit = false;
			}
			break;
		default:
			break;
		}

	}
}
