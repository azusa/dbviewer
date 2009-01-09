/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import kry.sql.format.ISqlFormat;
import kry.sql.format.SqlFormat;
import kry.sql.format.SqlFormatException;
import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import blanco.commons.sql.format.BlancoSqlFormatter;
import blanco.commons.sql.format.BlancoSqlFormatterException;
import blanco.commons.sql.format.BlancoSqlRule;

/**
 * SQLStringUtilクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/08/06 ZIGEN create.
 * 
 */
public class SQLFormatter {

	/**
	 * SQLフォーマット処理（By blancoCommons)
	 * 
	 * @param preSql
	 *            整形前のSQL文
	 * @return 整形後のSQL文
	 */
	public static String format(String preSql, int formatterType, boolean onPatch, int offset) {

		if (formatterType == SQLFormatPreferencePage.TYPE_DBVIEWER) {
			try {
				ISqlFormat formatter = new SqlFormat(DbPlugin.getSqlFormatRult());

				// このFormatterは、改行コード=システム改行コードになっているため、変換はしない
				return formatter.format(preSql, offset);
			} catch (SqlFormatException e) {
				DbPlugin.getDefault().log(e);
			}
			return preSql;
		} else {
			try {
				// BlancoSqlFormatterを使ったSQLフォーマット処理
				BlancoSqlFormatter formatter = new BlancoSqlFormatter(new BlancoSqlRule());
				String result = formatter.format(preSql);
				if (onPatch) {
					// 関数などの"("の前に半角空白が入るのを取り除く修正を加える
					result = SQLFormatterPach.format(result);
				}

				// BlancoFormatterは\nが改行コードになっているため、システム改行コードに変換する
				return StringUtil.convertLineSep(result, DbPluginConstant.LINE_SEP);

			} catch (BlancoSqlFormatterException e) {
				DbPlugin.getDefault().log(e);
			}
			return preSql;

		}

	}

	/**
	 * SQLフォーマット処理（By blancoCommons)
	 * 
	 * @param preSql
	 *            整形前のSQL文
	 * @return 整形後のSQL文
	 */
	public static String format(String preSql, int formatterType, boolean onPatch) {
		return format(preSql, formatterType, onPatch, 0);

	}

	/**
	 * 指定されたSQL文のアンフォーマットします。 ※無駄なブランクや改行を削除します。 ※コメント行も削除されます。 注意事項：Oracleのヒント文を使っている場合は表示されません
	 * 
	 * @param sql
	 * @return
	 */
	public static String unformat(String sql) {
		ISqlFormat formatter = new SqlFormat(DbPlugin.getSqlFormatRult());
		return formatter.unFormat(sql);

		// StringBuffer sb = new StringBuffer();
		// SqlStreamTokenizer tokenizer = new SqlStreamTokenizer(sql);
		// try {
		// for (;;) {
		// switch (tokenizer.nextToken()) {
		// case SqlStreamTokenizer.TT_EOF:
		// return sb.toString();
		//
		// case SqlStreamTokenizer.TT_WORD:
		// sb.append(tokenizer.getToken());
		// sb.append(SqlStreamTokenizer.TT_SPACE);
		// break;
		//
		// case SqlStreamTokenizer.TT_QUOTE:
		// sb.append(tokenizer.getToken());
		// sb.append(SqlStreamTokenizer.TT_SPACE);
		// break;
		//
		// case SqlStreamTokenizer.TT_DOUBLE_QUOTE:
		// sb.append(tokenizer.getToken());
		// sb.append(SqlStreamTokenizer.TT_SPACE);
		// break;
		//
		// case SqlStreamTokenizer.TT_COMMA:
		// String wk = sb.toString().trim();
		// sb = new StringBuffer(wk);
		// sb.append(SqlStreamTokenizer.TT_COMMA);
		// sb.append(SqlStreamTokenizer.TT_SPACE);
		// break;
		//
		// default:
		// sb.append((char) tokenizer.ttype);
		// sb.append(SqlStreamTokenizer.TT_SPACE);
		// break;
		// }
		//
		// }
		//
		// } catch (Exception e) {
		// DbPlugin.log(e);
		// return sql;
		// }

	}

	/**
	 * ORDER BY句の前後で分割する
	 * 
	 * @param whereCause
	 * @return
	 */
	public static String[] splitOrderCause(String whereCause) {
		String[] result = new String[2];
		StringBuffer main = new StringBuffer();
		StringBuffer order = new StringBuffer();

		if (whereCause == null) {
			result[0] = main.toString();
			result[1] = order.toString();
			return result;
		}

		SqlStreamTokenizer tokenizer = new SqlStreamTokenizer(whereCause);
		boolean isOrderCause = false;
		try {
			String s = null;
			for (;;) {
				switch (tokenizer.nextToken()) {
					case SqlStreamTokenizer.TT_EOF:
						result[0] = main.toString();
						result[1] = order.toString();
						return result;

					case SqlStreamTokenizer.TT_COMMA:

						if (isOrderCause) {
							order = new StringBuffer(order.toString().trim());
							order.append(SqlStreamTokenizer.TT_COMMA);
							order.append(SqlStreamTokenizer.TT_SPACE);
						} else {
							main = new StringBuffer(main.toString().trim());
							main.append(SqlStreamTokenizer.TT_COMMA);
							main.append(SqlStreamTokenizer.TT_SPACE);
						}
						break;

					default:
						s = tokenizer.getToken();
						if (isOrderCause || "order".equalsIgnoreCase(s)) {
							isOrderCause = true;
							order.append(s);
							// order.append(SqlStreamTokenizer.TT_SPACE);
						} else {
							main.append(s);
							// main.append(SqlStreamTokenizer.TT_SPACE);
						}
						break;
				}

			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return result;
	}

}

class SqlStreamTokenizer extends StreamTokenizer {

	public final static char TT_SPACE = ' ';

	public final static char TT_QUOTE = '\'';

	public final static char TT_DOUBLE_QUOTE = '"';

	public final static char TT_COMMA = ',';

	public final static int TT_WORD = StreamTokenizer.TT_WORD;

	private int tokenType = TT_EOF;

	private String token;

	public SqlStreamTokenizer(String sql) {
		super(new StringReader(sql));
		resetSyntax();
		wordChars('0', '9');
		wordChars('a', 'z');
		wordChars('A', 'Z');
		wordChars('_', '_');
		wordChars('.', '.'); // ピリオドでは分割しない

		// whitespaceChars(' ', ' '); // 空白は設定しない(そのまま取得する)
		whitespaceChars('\t', '\t');
		whitespaceChars('\n', '\n');
		whitespaceChars('\r', '\r');

		quoteChar(TT_QUOTE);
		quoteChar(TT_DOUBLE_QUOTE);
		// tokenizer.parseNumbers(); // 数字解析しない
		eolIsSignificant(false);// EOLの判定false
		slashStarComments(true);
		slashSlashComments(true);
	}

	public int nextToken() {
		try {
			tokenType = super.nextToken();

			switch (tokenType) {
				case SqlStreamTokenizer.TT_EOF:
					tokenType = SqlStreamTokenizer.TT_EOF;
					token = null;
					break;

				case SqlStreamTokenizer.TT_WORD:
					token = sval;
					tokenType = SqlStreamTokenizer.TT_WORD;
					break;
				case SqlStreamTokenizer.TT_QUOTE:
					token = "'" + sval + "'";
					break;
				case SqlStreamTokenizer.TT_DOUBLE_QUOTE:
					token = "\"" + sval + "\"";
					break;

				default:
					token = String.valueOf((char) ttype);
			}

		} catch (IOException e) {
			DbPlugin.log(e);
		}

		return tokenType;
	}

	public int getTokenType() {
		return tokenType;
	}

	public String getToken() {
		return token;
	}

}
