/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.Connection;

import zigen.plugin.db.core.TableColumn;

/**
 * 
 * IColumnSearchFactory.javaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/06 ZIGEN create.
 * 
 */
public interface IColumnSearcherFactory {
	public static final int COLUMN_NAME = 4; // カラム名

	public static final int DATA_TYPE = 5; // java.sql.Types での SQL データ型

	public static final int TYPE_NAME = 6; // DB 製品に依存する列のデータ型名

	public static final int COLUMN_SIZE = 7; // 列サイズ。char/date
												// タイプについては最大文字数、numeric や
												// decimal タイプについては桁数

	public static final int DECIMAL_DIGITS = 9; // 小数点以下の桁数

	public static final int NULLABLE = 11; // NULL 使用許可の可否

	public static final int REMARKS = 12; // コメント記述列 (null の場合がある)

	public static final int IS_NULLABLE = 18; // NULL 値使用の可否

	abstract TableColumn[] execute(Connection con, String schemaPattern, String tableName) throws Exception;

	abstract void setConvertUnicode(boolean b);

}
