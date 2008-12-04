/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */


package zigen.plugin.db.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import zigen.plugin.db.DbPlugin;

/**
 * DriverSearcherWithProgressクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/04/02 ZIGEN create.
 * 
 */
public class DriverSearcherWithProgress implements IRunnableWithProgress {

	private ClassLoader loader;

	private String[] classpaths;

	private List driverNames;

	/**
	 * コンストラクタ
	 * 
	 */
	public DriverSearcherWithProgress(ClassLoader loader, String[] classpaths) {
		this.loader = loader;
		this.classpaths = classpaths;

	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		monitor.beginTask("Search for DriverName...", calcTotalWork()); //$NON-NLS-1$
		driverNames = searchDriver(monitor);
		monitor.done();
	}

	/**
	 * トータル作業量の計算 ※正確な作業量を計算する
	 * 
	 * @return
	 */
	private int calcTotalWork() {
		int cnt = 0;
		ZipInputStream in = null;
		try {
			for (int i = 0; i < classpaths.length; i++) {
				try {

					if (classpaths[i].endsWith(".class")) { //$NON-NLS-1$
						cnt++;
					} else {
						File classFile = new File(classpaths[i]);
						in = new ZipInputStream(new FileInputStream(classFile));
						ZipEntry entry = null;
						while ((entry = in.getNextEntry()) != null) {
							cnt++;
						}
					}

				} catch (FileNotFoundException ex) {
					DbPlugin.log(ex);
					continue;
				} catch (IOException ex) {
					DbPlugin.log(ex);
					continue;
				}

			}
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
			}
		}

		return cnt;
	}

	/**
	 * 指定したZIPもしくはJARファイルからDriverNameを取得する
	 * 
	 * @param loader
	 * @param file
	 * @return
	 */
	public List searchDriver(IProgressMonitor monitor) throws InterruptedException {

		List driverList = new ArrayList();
		ZipInputStream in = null;

		try {
			for (int i = 0; i < classpaths.length; i++) {

				monitor.subTask(classpaths[i]);
				// Thread.sleep(50);
				try {

					if (classpaths[i].endsWith(".class")) { //$NON-NLS-1$
						addDriverList(monitor, driverList, classpaths[i]);

					} else {
						File classFile = new File(classpaths[i]);
						in = new ZipInputStream(new FileInputStream(classFile));
						ZipEntry entry = null;
						while ((entry = in.getNextEntry()) != null) {
							// Thread.sleep(1);
							// キャンセル時はInterruptedExceptionをスロー
							if (monitor.isCanceled()) {
								throw new InterruptedException("Search of DriverName was canceled. "); //$NON-NLS-1$
							}
							addDriverList(monitor, driverList, entry.getName());
						}

					}
				} catch (FileNotFoundException e) {
					DbPlugin.log(e);
					continue;
				} catch (IOException e) {
					DbPlugin.log(e);
					continue;
				}

			}

		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
			}

		}

		return driverList;
	}

	/**
	 * addDriverList
	 * 
	 * @param monitor
	 * @param driverList
	 * @param name
	 */
	private void addDriverList(IProgressMonitor monitor, List driverList, String name) {
		if (name.endsWith(".class")) { //$NON-NLS-1$
			// 区切り文字を/から.に変換し、.classを除く
			name = name.replace('/', '.').substring(0, name.length() - 6);
			monitor.subTask(name);
			try {
				Class clazz = loader.loadClass(name);
				if (Driver.class.isAssignableFrom(clazz) && !Driver.class.equals(clazz)) {
					driverList.add(name);
				} else {
					monitor.worked(1);
					return;
				}
			} catch (ClassNotFoundException ex) {
				monitor.worked(1);
				return;
			} catch (NoClassDefFoundError ex) {
				monitor.worked(1);
				return;
			} catch (Exception ex) {
				monitor.worked(1);
				return;
			}

		}
		monitor.worked(1);
	}

	/**
	 * @return driverNames を戻します。
	 */
	public List getDriverNames() {
		return driverNames;
	}
}
