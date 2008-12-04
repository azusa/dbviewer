/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import zigen.plugin.db.preference.CSVPreferencePage;
import zigen.plugin.db.preference.CodeAssistPreferencePage;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.preference.URLPreferencePage;

/**
 * PreferenceInitializerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/09 ZIGEN create.
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		// PreferencePageのデフォルト値の設定する
		IPreferenceStore store = DbPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferencePage.P_MAX_VIEW_RECORD, 500);// 最大検索結果
		store.setDefault(PreferencePage.P_NULL_SYMBOL, "<NULL>");// NULLの表示 //$NON-NLS-1$
																	// //$NON-NLS-1$
		store.setDefault(PreferencePage.P_CHANGE_NULL_COLOR, true);// NULLのFont色を変更する
		store.setDefault(PreferencePage.P_MAX_HISTORY, 50); // 履歴//
															// SQL実行履歴(保存するSQL数)
		store.setDefault(PreferencePage.P_COLOR_BACKGROUND, "232,242,254");// 明細の背景色（偶数行) //$NON-NLS-1$
																			// //$NON-NLS-1$
		store.setDefault(PreferencePage.P_QUERY_TIMEOUT_FOR_COUNT, 10); // 10秒
		store.setDefault(PreferencePage.P_CONNECT_TIMEOUT, 5); // 5秒

		store.setDefault(PreferencePage.P_NO_CONFIRM_CONNECT_DB, false);
		
		store.setDefault(PreferencePage.P_LOCKE_COLUMN_WIDTH, false);
		
		store.setDefault(DBTreeViewPreferencePage.P_DISPLAY_TBL_COMMENT, true);// テーブルコメントの表示
		store.setDefault(DBTreeViewPreferencePage.P_DISPLAY_COL_COMMENT, true);// カラムコメントの表示
		store.setDefault(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_MODE, CodeAssistPreferencePage.MODE_PARSE);
		store.setDefault(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_CACHE_TIME, 60); // ６０秒
		store.setDefault(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_AUTO_ACTIVATE_DELAY_TIME, 200); // 200ms
																				// //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_KEYWORD, "128,0,64"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_STRING, "0,0,255"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_COMMENT, "0,128,0"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_DEFAULT, "0,0,0"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_BACK, "255,255,255"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_SELECT_BACK, "178,180,191"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_SELECT_FORE, "0,0,0"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_MATCHING, "178,180,191"); //$NON-NLS-1$
        store.setDefault(SQLEditorPreferencePage.P_COLOR_FIND_SCOPE, "219,215,217"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_CURSOR_LINE, "232,242,254"); //$NON-NLS-1$
		store.setDefault(SQLEditorPreferencePage.P_COLOR_FUNCTION, "209,16,1"); //$NON-NLS-1$

		// store.setDefault(SQLEditorPreferencePage.P_LINE_DEMILITER,
		// DbPluginConstant.LINE_SEP);
		store.setDefault(SQLEditorPreferencePage.P_SQL_DEMILITER, "/"); //$NON-NLS-1$
		//store.setDefault(SQLEditorPreferencePage.P_FORMAT_PATCH, true);

		saveURLPreferencePage(store, createURL());

		store.setDefault(CSVPreferencePage.P_ENCODING, DbPluginConstant.FILE_ENCODING); //$NON-NLS-1$
		store.setDefault(CSVPreferencePage.P_NON_DOUBLE_QUATE, false); //$NON-NLS-1$
		store.setDefault(CSVPreferencePage.P_NON_HEADER, false); //$NON-NLS-1$
		store.setDefault(CSVPreferencePage.P_DEMILITER, ","); //$NON-NLS-1$
		
		store.setDefault(PreferencePage.P_SQL_FILE_CHARSET, DbPluginConstant.FILE_ENCODING); //$NON-NLS-1$

		
		store.setDefault(SQLFormatPreferencePage.P_MAX_SQL_COUNT, 10); //$NON-NLS-1$
		store.setDefault(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE, SQLFormatPreferencePage.TYPE_DBVIEWER); //$NON-NLS-1$
		

		store.setDefault(SQLFormatPreferencePage.P_FORMAT_OPTION_TABSIZE, 4); //$NON-NLS-1$
		store.setDefault(SQLFormatPreferencePage.P_FORMAT_OPTION_DECODE, false); //$NON-NLS-1$
		store.setDefault(SQLFormatPreferencePage.P_FORMAT_OPTION_IN, false); //$NON-NLS-1$
		store.setDefault(SQLFormatPreferencePage.P_FORMAT_OPTION_IN, false); //$NON-NLS-1$

	}

	private List createURL() {
		// 接続文字列
		List list = new ArrayList();
		// for oracle
		list.add(new String[] {
				"oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@<host>:1521:<sid>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for symfoware
		list.add(new String[] {
				"fujitsu.symfoware.jdbc.RDADriver", "jdbc:symforda://<host>:2002/<database>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for mysql
		list.add(new String[] {
				"com.mysql.jdbc.Driver", "jdbc:mysql://<host>:3306/<database>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for derby
		list.add(new String[] {
				"org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:<database>;create=false"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for postgresql
		list.add(new String[] {
				"org.postgresql.Driver", "jdbc:postgresql://<host>:5432/<database>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for interbase
		list.add(new String[] {
				"interbase.interclient.Driver", "jdbc:interbase://<host>/<database>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for H2
		list.add(new String[] {
				"org.h2.Driver", "jdbc:h2:<DataBasePath>"}); //$NON-NLS-1$ //$NON-NLS-2$

		/*
		 * list.add(new String[]{ "org.h2.Driver",
		 * "jdbc:h2:tcp://127.0.0.1:9092/DataBasePath" });
		 */

		// for hsqldb
		list.add(new String[] {
				"org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:<databasename>;shutdown=true"}); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * list.add(new String[]{ "org.hsqldb.jdbcDriver",
		 * "jdbc:hsqldb:hsql://127.0.0.1:9001/databasename" });
		 */

		// for sqlserver2000
		list.add(new String[] {
				"com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=<DBName>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for sqlserver2005
		list.add(new String[] {
				"com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=<DBName>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for db2
		list.add(new String[] {
				"com.ibm.db2.jcc.DB2Driver", "jdbc:db2://127.0.0.1:50000/<DataBaseName>"}); //$NON-NLS-1$ //$NON-NLS-2$

		// for sqlite
		list.add(new String[] {
				"SQLite.JDBCDriver", "jdbc:sqlite:/<DataBasePath>"}); //$NON-NLS-1$ //$NON-NLS-2$

		return list;
	}

	// URLテンプレート用プロパティの保存
	private void saveURLPreferencePage(IPreferenceStore store, List properties) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < properties.size(); i++) {
			String[] wk = (String[]) properties.get(i);
			for (int j = 0; j < wk.length; j++) {
				if (j == 0) {
					sb.append(wk[j]);
				} else {
					sb.append(URLPreferencePage.SEP_COLS + wk[j]);
				}
			}
			sb.append(URLPreferencePage.SEP_ROWS);
		}
		store.setDefault(URLPreferencePage.P_URLDefine, sb.toString());

	}

}
