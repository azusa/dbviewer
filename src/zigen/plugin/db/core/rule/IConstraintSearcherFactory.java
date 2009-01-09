/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.Connection;

import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;

/**
 * 
 * IColumnSearchFactory.javaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/06 ZIGEN create.
 * 
 */
public interface IConstraintSearcherFactory {

	// INDEX の TYPEカラムで返される定数
	public static final int TABLE_INDEX_STATISTIC = 0; // インデックスの統計値．

	public static final int TABLE_INDEX_CLSTERED = 1; // クラスターインデックス．

	public static final int TABLE_INDEX_HASHED = 2; // ハッシュインデックス．

	public static final int TABLE_INDEX_OTHER = 3; // 上記以外インデックス．

	TablePKColumn[] getPKColumns(Connection con, String schemaPattern, String tableName) throws Exception;

	TableFKColumn[] getFKColumns(Connection con, String schemaPattern, String tableName) throws Exception;

	TableIDXColumn[] getUniqueIDXColumns(Connection con, String schemaPattern, String tableName, boolean unique) throws Exception;

	TableConstraintColumn[] getConstraintColumns(Connection con, String schemaPattern, String tableName) throws Exception;

}
