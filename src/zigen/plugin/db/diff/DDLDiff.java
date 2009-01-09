/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;


public class DDLDiff extends DDLNode {

	private static final long serialVersionUID = 1L;

	DDL leftDDL;

	IDDL rightDDL;

	public DDLDiff(DDL leftContent, DDL rightContent) {
		this.leftDDL = leftContent;
		this.rightDDL = rightContent;

		// 表示用のテーブル名を設定
		if (leftContent != null)
			this.name = leftContent.targetName;
		if (rightContent != null)
			this.name = rightContent.targetName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getResultType()
	 */
	public int getResultType() {
		if (leftDDL != null && rightDDL != null) {
			if (leftDDL.getDdl().equals(rightDDL.getDdl())) {
				return TYPE_BOTH_SAME;
			} else {
				return TYPE_BOTH_DIFFERENCE;
			}
		} else if (leftDDL == null) {
			return TYPE_INCLUDE_ONLY_TARGET;
		} else if (rightDDL == null) {
			return TYPE_INCLUDE_ONLY_ORIGN;
		} else {
			return TYPE_NOTHING;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getTargetName()
	 */
	public String getName() {
		if (leftDDL != null) {
			return leftDDL.getTargetName();
		} else {
			return rightDDL.getTargetName();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getLeftDDLString()
	 */
	public String getLeftDDLString() {
		if (leftDDL != null) {
			return leftDDL.ddl;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getRightDDLString()
	 */
	public String getRightDDLString() {
		if (rightDDL != null) {
			return rightDDL.getDdl();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getLeftDisplayedName()
	 */
	public String getLeftDisplayedName() {
		if (this.leftDDL == null)
			return ""; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		sb.append("["); //$NON-NLS-1$
		sb.append(getLeftDBName());
		sb.append("] "); //$NON-NLS-1$
		sb.append(leftDDL.getDisplayedName());

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getRightDisplayedName()
	 */
	public String getRightDisplayedName() {
		if (this.rightDDL == null)
			return ""; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		sb.append("["); //$NON-NLS-1$
		sb.append(getRightDBName());
		sb.append("] "); //$NON-NLS-1$
		sb.append(rightDDL.getDisplayedName());
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getLeftDBName()
	 */
	public String getLeftDBName() {
		if (this.leftDDL == null)
			return ""; //$NON-NLS-1$
		return leftDDL.getDbName();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getRightDBName()
	 */
	public String getRightDBName() {
		if (this.rightDDL == null)
			return ""; //$NON-NLS-1$
		return rightDDL.getDbName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#isComparisonFailure()
	 */
	public boolean isComparisonFailure() {
		return leftDDL != null && rightDDL != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getLeftDDL()
	 */
	public IDDL getLeftDDL() {
		return leftDDL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zigen.plugin.db.diff.IDDLDiff#getRightDDL()
	 */
	public IDDL getRightDDL() {
		return rightDDL;
	}

	public String getType() {
		if (leftDDL != null) {
			return leftDDL.getType();
		} else {
			return rightDDL.getType();
		}
	}

}
