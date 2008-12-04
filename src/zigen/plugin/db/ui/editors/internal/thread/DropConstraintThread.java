/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.thread;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ITable;

public class DropConstraintThread extends AbstractSQLThread {

	private Constraint constraint;

	public DropConstraintThread(ITable table, Constraint constraint) {
		super(table);
		this.constraint = constraint;
	}

	public String[] createSQL(ISQLCreatorFactory factory, ITable table) {
		String sql = factory.createDropConstraintDDL(constraint.getName(), constraint.getType());
		return new String[] {
			sql
		};
	}

}
