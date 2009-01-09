/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.dialogs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import zigen.plugin.db.core.SchemaInfo;

/**
 * DBDialogSetting�N���X. org.eclipse.jface.dialogs.DialogSettings�N���X�� �ۑ�����Section���폜���郁�\�b�h��ǉ������N���X�B
 * 
 * original source:org.eclipse.jface.dialogs.DialogSettings
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/03/13 ZIGEN create.
 * 
 */
public class DBDialogSettings implements IDBDialogSettings {

	private String name;

	private Map sections;

	private Map items;

	private Map arrayItems;

	private Map schemaInfoItems;

	private final String TAG_SECTION = "section";//$NON-NLS-1$

	private final String TAG_NAME = "name";//$NON-NLS-1$

	private final String TAG_KEY = "key";//$NON-NLS-1$

	private final String TAG_VALUE = "value";//$NON-NLS-1$

	private final String TAG_LIST = "list";//$NON-NLS-1$

	private final String TAG_ITEM = "item";//$NON-NLS-1$


	private final String TAG_SCHEMA_LIST = "schemas";//$NON-NLS-1$

	private final String TAG_SCHEMA = "schema";//$NON-NLS-1$

	private final String TAG_CHECKED = "checked";//$NON-NLS-1$

	/**
	 * Create an empty dialog settings which loads and saves its content to a file. Use the methods <code>load(String)</code> and <code>store(String)</code> to load and store this dialog settings.
	 * 
	 * @param sectionName
	 *            the name of the section in the settings.
	 */
	public DBDialogSettings(String sectionName) {
		name = sectionName;
		items = new HashMap();
		arrayItems = new HashMap();
		sections = new HashMap();
		schemaInfoItems = new HashMap();
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public IDBDialogSettings addNewSection(String sectionName) {
		DBDialogSettings section = new DBDialogSettings(sectionName);
		addSection(section);
		return section;
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void addSection(IDialogSettings section) {
		sections.put(section.getName(), section);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public String get(String key) {
		return (String) items.get(key);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public String[] getArray(String key) {
		return (String[]) arrayItems.get(key);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public SchemaInfo[] getSchemaInfos(String key) {
		return (SchemaInfo[]) schemaInfoItems.get(key);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public boolean getBoolean(String key) {
		return new Boolean((String) items.get(key)).booleanValue();
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public double getDouble(String key) throws NumberFormatException {
		String setting = (String) items.get(key);
		if (setting == null)
			throw new NumberFormatException("There is no setting associated with the key \"" + key + "\"");//$NON-NLS-1$ //$NON-NLS-2$

		return new Double(setting).doubleValue();
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public float getFloat(String key) throws NumberFormatException {
		String setting = (String) items.get(key);
		if (setting == null)
			throw new NumberFormatException("There is no setting associated with the key \"" + key + "\"");//$NON-NLS-1$ //$NON-NLS-2$

		return new Float(setting).floatValue();
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public int getInt(String key) throws NumberFormatException {
		String setting = (String) items.get(key);
		if (setting == null) {
			// new Integer(null) will throw a NumberFormatException and meet our
			// spec, but this message
			// is clearer.
			throw new NumberFormatException("There is no setting associated with the key \"" + key + "\"");//$NON-NLS-1$ //$NON-NLS-2$
		}

		return new Integer(setting).intValue();
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public long getLong(String key) throws NumberFormatException {
		String setting = (String) items.get(key);
		if (setting == null) {
			// new Long(null) will throw a NumberFormatException and meet our
			// spec, but this message
			// is clearer.
			throw new NumberFormatException("There is no setting associated with the key \"" + key + "\"");//$NON-NLS-1$ //$NON-NLS-2$
		}

		return new Long(setting).longValue();
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public IDBDialogSettings getSection(String sectionName) {
		return (IDBDialogSettings) sections.get(sectionName);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public IDBDialogSettings[] getSections() {
		Collection values = sections.values();
		IDBDialogSettings[] result = new IDBDialogSettings[values.size()];
		values.toArray(result);
		return result;
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void load(Reader r) {
		Document document = null;
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			// parser.setProcessNamespace(true);
			document = parser.parse(new InputSource(r));

			// Strip out any comments first
			Node root = document.getFirstChild();
			while (root.getNodeType() == Node.COMMENT_NODE) {
				document.removeChild(root);
				root = document.getFirstChild();
			}
			load(document, (Element) root);
		} catch (ParserConfigurationException e) {
			// ignore
		} catch (IOException e) {
			// ignore
		} catch (SAXException e) {
			// ignore
		}
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void load(String fileName) throws IOException {
		FileInputStream stream = new FileInputStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));//$NON-NLS-1$
		load(reader);
		reader.close();
	}

	/*
	 * (non-Javadoc) Load the setting from the <code> document </code>
	 */
	private void load(Document document, Element root) {
		name = root.getAttribute(TAG_NAME);
		NodeList l = root.getElementsByTagName(TAG_ITEM);
		for (int i = 0; i < l.getLength(); i++) {
			Node n = l.item(i);
			if (root == n.getParentNode()) {
				String key = ((Element) l.item(i)).getAttribute(TAG_KEY);
				String value = ((Element) l.item(i)).getAttribute(TAG_VALUE);
				items.put(key, value);
			}
		}
		l = root.getElementsByTagName(TAG_LIST);
		for (int i = 0; i < l.getLength(); i++) {
			Node n = l.item(i);
			if (root == n.getParentNode()) {
				Element child = (Element) l.item(i);
				String key = child.getAttribute(TAG_KEY);
				NodeList list = child.getElementsByTagName(TAG_ITEM);

				List valueList = new ArrayList();
				for (int j = 0; j < list.getLength(); j++) {
					Element node = (Element) list.item(j);
					if (child == node.getParentNode()) {
						valueList.add(node.getAttribute(TAG_VALUE));
					}
				}
				String[] value = new String[valueList.size()];
				valueList.toArray(value);
				arrayItems.put(key, value);
			}
		}

		l = root.getElementsByTagName(TAG_SCHEMA_LIST);
		for (int i = 0; i < l.getLength(); i++) {
			Node n = l.item(i);
			if (root == n.getParentNode()) {
				Element child = (Element) l.item(i);
				String key = child.getAttribute(TAG_KEY);

				NodeList list = child.getElementsByTagName(TAG_SCHEMA);

				List valueList = new ArrayList();
				for (int j = 0; j < list.getLength(); j++) {
					Element node = (Element) list.item(j);
					if (child == node.getParentNode()) {
						SchemaInfo s = new SchemaInfo();
						s.setName(node.getAttribute(TAG_NAME));
						s.setChecked(new Boolean(node.getAttribute(TAG_CHECKED)).booleanValue());
						valueList.add(s);
						// valueList.add(node.getAttribute(TAG_VALUE));

					}
				}
				SchemaInfo[] value = new SchemaInfo[valueList.size()];
				valueList.toArray(value);
				schemaInfoItems.put(key, value);
			}
		}


		l = root.getElementsByTagName(TAG_SECTION);
		for (int i = 0; i < l.getLength(); i++) {
			Node n = l.item(i);
			if (root == n.getParentNode()) {
				DBDialogSettings s = new DBDialogSettings("NoName");//$NON-NLS-1$
				s.load(document, (Element) n);
				addSection(s);
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, String[] value) {
		arrayItems.put(key, value);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, SchemaInfo[] value) {
		schemaInfoItems.put(key, value);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, double value) {
		put(key, String.valueOf(value));
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, float value) {
		put(key, String.valueOf(value));
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, int value) {
		put(key, String.valueOf(value));
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, long value) {
		put(key, String.valueOf(value));
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, String value) {
		items.put(key, value);
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void put(String key, boolean value) {
		put(key, String.valueOf(value));
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void save(Writer writer) throws IOException {
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			save(document, document);
			Result result = new StreamResult(writer);
			Source source = new DOMSource(document);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$            
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			throw (IOException) (new IOException().initCause(e));
		} catch (TransformerException e) {
			throw (IOException) (new IOException().initCause(e));
		} catch (ParserConfigurationException e) {
			throw (IOException) (new IOException().initCause(e));
		}
	}

	/*
	 * (non-Javadoc) Method declared on IDialogSettings.
	 */
	public void save(String fileName) throws IOException {
		FileOutputStream stream = new FileOutputStream(fileName);
		OutputStreamWriter writer = new OutputStreamWriter(stream, "utf-8");//$NON-NLS-1$
		save(writer);
		writer.close();
	}

	/*
	 * (non-Javadoc) Save the settings in the <code> document </code> .
	 */
	private void save(Document document, Node parent) {
		Element root = document.createElement(TAG_SECTION);
		parent.appendChild(root);
		root.setAttribute(TAG_NAME, name == null ? "" : name); //$NON-NLS-1$

		for (Iterator i = items.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			Element child = document.createElement(TAG_ITEM);
			root.appendChild(child);
			child.setAttribute(TAG_KEY, key == null ? "" : key); //$NON-NLS-1$
			String string = (String) items.get(key);
			child.setAttribute(TAG_VALUE, string == null ? "" : string); //$NON-NLS-1$        
		}

		for (Iterator i = arrayItems.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			Element child = document.createElement(TAG_LIST);
			root.appendChild(child);
			child.setAttribute(TAG_KEY, key == null ? "" : key); //$NON-NLS-1$
			String[] value = (String[]) arrayItems.get(key);
			if (value != null) {
				for (int index = 0; index < value.length; index++) {
					Element c = document.createElement(TAG_ITEM);
					child.appendChild(c);
					String string = value[index];
					c.setAttribute(TAG_VALUE, string == null ? "" : string); //$NON-NLS-1$
				}
			}
		}

		for (Iterator i = schemaInfoItems.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			Element child = document.createElement(TAG_SCHEMA_LIST);
			root.appendChild(child);
			child.setAttribute(TAG_KEY, key == null ? "" : key); //$NON-NLS-1$
			SchemaInfo[] value = (SchemaInfo[]) schemaInfoItems.get(key);
			if (value != null) {
				for (int index = 0; index < value.length; index++) {
					Element c = document.createElement(TAG_SCHEMA);
					child.appendChild(c);
					SchemaInfo schema = value[index];
					c.setAttribute(TAG_NAME, schema.getName()); //$NON-NLS-1$
					c.setAttribute(TAG_CHECKED, String.valueOf(schema.isChecked())); //$NON-NLS-1$   

				}
			}

		}
		for (Iterator i = sections.values().iterator(); i.hasNext();) {
			((DBDialogSettings) i.next()).save(document, root);
		}
	}

	public void addSection(IDBDialogSettings section) {
		sections.put(section.getName(), section);
	}

	// �� �Ǝ��Ɏ����������\�b�h �� //

	/**
	 * �w�肵��Section���폜����
	 */
	public void removeSection(String sectionName) {
		sections.remove(sectionName);
	}

	/**
	 * �w�肵��SectionName���܂܂�邩
	 */
	public boolean hasSection(String sectionName) {
		return sections.containsKey(sectionName);
	}

	// �� �Ǝ��Ɏ����������\�b�h �� //

}
