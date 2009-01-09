/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
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
				TableInfo[] currentTableInfos = ci.getTableInfo(); // カレントスキーマに対応するテーブル情報リスト取得

				ASTFrom fromList = super.findASTFrom(st);
				int fromItemCount = fromList != null ? super.getSizeRemoveComma(fromList) : 0;

				switch (currentScope) {
				case SqlParser.SCOPE_SELECT:
				case SqlParser.SCOPE_WHERE:
				case SqlParser.SCOPE_BY: // ORDER BY, GROUP BY
					if (fromItemCount == 0) {
						// テーブル一覧が表示されるため、抜けるように修正 2007/12/06
						break; //
					} else if (fromItemCount == 1) {
						if (isAfterPeriod) {
							createColumnProposal(findFromNode(fromList, word));
						} else {
							// 単一テーブル指定 AND ピリオド以降
							int _offset = wordGroup.indexOf('.');
							if (_offset > 0) {
								String w_table = wordGroup.substring(0, _offset);
								createColumnProposal(findFromNode(fromList, w_table));
							} else {
								// 単一テーブルの場合はカラムリストを表示
								createColumnProposal(fromList.getChild(0));

								// テーブル名リストを表示
								createTableProposal(currentTableInfos, getFromNodes(fromList));
							}
						}

						break;
					} else {

						if (isAfterPeriod) {
							// 複数テーブル指定 AND ピリオドで終わっている場合は、カラムリストを出す
							createColumnProposal(findFromNode(fromList, word));
						} else {
							// 複数テーブル指定 AND ピリオド以降
							int _offset = wordGroup.indexOf('.');
							if (_offset > 0) {
								String w_table = wordGroup.substring(0, _offset);
								createColumnProposal(findFromNode(fromList, w_table));
							} else {
								// 複数テーブルの指定 AND テーブル名リストを表示
								createTableProposal(currentTableInfos, getFromNodes(fromList));
							}
						}
						break;
					}
				case SqlParser.SCOPE_FROM:

					if (isAfterPeriod) {
						String correctSchemaName = ci.findCorrectSchema(word);
						if (correctSchemaName != null)
							SQLProposalCreator2.addProposal(proposals, ci.getTableInfo(correctSchemaName), pinfo);// テーブルリストを表示する

					} else {
						// スキーマ指定の場合（ピリオド以降）
						int _offset = wordGroup.indexOf('.');
						if (_offset > 0) {
							String w_schema = wordGroup.substring(0, _offset);
							String correctSchemaName = ci.findCorrectSchema(w_schema);
							if (correctSchemaName != null)
								SQLProposalCreator2.addProposal(proposals, ci.getTableInfo(correctSchemaName), pinfo);// テーブルリストを表示する
						} else {
							// テーブルリストを表示する
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
				SQLProposalCreator2.addProposalForFunction(proposals, rule.getFunctionNames(), pinfo);// 関数の登録
				SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);// SQLキーワードの登録
				break;

			case SqlParser.SCOPE_FROM:
			default:
				SQLProposalCreator2.addProposal(proposals, ci.getSchemaInfos(), pinfo);// スキーマの一覧
				SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);// SQLキーワードの登録

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
