/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.part.ViewPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SchemaInfo;
import zigen.plugin.db.ui.actions.AutoDelayListener;
import zigen.plugin.db.ui.bookmark.DragBookmarkAdapter;
import zigen.plugin.db.ui.bookmark.DropBookmarkAdapter;
import zigen.plugin.db.ui.bookmark.TreeLeafListTransfer;
import zigen.plugin.db.ui.views.internal.ColumnFilter;
import zigen.plugin.db.ui.views.internal.TableFilter;

/**
 * TreeView�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/11/27 ZIGEN create.
 */
public abstract class AbstractTreeView extends ViewPart implements IStatusChangeListener {

	protected PluginSettingsManager settringMgr = DbPlugin.getDefault().getPluginSettingsManager();

	protected int maxSize = 20;

	protected TreeViewer viewer;

	Label tableFilterLabel;
	Label columnFilterLabel;
	
	protected Combo tableFilterComb;

	protected Combo columnFilterComb;

	protected Button searchBtn;

	protected List tableFilterHistory;

	protected List columnFilterHistory;

	// protected boolean fLinkingEnabled;

	// �����Ȑ���(�v���_�E�����đI�������ꍇ�╶���I�����)�����܂��s���Ȃ��̂ŁAfalse�Ƃ���
	protected boolean isAutoSearchMode = false; // �L�[���͌㎩����������ꍇ

	public void setLinkingEnabled(boolean enabled) {
		// fLinkingEnabled = enabled;
		settringMgr.setValue(PluginSettingsManager.KEY_LINKED_EDITOR, new Boolean(enabled));
	}

	public boolean isLinkingEnabled() {
		// return fLinkingEnabled;
		Boolean b = (Boolean) settringMgr.getValue(PluginSettingsManager.KEY_LINKED_EDITOR);
		return (b == null) ? false : b.booleanValue();
	}

	public void createPartControl(Composite parent) {

		Composite main = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		main.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 2;
		main.setLayout(gridLayout);
				
		createFilterBar(main);
		createTreeArea(main);
		
		// SelectionProvider�ɓo�^(�ύX��ʒm�����邽�߁j
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();

		hookDoubleClickAction();
		contributeToActionBars();

		// setGlobalAction(getViewSite().getActionBars());

		DbPlugin.addStatusChangeListener(this);
	}

	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	protected void hookDoubleClickAction() {
		// DoubleClickHandler�̒ǉ�
		viewer.addDoubleClickListener(new TreeDoubleClickHandler());
	}

	// <!-- [002] �ǉ� ZIGEN 2005/06/02
	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	abstract void makeActions();

	abstract void fillContextMenu(IMenuManager manager);

	abstract void fillLocalPullDown(IMenuManager manager);

	abstract void fillLocalToolBar(IToolBarManager manager);

	abstract void setGlobalAction(IStructuredSelection selection);

	/**
	 * �I�����ꂽ�v�f�ɂ����Action#setEnable��ݒ肷��
	 * 
	 * @param event
	 */
	abstract void selectionChangeHandler(SelectionChangedEvent event);

	protected TreeContentProvider contentProvider;

	protected void createTreeArea(Composite parent) {
		Composite body = new Composite(parent, SWT.NONE);
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
		body.setLayout(new FillLayout());

		viewer = new TreeViewer(body, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		// Drag�C�x���g�̎���
		// int dragOption = DND.DROP_COPY | DND.DROP_MOVE;
		// Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
		// viewer.addDragSupport(dragOption, transfers, new
		// DragElementAdapter(viewer));

		int dragOption = DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY;
		Transfer[] transfers = new Transfer[] { TreeLeafListTransfer.getInstance() };
		viewer.addDragSupport(dragOption, transfers, new DragBookmarkAdapter(viewer));
		viewer.addDropSupport(dragOption, transfers, new DropBookmarkAdapter(viewer));

		contentProvider = new TreeContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setSorter(new TreeViewSorter());
		// ������������
		viewer.setUseHashlookup(true); 

		viewer.setInput(getViewSite());
		// viewer.expandAll(); // ���ׂēW�J��ԂƂ���
		viewer.expandToLevel(2); // 2�K�w�ڂ܂œW�J����ꍇ

		// Listener�̒ǉ�(�X�L�[�}�W�J���Ƀe�[�u������������j
		viewer.addTreeListener(new TreeViewListener());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selectionChangeHandler(event);
			}
		});

	}

	// protected ToolItem targetSchema;

	// protected ToolItem targetTable;
	//
	// protected ToolItem targetColumn;

	protected ImageCacher ic = ImageCacher.getInstance();

	protected Composite tool;
	boolean onColumnFilter = false;
	
	
	protected void createFilterBar(final Composite parent) {
		GridData gridData;
		tool = new Composite(parent, SWT.NONE);

		gridData = new GridData(GridData.FILL_HORIZONTAL);
		tool.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = 1;
		gridLayout.verticalSpacing = 1;
		tool.setLayout(gridLayout);

		tool.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}

			public void controlResized(ControlEvent e) {
				parent.getParent().layout(true);
				parent.layout(true);
			}
		});
		
		
		addTableFilter(tool);
		addColumnFilter(tool);
		//onColumnFilter = true;
	}
	
	protected void addColumnFilter(Composite tool){
		columnFilterLabel = new Label(tool, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL);
		columnFilterLabel.setLayoutData(gridData);
		columnFilterLabel.setText("Column:");
		columnFilterComb = new Combo(tool, SWT.NONE);
		columnFilterComb.setVisibleItemCount(20);
		columnFilterComb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		columnFilterComb.setText(""); //$NON-NLS-1$

		if (!isAutoSearchMode) {
			// columnFilterComb.addFocusListener(new TextSelectionListener());
			// // Text�̂�
			columnFilterComb.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.character == SWT.CR) {
						e.doit = false;
						columnFilter(columnFilterComb.getText());
					}
				}
			});

		} else {
			columnFilterComb.addKeyListener(new AutoDelayFiltertListener(2));
		}
		
		columnFilterHistory = loadColumnFilterHistory();

		// �����ꗗ�̍쐬
		if (columnFilterHistory != null) {
			if (columnFilterHistory.size() == 0) {
				columnFilterComb.add(""); //$NON-NLS-1$
			} else {
				for (int i = 0; i < columnFilterHistory.size(); i++) {
					columnFilterComb.add((String) columnFilterHistory.get(i));
				}
			}
		}
	}
	
	protected void addTableFilter(Composite tool){
		tableFilterLabel = new Label(tool, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL);
		tableFilterLabel.setLayoutData(gridData);
		tableFilterLabel.setText("Filter:");

		// �c�[���o�[
		tableFilterComb = new Combo(tool, SWT.NONE);
		tableFilterComb.setVisibleItemCount(20);
		tableFilterComb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tableFilterComb.setText(""); //$NON-NLS-1$

		if (!isAutoSearchMode) {
			// tableFilterComb.addFocusListener(new TextSelectionListener()); //
			// Text�̂�
			tableFilterComb.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.character == SWT.CR) {
						e.doit = false;
						tableFilter(tableFilterComb.getText());
					}
				}
			});
		} else {
			tableFilterComb.addKeyListener(new AutoDelayFiltertListener(1));
		}
		

		tableFilterHistory = loadFilterHistory();

		// �����ꗗ�̍쐬
		if (tableFilterHistory != null) {
			if (tableFilterHistory.size() == 0) {
				tableFilterComb.add(""); //$NON-NLS-1$
			} else {
				for (int i = 0; i < tableFilterHistory.size(); i++) {
					tableFilterComb.add((String) tableFilterHistory.get(i));
				}
			}
		}

		
	}


	TableFilter fTableFilter;

	ColumnFilter fColumnFilter;

	protected void tableFilter(String condition) {
		if (fTableFilter != null)
			viewer.removeFilter(fTableFilter);
		fTableFilter = new TableFilter(condition);
		viewer.addFilter(fTableFilter);
		// ��v������������x�폜���A�ŏ�ʂɈړ�������
		if (tableFilterHistory.contains(condition)) {
			tableFilterHistory.remove(condition);
			tableFilterComb.remove(condition);
		}

		tableFilterHistory.add(0, condition); // �擪�ɒǉ�
		tableFilterComb.add(condition, 0); // �擪�ɒǉ�
		tableFilterComb.select(0);// �擪��I������
		// �ő�l�𒴂��������폜����
		removeOverHistory(tableFilterHistory, tableFilterComb);

		// tableFilterComb.setFocus();
		

	}

	protected void columnFilter(String condition) {
		if (fColumnFilter != null)
			viewer.removeFilter(fColumnFilter);
		fColumnFilter = new ColumnFilter(condition);
		viewer.addFilter(fColumnFilter);

		// ��v������������x�폜���A�ŏ�ʂɈړ�������
		if (columnFilterHistory.contains(condition)) {
			columnFilterHistory.remove(condition);
			columnFilterComb.remove(condition);
		}
		columnFilterHistory.add(0, condition); // �擪�ɒǉ�
		columnFilterComb.add(condition, 0); // �擪�ɒǉ�
		columnFilterComb.select(0);// �擪��I������
		// �ő�l�𒴂��������폜����
		removeOverHistory(columnFilterHistory, columnFilterComb);

		// columnFilterComb.setFocus();
	}

	Map filterMap = new TreeMap();

	public void setSchemaFilter(IDBConfig config, SchemaInfo[] scehmaInfos) {
		try {
			TreeViewSchemaFilter filter = null;
			if (filterMap.containsKey(config.getDbName())) {
				filter = (TreeViewSchemaFilter) filterMap.get(config.getDbName());
				viewer.removeFilter(filter);
			}

			filter = new TreeViewSchemaFilter(scehmaInfos);
			filterMap.put(config.getDbName(), filter);
			viewer.addFilter(filter);

		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void removeSchemaFilter(IDBConfig config, SchemaInfo[] scehmaInfos) {
		try {
			TreeViewSchemaFilter filter = null;
			if (filterMap.containsKey(config.getDbName())) {
				filter = (TreeViewSchemaFilter) filterMap.get(config.getDbName());
				viewer.removeFilter(filter);
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void removeOverHistory(List filterHistory, Combo filterComb) {
		while (filterHistory.size() > maxSize) { // �󔒗p���l��
			int i = filterHistory.size() - 1;
			filterHistory.remove(i);
			filterComb.remove(i);
		}
	}

	public void dispose() {
		DbPlugin.removeStatusChangeListener(this);
		saveFilterHistory();
	}

	private void saveFilterHistory() {
		settringMgr.setValue(PluginSettingsManager.KEY_FILTER_LIST, tableFilterHistory);
		settringMgr.setValue(PluginSettingsManager.KEY_FILTER_LIST_COLUMN, columnFilterHistory);
	}

	private List loadFilterHistory() {
		List list = (List) settringMgr.getValue(PluginSettingsManager.KEY_FILTER_LIST);
		if (list != null) {
			return list;
		} else {
			return new ArrayList();
		}
	}

	private List loadColumnFilterHistory() {
		List list = (List) settringMgr.getValue(PluginSettingsManager.KEY_FILTER_LIST_COLUMN);
		if (list != null) {
			return list;
		} else {
			return new ArrayList();
		}
	}

	public TreeContentProvider getContentProvider() {
		return contentProvider;
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	protected IDBConfig currentIDBConfig;

	public void setStatusMessage(IDBConfig currentIDBConfig, String message) {
		this.currentIDBConfig = currentIDBConfig;
		getIStatusLineManager().setMessage(message);
	}

	public void setStatusErrorMessage(IDBConfig currentIDBConfig, String message) {
		this.currentIDBConfig = currentIDBConfig;
		getIStatusLineManager().setErrorMessage(message);
	}

	protected IStatusLineManager getIStatusLineManager() {
		IViewSite vieweSite = super.getViewSite();
		IActionBars actionBars = vieweSite.getActionBars();
		return actionBars.getStatusLineManager();

	}

	class AutoDelayFiltertListener extends AutoDelayListener {
		private static final int delayTime = 300;

		int mode = 1;

		public AutoDelayFiltertListener(int mode) {
			super(delayTime);
			this.mode = mode;
		}

		public Runnable createExecutAction() {
			return new Runnable() {
				public void run() {
					try {
						if (mode == 1) {
							String condition = tableFilterComb.getText();
							tableFilter(condition);

						} else {

							String condition = columnFilterComb.getText();
							columnFilter(condition);

						}
					} catch (Exception e) {
						DbPlugin.log(e);
					}
				}
			};
		}
	}

}
