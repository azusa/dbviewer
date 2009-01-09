/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.csv;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;

/**
 * CSVWriterクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 * 
 */
public class CSVWriter {

	private String DEMILITER = ","; //$NON-NLS-1$

	private IDBConfig config = null;

	private CSVConfig csvConfig = null;

	private ICsvMappingFactory factory = null;

	/**
	 * コンストラクタ
	 * 
	 * @param config
	 */
	public CSVWriter(IDBConfig config, CSVConfig csvConfig) {
		this.config = config;
		this.csvConfig = csvConfig;
		this.factory = AbstractCsvMappingFactory.getFactory(config, csvConfig.isNonDoubleQuate());


		if (csvConfig.getSeparator() != null && !"".equals(csvConfig)) {
			this.DEMILITER = csvConfig.getSeparator();
		}

	}

	/**
	 * 実行
	 * 
	 * @param config
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public void execute() throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			execute(con, csvConfig.getQuery());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * コネクションを維持しながらSQLを連続実行する場合は直接起動する. その場合は、自分でConnectionのCloseを行うこと.
	 * 
	 * @param con
	 * @param query
	 * @throws Exception
	 */
	public void execute(Connection con, String query) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;

		PrintStream pout = null;

		try {
			pout = new PrintStream(new FileOutputStream(csvConfig.getCsvFile()), true, csvConfig.getCsvEncoding());

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			ResultSetMetaData meta = rs.getMetaData();

			if (!csvConfig.isNonHeader()) {
				// カラムラベルの書き込み
				writeColumnLabel(pout, meta);
			}

			// カラム値の書き込み
			writeColumnValue(pout, rs);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (pout != null)
				pout.close();
		}

	}

	// カラム名の書き込み
	private void writeColumnLabel(PrintStream pout, ResultSetMetaData meta) throws SQLException {
		int size = meta.getColumnCount();
		// カラムの書き込み
		for (int i = 0; i < size; i++) {
			if (i == size - 1) {
				pout.println("\"" + meta.getColumnLabel(i + 1) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				pout.print("\"" + meta.getColumnLabel(i + 1) + "\"" + DEMILITER); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	// カラムデータの書き込み
	private int writeColumnValue(PrintStream pout, ResultSet rs) throws Exception {
		ResultSetMetaData meta = rs.getMetaData();
		int size = meta.getColumnCount();
		int recordNo = 1;
		String value;
		while (rs.next()) {
			for (int i = 0; i < size; i++) {
				value = factory.getCsvValue(rs, i + 1);
				if (i == size - 1) {
					pout.println(value);
				} else {
					pout.print(value + DEMILITER);
				}
			}
			recordNo++;

		}
		return recordNo;
	}
}
