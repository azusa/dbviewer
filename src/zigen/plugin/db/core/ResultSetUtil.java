/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSetUtil�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/07/17 ZIGEN create.
 * 
 */
public class ResultSetUtil {

	/**
	 * �f�t�H���g�R���X�g���N�^
	 * 
	 */
	private ResultSetUtil() {}

	public static final void close(ResultSet rs) {
		if (rs == null)
			return;

		try {
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
