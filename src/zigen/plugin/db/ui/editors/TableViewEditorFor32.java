/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.contentassist.ContentAssistHandler;
import org.eclipse.ui.handlers.IHandlerService;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.contentassist.ColumnContentAssistantProcessor;
import zigen.plugin.db.ui.contentassist.ContentAssistUtil;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.views.internal.SQLWhitespaceDetector;

/**
 * TableEditor�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/26 ZIGEN create.
 */
public class TableViewEditorFor32 extends TableViewEditorFor31 implements ITableViewEditor {

	private int wordLen = 0;// �R�[�h�A�V�X�g�p�P��̒������i�[

	private boolean contentAssisting = false;

	private boolean proposalFiltering = false;

	static final String[] Keywords = {"AND", "ASC", "BETWEEN", "BY", "DESC", "EXISTS", "IN", "IS NULL", "IS NOT NULL", "LIKE", "NOT", "NOT EXISTS", "NULL", "OR", "ORDER BY"};

	private class ColumnContentProposal implements IContentProposalProvider {

		IContentProposal[] contentProposals;

		Column[] columns;

		public ColumnContentProposal(Column[] columns) {
			this.columns = columns;
			// this.contentProposals = new IContentProposal[columns.length];
			this.contentProposals = new IContentProposal[columns.length + Keywords.length];

		}

		private void setColumnProposal(List proposalList, String word) {
			for (int i = 0; i < columns.length; i++) {
				final Column col = columns[i];
				final String colName = col.getName();
				final String value = ContentAssistUtil.subString(colName, wordLen);
				if (word != null && !"".equals(word)) { //$NON-NLS-1$
					proposalFiltering = true; // ���̍i�荞�݃��[�hON
					if (value.compareToIgnoreCase(word) == 0) {
						// ���̍쐬�ƒǉ�
						IContentProposal p = new IContentProposal() {

							public String getContent() {
								return colName;
							}

							public String getDescription() {
								return null;
							}

							public String getLabel() {
								return col.getColumnLabel();
							}

							public int getCursorPosition() {
								// �i�荞�񂾕�����
								// return col.getName().length() - wordLen;
								// �i�荞�񂾏ꍇ�́A�㏑�����邽�߁A�ʏ�ǂ���Ԃ�
								return col.getName().length();

							}
						};
						proposalList.add(p);
					}
				} else {
					proposalFiltering = false; // ���̍i�荞�݃��[�hOFF
					// ���̍쐬�ƒǉ�
					IContentProposal p = new IContentProposal() {

						public String getContent() {
							return colName;
						}

						public String getDescription() {
							return null;
						}

						public String getLabel() {
							return col.getColumnLabel();
						}

						public int getCursorPosition() {
							return col.getName().length();
						}
					};
					proposalList.add(p);
				}

			}
		}

		private void setKeywordProposal(List proposalList, String word) {
			for (int i = 0; i < Keywords.length; i++) {
				final String keyword = Keywords[i];
				final String value = ContentAssistUtil.subString(keyword, wordLen);
				if (word != null && !"".equals(word)) { //$NON-NLS-1$
					proposalFiltering = true; // ���̍i�荞�݃��[�hON
					if (value.compareToIgnoreCase(word) == 0) {
						// ���̍쐬�ƒǉ�
						IContentProposal p = new IContentProposal() {

							public String getContent() {
								return keyword;
							}

							public String getDescription() {
								return null;
							}

							public String getLabel() {
								return keyword;
							}

							public int getCursorPosition() {
								// �i�荞�񂾕�����
								// return col.getName().length() - wordLen;
								// �i�荞�񂾏ꍇ�́A�㏑�����邽�߁A�ʏ�ǂ���Ԃ�
								return keyword.length();

							}
						};
						proposalList.add(p);
					}
				} else {
					proposalFiltering = false; // ���̍i�荞�݃��[�hOFF
					// ���̍쐬�ƒǉ�
					IContentProposal p = new IContentProposal() {

						public String getContent() {
							return keyword;
						}

						public String getDescription() {
							return null;
						}

						public String getLabel() {
							return keyword;
						}

						public int getCursorPosition() {
							return keyword.length();
						}
					};
					proposalList.add(p);
				}

			}
		}

		public IContentProposal[] getProposals(String contents, int position) {
			List proposalList = new ArrayList();
			String word = getPreviousWord(contents, position);
			wordLen = word.length();
			setColumnProposal(proposalList, word);
			setKeywordProposal(proposalList, word);
			// ��₪�O�̏ꍇ���l������
			if (proposalList.size() == 0) {
				contentAssisting = false;
			}
			return (IContentProposal[]) proposalList.toArray(new IContentProposal[0]);
		}

	}

	private class ColumnComboContentAdapter extends ComboContentAdapter {

		public void insertControlContents(Control control, String text, int cursorPosition) {
			super.insertControlContents(control, text, cursorPosition);
			contentAssisting = false;// �R���e���c�A�V�X�g�I��
		}

		public void setControlContents(Control control, String text, int cursorPosition) {
			super.setControlContents(control, text, cursorPosition);
		}

		public void setCursorPosition(Control control, int index) {
			// �i�荞�݂��Ă��镶������������ �� �i�荞�ݕ����͒u��������
			Combo cmb = (Combo) control;
			String text = cmb.getText();
			cmb.setText(text.substring(0, index - wordLen) + text.substring(index));
			super.setCursorPosition(control, (index - wordLen));
		}

	}

	public TableViewEditorFor32() {
		super();
	}

	protected void conditionEventHandler(KeyEvent e) {

		if (e.character == SWT.CR && !contentAssisting) {
			e.doit = true;
			whereString = conditionComb.getText();

			pager.setPageNo(1); // Where�����Ō�������ƕK��1�y�[�W�Ɉړ�����
			offset = 1; // offset��������
			limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD); // ������������
			updateTableViewer(whereString, offset, limit);

		} else if (e.stateMask == SWT.CTRL && e.character == ' ') {
			contentAssisting = true;
			e.doit = false;

		}
	}

	// ���݂̃I�t�Z�b�g���璼�O�̋󔒂܂ł̕������Ԃ����\�b�h
	private String getPreviousWord(String contents, int position) {
		SQLWhitespaceDetector whiteSpace = new SQLWhitespaceDetector();
		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				char c = contents.charAt(--position);
				// �󔒂��s���I�h�ɓ��B
				// if (Character.isWhitespace(c) || c == '.') return
				// buf.reverse().toString();
				if (whiteSpace.isWhitespace(c) || c == '.')
					return buf.reverse().toString();
				// ������ǉ�
				buf.append(c);
			} catch (Exception e) {
				// �h�L�������g�̐擪�܂œ��B�����ꍇ
				return buf.reverse().toString();
			}
		}
	}

	IContentProposalProvider proposal = null;

	protected void AddContentAssist2() {
		// �e�[�u����Reflesh�̏ꍇ�A�ēx���s���邽�߁A�L���b�V�����Ȃ��悤�ɂ���
		try {
			Column[] columns = tableNode.getColumns();
			proposal = new ColumnContentProposal(columns);
			KeyStroke keyStroke = KeyStroke.getInstance("Ctrl+Space"); //$NON-NLS-1$
			new ContentProposalAdapter(conditionComb, new ColumnComboContentAdapter(), proposal, keyStroke, null);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	ContentAssistHandler fContentAssistHandler;

	protected void AddContentAssist() {
		if (fContentAssistHandler == null) {
			fContentAssistHandler = ContentAssistHandler.createHandlerForCombo(conditionComb, createContentAssistant());
			fContentAssistHandler.setEnabled(true);
		}
	}

	public SubjectControlContentAssistant createContentAssistant() {
		final SubjectControlContentAssistant contentAssistant = new SubjectControlContentAssistant();

		Column[] columns = tableNode.getColumns();

		IContentAssistProcessor processor = new ColumnContentAssistantProcessor(columns);
		contentAssistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		contentAssistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		contentAssistant.setInformationControlCreator(new IInformationControlCreator() {

			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent);
			}
		});

		// IInformationControlCreator�̐ݒ�
		// contentAssistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));

		// �����A�N�e�B�x�[�V������L���ɂ���ꍇ��true
		contentAssistant.enableAutoActivation(true);

		// ��₪�P�����Ȃ��ꍇ�́A�����I�Ɍ���}������ꍇ��true
		contentAssistant.enableAutoInsert(true);

		return contentAssistant;
	}

	protected void setKeyBinding() {
		try {
			IHandlerService service = (IHandlerService) getEditorSite().getService(IHandlerService.class);
			service.activateHandler("zigen.plugin.InsertRecordCommand", new ActionHandler(insertRecordAction)); //$NON-NLS-1$
		} catch (NoClassDefFoundError e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}
}
