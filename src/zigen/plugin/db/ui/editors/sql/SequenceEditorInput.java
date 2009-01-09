/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.sql;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ext.oracle.internal.OracleSequenceInfo;

/**
 * QueryViewEditorInputクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create. [2] 2005/10/10 ZIGEN 長いQueryの場合tooltipが見えなくなるためコメントアウト [3] 2005/10/18 ZIGEN Eclipse3.1.x系でtooltipを設定しないとエラーになる
 * 
 */

public class SequenceEditorInput implements IEditorInput {

	private String tooltip;

	private String name;

	private IDBConfig config;;

	private OracleSequenceInfo sequenceInfo;

	public SequenceEditorInput(IDBConfig config, OracleSequenceInfo sequenceInfo) {

		super();
		this.config = config;
		this.sequenceInfo = sequenceInfo;
		this.name = sequenceInfo.getSequence_name() + "[SEQUENCE]"; //$NON-NLS-1$
		this.tooltip = this.name;

	} /*
		 * @see org.eclipse.ui.IEditorInput#exists()
		 */

	public boolean exists() {
		return false;
	}

	/*
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/*
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/*
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return this.tooltip;
	}

	/*
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		// if (ILocationProvider.class.equals(adapter))return this;
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o instanceof SequenceEditorInput) {
			SequenceEditorInput input = (SequenceEditorInput) o;

			if (config.getDbName().equals(input.config.getDbName())) {
				return name.equals(input.getName());
			} else {
				return false;
			}
		}
		return false;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * @return config を戻します。
	 */
	public IDBConfig getConfig() {
		return config;
	}

	/**
	 * ToolTipTextを設定
	 * 
	 * @param tooltip
	 */
	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public OracleSequenceInfo getSequenceInfo() {
		return sequenceInfo;
	}

	public void setSequenceInfo(OracleSequenceInfo sequenceInfo) {
		this.sequenceInfo = sequenceInfo;
	}

}
