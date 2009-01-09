/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.io.Serializable;

public class OracleSourceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String owner;

	protected String name;

	protected String type;

	/**
	 * �R���X�g���N�^
	 */
	public OracleSourceInfo() {}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[SourceInfo:"); //$NON-NLS-1$
		buffer.append(" owner: "); //$NON-NLS-1$
		buffer.append(owner);
		buffer.append(" name: "); //$NON-NLS-1$
		buffer.append(name);
		buffer.append(" type: "); //$NON-NLS-1$
		buffer.append(type);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

}
