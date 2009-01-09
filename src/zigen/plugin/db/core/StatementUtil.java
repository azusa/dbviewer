/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * StatementUtil�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/07/17 ZIGEN create.
 * 
 */
public class StatementUtil {

	/**
	 * �f�t�H���g�R���X�g���N�^
	 * 
	 */
	private StatementUtil() {}

	public static final void close(Statement st) {
		if (st == null)
			return;

		try {
			st.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
