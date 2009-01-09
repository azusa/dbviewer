/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPluginFormatRule;
import zigen.plugin.db.preference.SQLEditorPreferencePage;

/**
 * SQLKeywordScannerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/04/07 ZIGEN create. [002] 2005/05/29 ZIGEN SQLキーワードのシンタックスハイライトの修正.
 * 
 */
public class SQLKeywordScanner extends RuleBasedScanner implements ISQLTokenScanner {

	static class WordDetector implements IWordDetector {

		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}

		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}
	}

	static class WhitespaceDetector implements IWhitespaceDetector {

		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}

	private ColorManager colorManager;

	protected DbPluginFormatRule rule;

	public SQLKeywordScanner(ColorManager colorManager) {
		this.colorManager = colorManager;

		rule = DbPluginFormatRule.getInstance();

		// getInstanceで一度margeしているため、falseで起動
		initialize(false);
	}

	public void initialize() {
		initialize(true);
	}

	public void initialize(boolean marge) {

		if (marge)
			rule.margeTemplate();

		// 記号などの色をデフォルト色にするための指定を追加
		setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT))));

		IRule[] rules = new IRule[2];
		IToken other = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT)));
		IToken keyword = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_KEYWORD), null, SWT.BOLD));
		IToken function = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_FUNCTION), null, SWT.BOLD));
		WordRule wordRule = new WordRule(new WordDetector(), other);


		// SQLキーワードのシンタックスハイライト
		String[] keywords = rule.getKeywordNames();
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key, keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}

		// 関数のシンタックスハイライト(テンプレート分は除いたもの）
		String[] functions = rule.getFunctionNames();
		for (int i = 0; i < functions.length; i++) {
			String name = functions[i];
			wordRule.addWord(name, function);
			wordRule.addWord(name.toLowerCase(), function);

		}

		rules[0] = wordRule;
		rules[1] = new WhitespaceRule(new WhitespaceDetector());
		setRules(rules);

	}

}
