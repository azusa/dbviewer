/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.actions.ConfirmConnectDBAction;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ContentAssistTable;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.views.TreeView;

public class ContentInfo {

	IDBConfig config;

	Transaction trans;

	boolean isConnected = false;

	String currentSchema = null;

	public ContentInfo(IDBConfig config) {
		this.config = config;
		if (config != null){
			configure();
		}else{
			DbPlugin.log("ContentInfo�̐��������ŃG���[���������܂����B�f�[�^�x�[�X�ڑ���`��񂪂���܂���");
		}
	}

	private void configure() {
		try {
			trans = Transaction.getInstance(config);
			if (trans.isConneting()) {
				isConnected = true;
				DbPlugin.fireStatusChangeListener(config, IStatusChangeListener.EVT_ChangeDataBase);
				this.currentSchema = getSchemaName(trans.getConnection(), config);
			} else {
				Display.getDefault().syncExec(new ConfirmConnectDBAction(trans));
				if (trans.isConneting()) {
					configure();
				}else{
					isConnected = false;
				}
			}
			
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	/**
	 * �X�L�[�}���͑啶���������𔻒f����f�[�^�x�[�X�����邽�߁A �f�[�^�x�[�X��`����ł͂Ȃ��ADB�c���[����擾����
	 * 
	 * @param con
	 * @param config
	 * @return
	 */
	private String getSchemaName(Connection con, IDBConfig config) {
		TreeView tw = (TreeView) DbPlugin.getDefault().findView(DbPluginConstant.VIEW_ID_TreeView);
		if (tw != null) {
			DataBase db = tw.getContentProvider().findDataBase(config);
			if (SchemaSearcher.isSupport(con)) {
				List list = db.getChildren();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					TreeNode node = (TreeNode) iterator.next();
					if (node instanceof Schema) {
						Schema schema = (Schema) node;
						if (schema.getName().equalsIgnoreCase(config.getSchema())) {
							return schema.getName();
						}
					} else {
						return null;
					}
				}
			}
		} else {
			return matchSchemaName(getSchemas(con, config), config);
		}
		return null;

	}

	// DBTreeView���Schema�����擾����
	private String matchSchemaName(String[] schemas, IDBConfig config) {
		for (int i = 0; i < schemas.length; i++) {
			String schema = schemas[i];
			if (schema.equalsIgnoreCase(config.getSchema())) {
				return schema;
			}
		}
		return null;
	}

	// private String[] getTableTypes(Connection con) {
	//		
	// String[] result = null;
	// ObjectCacher holder = ObjectCacher.getInstance(config.getDbName() +
	// "@TableType"); //$NON-NLS-1$
	// synchronized (holder) {
	// result = (String[]) holder.get();
	// if (result == null) {
	// try {
	// result = TableTypeSearcher.execute(con);
	// } catch (Exception e) {
	// DbPlugin.log(e);
	// }
	// holder.put(result);
	// } else {
	// }
	// }
	// return result;
	//
	// }

	// ���g�p
	private String[] getSchemas(Connection con, IDBConfig config) {
		String[] result = null;
		ObjectCacher holder = ObjectCacher.getInstance(config.getDbName() + "@Schema"); //$NON-NLS-1$
		synchronized (holder) {
			result = (String[]) holder.get();
			if (result == null) {
				try {
					result = SchemaSearcher.execute(con);
				} catch (Exception e) {
					DbPlugin.log(e);
				}
				holder.put(result);
			} else {
			}
		}
		return result;

	}

	public TableInfo[] getTableInfo() {

		if (config == null)
			return null;
		
		String[] tableTypes = null;
		switch (config.getDbType()) {
		case DBType.DB_TYPE_ORACLE:
			tableTypes = new String[] { "TABLE", "VIEW", "SYNONYM" }; //$NON-NLS-1$ //$NON-NLS-2$
			break;
		default:
			// <-- ���\���l���āA��L�ȊO��DB�́ATABLE��VIEW�����e�[�u���⊮�����悤�ɏC��
			tableTypes = new String[] { "TABLE", "VIEW" }; //$NON-NLS-1$ //$NON-NLS-2$
			// -->
			break;
		}

		TableInfo[] result = null;
		try {
			Connection con = trans.getConnection();
			// Config��̃X�L�[�}���ł͂Ȃ��ADB�ڑ����Ď擾�ł���X�L�[�}�����g���B

			String keySchemaName = (config.getSchema() != null) ? config.getSchema() : config.getDbName();
			ObjectCacher holder = ObjectCacher.getInstance(keySchemaName);
			synchronized (holder) {
				result = (TableInfo[]) holder.get();
				if (result == null) {
					try {
						if (currentSchema != null) {
							result = TableSearcher.execute(con, currentSchema, tableTypes);
						} else {
							result = TableSearcher.execute(con, null, tableTypes);
						}

						// �\�[�g����

					} catch (Exception e) {
						DbPlugin.log(e);
					}
					holder.put(result);
				} else {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public Column[] getColumns(String tableName) {
		Column[] result = null;
		ObjectCacher holder = ObjectCacher.getInstance(tableName);
		synchronized (holder) {
			result = (Column[]) holder.get();
			if (result == null) {
				ContentAssistTable table = ContentAssistUtil.createContentAssistTable(currentSchema, tableName);
				result = table.getColumns();
				holder.put(result);
			} else {
			}
		}
		return result;
	}

	public String getCurrentSchema() {
		return currentSchema;
	}

	public IDBConfig getConfig() {
		return config;
	}

	public boolean isConnected() {
		return isConnected;
	}

}
