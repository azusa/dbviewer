/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
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
	 * �C���X�^���X����
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
	 * �R���X�g���N�^
	 */
	private DbPluginFormatRule() {
		fRule = new SqlFormatRule();
		fRule.setRemoveEmptyLine(true); // ��s���폜����
		fDefaultFunctions = fRule.getFunctions();
		
		
		fDefaultKeywords = TokenUtil.KEYWORD;
		fDefaultDataTypes = TokenUtil.KEYWORD_DATATYPE;
	
		// Template�ɓo�^���Ă���֐����}�[�W����
		margeTemplate();
	}
	
	public void margeTemplate(){
		// �f�t�H���g�Ɉ�x�߂�
		fFunctionList = new LinkedList(Arrays.asList(fDefaultFunctions));

		// �e���v���[�g���������Ēǉ�����
		templates = SQLTemplateEditorUI.getDefault().getTemplateStore().getTemplates(SQLContextType.CONTEXT_TYPE_FUNCTION);
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			String func = template.getName().toUpperCase();
			if(!fFunctionList.contains(func)){
				fFunctionList.add(func);	// �啶���œo�^����
			}
		}
		fRule.setFunctions((String[])fFunctionList.toArray(new String[0]));
		
	}
	
	/**
	 * �t�H�[�}�b�^�[�ɓo�^����Ă���֐��{�e���v���[�g�֐�
	 * @return
	 */
	public String[] getFunctionNames(){
		return (String[])fFunctionList.toArray(new String[0]);
	}
	
	/**
	 * SQL�L�[���[�h
	 * @return
	 */
	public String[] getKeywordNames(){
		return fDefaultKeywords;
	}
	
	/**
	 * �f�[�^�^�C�v
	 * @return
	 */
	public String[] getDataTypeNames(){
		return fDefaultDataTypes;
	}
	
	
	/**
	 * �f�t�H���g�̊֐��ɖ߂�
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
