/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.hsqldb;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

/**
 * 
 * HSQLDBSQLCreatorFactory.javaクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/07 ZIGEN create.
 * 
 */
public class HSQLDBSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public HSQLDBSQLCreatorFactory(ITable table) {
		super(table);
	}

	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ");
		sb.append(table.getSqlTableName());

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) {
			sb.append(" " + orderBy);
		} else {
			// HSQLDBではORDER BYが必要
			int i;
			if (pks != null && pks.length > 0) {
				for (i = 0; i < pks.length; i++) {
					TablePKColumn pkc = pks[i];
					if (i == 0) {
						sb.append(" ORDER BY ");
						sb.append(pkc.getColumnName());
					} else {
						sb.append(", " + pkc.getColumnName());
					}
				}
			}
			// ↑ ORDER BYを追加
		}

		if (pks != null && pks.length > 0) {
			sb.append(" LIMIT " + (limit + 1)); // ダイアログを出す為に＋１
		} else {
			; // PKが無い場合はLIMITは使わない
		}

		return sb.toString();
	}

	public boolean isSupportPager() {
		if (pks != null && pks.length > 0) {
			return true;
		} else {
			// PKが無い場合はPagerは使えない
			return false;
		}
	}

	public String createSelectForPager(String _condition, int offset, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ");
		sb.append(table.getSqlTableName());

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) {
			sb.append(" " + orderBy);
		} else {
			// HSQLDBではORDER BYが必要
			int i;
			if (pks != null && pks.length > 0) {
				for (i = 0; i < pks.length; i++) {
					TablePKColumn pkc = pks[i];
					if (i == 0) {
						sb.append(" ORDER BY ");
						sb.append(pkc.getColumnName());
					} else {
						sb.append(", " + pkc.getColumnName());
					}
				}
			}
			// ↑ ORDER BYを追加
		}

		if (pks != null && pks.length > 0) {

			if (limit > 0) {
				sb.append(" LIMIT ");
				sb.append(limit);
				sb.append(" OFFSET ");
				sb.append(offset - 1);
			}

		} else {
			; // PKが無い場合はLIMITは使わない
		}

		return sb.toString();
	}

	public String[] getSupportColumnType() {
		return new String[] {"INT", "INTEGER", "DOUBLE", "FLOAT", "VARCHAR", "VARCHAR_IGNORECASE", "CHAR", "CHARACTER", "LONGVARCHAR", "DATE", "TIME", "TIMESTAMP", "DATETIME",
				"DECIMAL", "NUMERIC", "BOOLEAN", "BIT", "TINYINT", "SMALLINT", "BIGINT", "REAL", "BINARY", "VARBINARY", "LONGVARBINARY", "OTHER", "OBJECT"};
	}

	public String VisibleColumnSizePattern() {
		return "^CHAR|^VARCHAR.*|DOUBLE|NUMERIC|DECIMAL|TIMESTAMP";
	}

	public boolean supportsModifyColumnSize(String columnType) {
		return true;
	}

	public boolean supportsModifyColumnType() {
		return true;
	}

	public boolean supportsRemarks() {
		return false;
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	public boolean supportsRollbackDDL() {
		return false;
	}

	public String createCommentOnTableDDL(String commnets) {
		return null;
	}

	public String createCommentOnColumnDDL(Column column) {
		return null;
	}

	public String createRenameColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" ALTER COLUMN ");
		sb.append(SQLUtil.encodeQuotation(from.getName()));
		sb.append(" RENAME TO ");
		sb.append(SQLUtil.encodeQuotation(to.getName()));
		return sb.toString();

	}

	public String[] createAddColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" ADD COLUMN ");
		sb.append(SQLUtil.encodeQuotation(column.getName()));
		sb.append(" ");

		// 型
		sb.append(column.getTypeName());

		// 桁
		if (isVisibleColumnSize(column.getTypeName())) {
			sb.append("(");
			sb.append(column.getSize());
			sb.append(")");
		}

		// DEFAULT
		if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {
			sb.append(" DEFAULT ");
			sb.append(column.getDefaultValue());
		}

		// NOT NULL
		if (column.isNotNull()) {
			sb.append(" NOT NULL");
		} else {
			sb.append(" NULL");
		}

		return new String[] {sb.toString()};

	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" DROP COLUMN ");
		sb.append(SQLUtil.encodeQuotation(column.getName()));

		// H2:制約の削除は、CASCADEできない
		return new String[] {sb.toString()};

	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" ALTER COLUMN ");
		sb.append(SQLUtil.encodeQuotation(to.getName()));
		sb.append(" ");

		sb.append(to.getTypeName());// 型

		if (isVisibleColumnSize(to.getTypeName())) {// 桁
			sb.append("(");
			sb.append(to.getSize());
			sb.append(")");
		}

		/*
		 * if (!from.getDefaultValue().equals(to.getDefaultValue())) {// DEFAULT sb.append(" DEFAULT "); if ("".equals(to.getDefaultValue())) { sb.append("NULL"); } else {
		 * sb.append(to.getDefaultValue()); } }
		 */
		sb.append(" DEFAULT ");
		if ("".equals(to.getDefaultValue())) {
			sb.append("NULL");
		} else {
			sb.append(to.getDefaultValue());
		}

		if (to.isNotNull()) {
			sb.append(" NOT NULL");
		} else {
			sb.append(" NULL");
		}
		return new String[] {sb.toString()};

	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" RENAME TO ");
		sb.append(SQLUtil.encodeQuotation(newTableName));
		return sb.toString();
	}

	// / ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ //
	public String createCreateIndexDDL(String indexName, Column[] columns, int indexType) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE");

		if (TYPE_UNIQUE_INDEX == indexType) {
			sb.append(" UNIQUE");
		} else if (TYPE_BITMAP_INDEX == indexType) {
			sb.append(" BITMAP");
		}
		sb.append(" INDEX ");
		// INDEX名
		sb.append("\"");
		sb.append(table.getSchemaName());
		sb.append("\"");
		sb.append(".");
		sb.append(indexName);
		sb.append(" ON ");
		if (isVisibleSchemaName) {
			sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		} else {
			sb.append(SQLUtil.encodeQuotation(table.getName()));
		}

		sb.append("(");
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")");

		return sb.toString();
	}

	public String createDropIndexDDL(String indexName) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP INDEX ");
		sb.append("\"");
		sb.append(table.getSchemaName());
		sb.append("\"");
		sb.append(".");
		sb.append(indexName);
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT PK_TEST PRIMARY KEY (EMPNO)
	public String createCreateConstraintPKDDL(String constraintName, Column[] columns) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT ");
		sb.append(constraintName);
		sb.append(" PRIMARY KEY");
		sb.append("(");
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")");
		return sb.toString();
	}

	public String createCreateConstraintUKDDL(String constraintName, Column[] columns) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT ");
		sb.append(constraintName);
		sb.append(" UNIQUE "); // UNIQUE KEY ではなく、 UNIQUE
		sb.append("(");
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")");
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT FK_EMP
	// FOREIGN KEY (EMPNO) REFERENCES SCOTT.DEPT(DEPTNO) ON DELETE CASCADE
	public String createCreateConstraintFKDDL(String constraintName, Column[] columns, ITable refTable, Column[] refColumns, boolean onDeleteCascade) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT ");
		sb.append(constraintName);
		sb.append(" FOREIGN KEY");
		sb.append("(");
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")");
		sb.append(" REFERENCES ");

		sb.append(refTable.getSqlTableName());
		sb.append("(");
		for (int i = 0; i < refColumns.length; i++) {
			Column refColumn = refColumns[i];
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(refColumn.getColumn().getColumnName());
		}
		sb.append(")");
		if (onDeleteCascade) {
			sb.append(" ON DELETE CASCADE");
		}

		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT MY_CHECK CHECK (SAL > 0)
	public String createCreateConstraintCheckDDL(String constraintName, String check) {
		// StringBuffer sb = new StringBuffer();
		// sb.append("ALTER TABLE ");
		// sb.append(table.getSqlTableName());
		// sb.append(" ADD CONSTRAINT ");
		// sb.append(constraintName);
		// sb.append(" CHECK");
		// sb.append("(");
		// sb.append(check);
		// sb.append(")");
		// return sb.toString();
		return null;
	}

	// ALTER TABLE SCOTT.EMP3 DROP CONSTRAINT TESTPK
	public String createDropConstraintDDL(String constraintName, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(table.getSqlTableName());
		sb.append(" DROP CONSTRAINT ");
		sb.append(constraintName);
		return sb.toString();

	}

}
