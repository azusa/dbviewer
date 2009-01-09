/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.SQLHistory;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.actions.DeleteHistoryAction;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.internal.HistoryFolder;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.util.LineNumberRulerColumnUtil;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.SQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.SQLDocument;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

/**
 * TreeView�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/11/27 ZIGEN create.
 */
public class HistoryView extends ViewPart implements IStatusChangeListener {

	protected SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();

	protected PluginSettingsManager settringMgr = DbPlugin.getDefault().getPluginSettingsManager();

	protected TreeViewer viewer;

	protected String lastSecondaryId;

	SashForm sash;

	Combo filterComb;

	Button searchBtn;

	List filterHistory;

	int maxSize = 20;

	SQLSourceViewer sourceViewer;

	IDocument doc;

	DeleteHistoryAction removeHistoryAction;

	// RemoveHistoryAction removeHistoryAction;

	protected ColorManager colorManager = new ColorManager();

	ImageCacher ic = ImageCacher.getInstance();

	public TreeViewer getTreeView() {
		return this.viewer;
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

	}

	protected void createTreeArea(Composite parent) {
		Composite body = new Composite(parent, SWT.NONE);
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
		body.setLayout(new FillLayout());

		sash = new SashForm(body, SWT.VERTICAL | SWT.NONE);

		viewer = new TreeViewer(sash, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		// viewer = new TreeViewer(sash, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);

		/*
		 * DND�����̓R�����g�A�E�g int dragOption = DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY; Transfer[] transfers = new Transfer[]{ TreeLeafListTransfer.getInstance() };
		 * viewer.addDragSupport(dragOption, transfers, new DragBookmarkAdapter(viewer)); viewer.addDropSupport(dragOption, transfers, new DropBookmarkAdapter(viewer));
		 */

		HistoryContentProvider hcp = new HistoryContentProvider();

		viewer.setContentProvider(hcp);
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setSorter(new HistoryViewSorter());
		viewer.setInput(getViewSite());

		HistoryFolder folder = hcp.getHistoryHolder(Calendar.getInstance().getTime());
		viewer.expandToLevel(folder, 1);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				selectionChangeHandler(event);
			}
		});

		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn rulerCol = new LineNumberRulerColumn();
		LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
		ruler.addDecorator(0, rulerCol);
		ruler.setFont(DbPlugin.getSmallFont());
		sourceViewer = new SQLSourceViewer(sash, ruler, null, false, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		sourceViewer.getTextWidget().setFont(DbPlugin.getSmallFont());

		SQLCodeConfiguration sqlConfiguration = new SQLCodeConfiguration(colorManager);

		sourceViewer.configure(sqlConfiguration);

		doc = new SQLDocument();
		sourceViewer.setDocument(doc);
		sourceViewer.setEditable(false);
		// sourceViewer.getTextWidget().setWordWrap(true);

		sash.setWeights(new int[] {50, 50});

		// SelectionProvider�ɓo�^(�ύX��ʒm�����邽�߁j
		getSite().setSelectionProvider(viewer);
		DbPlugin.addStatusChangeListener(this);

		hookDoubleClickAction();

		makeActions();
		hookContextMenu();
	}

	/**
	 * �I�������v�f�ɂ���ă��j���[�̊����E�񊈐��𐧌�
	 */
	void selectionChangeHandler(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		if (selection.size() == 1) {
			Object element = (Object) (selection).getFirstElement();
			if (element instanceof HistoryFolder) {
				doc = new SQLDocument();
				sourceViewer.setDocument(doc);

			} else if (element instanceof History) {
				History h = (History) element;
				doc.set(mgr.loadContents(h.getSqlHistory()));
			}

		}

		if (selection.size() > 0) {
			Object element = (Object) (selection).getFirstElement();
			if (element instanceof HistoryFolder || element instanceof History) {
				removeHistoryAction.setEnabled(true);
			} else {
				removeHistoryAction.setEnabled(false);
			}
		} else {
			removeHistoryAction.setEnabled(false);
		}

		setGlobalAction(selection);
	}

	void setGlobalAction(IStructuredSelection selection) {
		IActionBars bars = getViewSite().getActionBars();
		bars.clearGlobalActionHandlers();
		bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), removeHistoryAction);
		bars.updateActionBars();
	}

	protected void createFilterBar(Composite parent) {
		GridData gridData;
		final Composite tool = new Composite(parent, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		tool.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		tool.setLayout(gridLayout);

		Label label1 = new Label(tool, SWT.NULL);
		gridData = new GridData(GridData.FILL);
		label1.setLayoutData(gridData);
		label1.setText(" Filter:"); //$NON-NLS-1$

		// �c�[���o�[
		// ToolBar toolbar = new ToolBar(tool, SWT.FLAT);

		filterComb = new Combo(tool, SWT.NONE);
		filterComb.setVisibleItemCount(20);
		filterComb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filterComb.setText(""); //$NON-NLS-1$

		filterComb.addFocusListener(new TextSelectionListener());
		filterComb.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					e.doit = false;
					filter(filterComb.getText());
				}
			}
		});

		searchBtn = new Button(tool, SWT.NONE);
		searchBtn.setText(Messages.getString("HistoryView.2")); //$NON-NLS-1$

		searchBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				filter(filterComb.getText());
			}

		});
		filterHistory = loadFilterHistory();
		// �����ꗗ�̍쐬
		if (filterHistory != null) {
			if (filterHistory.size() == 0) {
				filterComb.add("");
			} else {
				for (int i = 0; i < filterHistory.size(); i++) {
					filterComb.add((String) filterHistory.get(i));
				}
			}
		}
	}

	private void filter(String condition) {

		for (int i = 0; i < viewer.getFilters().length; i++) {
			ViewerFilter filter = viewer.getFilters()[i];
			viewer.removeFilter(filter);
		}
		if (!"".equals(condition)) { //$NON-NLS-1$
			viewer.addFilter(new HistoryViewTableFilter(condition));
		}
		// ��v������������x�폜���A�ŏ�ʂɈړ�������
		if (filterHistory.contains(condition)) {
			filterHistory.remove(condition);
			filterComb.remove(condition);
		}
		filterHistory.add(0, condition); // �擪�ɒǉ�
		filterComb.add(condition, 0); // �擪�ɒǉ�
		filterComb.select(0);// �擪��I������
		// �ő�l�𒴂��������폜����
		removeOverHistory();

		filterComb.setFocus();
	}

	private void removeOverHistory() {
		while (filterHistory.size() > maxSize) { // �󔒗p���l��
			int i = filterHistory.size() - 1;
			filterHistory.remove(i);
			filterComb.remove(i);
		}
	}

	private void saveFilterHistory() {
		settringMgr.setValue(PluginSettingsManager.KEY_FILTER_LIST_HISTORY, filterHistory);

	}

	private List loadFilterHistory() {
		List list = (List) settringMgr.getValue(PluginSettingsManager.KEY_FILTER_LIST_HISTORY);
		if (list != null) {
			return list;
		} else {
			return new ArrayList();
		}
	}

	protected void hookDoubleClickAction() {
		// DoubleClickHandler�̒ǉ�
		viewer.addDoubleClickListener(new HistoryDoubleClickHandler());
	}

	protected void makeActions() {
		removeHistoryAction = new DeleteHistoryAction(viewer);
		// removeHistoryAction = new RemoveHistoryAction(viewer);
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

		contributeToActionBars();
	}

	void fillContextMenu(IMenuManager manager) {
		// �I���������̂ɂ���ĕ\�����郁�j���[��ύX
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (obj instanceof HistoryFolder || obj instanceof History) {
			manager.add(removeHistoryAction);
		}
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void setFocus() {
		// SQL���s�r���[�̎��s�����{�^����I���ɂ���
		viewer.getControl().notifyListeners(SWT.Selection, null);
		DbPlugin.fireStatusChangeListener(this, SWT.Selection);

	}

	public void dispose() {
		saveFilterHistory();
		DbPlugin.fireStatusChangeListener(this, SWT.Dispose);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof SQLExecuteView) {
			this.lastSecondaryId = ((SQLExecuteView) part).getViewSite().getSecondaryId();
		}
	}

	public void statusChanged(Object obj, int status) {
		if (obj instanceof SQLExecuteView) {
			if (SWT.Selection == status) {
				this.lastSecondaryId = ((SQLExecuteView) obj).getViewSite().getSecondaryId();
			}
		}
	}

	private class HistoryDoubleClickHandler implements IDoubleClickListener {

		SQLHistoryManager historyManager = DbPlugin.getDefault().getHistoryManager();

		public HistoryDoubleClickHandler() {}

		public void doubleClick(DoubleClickEvent event) {

			try {

				Viewer view = event.getViewer();
				ISelection selection = event.getSelection();

				if (view instanceof TreeViewer && selection instanceof StructuredSelection) {
					TreeViewer viewer = (TreeViewer) view;
					Object element = ((StructuredSelection) selection).getFirstElement();
					if (element instanceof History) {
						// �_�u���N���b�N�Őڑ�����
						History history = (History) element;
						SQLExecuteView sview;
						if (lastSecondaryId == null) {
							sview = (SQLExecuteView) DbPlugin.showView(DbPluginConstant.VIEW_ID_SQLExecute);
						} else {
							sview = (SQLExecuteView) DbPlugin.showView(DbPluginConstant.VIEW_ID_SQLExecute, lastSecondaryId);
						}
						if (sview != null) {
							SQLHistory sh = history.getSqlHistory();
							sview.setSqlText(mgr.loadContents(sh));
							if (sh.getConfig() != null) {
								// ��Version�́ADBConfig�������Ȃ�
								sview.updateCombo(sh.getConfig());
							}

							// �����̈ʒu���X�V����
							historyManager.modifyCurrentPosition(sh);
							// �����{�^�����X�V����
							sview.updateHistoryButton();

						}
					} else if (element instanceof TreeNode) {
						// ����ȊO�̃m�[�h�v�f�͓W�J�܂��͔�W�J�������s��
						changeExpandedState(viewer, (TreeNode) element);
					}

				}
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

		/**
		 * �W�J�E��W�J�̐؂�ւ�
		 * 
		 * @param element
		 */
		private void changeExpandedState(TreeViewer viewer, TreeNode element) {
			// �v�f���W�J��Ԃ��ǂ���
			if (!viewer.getExpandedState(element)) {
				viewer.expandToLevel(element, 1); // �W�J��Ԃɂ���

			} else {
				viewer.collapseToLevel(element, 1); // ��W�J��Ԃɂ���
			}

		}

	}

	/**
	 * SQL�����r���[���X�V����
	 * 
	 * @param history
	 */
	public void updateHistoryView(SQLHistory history) {
		IContentProvider cp = viewer.getContentProvider();
		if (cp instanceof HistoryContentProvider) {
			HistoryContentProvider hcp = (HistoryContentProvider) cp;
			hcp.reflesh(history);
			viewer.refresh();
			HistoryFolder currentFolder = hcp.getHistoryHolder(history.getDate());
			viewer.expandToLevel(currentFolder, 1);
		}
	}

	// <!-- [002] �ǉ� ZIGEN 2005/06/02
	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		// fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	void fillLocalPullDown(IMenuManager manager) {}

	void fillLocalToolBar(IToolBarManager manager) {

		manager.add(removeHistoryAction);
		manager.add(new Separator());
		manager.add(new MyButtonContiribution());

	}

	public class MyButtonContiribution extends ControlContribution {

		Button button;

		public MyButtonContiribution() {
			super("verticalLayout"); //$NON-NLS-1$
		}

		public Control createControl(Composite parent) {
			ToolBar toolbar = new ToolBar(parent, SWT.FLAT);
			ToolItem verticalItem = new ToolItem(toolbar, SWT.RADIO);
			ToolItem horizonItem = new ToolItem(toolbar, SWT.RADIO);

			// �ۑ�����Ă��郌�C�A�E�g�^�C�v���f�t�H���g�Ƃ��Ă���
			Object obj = settringMgr.getValue(PluginSettingsManager.KEY_SQLHISTORY_LAYOUT);
			if (obj != null && obj instanceof String) {
				String value = (String) obj;
				if ("SWT.VERTICAL".equals(value)) { //$NON-NLS-1$
					sash.setOrientation(SWT.VERTICAL);
					verticalItem.setSelection(true);
				} else {
					sash.setOrientation(SWT.HORIZONTAL);
					horizonItem.setSelection(true);
				}
			} else {
				verticalItem.setSelection(true);
			}

			verticalItem.setToolTipText(Messages.getString("HistoryView.7")); //$NON-NLS-1$
			horizonItem.setToolTipText(Messages.getString("HistoryView.8")); //$NON-NLS-1$

			verticalItem.setImage(ic.getImage(DbPlugin.IMG_CODE_VERTICALLAYOUT));
			horizonItem.setImage(ic.getImage(DbPlugin.IMG_CODE_HORIZONTALLAYOUT));

			verticalItem.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent arg0) {
					sash.setOrientation(SWT.VERTICAL);
					settringMgr.setValue(PluginSettingsManager.KEY_SQLHISTORY_LAYOUT, "SWT.VERTICAL"); //$NON-NLS-1$
				}
			});
			horizonItem.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent arg0) {
					sash.setOrientation(SWT.HORIZONTAL);
					settringMgr.setValue(PluginSettingsManager.KEY_SQLHISTORY_LAYOUT, "SWT.HORIZONTAL"); //$NON-NLS-1$
				}
			});

			return toolbar;
		}

	}

}
