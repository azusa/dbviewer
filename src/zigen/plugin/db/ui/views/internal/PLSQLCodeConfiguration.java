/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;

public class PLSQLCodeConfiguration extends SQLCodeConfiguration {

	public PLSQLCodeConfiguration(ColorManager colorManager) {
		super(colorManager);
	}

	protected ISQLTokenScanner getSQLKeywordScanner() {
		if (keyWorkScanner == null) {
			keyWorkScanner = new PLSQLKeywordScanner(colorManager);
		} else {
			keyWorkScanner.initialize();
		}
		return keyWorkScanner;
	}

	// �R���e���c�A�V�X�g�p�̃I�[�o���C�h���\�b�h
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		// ������
		return null;
	}

	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new PlsqlAnnotationHover();
	}
}
