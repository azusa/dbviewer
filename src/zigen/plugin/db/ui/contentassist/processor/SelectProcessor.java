/*
 * Copyright (c) 2007－2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
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
								createColumnProposal(fromList.getChild(0)); // 単一テーブルの場合はカラムリストを表示
								createTableProposal(ci, getFromNodes(fromList));// テーブル名の一覧
								SQLProposalCreator2.addProposal(proposals, ci.getSchemaInfos(), pinfo);// スキーマの一覧
							}
						}

						break;
					} else {

						if (isAfterPeriod) {
							if(!addTableProposalBySchema(ci, word)){
								// スキーマの後ではない場合(つまり、テーブル名の後)
								createColumnProposal(findFromNode(fromList, word));
							}
						} else {
							int _offset = wordGroup.lastIndexOf('.');
							if (_offset > 0) {
								String w_str = wordGroup.substring(0, _offset);
								int _offset2 = w_str.lastIndexOf('.');
								if (_offset2 > 0) {
									createColumnProposal(findFromNode(fromList, w_str));
								}else{
									if(!addTableProposalBySchema(ci, w_str)){
										createColumnProposal(findFromNode(fromList, w_str));
									}
								}
							} else {
								createTableProposal(ci, getFromNodes(fromList));// テーブル名の一覧
								SQLProposalCreator2.addProposal(proposals, ci.getSchemaInfos(), pinfo);// スキーマの一覧
							}
						}
						break;
					}
				case SqlParser.SCOPE_FROM:

					if (isAfterPeriod) {
						addTableProposalBySchema(ci, word);

					} else {
						int _offset = wordGroup.indexOf('.');
						if (_offset > 0) {
							String w_schema = wordGroup.substring(0, _offset);
							addTableProposalBySchema(ci, w_schema);
						} else {
							SQLProposalCreator2.addProposal(proposals, ci.getTableInfo(), pinfo);// テーブル名の一覧
							SQLProposalCreator2.addProposal(proposals, ci.getSchemaInfos(), pinfo);// スキーマの一覧
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
				SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);// SQLキーワードの登録
				break;
			}

		}

	}


	private void createTableProposal(ContentInfo ci, INode[] target) throws Exception{
		if (target != null) {
			List list = new ArrayList();
			for (int i = 0; i < target.length; i++) {
				INode node = target[i];
				if (node instanceof ASTTable) {
					ASTTable table = (ASTTable) node;
					TableInfo info;
					if(table.getSchemaName() != null){
						// スキーマ対応用
						info = findTableInfo(ci.getTableInfo(table.getSchemaName()), table.getTableName());
					}else{
						info = findTableInfo(ci.getTableInfo(), table.getTableName());
					}

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
