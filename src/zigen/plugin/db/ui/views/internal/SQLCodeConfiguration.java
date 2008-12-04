/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.CodeAssistPreferencePage;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.contentassist.SQLContentAssistantProcessor2;

/**
 * SQLCodeConfigurationクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/10 ZIGEN create.
 * 
 */
public class SQLCodeConfiguration extends SourceViewerConfiguration {

    ISQLTokenScanner keyWorkScanner;

	ColorManager colorManager;

	PresentationReconciler reconciler;

	IPreferenceStore preferenceStore;

	public SQLCodeConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
		this.preferenceStore = DbPlugin.getDefault().getPreferenceStore();
	}

	protected ISQLTokenScanner getSQLKeywordScanner() {
		if (keyWorkScanner == null) {
			keyWorkScanner = new SQLKeywordScanner(colorManager);
		}else{
		    keyWorkScanner.initialize();
		}
		return keyWorkScanner;
	}

	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE,
				SQLPartitionScanner.SQL_COMMENT,
				SQLPartitionScanner.SQL_STRING
		};
	}

	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	};

	// 強調表示用実装メソッド
	// オーバーライド
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		// 表示リコンサイラーの作成
		reconciler = new PresentationReconciler();
		InitializeDamagerRepairer();
		return reconciler;
	}

	/** 色の設定を変更した場合にこのメソッドを呼び出します */
	public void updatePreferences(IDocument document) {

		getSQLKeywordScanner().initialize();

		// コメント部分のスタイルを設定
		NonRuleBasedDamagerRepairer commentDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_COMMENT)));
		commentDR.setDocument(document);

		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		// 文字列リテラルのスタイルを設定
		NonRuleBasedDamagerRepairer stringDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_STRING)));
		stringDR.setDocument(document);

		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);

		// デフォルト領域のスタイルを設定

		// DefaultDamagerRepairer defaultDR = new
		// DefaultDamagerRepairer(getSQLKeywordScanner());
		// reconciler.setDamager(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		// reconciler.setRepairer(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		//	

//		// ↓記号の色がデフォルトにならないため、コメントを解除
//		NonRuleBasedDamagerRepairer defaultDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT)));
//		defaultDR.setDocument(document);
//		reconciler.setDamager(defaultDR, IDocument.DEFAULT_CONTENT_TYPE);
//		reconciler.setRepairer(defaultDR, IDocument.DEFAULT_CONTENT_TYPE);
//		// ここまで

	}

	private void InitializeDamagerRepairer() {

		// コメント部分のスタイルを設定
		DefaultDamagerRepairer commentDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_COMMENT))));
		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		// 文字列リテラルのスタイルを設定
		DefaultDamagerRepairer stringDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_STRING))));
		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);

		// デフォルト領域のスタイルを設定
		DefaultDamagerRepairer keywordDR = new DefaultDamagerRepairer(getSQLKeywordScanner());
		reconciler.setDamager(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);

	}

	// コンテンツアシスト用のオーバライドメソッド
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();

		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		// IContentAssistProcessor p = new SQLCompletionProcessor();
		// assistant.setContentAssistProcessor(p,
		// IDocument.DEFAULT_CONTENT_TYPE);

		// 旧解析(JavaCC)
		// SQLContentAssistantProcessor processor = new
		// SQLContentAssistantProcessor();

		// 新解析(手動)
		SQLContentAssistantProcessor2 processor = new SQLContentAssistantProcessor2();
		assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);

		// IInformationControlCreatorの設定
		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));

		// 自動アクティベーションを有効にする場合はtrue
		assistant.enableAutoActivation(true);

		int delayTime = preferenceStore.getInt(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_AUTO_ACTIVATE_DELAY_TIME);
		// 自動アクティベーションまでの時間
		assistant.setAutoActivationDelay(delayTime);

		// 候補が１つしかない場合は、自動的に候補を挿入する場合はtrue
		assistant.enableAutoInsert(true);

		// //assistant.install(sourceViewer);
		return assistant;
	}

	/*
	 * 自動編集 public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer
	 * sourceViewer, String contentType) { IAutoEditStrategy[] parentStrategies =
	 * super.getAutoEditStrategies(sourceViewer, contentType);
	 * IAutoEditStrategy[] newStrategies = new
	 * IAutoEditStrategy[parentStrategies.length+1];
	 * System.arraycopy(parentStrategies, 0, newStrategies, 0,
	 * parentStrategies.length); newStrategies[newStrategies.length-1] = new
	 * SQLAutoEditStrategy();
	 * 
	 * return newStrategies; }
	 */

	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		ContentFormatter formatter = new ContentFormatter();
		formatter.setFormattingStrategy(new SQLFormattingStrategy(sourceViewer), IDocument.DEFAULT_CONTENT_TYPE);
		formatter.setFormattingStrategy(new SQLFormattingStrategy(sourceViewer), SQLPartitionScanner.SQL_STRING);
		
		return formatter;
	}

}
