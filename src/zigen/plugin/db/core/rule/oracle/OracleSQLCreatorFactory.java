/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.oracle;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

/**
 * OracleInsertFactory.java�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/07 ZIGEN create.
 */
public class OracleSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public OracleSQLCreatorFactory(ITable table) {
		super(table);
	}


	/**
	 * �J�������{�^�{��(Oracle�p��Default�\����ǉ�)
	 * 
	 * @return
	 */
	protected String getColumnLabel(TableColumn column) {

		StringBuffer sb = new StringBuffer();
		sb.append("    "); //$NON-NLS-1$
		sb.append(StringUtil.padding(column.getColumnName(), 28));

		String typeName = column.getTypeName().toUpperCase();

		sb.append(typeName);

		// �p�����[�^�����̌^�Ή�(�� NUMBER�^)
		//if (isVisibleColumnSize(typeName)) {
	    if (isVisibleColumnSize(typeName) && !column.isWithoutParam()) {
			if (column.getDecimalDigits() == 0) {
				sb.append("(" + column.getColumnSize() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}

		// DEFAULT�̒ǉ�
		if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) { //$NON-NLS-1$
			sb.append(" DEFAULT "); //$NON-NLS-1$
			sb.append(column.getDefaultValue().trim());
		}

		return sb.toString();
	}

	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT * FROM "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		if (condition != null && !"".equals(condition.trim())) { //$NON-NLS-1$
			sb.append(" WHERE " + condition); //$NON-NLS-1$
			if (limit > 0) {
				sb.append(" AND ROWNUM <= " + (++limit));// �_�C�A���O���o�����߂Ɂ{�P
				// //$NON-NLS-1$
			}
		} else {
			if (limit > 0) {
				sb.append(" WHERE ROWNUM <= " + (++limit));// �_�C�A���O���o�����߂Ɂ{�P
				// //$NON-NLS-1$
			}
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}

		return sb.toString();
	}
	
	
	private void addColumnName(StringBuffer sb){
		Column[] cols = table.getColumns();
		for (int i = 0; i < cols.length; i++) {
			Column column = cols[i];
			if(i == 0){
				sb.append(column.getName());
			}else{
				sb.append(", ");
				sb.append(column.getName());				
			}
		}
	}
	
	public boolean isSupportPager(){
		return true;
	}
	
	public String createSelectForPager(String _condition, int offset, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		addColumnName(sb);
		sb.append(" FROM (SELECT W.*, ROWNUM LINE FROM ("); //$NON-NLS-1$
		
		// <-- ����SQL
		sb.append(" SELECT * FROM "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());
		
		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];
		
		if (condition != null && !"".equals(condition.trim())) { //$NON-NLS-1$
			sb.append(" WHERE " + condition); //$NON-NLS-1$
		}
		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}
		// -->
		
		//sb.append(") W ) T WHERE line BETWEEN ").append(offset).append(" AND ").append(offset + limit);	// MaxReocordException�������Ĕ�������ꍇ
		sb.append(") W ) T WHERE line BETWEEN ").append(offset).append(" AND ").append(offset + limit - 1);
				
		return sb.toString();
	}
	

	public String VisibleColumnSizePattern() {
		return "^CHAR|^VARCHAR.*|^NUMBER"; //$NON-NLS-1$
	}

	public String[] getSupportColumnType() {
		return new String[] { "CHAR", //$NON-NLS-1$
				"VARCHAR2", //$NON-NLS-1$
				"NUMBER", //$NON-NLS-1$
				"DATE", //$NON-NLS-1$
				"TIMESTAMP(6)", //$NON-NLS-1$
				"FLOAT", //$NON-NLS-1$
				"RAW", //$NON-NLS-1$
				"NCHAR", //$NON-NLS-1$
				"NVARCHAR2", //$NON-NLS-1$
				"LONG", //$NON-NLS-1$
				"CLOB", //$NON-NLS-1$
				"BLOB", //$NON-NLS-1$
				"BFILE", //$NON-NLS-1$
				"RAW" //$NON-NLS-1$
		};

	}

	public boolean supportsRemarks() {
		return true;
	}

	public boolean supportsModifyColumnType() {
		return true;
	}

	public boolean supportsModifyColumnSize(String columnType) {
		return isVisibleColumnSize(columnType);
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return true;
	}

	public boolean supportsRollbackDDL() {
		return false;
	}

	public String createCommentOnTableDDL(String commnets) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON TABLE "); //$NON-NLS-1$
		if(isVisibleSchemaName){
			sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		}else{
			sb.append(SQLUtil.encodeQuotation(table.getName()));
		}


		sb.append(" IS "); //$NON-NLS-1$
		sb.append(" '" + SQLUtil.encodeQuotation(commnets) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String createCommentOnColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON COLUMN "); //$NON-NLS-1$
		if(isVisibleSchemaName){
			sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		}else{
			sb.append(SQLUtil.encodeQuotation(table.getName()));
		}


		sb.append("."); //$NON-NLS-1$
		sb.append(column.getName());
		sb.append(" IS"); //$NON-NLS-1$
		sb.append(" '" + SQLUtil.encodeQuotation(column.getRemarks()) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" RENAME TO "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(newTableName));
		return sb.toString();
	}

	// for Oracle
	public String createRenameColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" RENAME COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(from.getName()));
		sb.append(" TO "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(to.getName()));
		return sb.toString();

	}

	// for Oracle
	public String[] createAddColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" ADD ("); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(column.getName()));
		sb.append(" "); //$NON-NLS-1$
		sb.append(column.getTypeName());// �^
		if (isVisibleColumnSize(column.getTypeName()) && !column.getColumn().isWithoutParam()) {// ��
			sb.append("("); //$NON-NLS-1$
			sb.append(column.getSize());
			sb.append(")"); //$NON-NLS-1$
		}
		if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {// DEFAULT
			// //$NON-NLS-1$
			sb.append(" DEFAULT "); //$NON-NLS-1$
			sb.append(column.getDefaultValue());
		}

		if (column.isNotNull()) { // NOT NULL
			sb.append(" NOT NULL"); //$NON-NLS-1$
		} else {
			sb.append(" NULL"); //$NON-NLS-1$
		}
		sb.append(")"); //$NON-NLS-1$

		return new String[] { sb.toString() };

	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();

		if (!from.getTypeName().equals(to.getTypeName()) || !from.getSize().equals(to.getSize())) {
			sb.append("ALTER TABLE "); //$NON-NLS-1$
			sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
			sb.append(" MODIFY ("); //$NON-NLS-1$
			sb.append(SQLUtil.encodeQuotation(to.getName()));
			sb.append(" "); //$NON-NLS-1$
			sb.append(to.getTypeName());// �^
			if (isVisibleColumnSize(to.getTypeName()) && !to.getColumn().isWithoutParam()) {// ��
				sb.append("("); //$NON-NLS-1$
				sb.append(to.getSize());
				sb.append(")"); //$NON-NLS-1$
			}
			sb.append(")"); //$NON-NLS-1$
		}

		if (!from.getDefaultValue().equals(to.getDefaultValue())) {// DEFAULT
			sb2.append("ALTER TABLE "); //$NON-NLS-1$
			sb2.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
			sb2.append(" MODIFY ("); //$NON-NLS-1$
			sb2.append(SQLUtil.encodeQuotation(to.getName()));
			sb2.append(" "); //$NON-NLS-1$
			sb2.append(" DEFAULT "); //$NON-NLS-1$
			if ("".equals(to.getDefaultValue())) { //$NON-NLS-1$
				sb2.append("NULL"); //$NON-NLS-1$
			} else {
				sb2.append(to.getDefaultValue());
			}
			sb2.append(")"); //$NON-NLS-1$

		}

		if (from.isNotNull() != to.isNotNull()) {
			sb3.append("ALTER TABLE "); //$NON-NLS-1$
			sb3.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
			sb3.append(" MODIFY ("); //$NON-NLS-1$
			sb3.append(SQLUtil.encodeQuotation(to.getName()));
			sb3.append(" "); //$NON-NLS-1$
			if (to.isNotNull()) {
				sb3.append(" NOT NULL"); //$NON-NLS-1$
			} else {
				sb3.append(" NULL"); //$NON-NLS-1$
			}
			sb3.append(")"); //$NON-NLS-1$

		}

		return new String[] { sb.toString(), sb2.toString(), sb3.toString() };

	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" DROP COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.encodeQuotation(column.getName()));

		if (cascadeConstraints) {
			sb.append(" CASCADE CONSTRAINTS "); //$NON-NLS-1$
		}

		return new String[] { sb.toString() };

	}

	// / ������������������������������������������������������������������������������������ //
	public String createCreateIndexDDL(String indexName, Column[] columns, int indexType) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE"); //$NON-NLS-1$

		if (TYPE_UNIQUE_INDEX == indexType) {
			sb.append(" UNIQUE"); //$NON-NLS-1$
		} else if (TYPE_BITMAP_INDEX == indexType) {
			sb.append(" BITMAP"); //$NON-NLS-1$
		}
		sb.append(" INDEX "); //$NON-NLS-1$
		// INDEX��
		sb.append("\""); //$NON-NLS-1$
		sb.append(table.getSchemaName());
		sb.append("\""); //$NON-NLS-1$
		sb.append("."); //$NON-NLS-1$
		sb.append(indexName);
		sb.append(" ON "); //$NON-NLS-1$
		if(isVisibleSchemaName){
			sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		}else{
			sb.append(SQLUtil.encodeQuotation(table.getName()));
		}



		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")"); //$NON-NLS-1$

		return sb.toString();
	}

	public String createDropIndexDDL(String indexName) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP INDEX "); //$NON-NLS-1$
		sb.append("\""); //$NON-NLS-1$
		sb.append(table.getSchemaName());
		sb.append("\""); //$NON-NLS-1$
		sb.append("."); //$NON-NLS-1$
		sb.append(indexName);
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT PK_TEST PRIMARY KEY (EMPNO)
	public String createCreateConstraintPKDDL(String constraintName, Column[] columns) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(constraintName);
		sb.append(" PRIMARY KEY"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")"); //$NON-NLS-1$
		return sb.toString();
	}

	public String createCreateConstraintUKDDL(String constraintName, Column[] columns) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(constraintName);
		sb.append(" UNIQUE "); // UNIQUE KEY �ł͂Ȃ��A UNIQUE //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")"); //$NON-NLS-1$
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT FK_EMP
	// FOREIGN KEY (EMPNO) REFERENCES SCOTT.DEPT(DEPTNO) ON DELETE CASCADE
	public String createCreateConstraintFKDDL(String constraintName, Column[] columns, ITable refTable, Column[] refColumns, boolean onDeleteCascade) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(constraintName);
		sb.append(" FOREIGN KEY"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(column.getColumn().getColumnName());
		}
		sb.append(")"); //$NON-NLS-1$
		sb.append(" REFERENCES "); //$NON-NLS-1$

		sb.append(refTable.getSqlTableName());
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < refColumns.length; i++) {
			Column refColumn = refColumns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(refColumn.getColumn().getColumnName());
		}
		sb.append(")"); //$NON-NLS-1$
		if (onDeleteCascade) {
			sb.append(" ON DELETE CASCADE"); //$NON-NLS-1$
		}

		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT MY_CHECK CHECK (SAL > 0)
	public String createCreateConstraintCheckDDL(String constraintName, String check) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(constraintName);
		sb.append(" CHECK"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		sb.append(check);
		sb.append(")"); //$NON-NLS-1$
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 DROP CONSTRAINT TESTPK
	public String createDropConstraintDDL(String constraintName, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());
		sb.append(" DROP CONSTRAINT "); //$NON-NLS-1$
		sb.append(constraintName);
		return sb.toString();

	}
	
	protected String getViewDDL_SQL(String owner, String view){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TEXT"); //$NON-NLS-1$
		sb.append(" FROM ALL_VIEWS"); //$NON-NLS-1$
		sb.append(" WHERE OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(" AND VIEW_NAME = '" + SQLUtil.encodeQuotation(view) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}
	
//	/**
//	 * Oralce�ł́AUniqueIndex��DDL�Ɋ܂߂Ȃ�
//	 */
//	protected String getConstraints() {
//		StringBuffer sb = new StringBuffer();
//
//		String pks = getConstraintPKStr();
//		String[] fks = getConstraintFKStr();
//		String[] cons = getConstraintOtherStr();
//		
//		boolean p = (pks != null && pks.length() > 0);
//		boolean f = (fks != null && fks.length > 0);
//		boolean c = (cons != null && cons.length > 0);
//		
//		if (pks != null) {
//			sb.append("    " + pks);
//			if (f || c ) {
//				sb.append(",");
//
//			}
//			sb.append(DbPluginConstant.LINE_SEP);
//		} else {
//
//		}
//
//		if (fks != null) {
//			for (int i = 0; i < fks.length; i++) {
//				if (i == fks.length - 1) {
//					sb.append("    " + fks[i]);
//					if (c) {
//						sb.append(",");
//					}
//				} else {
//					sb.append("    " + fks[i] + ",");
//				}
//				sb.append(DbPluginConstant.LINE_SEP);
//
//			}
//		}
//		
//		if (cons != null) {
//			for (int i = 0; i < cons.length; i++) {
//				if (i == cons.length - 1) {
//					sb.append("    " + cons[i]);
//				} else {
//					sb.append("    " + cons[i] + ",");
//				}
//				sb.append(DbPluginConstant.LINE_SEP);
//
//			}
//		}
//
//		return sb.toString();
//	}

}
