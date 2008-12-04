/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.ITable;

/**
 * TableEditorInputクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 * 
 */

public class TableViewEditorInput implements IEditorInput {

	private String tooltip;

	private String name;

	private IDBConfig config;

	private String schemaName;

	private ITable table;

	public TableViewEditorInput(IDBConfig config, ITable table) {
		super();
		this.config = config;
		this.schemaName = table.getSchemaName(); // スキーマが無い場合はNULL
		this.table = table;
		this.name = table.getName();
		this.tooltip = table.getName() + " [" + config.getUserId() + " : " + config.getDbName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		
        //input.setToolTipText("[" + input.getConfig().getDbName() + "] " + partName); //$NON-NLS-1$ //$NON-NLS-2$
        
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(config.getDbName());
        sb.append("] ");
        sb.append(table.getSqlTableName());
        if(table.getRemarks() != null && !"".equals(table.getRemarks().trim())){
        	sb.append(" [");
        	sb.append(table.getRemarks());
        	sb.append("]");
        }
       this.tooltip = sb.toString();
       
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
	    boolean b = false;
		if (o == this)
			return true;

		if (o instanceof TableViewEditorInput) {
			TableViewEditorInput input = (TableViewEditorInput) o;

			if (schemaName != null && input.schemaName != null) {
				b =  (config.equals(input.config) 
						&& schemaName.equals(input.schemaName) 
						&& name.equals(input.name)
						&& table.getClass() == input.table.getClass()
				);
				return b;
			} else if (schemaName == null && input.schemaName == null) {
				b = (config.equals(input.config) && name.equals(input.name));
				return b;
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

	public ITable getTable() {
		return table;
	}

	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

}
