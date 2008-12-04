/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.actions;

import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableNewElement;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

/**
 * InsertRecordAction�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/12 ZIGEN create.
 * 
 */
public class InsertRecordAction extends TableViewEditorAction {

	public InsertRecordAction() {
		// �e�L�X�g��c�[���`�b�v�A�A�C�R���̐ݒ�
		this.setText(Messages.getString("InsertRecordAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("InsertRecordAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.InsertRecordCommand"); //$NON-NLS-1$
//		this.setImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_ADD));

	}

	public void run() {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		ITable table = editor.getTableNode();

		TableViewer viewer = editor.getViewer();
		TableElement elem = editor.getHeaderTableElement();
		int count = viewer.getTable().getItems().length;

		// NewRecord�̏����l
		Object[] items = new Object[elem.getColumns().length];
		for (int i = 0; i < items.length; i++) {
			TableColumn column = elem.getColumns()[i];
			items[i] = getDefaultValue(column);
		}

		// NewRecord
		// log.debug(elem.getUniqueColumns());
		TableElement newElement = new TableNewElement(table, count + 1, elem.getColumns(), items, elem.getUniqueColumns());

		// ���R�[�h�̒ǉ�
		TableViewerManager.insert(viewer, newElement);

		// �P�J������(Row�����������́j��ҏW
		editor.editTableElement(newElement, 1);

	}

	/**
	 * �V�K���R�[�h�쐬���̏����l��ݒ肷�郁�\�b�h
	 * 
	 * @param column
	 * @return
	 */
	public static String getDefaultValue(TableColumn column) {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
        
		// �����l��ݒ肷��悤�ɏC��
		String defaultValue = column.getDefaultValue();
		
		if (defaultValue != null && !"".equals(defaultValue)) { //$NON-NLS-1$

			if (defaultValue.matches("^'.*'$")) { //$NON-NLS-1$
				defaultValue = defaultValue.replaceAll("^'|'$", "");// �O���'���O�� //$NON-NLS-1$ //$NON-NLS-2$
				defaultValue = defaultValue.replaceAll("''", "'");// '' �� //$NON-NLS-1$ //$NON-NLS-2$
																																				// ' �ϊ�
				return defaultValue;
			} else if (defaultValue.matches("^\\(.*\\)$")) { //$NON-NLS-1$
				defaultValue = defaultValue.replaceAll("^\\(|\\)$", "");// �O���()���O�� //$NON-NLS-1$ //$NON-NLS-2$
				
				// ���ʂ��O������ɁA�O���'���O��
				if (defaultValue.matches("^'.*'$")) { //$NON-NLS-1$
					defaultValue = defaultValue.replaceAll("^'|'$", "");// �O���'���O�� //$NON-NLS-1$ //$NON-NLS-2$
					defaultValue = defaultValue.replaceAll("''", "'");// '' �� //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				return defaultValue;
				
			} else {
				if (defaultValue.equalsIgnoreCase("NULL")) { //$NON-NLS-1$
				    return nullSymbol; // NULL�Ȃ��NULL������ݒ�
				} else if (StringUtil.isNumeric(defaultValue.trim())) {
				    return defaultValue.trim();
				} else {
					; // �ݒ肵�Ȃ�(SYSDATE�Ȃǂ̊֐��������l�ɐݒ肵�Ă���ꍇ)
					return ""; //$NON-NLS-1$
				}
			}

		} else {
			if (!column.isNotNull()) {
			    return nullSymbol; // NULL�Ȃ��NULL������ݒ�
			} else {
			    return ""; //$NON-NLS-1$
			}
		}
	}

}
