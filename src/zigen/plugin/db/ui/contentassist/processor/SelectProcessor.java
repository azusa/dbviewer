/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist.processor;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.ui.contentassist.ContentAssistUtil;
import zigen.plugin.db.ui.contentassist.ContentInfo;
import zigen.plugin.db.ui.contentassist.ProcessorInfo;
import zigen.plugin.db.ui.contentassist.SQLProposalCreator2;
import zigen.sql.parser.INode;
import zigen.sql.parser.SqlParser;
import zigen.sql.parser.ast.ASTAlias;
import zigen.sql.parser.ast.ASTFrom;
import zigen.sql.parser.ast.ASTSelectStatement;
import zigen.sql.parser.ast.ASTTable;

public class SelectProcessor extends DefaultProcessor {

	public SelectProcessor(List proposals, ProcessorInfo info) {
		super(proposals, info);
	}

	public void createProposals(ASTSelectStatement st) {
		ContentInfo ci = null;
		try {
			ci = new ContentInfo(ContentAssistUtil.getIDBConfig());

			if (ci.isConnected()) {
				TableInfo[] currentTableInfos = ci.getTableInfo(); // �J�����g�X�L�[�}�ɑΉ�����e�[�u����񃊃X�g�擾

				ASTFrom fromList = super.findASTFrom(st);
				int fromItemCount = fromList != null ? super.getSizeRemoveComma(fromList) : 0;

				switch (currentScope) {
				case SqlParser.SCOPE_SELECT:
				case SqlParser.SCOPE_WHERE:
				case SqlParser.SCOPE_BY: // ORDER BY, GROUP BY
					if (fromItemCount == 0) {
						// �e�[�u���ꗗ���\������邽�߁A������悤�ɏC�� 2007/12/06
						break; //
					} else if (fromItemCount == 1) {
						if (isAfterPeriod) {
							createColumnProposal(findFromNode(fromList, word));
						} else {
							// �P��e�[�u���w�� AND �s���I�h�ȍ~
							int _offset = wordGroup.indexOf('.');
							if (_offset > 0) {
								String w_table = wordGroup.substring(0, _offset);
								createColumnProposal(findFromNode(fromList, w_table));
							} else {
								// �P��e�[�u���̏ꍇ�̓J�������X�g��\��
								createColumnProposal(fromList.getChild(0));

								// �e�[�u�������X�g��\��
								createTableProposal(currentTableInfos, getFromNodes(fromList));
							}
						}

						break;
					} else {

						if (isAfterPeriod) {
							// �����e�[�u���w�� AND �s���I�h�ŏI����Ă���ꍇ�́A�J�������X�g���o��
							createColumnProposal(findFromNode(fromList, word));
						} else {
							// �����e�[�u���w�� AND �s���I�h�ȍ~
							int _offset = wordGroup.indexOf('.');
							if (_offset > 0) {
								String w_table = wordGroup.substring(0, _offset);
								createColumnProposal(findFromNode(fromList, w_table));
							} else {
								// �����e�[�u���̎w�� AND �e�[�u�������X�g��\��
								createTableProposal(currentTableInfos, getFromNodes(fromList));
							}
						}
						break;
					}
				case SqlParser.SCOPE_FROM:

					if (isAfterPeriod) {
						String correctSchemaName = ci.findCorrectSchema(word);
						if (correctSchemaName != null)
							SQLProposalCreator2.addProposal(proposals, ci.getTableInfo(correctSchemaName), pinfo);// �e�[�u�����X�g��\������

					} else {
						// �X�L�[�}�w��̏ꍇ�i�s���I�h�ȍ~�j
						int _offset = wordGroup.indexOf('.');
						if (_offset > 0) {
							String w_schema = wordGroup.substring(0, _offset);
							String correctSchemaName = ci.findCorrectSchema(w_schema);
							if (correctSchemaName != null)
								SQLProposalCreator2.addProposal(proposals, ci.getTableInfo(correctSchemaName), pinfo);// �e�[�u�����X�g��\������
						} else {
							// �e�[�u�����X�g��\������
							SQLProposalCreator2.addProposal(proposals, currentTableInfos, pinfo);
						}

					}

					break;
				}

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {
			switch (currentScope) {
			case SqlParser.SCOPE_SELECT:
			case SqlParser.SCOPE_WHERE:
				SQLProposalCreator2.addProposalForFunction(proposals, rule.getFunctionNames(), pinfo);// �֐��̓o�^
				SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);// SQL�L�[���[�h�̓o�^
				break;

			case SqlParser.SCOPE_FROM:
			default:
				SQLProposalCreator2.addProposal(proposals, ci.getSchemaInfos(), pinfo);// �X�L�[�}�̈ꗗ
				SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);// SQL�L�[���[�h�̓o�^

				break;
			}

		}

	}

	private void createTableProposal(TableInfo[] infos, INode[] target) {
		if (target != null) {
			List list = new ArrayList();
			for (int i = 0; i < target.length; i++) {
				INode node = target[i];
				if (node instanceof ASTTable) {
					ASTTable table = (ASTTable) node;
					TableInfo info = findTableInfo(infos, table.getTableName());
					if (info != null) {
						if (table.hasAlias()) {

							String comment = info.getComment();
							if (comment == null)
								comment = info.getName();
							list.add(new TableInfo(table.getAliasName(), comment + Messages.getString("SelectProcessor.0"))); //$NON-NLS-1$
						} else {
							list.add(new TableInfo(table.getTableName(), info.getComment()));
						}
					}
				} else if (node instanceof ASTAlias) {
					ASTAlias alias = (ASTAlias) node;
					if (alias.getAliasName() != null) {
						list.add(new TableInfo(alias.getAliasName(), Messages.getString("SelectProcessor.1"))); //$NON-NLS-1$
					}
				}
			}
			SQLProposalCreator2.addProposal(proposals, (TableInfo[]) list.toArray(new TableInfo[0]), pinfo);

		}
	}

}
