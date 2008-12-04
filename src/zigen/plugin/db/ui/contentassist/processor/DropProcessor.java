/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist.processor;

import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.ui.contentassist.ContentAssistUtil;
import zigen.plugin.db.ui.contentassist.ContentInfo;
import zigen.plugin.db.ui.contentassist.ProcessorInfo;
import zigen.plugin.db.ui.contentassist.SQLProposalCreator2;
import zigen.sql.parser.SqlParser;
import zigen.sql.parser.ast.ASTDropStatement;

public class DropProcessor extends DefaultProcessor {

	public DropProcessor(List proposals, ProcessorInfo info) {
		super(proposals, info);
	}

	public void createProposals(ASTDropStatement st) {

		String[] modifiers = rule.getKeywordNames();
		try {
			ContentInfo ci = new ContentInfo(ContentAssistUtil.getIDBConfig());

			if (ci.isConnected()) {

				TableInfo[] tinfos = ci.getTableInfo(); // �e�[�u����񃊃X�g�擾

				switch (currentScope) {
				case SqlParser.SCOPE_DROP:
					// Delete�̂��Ƃ́AFrom�̂ݕ\��
					modifiers = new String[] {
							"TABLE", //$NON-NLS-1$
							"VIEW", //$NON-NLS-1$
							"SYNONYM" //$NON-NLS-1$
					};
					
					break;

				case SqlParser.SCOPE_TARGET:
					// �e�[�u�����X�g��\������
					SQLProposalCreator2.addProposal(proposals, tinfos, pinfo);
					break;
				}

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {
			// SQL�L�[���[�h�̓o�^
			SQLProposalCreator2.addProposal(proposals, modifiers, pinfo);
		}

	}

}
