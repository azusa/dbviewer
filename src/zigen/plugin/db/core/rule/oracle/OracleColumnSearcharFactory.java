package zigen.plugin.db.core.rule.oracle;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;

public class OracleColumnSearcharFactory extends DefaultColumnSearcherFactory {

	public OracleColumnSearcharFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	/**
	 * INTER(パラメータ無)の場合のスケールを正しく表示するために、カラム情報を上書きする
	 */
	protected void overrideColumnInfo(Map map, Connection con, String schemaPattern, String tableName, boolean convUnicode) throws Exception {

		OracleColumnInfo[] oraColumns = OracleColumnSearcher.execute(con, schemaPattern, tableName, convUnicode);

		for (int i = 0; i < oraColumns.length; i++) {
			OracleColumnInfo oracleColumn = oraColumns[i];

			TableColumn tCol = (TableColumn) map.get(oracleColumn.getColumn_name());

			// ---------------------------------------------------------------------
			// INTEGER(パラメータ無)の場合は、
			// DATA_PRECISION = null , DATA_SCALE = 0 になるため、処理を見直す。
			// ---------------------------------------------------------------------

			if (oracleColumn.getData_precision() == null) {
				// パラメータ無しの型と判定する
				tCol.setColumnSize(0);
				tCol.setDecimalDigits(0);
				tCol.setWithoutParam(true); // パラメータ無し
			} else {
				if (oracleColumn.getData_precision() != null) {
					tCol.setColumnSize(oracleColumn.getData_precision().intValue());
				} else {
					tCol.setColumnSize(0);
				}
				if (oracleColumn.getData_scale() != null) {
					tCol.setDecimalDigits(oracleColumn.getData_scale().intValue());
				} else {
					tCol.setDecimalDigits(0);
				}
				tCol.setWithoutParam(false); // パラメータ有り
			}

			// 初期値には不要な空白が入ることがある場合があるためTrimする
			if (oracleColumn.getData_default() != null) {
				tCol.setDefaultValue(oracleColumn.getData_default().trim());
			}
			tCol.setRemarks(oracleColumn.getComments());

		}
	}
}

class OracleColumnInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	String column_name;

	String data_type;

	java.math.BigDecimal data_precision;

	java.math.BigDecimal data_scale;

	String data_default;

	String comments;

	/**
	 * コンストラクタ
	 */
	public OracleColumnInfo() {}

	public String getColumn_name() {
		return this.column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getData_type() {
		return this.data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public java.math.BigDecimal getData_precision() {
		return this.data_precision;
	}

	public void setData_precision(java.math.BigDecimal data_precision) {
		this.data_precision = data_precision;
	}

	public java.math.BigDecimal getData_scale() {
		return this.data_scale;
	}

	public void setData_scale(java.math.BigDecimal data_scale) {
		this.data_scale = data_scale;
	}

	public String getData_default() {
		return this.data_default;
	}

	public void setData_default(String data_default) {
		this.data_default = data_default;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[OracleColumnInfo:");
		buffer.append(" column_name: ");
		buffer.append(column_name);
		buffer.append(" data_type: ");
		buffer.append(data_type);
		buffer.append(" data_precision: ");
		buffer.append(data_precision);
		buffer.append(" data_scale: ");
		buffer.append(data_scale);
		buffer.append(" data_default: ");
		buffer.append(data_default);
		buffer.append(" comments: ");
		buffer.append(comments);
		buffer.append("]");
		return buffer.toString();
	}

}

class OracleColumnSearcher {

	public static OracleColumnInfo[] execute(IDBConfig config, String owner, String table) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, table, config.isConvertUnicode());

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleColumnInfo[] execute(Connection con, String owner, String table, boolean convertUnicode) throws Exception {
		List list = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, table, getDatabaseMajorVersion(con)));

			list = new ArrayList();
			while (rs.next()) {
				OracleColumnInfo info = new OracleColumnInfo();
				info.setColumn_name(rs.getString("COLUMN_NAME"));
				info.setData_type(rs.getString("DATA_TYPE"));
				info.setData_precision(rs.getBigDecimal("DATA_PRECISION"));
				info.setData_scale(rs.getBigDecimal("DATA_SCALE"));
				info.setData_default(rs.getString("DATA_DEFAULT"));
				info.setComments(rs.getString("COMMENTS"));

				if (convertUnicode) {
					info.setColumn_name(JDBCUnicodeConvertor.convert(info.getColumn_name()));
					info.setData_type(JDBCUnicodeConvertor.convert(info.getData_type()));
					info.setData_default(JDBCUnicodeConvertor.convert(info.getData_default()));
					info.setComments(JDBCUnicodeConvertor.convert(info.getComments()));
				}

				list.add(info);
			}
			return (OracleColumnInfo[]) list.toArray(new OracleColumnInfo[0]);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

	}

	private static int getDatabaseMajorVersion(Connection con) {
		int version = 0;
		try {
			version = con.getMetaData().getDatabaseMajorVersion();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		}
		return version;
	}

	// Oracle用SQL
	private static String getSQL(String owner, String table, int databaseProductMajorVersion) {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        COL.COLUMN_NAME");
		sb.append("        ,COL.DATA_TYPE");
		// sb.append(" ,COL.DATA_PRECISION");

		if (databaseProductMajorVersion <= 8) {
			// for Oracle8i
			// Oracle 8i では、ALL_TAB_COLUMNSにCHAR_LENGTHが存在しなため、DATA_TYPEを使う。
			// NUMBER型の場合は、DATA_PRECISIONを見るように変更したが、他の型を追加する必要があるかも
			sb.append("        ,DECODE(COL.DATA_TYPE, 'NUMBER', COL.DATA_PRECISION, COL.DATA_LENGTH) DATA_PRECISION");
		} else {
			// for Oracle 9i, 10g
			// CHAR_LENGTHを使って、数値型の桁と文字型の桁を判定する
			sb.append("        ,DECODE(CHAR_LENGTH, 0, COL.DATA_PRECISION, CHAR_LENGTH) DATA_PRECISION"); // Oracle9i
			// or
			// Oracle10gのみ

		}

		sb.append("        ,COL.DATA_SCALE");
		sb.append("        ,COL.DATA_DEFAULT");
		sb.append("        ,COM.COMMENTS");
		sb.append("    FROM");
		sb.append("        ALL_TAB_COLUMNS COL");
		sb.append("        ,ALL_COL_COMMENTS COM");
		sb.append("    WHERE");
		sb.append("        COL.OWNER = COM.OWNER");
		sb.append("        AND COL.TABLE_NAME = COM.TABLE_NAME");
		sb.append("        AND COL.COLUMN_NAME = COM.COLUMN_NAME");
		sb.append("        AND COL.OWNER = '" + SQLUtil.encodeQuotation(owner) + "'");
		sb.append("        AND COL.TABLE_NAME = '" + SQLUtil.encodeQuotation(table) + "'");
		sb.append("    ORDER BY");
		sb.append("        COL.COLUMN_ID");

		return sb.toString();
	}

}