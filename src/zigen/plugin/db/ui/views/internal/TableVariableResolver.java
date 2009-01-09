/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.ui.contentassist.ContentAssistUtil;
import zigen.plugin.db.ui.contentassist.ContentInfo;

public class TableVariableResolver extends TemplateVariableResolver {

	protected String[] resolveAll(TemplateContext context) {
		ContentInfo ci = new ContentInfo(ContentAssistUtil.getIDBConfig());
		String[] proposals = null;
		try {
			if (ci.isConnected()) {
				TableInfo[] infos = ci.getTableInfo(); // テーブル情報リスト取得
				proposals = new String[infos.length];
				for (int i = 0; i < infos.length; i++) {
					proposals[i] = infos[i].getName();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return proposals;
	}

	/*
	 * @see org.eclipse.jface.text.templates.TemplateVariableResolver#isUnambiguous(org.eclipse.jface.text.templates.TemplateContext)
	 */
	protected boolean isUnambiguous(TemplateContext context) {
		return resolve(context) != null;
	}
}
