/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.dialogs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import zigen.plugin.db.core.SchemaInfo;

/**
 * IDataBaseSettings�N���X. �I���W�i���\�[�X�Forg.eclipse.jface.dialogs.IDialogSettings
 * 
 * org.eclipse.jface.dialogs.IDialogSettings�N���X�� �ۑ�����Section���폜���郁�\�b�h��ǉ������N���X�B
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4
 * @see org.eclipse.jface.dialogs.IDialogSettings history Symbol Date Person
 *      Note [1] 2005/03/15 ZIGEN create.
 * 
 */
public interface IDBDialogSettings {

	public IDBDialogSettings addNewSection(String name);

	public void addSection(IDBDialogSettings section);

	public String get(String key);

	public String[] getArray(String key);
	
	public SchemaInfo[] getSchemaInfos(String key); // ADD

	public boolean getBoolean(String key);

	public double getDouble(String key) throws NumberFormatException;

	public float getFloat(String key) throws NumberFormatException;

	public int getInt(String key) throws NumberFormatException;

	public long getLong(String key) throws NumberFormatException;

	public String getName();

	public IDBDialogSettings getSection(String sectionName);

	public IDBDialogSettings[] getSections();

	public void load(Reader reader) throws IOException;

	public void load(String fileName) throws IOException;

	public void put(String key, String[] value);
	
	public void put(String key, SchemaInfo[] value);

	public void put(String key, double value);

	public void put(String key, float value);

	public void put(String key, int value);

	public void put(String key, long value);

	public void put(String key, String value);

	public void put(String key, boolean value);

	public void save(Writer writer) throws IOException;

	public void save(String fileName) throws IOException;

	public void removeSection(String sectionName);

	public boolean hasSection(String sectionName);
}
