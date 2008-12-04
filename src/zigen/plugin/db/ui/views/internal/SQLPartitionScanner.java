/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/** コメント、文字列リテラルを認識するパーテーションスキャナ */
public class SQLPartitionScanner extends RuleBasedPartitionScanner {

	public static final String SQL_COMMENT = "__sql_comment"; //$NON-NLS-1$

	public static final String SQL_STRING = "__sql_string"; //$NON-NLS-1$

	public SQLPartitionScanner() {
		IPredicateRule[] rules = new IPredicateRule[4];

		IToken comment = new Token(SQL_COMMENT);
		// rules[0] = new MultiLineRule("=pod", "=cut", comment, (char) 0,
		// true);
		rules[0] = new MultiLineRule("/*", "*/", comment); //$NON-NLS-1$ //$NON-NLS-2$

		IToken string = new Token(SQL_STRING);
		// rules[2] = new SingleLineRule("\"", "\"", string, '\\');
		// rules[3] = new SingleLineRule("\'", "\'", string, '\\');
		// rules[1] = new MultiLineRule("\"", "\"", string, '\\');
		// rules[2] = new MultiLineRule("\'", "\'", string, '\\');

		// エスケープ無しに変更
		rules[1] = new MultiLineRule("\"", "\"", string); //$NON-NLS-1$ //$NON-NLS-2$
		rules[2] = new MultiLineRule("\'", "\'", string); //$NON-NLS-1$ //$NON-NLS-2$

		// JDBCで実行時に--は通らない（またはSQL整形も通らない）
		rules[3] = new EndOfLineRule("--", comment); // //$NON-NLS-1$

		setPredicateRules(rules);
	}

}
