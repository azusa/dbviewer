/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

public class ConstraintRoot extends TreeNode {
	
	private static final long serialVersionUID = 1L;
	
	private String name = "CONSTRAINT";
	
	private String type = "";
	
	private String paramater = "";
	
	public ConstraintRoot() {}
	
	public void addConstraint(Constraint model) {
		addChild(model);
	}
	
	public String getName() {
		return name;
	}
	
	public String getParamater() {
		return paramater;
	}
	
	public String getType() {
		return type;
	}
	
}
