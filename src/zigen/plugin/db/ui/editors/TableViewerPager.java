package zigen.plugin.db.ui.editors;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class TableViewerPager {

	protected ImageCacher ic = ImageCacher.getInstance();

	/**
	 * �I�t�Z�b�g
	 */
	int offset = 1;

	/**
	 * 1��ʂ�����̕\����
	 */
	int limit = 0;

	/**
	 * ���R�[�h����
	 */
	int recordCount = 0;

	/**
	 * ���y�[�W��
	 */
	int pageCount;

	/**
	 * �\���y�[�W�ԍ��i�f�t�H���g1)
	 */
	protected int pageNo = 1;

	Label sura;

	Label totalPage;

	Text pageNoTxt;

	ToolItem topPage;

	ToolItem backPage;

	ToolItem nextPage;

	ToolItem endPage;

	ITable table;

	public TableViewerPager(ITable table, int limit) {
		this.table = table;
		this.limit = limit;
	}

	public Control createStackedButtons(Composite composite) {
		Composite c = new Composite(composite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		c.setLayout(gridLayout);

		final ToolBar toolBar4 = new ToolBar(c, SWT.HORIZONTAL);
		topPage = new ToolItem(toolBar4, SWT.PUSH);
		topPage.setImage(ic.getImage(DbPlugin.IMG_CODE_TOP));
		topPage.setToolTipText(Messages.getString("TableViewerPager.0")); //$NON-NLS-1$
		topPage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				offset = 1;
				firePageChangeListener(IPageChangeListener.EVT_MOVE_PREVIOUS, offset, limit);
				setPageNo(1);

			}
		});

		backPage = new ToolItem(toolBar4, SWT.NONE);
		backPage.setImage(ic.getImage(DbPlugin.IMG_CODE_PREVIOUS));
		backPage.setToolTipText(Messages.getString("TableViewerPager.8")); //$NON-NLS-1$
		backPage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				offset = offset - limit;
				firePageChangeListener(IPageChangeListener.EVT_MOVE_PREVIOUS, offset, limit);
				setPageNo(--pageNo);
			}
		});

		pageNoTxt = new Text(c, SWT.BORDER | SWT.RIGHT);
		pageNoTxt.setText("999999999"); //$NON-NLS-1$
		pageNoTxt.pack();
		pageNoTxt.setEditable(false);
		// pageNoTxt.setText("1");
		// pageNoTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final ToolBar toolBar5 = new ToolBar(c, SWT.HORIZONTAL);
		nextPage = new ToolItem(toolBar5, SWT.NONE);
		nextPage.setImage(ic.getImage(DbPlugin.IMG_CODE_NEXT));
		nextPage.setToolTipText(Messages.getString("TableViewerPager.9")); //$NON-NLS-1$
		nextPage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				offset = offset + limit;
				firePageChangeListener(IPageChangeListener.EVT_MOVE_PREVIOUS, offset, limit);
				setPageNo(++pageNo);
			}
		});

		endPage = new ToolItem(toolBar5, SWT.PUSH);
		endPage.setImage(ic.getImage(DbPlugin.IMG_CODE_END));
		endPage.setToolTipText(Messages.getString("TableViewerPager.2")); //$NON-NLS-1$
		endPage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				offset = (pageCount - 1) * limit + 1;
				firePageChangeListener(IPageChangeListener.EVT_MOVE_PREVIOUS, offset, limit);
				setPageNo(pageCount);
			}
		});

		sura = new Label(c, SWT.NONE);
		totalPage = new Label(c, SWT.NONE);

		sura.setText("/ "); //$NON-NLS-1$
		sura.pack();

		totalPage.setText("999999999"); //$NON-NLS-1$
		totalPage.pack();

		setEnabledPagerButton();

		return c;
	}

	/**
	 * ���y�[�W���̌v�Z
	 * 
	 * @param <code>pageSize</code> �\������
	 * @param <code>recordCount</code> ���R�[�h����
	 * @return ���y�[�W��
	 */
	private void calcPageCount(int aPageSize) {

		int wk_recordCount = getRecordCount(); // ���R�[�h�����̎擾

		if (aPageSize == 0) {
			this.pageCount = 1; // ��ʂ�����̃��R�[�h�����������̏ꍇ�́A�y�[�W�J�E���g��1
		} else {
			if (wk_recordCount % aPageSize == 0) {
				this.pageCount = wk_recordCount / aPageSize;
			} else {
				this.pageCount = wk_recordCount / aPageSize + 1;
			}
		}

	}

	private List listeners = new ArrayList();

	public void addPageChangeListener(IPageChangeListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removePageChangeListener(IPageChangeListener listener) {
		listeners.remove(listener);
	}

	public void firePageChangeListener(int status, int offset, int limit) {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			IPageChangeListener element = (IPageChangeListener) iter.next();
			if (element != null) {
				element.pageChanged(status, offset, limit);
			}
		}

	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int totalCount) {
		this.recordCount = totalCount;

		// �ŐV�̌������擾����(����)
		// limit =
		// DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);

		if (this.getRecordCount() > 0) {
			// ���y�[�W���̌v�Z����
			this.calcPageCount(limit);

			// �\������y�[�W�ԍ����A���y�[�W�ԍ��𒴂��Ă���ꍇ�̃G���[����
			if (pageNo > this.pageCount) {
				throw new RuntimeException(Messages.getString("TableViewerPager.5")); //$NON-NLS-1$
			}

			NumberFormat format = NumberFormat.getInstance();
			sura.setText(" / "); //$NON-NLS-1$
			totalPage.setText(format.format(pageCount) + Messages.getString("TableViewerPager.7")); //$NON-NLS-1$

		} else {
			sura.setText(""); //$NON-NLS-1$
			totalPage.setText(""); //$NON-NLS-1$

		}
		// sura.pack();
		// totalPage.pack();

		setEnabledPagerButton();


	}


	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		pageNoTxt.setText(String.valueOf(pageNo)); // �����Pack���Ȃ�
		setEnabledPagerButton();
	}

	// ���R�[�h�J�E���g��ݒ肷��ƘA�����āA�{�^����Enable���ύX�ɂȂ�
	private void setEnabledPagerButton() {
		IDBConfig config = table.getDbConfig();
		ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, table);
		if (table != null && factory.isSupportPager()) {
			topPage.setEnabled((pageNo > 1));
			backPage.setEnabled((pageNo > 1));
			nextPage.setEnabled((pageNo < pageCount));
			endPage.setEnabled((pageNo < pageCount));
		} else {
			topPage.setEnabled(false);
			backPage.setEnabled(false);
			nextPage.setEnabled(false);
			endPage.setEnabled(false);

		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
