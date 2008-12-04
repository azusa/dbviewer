/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
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
 * SQLCodeConfiguration�N���X.
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

	// �����\���p�������\�b�h
	// �I�[�o�[���C�h
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		// �\�����R���T�C���[�̍쐬
		reconciler = new PresentationReconciler();
		InitializeDamagerRepairer();
		return reconciler;
	}

	/** �F�̐ݒ��ύX�����ꍇ�ɂ��̃��\�b�h���Ăяo���܂� */
	public void updatePreferences(IDocument document) {

		getSQLKeywordScanner().initialize();

		// �R�����g�����̃X�^�C����ݒ�
		NonRuleBasedDamagerRepairer commentDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_COMMENT)));
		commentDR.setDocument(document);

		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		// �����񃊃e�����̃X�^�C����ݒ�
		NonRuleBasedDamagerRepairer stringDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_STRING)));
		stringDR.setDocument(document);

		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);

		// �f�t�H���g�̈�̃X�^�C����ݒ�

		// DefaultDamagerRepairer defaultDR = new
		// DefaultDamagerRepairer(getSQLKeywordScanner());
		// reconciler.setDamager(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		// reconciler.setRepairer(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		//	

//		// ���L���̐F���f�t�H���g�ɂȂ�Ȃ����߁A�R�����g������
//		NonRuleBasedDamagerRepairer defaultDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT)));
//		defaultDR.setDocument(document);
//		reconciler.setDamager(defaultDR, IDocument.DEFAULT_CONTENT_TYPE);
//		reconciler.setRepairer(defaultDR, IDocument.DEFAULT_CONTENT_TYPE);
//		// �����܂�

	}

	private void InitializeDamagerRepairer() {

		// �R�����g�����̃X�^�C����ݒ�
		DefaultDamagerRepairer commentDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_COMMENT))));
		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		// �����񃊃e�����̃X�^�C����ݒ�
		DefaultDamagerRepairer stringDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_STRING))));
		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);

		// �f�t�H���g�̈�̃X�^�C����ݒ�
		DefaultDamagerRepairer keywordDR = new DefaultDamagerRepairer(getSQLKeywordScanner());
		reconciler.setDamager(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);

	}

	// �R���e���c�A�V�X�g�p�̃I�[�o���C�h���\�b�h
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();

		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		// IContentAssistProcessor p = new SQLCompletionProcessor();
		// assistant.setContentAssistProcessor(p,
		// IDocument.DEFAULT_CONTENT_TYPE);

		// �����(JavaCC)
		// SQLContentAssistantProcessor processor = new
		// SQLContentAssistantProcessor();

		// �V���(�蓮)
		SQLContentAssistantProcessor2 processor = new SQLContentAssistantProcessor2();
		assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);

		// IInformationControlCreator�̐ݒ�
		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));

		// �����A�N�e�B�x�[�V������L���ɂ���ꍇ��true
		assistant.enableAutoActivation(true);

		int delayTime = preferenceStore.getInt(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_AUTO_ACTIVATE_DELAY_TIME);
		// �����A�N�e�B�x�[�V�����܂ł̎���
		assistant.setAutoActivationDelay(delayTime);

		// ��₪�P�����Ȃ��ꍇ�́A�����I�Ɍ���}������ꍇ��true
		assistant.enableAutoInsert(true);

		// //assistant.install(sourceViewer);
		return assistant;
	}

	/*
	 * �����ҏW public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer
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
