/*
 * Copyright (c) 2007Å|2009 ZIGEN
 * Eclipse Public License - v 1.0 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import zigen.plugin.db.ui.views.TreeView;

public class ToggleLinkingAction extends AbstractToggleLinkingAction {

	TreeView view;

	public ToggleLinkingAction(TreeView view) {
		this.view = view;
		setChecked(view.isLinkingEnabled());
	}

	/**
	 * Runs the action.
	 */
	public void run() {
		view.setLinkingEnabled(isChecked());
	}

}
