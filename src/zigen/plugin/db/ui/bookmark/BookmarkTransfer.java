/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.bookmark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.Bookmark;

/**
 * BookmarkTransfer
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2006/02/08 ZIGEN create.
 * 
 */
public class BookmarkTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "Bookmark"; //$NON-NLS-1$

	private static final int MYTYPEID = registerType(MYTYPENAME);

	private static BookmarkTransfer instance = new BookmarkTransfer();

	public BookmarkTransfer() {
	}

	public static BookmarkTransfer getInstance() {
		return instance;
	}

	// シリアライズ
	public void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof Bookmark)) {
			return;
		}
		if (isSupportedType(transferData)) {
			Bookmark bm = (Bookmark) object;
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(out);
				os.writeObject(bm);
				byte[] buffer = out.toByteArray();
				os.flush();
				os.close();
				super.javaToNative(buffer, transferData);
			} catch (IOException e) {
				DbPlugin.log(e);
			}
		}
	}

	// デシリアライズ
	public Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null) {
				return null;
			}
			Bookmark bm = null;
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream os = new ObjectInputStream(in);
				bm = (Bookmark) os.readObject();

				os.close();
			} catch (Exception e) {
				DbPlugin.log(e);
				return null;
			}
			return bm;
		}
		return null;
	}

	protected String[] getTypeNames() {
		return new String[] {
			MYTYPENAME
		};
	}

	protected int[] getTypeIds() {
		return new int[] {
			MYTYPEID
		};
	}

}
