/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.Comparator;

public class SQLHistorySorter implements Comparator {

	public SQLHistorySorter(){
		
	}
	public int compare(Object e1, Object e2) {
		
		if (e1 instanceof SQLHistory && e2 instanceof SQLHistory) {
			SQLHistory c1 = (SQLHistory) e1;
			SQLHistory c2 = (SQLHistory) e2;

			if(!c1.isBlank() && !c2.isBlank()){
				return c1.getDate().compareTo(c2.getDate());
				
			}else if(c1.isBlank()){
				return 1;
			
			}else if(c2.isBlank()){
				return -1;
				
			}else{
				return 0;
			}
		} else {
			// ��L�ȊO�͖��O�Ń\�[�g
			String a = e1.toString();
			String b = e2.toString();
			return a.compareTo(b);
		}

	}

}
