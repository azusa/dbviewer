/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.event;

import java.sql.Connection;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableElementSearcher;
import zigen.plugin.db.core.TableNewElement;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.core.rule.AbstractValidatorFactory;
import zigen.plugin.db.core.rule.IValidatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.InsertRecordAction;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.TextCellEditor;
import zigen.plugin.db.ui.editors.exceptions.UpdateException;
import zigen.plugin.db.ui.editors.exceptions.ZeroUpdateException;
import zigen.plugin.db.ui.editors.internal.RecordUpdateThread;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

/**
 * 
 * TableKeyEventHandler�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class TableKeyEventHandler {

	protected ITableViewEditor editor;

	protected TableViewer viewer;

	protected Table table;

	protected IDBConfig config;

	/**
	 * �R���X�g���N�^
	 * 
	 */
	public TableKeyEventHandler(ITableViewEditor editor) {
		this.editor = editor;
		this.viewer = editor.getViewer();
		this.table = editor.getViewer().getTable();
		this.config = editor.getDBConfig();
	}

	/**
	 * �I�����Ă���s�ԍ����擾
	 * 
	 * @return
	 */
	public int getSelectedRow() {
		return table.getSelectionIndex(); // �s�i�擪��0����)
	}

	public void selectRow(int index) {
		table.select(index);
	}

	/**
	 * �I�����Ă����ԍ����擾
	 * 
	 * @return
	 */
	public int getSelectedCellEditorIndex() {
		int defaultIndex = 0;
		CellEditor[] editors = viewer.getCellEditors();
		if (editors == null)
			return -1;
		for (int i = 0; i < editors.length; i++) {
			if (editors[i] != null && editors[i].isActivated()) {
				return i;
			}
		}
		return defaultIndex;
	}

	/**
	 * �ҏW�\�Ȏ��̗�ԍ����擾
	 * 
	 * @param cuurentCol
	 * @return
	 */
	public int getEditableNextColumn(int cuurentCol) {
		ICellModifier modifier = viewer.getCellModifier();
		int nextCol = (cuurentCol < table.getColumnCount() - 1) ? cuurentCol + 1 : 1;
		if (modifier.canModify(getHeaderTableElement(), String.valueOf(nextCol))) {
			return nextCol;
		} else {
			return getEditableNextColumn(nextCol);
		}
	}

	/**
	 * �ҏW�\�ȑO�̗�ԍ����擾
	 * 
	 * @param CurrentCol
	 * @return
	 */
	public int getEditablePrevColumn(int CurrentCol) {
		ICellModifier modifier = viewer.getCellModifier();
		int nextCol = (CurrentCol == 1) ? table.getColumnCount() - 1 : CurrentCol - 1;
		if (modifier.canModify(getHeaderTableElement(), String.valueOf(nextCol))) {
			return nextCol;
		} else {
			return getEditablePrevColumn(nextCol);
		}
	}

	/**
	 * �e�[�u���̃w�b�_�v�f���擾
	 * 
	 * @return
	 */
	public TableElement getHeaderTableElement() {
		Object obj = viewer.getInput();
		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) obj;
			if (elements.length > 0) {
				return elements[0];
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * �w�肵���J������ҏW���[�h�ɂ���
	 * 
	 * @param rowIndex
	 *            �s�ԍ�
	 * @param columnIndex
	 *            ��ԍ�
	 */
	public void editTableElement(int rowIndex, int columnIndex) {
		Object element = viewer.getElementAt(rowIndex);
		if (element != null) {

			// ���܂����X�N���[�����ĕҏW���[�h�ɂȂ�Ȃ�
			// viewer.editElement(element, columnIndex);
			// table.showColumn(table.getColumn(columnIndex));

			// ���ԓ��ꂩ���邱�ƂŁA���܂��X�N���[���{�ҏW���[�h�ɂȂ�
			if(columnIndex == 1){
				table.showColumn(table.getColumn(0)); // �s�ԍ���������悤�ɂ���
			}else{
				table.showColumn(table.getColumn(columnIndex));
			}
			viewer.editElement(element, columnIndex);

		}
	}

	/**
	 * ��ʏ�̃J������������X�V���� ���f�[�^�x�[�X�ւ̍X�V�͍s��Ȃ��i�����܂ł������ڏ�j
	 * 
	 */
	public void updateColumn(TableElement element, int col, Object newValue) {
		
		// TableElement�̒l���X�V���A�ҏW�ς݃��X�g�ɒǉ�
		element.updateItems(col - 1, newValue);// row��-1����
		// �e�[�u���E�r���[�����X�V
		viewer.update(element, null);
		columnsPack();

	}


	public void setMessage(String msg) {
		DbPlugin.getDefault().showWarningMessage(msg);
		// DbPlugin.getActiveTableViewEditor().setStatusErrorMessage(msg);
	}

	/**
	 * Row�ԍ��̃J������Pack
	 */
	private void columnsPack() {
/*
		table.setVisible(false);

		TableColumn[] cols = table.getColumns();
		cols[0].pack();

		table.setVisible(true);
*/
	}

	

	/**
	 * ���̓`�F�b�N����эX�V�f�[�^��TableElement�ɓo�^����
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean validate(int row, int col) {
		TableElement element = (TableElement) viewer.getElementAt(row);
		Object newValue = null;
		CellEditor editor = viewer.getCellEditors()[col];
		if (editor == null)
			throw new IllegalStateException("CellEditor���ݒ肳��Ă��܂���"); //$NON-NLS-1$
		int columnIndex = -1;
		if (editor instanceof TextCellEditor) {
			TextCellEditor tce = (TextCellEditor)editor;
			newValue = tce.getInputValue();
			columnIndex = tce.getColumnIndex();
			
		}
		Object oldValue = element.getItems()[col - 1];// row�� index-1 �����J�����ԍ�
		if (!oldValue.equals(newValue)) {
			String msg = editor.getErrorMessage();
			if (msg == null) {
				// �����ڍX�V
				updateColumn(element, col, newValue);
				return true; // ����

			} else {
				// �ҏW���L�����Z��(modify���Ă΂�Ȃ��悤�ɂ���)
				viewer.cancelEditing();
				// �����ڂ��X�V
				updateColumn(element, col, newValue);
				// �G���[���b�Z�[�W�\��
				setMessage(msg); //$NON-NLS-1$
				// ���͌��̃J������ҏW��Ԃɂ���
				editTableElement(row, col);
				return false; // ���s
			}

		}
		return true;

	}
	
	public boolean validateAll() {
		// log.debug("validateAll");
		try {
			int row = getSelectedRow();

			if (row == -1) {
				;// CTRL+C�̏ꍇ�͂�����
			} else {
				TableElement element = (TableElement) viewer.getElementAt(row);
				IDBConfig config = element.getTable().getDbConfig();
				IValidatorFactory factory = AbstractValidatorFactory.getFactory(config);
				zigen.plugin.db.core.TableColumn[] columns = element.getColumns();
				String msg = null;
				for (int col = 0; col < columns.length; col++) {
//					
//					if(!validate(row, col+1)){
//						return false;
//					}
						
						
					
					Object[] items = element.getItems();
					msg = factory.validate(columns[col], items[col]);
					if (msg != null) {
						viewer.cancelEditing();
						setMessage(msg);
						editTableElement(row, col + 1); // row���ǉ� (�����ł� col+1)
						return false;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return true;
	}

	public boolean updateDataBase(TableElement element) throws Exception {
		try {
			PasteRecordMonitor.isPasting();
			
			//TimeWatcher tw = new TimeWatcher();
			//tw.start();
			Display display = Display.getDefault();
			// �f�[�^�x�[�X�X�V�t���O��OFF�ɂ���
			element.setUpdatedDataBase(false);
			if (element.isNew()) {
				if (validateAll()) {
					// INSERT�̏ꍇ�̂ݓ���f�[�^�����݂���ꍇ�͓o�^�ł��Ȃ��悤�ɂ���
					if(hasSameRecord(element)){
						return false;
					}else{
						display.syncExec(new RecordUpdateThread(editor, element));
					}		
					// Row�ԍ��̃J��������Pack
					columnsPack();
				} else {
					return false;
				}

			} else if (element.isModify()) {
				// log.debug("UPDATE���s");
				if (validateAll()) {
					display.syncExec(new RecordUpdateThread(editor, element));
				} else {
					return false;
				}
			} else {
				;
			}
			// �f�[�^�x�[�X�X�V��A�R�s�[�\�ɂ��邽�߂ɑI����Ԃɂ��AListener�֒ʒm����
			viewer.getTable().select(getSelectedRow());	// �C���s�Ƀt�H�[�J�X�𓖂Ă�
			viewer.getControl().notifyListeners(SWT.Selection, null);

			//tw.stop();
			
			// �f�[�^�x�[�X�X�V�t���O��ON�ɂ���
			element.setUpdatedDataBase(true);
			
			return true;	// �X�V���Ă��Ȃ��ꍇ�ł��G���[�łȂ����True��Ԃ�

		} catch (ZeroUpdateException e) {
			return false;
		} catch (UpdateException e) {
			return false;
		}

	}

	// �����f�[�^���o�^�ς݂��ǂ����`�F�b�N����
	public boolean hasSameRecord(TableElement element) throws Exception{
		Connection con = TransactionForTableEditor.getInstance(config).getConnection();
		TableElement registedElem = TableElementSearcher.findElement(con, element, true);
		return registedElem != null ? true : false;
	}
	
	/**
	 * �e�[�u���ɐV�K�o�^�p�̃��R�[�h��\������
	 * 
	 */
	public void createNewRecord() {

		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		TableElement elem = getHeaderTableElement();
		ITable tbl = elem.getTable();
		int count = table.getItems().length;

		// NewRecord�̏����l
		Object[] items = new Object[elem.getColumns().length];
		for (int i = 0; i < items.length; i++) {
			zigen.plugin.db.core.TableColumn column = elem.getColumns()[i];
			items[i] = InsertRecordAction.getDefaultValue(column);
		}

		TableElement newElement = new TableNewElement(tbl, count + 1, elem.getColumns(), items, elem.getUniqueColumns());

		// ���R�[�h�̒ǉ�
		TableViewerManager.insert(viewer, newElement);

		// �P�J������(Row�����������́j��ҏW
		editTableElement(count, 1); // �ŏI���R�[�h��1�J�����ڂ�ҏW

	}

	/**
	 * ��ʏ�̐V�K�p���R�[�h���폜���� ���f�[�^�x�[�X�ւ̍폜�͍s��Ȃ��i�����܂ł������ڏ�j
	 * 
	 */
	public void removeRecord(TableElement element) {
		TableViewerManager.remove(viewer, element);
	}

	/**
	 * �R�l�N�V�����̊J��
	 */
	public void dispose() {
		// trans.cloesConnection();
	}

//	public void createNewRecord(Object[] items) throws Exception {
//		// String nullSymbol =
//		// DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
//		TableElement elem = getHeaderTableElement();
//		ITable tbl = elem.getTable();
//		int count = table.getItems().length;
//		TableElement newElement = new TableNewElement(tbl, count + 1, elem.getColumns(), items, elem.getUniqueColumns());
//		// ���R�[�h�̒ǉ�
//		TableViewerManager.insert(viewer, newElement);
//
//		// �C�������J�����Ƃ��Đݒ肷��
//		for (int i = 0; i < items.length; i++) {
//			newElement.addMofiedColumn(i);
//		}
//
//		// �I���s��ύX���Ă��� 2007-09-06 ZIGEN
//		table.setSelection(count);
//
//		// �X�V���� 2007-06-19 ZIGEN
//		updateDataBase(newElement);
//
//		// �P�J������(Row�����������́j��ҏW
//		editTableElement(count, 1); // �ŏI���R�[�h��1�J�����ڂ�ҏW
//
//	}

}
