/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableColumn;


/**
 * DefaultColumnSearcherFactory.java.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/25 ZIGEN create.
 * 
 */
public class DefaultColumnSearcherFactory extends AbstractColumnSearcherFactory implements IColumnSearcherFactory {
	
	
	public DefaultColumnSearcherFactory(boolean convertUnicode) {
		this.convertUnicode = convertUnicode;
	}
	
	
	/**
	 * 指定したテーブルのカラム情報取得 性能改善のために、プライマリかどうかの判定は外している。(TODO)
	 */
	public TableColumn[] execute(Connection con, String schemaPattern, String tableName) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;
		
		try {
			Map map = getCustomColumnInfoMap(con, schemaPattern, tableName, convertUnicode);
			
			DatabaseMetaData objMet = con.getMetaData();
			// TablePKColumn[] pks = ConstraintSearcher.getPKColumns(con, schemaPattern, tableName); // PK取得;
			if (schemaPattern != null) {
				rs = objMet.getColumns(null, schemaPattern, tableName, "%"); //$NON-NLS-1$
			} else {
				rs = objMet.getColumns(null, "%", tableName, "%"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			int seq = 1;
			while (rs.next()) {
				TableColumn column = new TableColumn();
				
				column.setSeq(seq); // カラム定義順（ソートに使用）
				
				column.setColumnName(rs.getString(COLUMN_NAME));
				column.setDataType(rs.getShort(DATA_TYPE)); // Types.VARCHARなど
				column.setTypeName(rs.getString(TYPE_NAME));
				column.setColumnSize(rs.getInt(COLUMN_SIZE));
				column.setDecimalDigits(rs.getInt(DECIMAL_DIGITS));
				
				// デフォルト値の追加
				// column.setDefaultValue(rs.getString("COLUMN_DEF"));
				// 2006/12/15 ZIGEN Default値をサポートしていない場合を想定
				// デフォルト値の追加
				// String defaultValue = rs.getString("COLUMN_DEF");
				String defaultValue = getDefaultValue(rs, convertUnicode);
				if (defaultValue != null) {
					column.setDefaultValue(defaultValue);
				}
				// 2006/12/15 ZIGEN end
				
				// コメント追加
				// column.setRemarks(rs.getString(REMARKS));
				String remarks = rs.getString("REMARKS"); //$NON-NLS-1$
				if (convertUnicode) {
					remarks = JDBCUnicodeConvertor.convert(remarks);
				}
				column.setRemarks(remarks);
				
				if (rs.getInt(NULLABLE) == DatabaseMetaData.columnNoNulls) {
					column.setNotNull(true);
				} else {
					column.setNotNull(false);
				}
				
				// <!-- [002] 修正 ZIGEN 2005/09/17
				// if (ConstraintSearcher.isPKColumn(pks, column.getColumnName())) {
				// column.setUniqueKey(true);
				// }
				// [002] 修正 ZIGEN 2005/09/17 -->
				
				// カスタムカラム情報がある場合は上書きする
				overrideColumnInfo(map, column);
				
				map.put(column.getColumnName(), column);
				list.add(column);
				
				seq++;
			}
			
			return (TableColumn[]) list.toArray(new TableColumn[0]);
			
		} catch (Exception e) {
			throw e;
		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}
	}
	
	protected void overrideColumnInfo(Map map, TableColumn column) throws Exception {
		;
	}
	
	/**
	 * 古いSymfoWAREではCOLUMN_DEFのカラムが無いため、SQLExceptionとなる。 そのため、以下のメソッドで個別に処理を行う。
	 * 
	 * @param rs
	 * @return
	 */
	protected String getDefaultValue(ResultSet rs, boolean convertUnicode) {
		String defaultValue = null;
		try {
			defaultValue = rs.getString("COLUMN_DEF"); //$NON-NLS-1$
			
			// TRIMする
			if (defaultValue != null)
				defaultValue = defaultValue.trim();// 初期値には不要な空白が入ることがある場合があるためTrimする
				
			if (convertUnicode) {
				return JDBCUnicodeConvertor.convert(defaultValue);
			}
		} catch (SQLException e) {
			// DbPlugin.log(e);
		}
		return defaultValue;
		
	}
	
	
	protected String getCustomColumnInfoSQL(DatabaseMetaData objMet, String owner, String table) {
		return null;
	}
	
}
