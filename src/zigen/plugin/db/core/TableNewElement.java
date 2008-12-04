/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.Serializable;

import zigen.plugin.db.ui.internal.ITable;


public class TableNewElement extends TableElement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * �R���X�g���N�^(INSERT�p�j ���j�[�N�J�������w�肷�邪�A�Ή����郆�j�[�N�f�[�^�͖����i�V�K�̂��߁j
	 */
	public TableNewElement(ITable table, int recordNo, TableColumn[] columns, Object[] items, TableColumn[] uniqueColumns) {
		super(table, recordNo, columns, items, null, null);
		this.uniqueColumns = uniqueColumns;
		this.isNew = true;
	
	
		convertItems();
	}
	
	
	// Excel����\��t����ꍇ�ACHAR�^�̋󔒂�������ꍇ�����邽�߁A�����ŋ����I�Ƀp�f�B���O����
	private void convertItems(){
		for (int i = 0; i < items.length; i++) {
			items[i] = padding(i, items[i]);
		}		
	}
	

}
