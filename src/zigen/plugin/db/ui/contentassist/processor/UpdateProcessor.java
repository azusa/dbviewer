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
import zigen.sql.parser.ast.ASTTable;
import zigen.sql.parser.ast.ASTUpdateStatement;

public class UpdateProcessor extends DefaultProcessor {

	public UpdateProcessor(List proposals, ProcessorInfo info) {
		super(proposals, info);
	}

	public void createProposals(ASTUpdateStatement st) {

		String[] modifiers = rule.getKeywordNames();
		try {
			ContentInfo ci = new ContentInfo(ContentAssistUtil.getIDBConfig());

			if (ci.isConnected()) {
				TableInfo[] tinfos = ci.getTableInfo(); // �e�[�u����񃊃X�g�擾
				ASTTable table = findASTTable(st);
				switch (currentScope) {
				case SqlParser.SCOPE_UPDATE:
					// �e�[�u�����X�g��\������
					SQLProposalCreator2.addProposal(proposals, tinfos, pinfo);
					break;
				case SqlParser.SCOPE_SET:
					createColumnProposal(table);
					break;
				case SqlParser.SCOPE_WHERE:
					createColumnProposal(table);
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
