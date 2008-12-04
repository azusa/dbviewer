/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kry.sql.format.SqlFormatRule;
import kry.sql.tokenizer.TokenUtil;

import org.eclipse.jface.text.templates.Template;

import zigen.plugin.db.preference.SQLTemplateEditorUI;
import zigen.plugin.db.ui.views.internal.SQLContextType;

public class DbPluginFormatRule {

	private static DbPluginFormatRule instance;

	private SqlFormatRule fRule;

	private String[] fDefaultKeywords;
	private String[] fDefaultDataTypes;
	private String[] fDefaultFunctions;

	private List fFunctionList;
	
	private Template[] templates;
	/**
	 * インスタンス生成
	 * 
	 * @param<code>_instance</code>
	 */
	public synchronized static DbPluginFormatRule getInstance() {
		if (instance == null) {
			instance = new DbPluginFormatRule();
		}
		return instance;
	}

	/**
	 * コンストラクタ
	 */
	private DbPluginFormatRule() {
		fRule = new SqlFormatRule();
		fRule.setRemoveEmptyLine(true); // 空行を削除する
		fDefaultFunctions = fRule.getFunctions();
		
		
		fDefaultKeywords = TokenUtil.KEYWORD;
		fDefaultDataTypes = TokenUtil.KEYWORD_DATATYPE;
	
		// Templateに登録している関数をマージする
		margeTemplate();
	}
	
	public void margeTemplate(){
		// デフォルトに一度戻す
		fFunctionList = new LinkedList(Arrays.asList(fDefaultFunctions));

		// テンプレートを検索して追加する
		templates = SQLTemplateEditorUI.getDefault().getTemplateStore().getTemplates(SQLContextType.CONTEXT_TYPE_FUNCTION);
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			String func = template.getName().toUpperCase();
			if(!fFunctionList.contains(func)){
				fFunctionList.add(func);	// 大文字で登録する
			}
		}
		fRule.setFunctions((String[])fFunctionList.toArray(new String[0]));
		
	}
	
	/**
	 * フォーマッターに登録されている関数＋テンプレート関数
	 * @return
	 */
	public String[] getFunctionNames(){
		return (String[])fFunctionList.toArray(new String[0]);
	}
	
	/**
	 * SQLキーワード
	 * @return
	 */
	public String[] getKeywordNames(){
		return fDefaultKeywords;
	}
	
	/**
	 * データタイプ
	 * @return
	 */
	public String[] getDataTypeNames(){
		return fDefaultDataTypes;
	}
	
	
	/**
	 * デフォルトの関数に戻す
	 */
	public void setDefaultFunction(){
		fRule.setFunctions(fDefaultFunctions);
	}

	public SqlFormatRule getRule() {
		return fRule;
	}

	public void addFunctions(String[] addFunctions){
		fRule.addFunctions(addFunctions);
	}
	
	public void subtractFunctions(String[] subtractFunctions){
		fRule.subtractFunctions(subtractFunctions);
	}
	
	public void setFunctions(String[] functions){
		fRule.setFunctions(functions);
	}
	
}
