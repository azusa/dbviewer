/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.ColumnFilterInfo;

/**
 * CopyRecordDataAction.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/12/04 ZIGEN create.
 * 
 */
public class CopyRecordDataAction extends TableViewEditorAction {
	private final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	private final String DEMILITER = "\t"; //$NON-NLS-1$

	protected IStructuredSelection selection;

	public CopyRecordDataAction() {
		// �e�L�X�g��c�[���`�b�v�A�A�C�R���̐ݒ�
		setEnabled(false);
		setImage(ITextOperationTarget.COPY);
	}

	private boolean isVisibled(int columnIndex) {
		if (editor instanceof TableViewEditorFor31) {
			TableViewEditorFor31 editor31 = (TableViewEditorFor31) editor;
			ColumnFilterInfo[] filters = editor31.getFilterInfos();
			return filters[columnIndex].isChecked();
			/*
			 * TableViewEditorFor31 editor31 = (TableViewEditorFor31) editor;
			 * org.eclipse.swt.widgets.TableColumn[] cols =
			 * editor31.getViewer().getTable().getColumns(); ColumnFilterInfo[]
			 * filters = editor31.getFilterInfos(); for (int i = 1; i <
			 * cols.length; i++) { ColumnFilterInfo info = filters[i - 1]; if
			 * (info.isChecked()) { return true; } else { return false; } }
			 * return true;
			 */

		} else {
			return true;
		}

	}

	public void run() {

		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			IStructuredSelection selection = (IStructuredSelection) editor.getViewer().getSelection();
			Iterator iter = selection.iterator();

			int index = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TableElement) {
					TableElement elem = (TableElement) obj;
					TableColumn[] columns = elem.getColumns();

					if (index == 0) {
						createHeader(sb, columns);
					}

					boolean isFirst = true;
					for (int i = 0; i < columns.length; i++) {
						if (isVisibled(i)) {
							TableColumn col = columns[i];
							int type = col.getDataType();
							// �����̃J�����̏ꍇ�A�������R�s�[����Ȃ���Q�ɑΉ�����
							//String value = String.valueOf(elem.getItem(col));
							String value = String.valueOf(elem.getItem(i));
							

							// ��TAB�Ή�
							if (value.indexOf("\"") >= 0) { //$NON-NLS-1$
								value = value.replaceAll("\"", "\"\""); // " ��
																		// ""
																		// //$NON-NLS-1$
																		// //$NON-NLS-2$
							}
							// 0�o�C�g�Ή� 2007-10-15
                            if(value.length() == 0){
                                value = "\"\"";
                            }else{
                                value = value.replaceAll("^|$", "\""); // �擪�ƍs����"������
                                                                        // //$NON-NLS-1$
                                                                        // //$NON-NLS-2$
                            }
                            
							if (isFirst) {
								sb.append(value);
								isFirst = false;
							} else {
								sb.append(DEMILITER + value);
							}
						}

					}
					sb.append(LINE_SEP);
					index++;

				}
			}
			clipboard.setContents(new Object[] {
				sb.toString()
			}, new Transfer[] {
				TextTransfer.getInstance()
			});

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {
			// �I����Ԃ��ēx�ʒm����
			editor.getViewer().getControl().notifyListeners(SWT.Selection, null);
			/*
			 * if (editor instanceof TableViewEditorFor31) { //
			 * �R�s�[��A�\��t�����ł���悤��Reflesh���� ((TableViewEditorFor31)
			 * editor).refleshAction(); }
			 */
		}

	}

	private void createHeader(StringBuffer sb, TableColumn[] columns) {
		// index=0�̓��x��������
		boolean isFirst = true;
		for (int i = 0; i < columns.length; i++) {
			if (isVisibled(i)) {
				TableColumn col = columns[i];
				if (isFirst) {
					sb.append(col.getColumnName());
					isFirst = false;
				} else {
					sb.append(DEMILITER + col.getColumnName());
				}
			}

		}
		sb.append(LINE_SEP);
	}

	/**
	 * Enable���[�h��ݒ肷��
	 * 
	 */
	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (editor.getViewer() == null) {
			setEnabled(false);
		} else {
			selection = (IStructuredSelection) editor.getViewer().getSelection();
			if (selection.size() > 0) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}
    
    public void selectionChanged(ISelection selection) {
        IStructuredSelection _selection = (IStructuredSelection) editor.getViewer().getSelection();
        if (_selection != null && _selection.size() > 0) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }


}
