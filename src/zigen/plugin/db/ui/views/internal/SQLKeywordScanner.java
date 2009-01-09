/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
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
 * SQLKeywordScanner�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/04/07 ZIGEN create. [002] 2005/05/29 ZIGEN SQL�L�[���[�h�̃V���^�b�N�X�n�C���C�g�̏C��.
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

		// getInstance�ň�xmarge���Ă��邽�߁Afalse�ŋN��
		initialize(false);
	}

	public void initialize() {
		initialize(true);
	}

	public void initialize(boolean marge) {

		if (marge)
			rule.margeTemplate();

		// �L���Ȃǂ̐F���f�t�H���g�F�ɂ��邽�߂̎w���ǉ�
		setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT))));

		IRule[] rules = new IRule[2];
		IToken other = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT)));
		IToken keyword = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_KEYWORD), null, SWT.BOLD));
		IToken function = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_FUNCTION), null, SWT.BOLD));
		WordRule wordRule = new WordRule(new WordDetector(), other);


		// SQL�L�[���[�h�̃V���^�b�N�X�n�C���C�g
		String[] keywords = rule.getKeywordNames();
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key, keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}

		// �֐��̃V���^�b�N�X�n�C���C�g(�e���v���[�g���͏��������́j
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
