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
import zigen.sql.parser.ast.ASTInsertStatement;
import zigen.sql.parser.ast.ASTTable;

public class InsertProcessor extends DefaultProcessor {

	public InsertProcessor(List proposals, ProcessorInfo info) {
		super(proposals, info);
	}

	public void createProposals(ASTInsertStatement st) {

		String[] modifiers = rule.getKeywordNames();
		try {
			ContentInfo ci = new ContentInfo(ContentAssistUtil.getIDBConfig());

			if (ci.isConnected()) {
				TableInfo[] currentTableInfos = ci.getTableInfo(); // �J�����g�X�L�[�}�ɑΉ�����e�[�u����񃊃X�g�擾
				ASTTable table = super.findASTTable(st);

				switch (currentScope) {
				case SqlParser.SCOPE_INSERT:
					modifiers = new String[] {"into" //$NON-NLS-1$
					};
					break;

				case SqlParser.SCOPE_INTO:
					// �e�[�u�����X�g��\������
					//SQLProposalCreator2.addProposal(proposals, currentTableInfos, pinfo);
					if (isAfterPeriod) {
						addTableProposalBySchema(ci, word);

					} else {
						int _offset = wordGroup.indexOf('.');
						if (_offset > 0) {
							String w_schema = wordGroup.substring(0, _offset);
							addTableProposalBySchema(ci, w_schema);
						} else {
							SQLProposalCreator2.addProposal(proposals, currentTableInfos, pinfo);// �e�[�u�����̈ꗗ
							SQLProposalCreator2.addProposal(proposals, ci.getSchemaInfos(), pinfo);// �X�L�[�}�̈ꗗ
						}

					}

					break;

				case SqlParser.SCOPE_INTO_COLUMNS:
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
