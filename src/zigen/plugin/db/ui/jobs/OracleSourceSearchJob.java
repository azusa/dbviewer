/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceSearcher;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleFunction;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;

public class OracleSourceSearchJob extends AbstractJob {

	private TreeViewer viewer;

	private Folder folder;

	public OracleSourceSearchJob(TreeViewer viewer, Folder folder) {
		super(Messages.getString("OracleSourceSearchJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.folder = folder;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Search Oracle Source...", 10);
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			folder.removeChildAll(); // �q�m�[�h��S�č폜
			showResults(new RefreshTreeNodeAction(viewer, folder)); // �ĕ`��

			// Folder�̏�ʂ́ASchema�O��
			Schema schema = (Schema) folder.getParent();
			String owner = schema.getName();
			String type = folder.getName();

			OracleSourceInfo[] infos = OracleSourceSearcher.execute(con, owner, type);
			addSources(con, folder, infos);

			folder.setExpanded(true);
			showResults(new RefreshTreeNodeAction(viewer, folder, RefreshTreeNodeAction.MODE_NOTHING)); // �ĕ`��

			monitor.done();
		} catch (Exception e) {
			folder.setExpanded(false);
			showErrorMessage(Messages.getString("OracleSourceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

	/**
	 * �\�[�X�v�f���t�H���_�z���ɒǉ�����
	 * 
	 * @param con
	 * @param folder
	 * @param infos
	 * @throws Exception
	 */
	private void addSources(Connection con, Folder folder, OracleSourceInfo[] infos) throws Exception {
		for (int i = 0; i < infos.length; i++) {
			OracleSource source;
			if ("FUNCTION".equals(folder.getName())) { //$NON-NLS-1$
				source = new OracleFunction();
			} else {
				source = new OracleSource();
			}
			source.setOracleSourceInfo(infos[i]);
			folder.addChild(source);
		}
	}
}
