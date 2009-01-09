/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.editors.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;

/**
 * TableViewerManager�N���X.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/09/17 ZIGEN create.
 * 
 */
public class TableViewerManager {

	/**
	 * �v�f��ǉ�����
	 * 
	 * @param viewer
	 * @param newElement
	 */
	public static void insert(TableViewer viewer, TableElement newElement) {

		Object obj = viewer.getInput();

		if (obj instanceof TableElement[]) {
			TableElement[] old = (TableElement[]) viewer.getInput();

			TableElement[] wk = new TableElement[old.length + 1];
			System.arraycopy(old, 0, wk, 0, old.length);

			// �Ō�ɐV�KElement��ǉ�����
			wk[old.length] = newElement;

			// viewer�ɒǉ�����Element���Z�b�g����
			viewer.setInput(wk);

		} else {
			throw new IllegalArgumentException("TableViewer�Ɋi�[����Ă���I�u�W�F�N�g���قȂ�܂�"); //$NON-NLS-1$
		}
	}

	/**
	 * �w�肵���v�f���X�V���� �������s�X�V���ꂽ�ꍇ�ł��A���ۂɍX�V�����s�̂ݍĕ`�悳���
	 * 
	 * @param viewer
	 * @param from
	 * @param to
	 */
	public static void update(TableViewer viewer, TableElement from, TableElement to) {

		Object obj = viewer.getInput();
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) viewer.getInput();
			for (int i = 1; i < elements.length; i++) {
				if (elements[i].equals(from)) {
					TableElement ele = elements[i];
					ele.copy(to);
					ele.clearMofiedColumn();

					// System.out.println(to.getRecordNo());
					// System.out.println(from.getRecordNo());
					// System.out.println();
					// ele.setRecordNo(i); // 1�Ԗڂ̓w�b�_�[
					ele.setRecordNo(from.getRecordNo()); // 1�Ԗڂ̓w�b�_�[

					ele.isNew(false);
					viewer.update(ele, null);// �e�[�u���E�r���[�����X�V
					break; // �X�V�s����������break����
				} else {


				}
			}
		} else {
			throw new IllegalArgumentException("TableViewer�Ɋi�[����Ă���I�u�W�F�N�g���قȂ�܂�"); //$NON-NLS-1$
		}
		tw.stop();
	}

	/**
	 * �w�肵���v�f���폜����
	 * 
	 * @param viewer
	 * @param from
	 * @param to
	 */
	public static void remove(TableViewer viewer, Object target) {
		Object obj = viewer.getInput();

		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) viewer.getInput();

			List newList = new ArrayList();
			for (int i = 0; i < elements.length; i++) {
				if (!elements[i].equals(target)) {
					// �s��v��Element�̂ݒǉ�
					newList.add(elements[i]);
				}
			}
			// viewer�ɐV����Element���Z�b�g����
			viewer.setInput((TableElement[]) newList.toArray(new TableElement[0]));

			// TableViewer���ĕ`��
			// viewer.refresh();

		} else {
			throw new IllegalArgumentException("TableViewer�Ɋi�[����Ă���I�u�W�F�N�g���قȂ�܂�"); //$NON-NLS-1$
		}
	}

	/**
	 * �w�肵�������̗v�f���폜����
	 * 
	 * @param viewer
	 * @param target
	 */
	public static void remove(TableViewer viewer, Object[] target) {
		TableElement[] contents = (TableElement[]) viewer.getInput();

		LinkedList srcList = new LinkedList(Arrays.asList(contents));
		for (int i = 0; i < target.length; i++) {
			srcList.remove(target[i]);
		}
		contents = (TableElement[]) srcList.toArray(new TableElement[srcList.size()]);

		viewer.setInput(contents);
	}


}
