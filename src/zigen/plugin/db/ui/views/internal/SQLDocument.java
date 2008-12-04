/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.SQLEditorPreferencePage;

public class SQLDocument extends Document {

	public SQLDocument(String contents) {
		super(contents);
		String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
		
		IDocumentPartitioner partitioner = new FastPartitioner(new SQLPartitionScanner(), new String[] {
				SQLPartitionScanner.SQL_STRING,
				SQLPartitionScanner.SQL_COMMENT
		});
		partitioner.connect(this);
		this.setDocumentPartitioner(partitioner);
	}

	public SQLDocument() {
		this(""); //$NON-NLS-1$
	}

	// private IDocumentPartitioner createDocumentPartitioner() {
	// //IDocumentPartitioner partitioner = new DefaultPartitioner(new
	// SQLPartitionScanner(), new String[]{
	// // SQLPartitionScanner.SQL_STRING, SQLPartitionScanner.SQL_COMMENT
	// //});
	// IDocumentPartitioner partitioner = new DefaultPartitioner(new
	// SQLPartitionScanner(), new String[]{
	// SQLPartitionScanner.SQL_STRING, SQLPartitionScanner.SQL_COMMENT
	// });
	// return partitioner;
	//
	// }

}
