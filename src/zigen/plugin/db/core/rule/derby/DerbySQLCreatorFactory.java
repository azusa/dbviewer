/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core.rule.derby;

import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ITable;

/**
 * 
 * H2SQLCreatorFactory.java�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/05/07 ZIGEN create.
 * 
 */
public class DerbySQLCreatorFactory extends DefaultSQLCreatorFactory {

	public DerbySQLCreatorFactory(ITable table) {
		super(table);
	}

	public String VisibleColumnSizePattern() {
		// return
		// ".*CHAR|^VARCHAR.*|^NUMBER|^DECIMAL|.*INT.*|^FLOAT|^DOUBLE|^REAL|^TIMESTAMP|^TIME|.*VARYING";
		return "^VARCHAR";
	}

	public String[] getSupportColumnType() {
		return new String[] {
				"BIGINT",
				"CHAR",
				"DATE",
				"DECIMAL",
				"DOUBLE",
				"DOUBLE PRECISION",
				"FLOAT",
				"INTEGER",
				"NUMERIC",
				"REAL",
				"SMALLINT",
				"TIME",
				"TIMESTAMP",
				"VARCHAR",
				"CLOB",
				"LONG VARCHAR",
				"BLOB",
				"CHAR FOR BIT DATA",
				"VARCHAR FOR BIT DATA",
				"LONG VARCHAR FOR BIT DATA"
		};
	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("RENAME TABLE ");
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" TO ");
		sb.append(SQLUtil.encodeQuotation(newTableName));
		return sb.toString();

	}

	public String createRenameColumnDDL(Column from, Column to) {

		// Derby�́A�J�������̕ύX���T�|�[�g���Ă���͂��ł��邪�A���s�ł��Ȃ�
		// StringBuffer sb = new StringBuffer();
		// sb.append("RENAME COLUMN ");
		// sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		// sb.append(".");
		// sb.append(SQLUtil.encodeQuotation(from.getName()));
		// sb.append(" TO ");
		// sb.append(SQLUtil.encodeQuotation(to.getName()));
		// return sb.toString();

		return null;

	}

	public String[] createAddColumnDDL(Column column) {

		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb.append(" ADD COLUMN ");
		sb.append(SQLUtil.encodeQuotation(column.getName()));
		sb.append(" ");

		// �^
		sb.append(column.getTypeName());

		// ��
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
		// StringBuffer sb2 = new StringBuffer();
		// sb2.append("ALTER TABLE ");
		// sb2.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		// sb2.append(" ALTER COLUMN ");
		// sb2.append(SQLUtil.encodeQuotation(column.getName()));
		//
		// // NOT NULL
		// if (column.isNotNull()) {
		// sb2.append(" NOT NULL");
		// } else {
		// sb2.append(" NULL");
		// }

		StringBuffer sb2 = new StringBuffer();
		if (column.isNotNull()) {
			sb2.append("ALTER TABLE ");
			sb2.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
			sb2.append(" ALTER COLUMN  ");
			sb2.append(SQLUtil.encodeQuotation(column.getName()));
			sb2.append(" NOT NULL");
		}
		return new String[] {
				sb.toString(),
				sb2.toString()
		};

	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		// sb.append("ALTER TABLE ");
		// sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		// sb.append(" ALTER COLUMN ");
		// sb.append(SQLUtil.encodeQuotation(to.getName()));
		// sb.append(" SET ");
		//        
		// // �^
		// sb.append(to.getTypeName());
		//        
		// // ��
		// if(isVisibleColumnSize(to.getTypeName())){
		// sb.append("(");
		// sb.append(to.getSize());
		// sb.append(")");
		// }

		// VARCHAR�̏ꍇ�̂݌���ύX���邱�Ƃ��ł���
		if (!from.getSize().equals(to.getSize()) && "VARCHAR".equals(to.getTypeName())) {
			sb.append("ALTER TABLE ");
			sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
			sb.append(" ALTER COLUMN ");
			sb.append(SQLUtil.encodeQuotation(to.getName()));
			sb.append(" SET DATA TYPE VARCHAR(");
			sb.append(to.getSize());
			sb.append(")");
		}

		StringBuffer sb2 = new StringBuffer();
		// DEFAULT
		if (!from.getDefaultValue().equals(to.getDefaultValue())) {
			sb2.append("ALTER TABLE ");
			sb2.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
			sb2.append(" ALTER COLUMN ");
			sb2.append(SQLUtil.encodeQuotation(to.getName()));

			sb2.append(" DEFAULT ");
			if ("".equals(to.getDefaultValue())) {
				// NULL�ɐݒ肵�����A���f����Ȃ��B(Derby�̃o�O�H)
				sb2.append("NULL");
			} else {
				sb2.append(to.getDefaultValue());
			}
		}

		StringBuffer sb3 = new StringBuffer();

		sb3.append("ALTER TABLE ");
		sb3.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		sb3.append(" ALTER COLUMN ");
		sb3.append(SQLUtil.encodeQuotation(to.getName()));

		// NOT NULL
		if (to.isNotNull()) {
			sb3.append(" NOT NULL");
		} else {
			sb3.append(" NULL");
		}

		return new String[] {
				sb.toString(),
				sb2.toString(),
				sb3.toString()
		};

	}

	public String createCommentOnTableDDL(String commnets) {
		return null;
	}

	public String createCommentOnColumnDDL(Column column) {
		return null;
	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		// Derby�ł́A�J�����̍폜���ł��Ȃ�

		// StringBuffer sb = new StringBuffer();
		// sb.append("ALTER TABLE ");
		// sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		// sb.append(" DROP COLUMN ");
		// sb.append(SQLUtil.encodeQuotation(column.getName()));
		// return new String[] {
		// sb.toString()
		// };

		return null;
	}

	public boolean supportsModifyColumnSize(String columnType) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	public boolean supportsModifyColumnType() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	public boolean supportsRemarks() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	public boolean supportsRollbackDDL() {
		return true;
	}

	/*
	 * 
	 * �P�D�J�������̍X�V�͂قƂ�ŕs�� �� �J�����̗�̌^�ύX�͂ł��Ȃ��B�iVARCHAR�̂݌���ύX�ł���B�ȊO�͕s�j
	 * 
	 * �Q�D�J�����̍폜���ł��Ȃ��i���R�͕s���j
	 * 
	 * �R�DDEFAULT�ɐݒ肵�����ƁA�����̕��@�������H DEFAULT NULL�͎��s�ł��邪���f����Ȃ�
	 * 
	 * 
	 * 
	 * ALTER TABLE APP.TEST ALTER COLUMN COL2 NOT NULL ALTER TABLE APP.TEST
	 * ALTER COLUMN COL2 NULL
	 * 
	 * ALTER TABLE APP.TEST ALTER COLUMN COL2 DEFAULT 1 ALTER TABLE APP.TEST
	 * ALTER COLUMN COL2 WITH DEFAULT NULL << ���s�G���[�ɂ͂Ȃ�Ȃ����A���f����Ȃ�
	 * 
	 * //DERBY �ł́A��̌^�͕ύX�s�� //�T�C�Y�̕ύX�́AVARCHAR�̂݁i���Ƃ́A�����ŌŒ�j ALTER TABLE APP.TEST
	 * ALTER COLUMN COL2 SET DATA TYPE VARCHAR(100)
	 */

	// / ������������������������������������������������������������������������������������ //
	public String createCreateIndexDDL(String indexName, Column[] columns, int indexType) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE");

		if (TYPE_UNIQUE_INDEX == indexType) {
			sb.append(" UNIQUE");
		} else if (TYPE_BITMAP_INDEX == indexType) {
			sb.append(" BITMAP");
		}
		sb.append(" INDEX ");
		// INDEX��
		sb.append("\"");
		sb.append(table.getSchemaName());
		sb.append("\"");

		sb.append(".");
		sb.append(indexName);
		sb.append(" ON ");
		if(isVisibleSchemaName){
			sb.append(SQLUtil.encodeQuotation(table.getSqlTableName()));
		}else{
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
		sb.append(" UNIQUE "); // UNIQUE KEY �ł͂Ȃ��A UNIQUE
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
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(table.getSqlTableName());
		sb.append(" ADD CONSTRAINT ");
		sb.append(constraintName);
		sb.append(" CHECK");
		sb.append("(");
		sb.append(check);
		sb.append(")");
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 DROP CONSTRAINT TESTPK
	public String createDropConstraintDDL(String constraintName, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(table.getSqlTableName());

		if (Constraint.PRIMARY_KEY.equals(type)) {
			// PK���폜����ꍇ�͈ȉ�
			sb.append(" DROP PRIMARY KEY ");
		} else {
			sb.append(" DROP CONSTRAINT ");
			sb.append(constraintName);
		}

		return sb.toString();

	}

}
