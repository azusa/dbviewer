/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import zigen.plugin.db.core.IDBConfig;

/**
 * QueryViewEditorInput�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 *        [2] 2005/10/10 ZIGEN ����Query�̏ꍇtooltip�������Ȃ��Ȃ邽�߃R�����g�A�E�g [3] 2005/10/18
 *        ZIGEN Eclipse3.1.x�n��tooltip��ݒ肵�Ȃ��ƃG���[�ɂȂ�
 * 
 */

public class QueryViewEditorInput implements IEditorInput {
	private String tooltip;

	private String name;

	private IDBConfig config;;

	private String query;

	private String secondarlyId;

	public QueryViewEditorInput(IDBConfig config, String query, String secondarlyId) {
		super();
		this.config = config;
		this.query = query;
		this.name = config.getDbName();
		// [2] modify
		// this.tooltip = query;
		this.tooltip = Messages.getString("QueryViewEditorInput.0"); //$NON-NLS-1$
		this.secondarlyId = secondarlyId;

	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return tooltip;
	}

	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o instanceof QueryViewEditorInput) {
			QueryViewEditorInput input = (QueryViewEditorInput) o;

			if (config.getDbName().equals(input.config.getDbName())) {
				if (secondarlyId == null && input.secondarlyId == null) {
					return true;
				} else if (secondarlyId == null || input.secondarlyId == null) {
					return false;
				} else if (secondarlyId.equals(input.secondarlyId)) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}

		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public IDBConfig getConfig() {
		return config;
	}

	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getQuery() {
		return query;
	}

	public String getSecondarlyId() {
		return secondarlyId;
	}

}
