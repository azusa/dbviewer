/*
 * 著作権: Copyright (c) 2007－2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.core;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.IPreferenceStore;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.DefaultXmlManager;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.util.FileUtil;

public class SQLHistoryManager extends DefaultXmlManager {

	private int maxSize = 100;

	private List history = null;

	int currentPosition = 0;

	private IPreferenceStore preferenceStore;

	public SQLHistoryManager(IPath path) {
		super(path, DbPluginConstant.FN_SQL_HISTORY);

		this.preferenceStore = DbPlugin.getDefault().getPreferenceStore();

		history = load();
		if (history == null) {
			// history = new ArrayList();
			history = new LinkedList();
			history.add(new SQLHistory(true)); // blank
		}

	}

	public List load() {
		try {
			Object obj = super.loadXml();
			if (obj instanceof List) {
				history = (List) obj;
				// 現在の位置を設定
				if (history.size() > 0) {
					currentPosition = history.size();
				}
				// 空のSQLを追加する(履歴には保存されない）
				// history.add(currentPosition, new SQLHistory(true));
				history.add(new SQLHistory(true));

				// ソート
				Collections.sort(history, new SQLHistorySorter());
				
				return history;
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return null;
	}

	public void save() {
		try {
			TimeWatcher tw = new TimeWatcher();
			tw.start();
			
			// 空白は取り除く
			for (Iterator iter = history.iterator(); iter.hasNext();) {
				SQLHistory sql = (SQLHistory) iter.next();
				if (sql.isBlank()) {
					iter.remove();
				}
				
				if(!sql.isFileMode()){
					// 旧Versionの履歴を変換する処理
					saveContents(sql);
				}

			}
			super.saveXml(history);
			tw.stop();
			
		} catch (IOException e) {
			DbPlugin.log(e);
		}

	}


	public String loadContents(SQLHistory history){
		String sql = null;
		String folderName = history.getFolderName();
		String fileName = history.getFileName();
		String filePath = file.getParent() + File.separator + folderName + File.separator + fileName + ".sql";
		File file = new File(filePath);
		if(file.exists()){
			sql = FileUtil.getContents(new File(filePath));
		}else{
			sql = history.getSql();
		}
		return sql;
	}
	
	public void saveContents(SQLHistory history) {
		try {
			// フォルダ作成
			createFolder(history);
			// SQLファイル作成
			createFile(history);
			// 短くしたSQLに変換
			history.setSql(getShortSql(history.getSql()));
			// フラグを立てる
			history.setFileMode(true);
			super.saveXml(history);

		} catch (IOException e) {
			DbPlugin.log(e);
		}

	}

	String getShortSql(String fullSql) {
//		String sql = SQLFormatter.unformat(fullSql);
		String sql = fullSql; // レスポンス悪化のため、Unformatしない
		if (sql == null)
			return "";
		if (sql.length() > History.MAX_LEN) {
			return sql.substring(0, History.MAX_LEN) + "...";
		} else {
			return sql;
		}
	}

	boolean createFolder(SQLHistory history) throws IOException {
		String folderName = history.getFolderName();

		String folderPath = file.getParent() + File.separator + folderName;
		File f = new File(folderPath);
		if (!f.exists()) {
			return f.mkdir();
		} else {
			return true;
		}
	}

	void createFile(SQLHistory history) throws IOException {
		String folderName = history.getFolderName();
		String fileName = history.getFileName();

		String filePath = file.getParent() + File.separator + folderName + File.separator + fileName + ".sql";
		File f = new File(filePath);
		InputStreamUtil.save(f, new StringReader(history.getSql()));
	}

	void removeFile(SQLHistory history) throws IOException {
		String folderName = history.getFolderName();
		String fileName = history.getFileName();

		String filePath = file.getParent() + File.separator + folderName + File.separator + fileName + ".sql";
		File f = new File(filePath);
		if(f.exists()){
			f.delete();
		}
		
		
		if(f.getParentFile().list().length == 0){
			f.getParentFile().delete();
		}
	}

	

	public List getHistory() {
		return this.history;
	}

	public int getHistoryCount() {
		return history.size();
	}

	public void clearHistory() {
		history = new ArrayList();
		currentPosition = 0;

	}

	public void removeOverHistory() throws IOException{
		try {
			this.maxSize = preferenceStore.getInt(PreferencePage.P_MAX_HISTORY);
			while (history.size() - 1 > maxSize) { // 空白用を考慮
				SQLHistory sh = (SQLHistory)history.get(0);
				removeFile(sh); //add
				
				history.remove(0);
				currentPosition--; // 減らした分位置を--
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

	private boolean isSameHistory(String sql, int position) {
		if (position >= 0) {
			TimeWatcher tw = new TimeWatcher();
			tw.start();

			SQLHistory his = (SQLHistory) history.get(position);
			String targetSql = loadContents(his);
			
			// 大量のINSERT文をunFormatして比較すると、CPUが100%になり、レスポンスが悪化するため、Unformatしないで比較する
//			String hSql = SQLFormatter.unformat(targetSql);
//			String uSql = SQLFormatter.unformat(sql);
			String hSql = targetSql;
			String uSql = sql;
			
			tw.stop();			
//			tw.start();
//			// 仮に全部の履歴とチェックする
//			for (Iterator iter = history.iterator(); iter.hasNext();) {
//				SQLHistory sh = (SQLHistory) iter.next();
//				String targetSql2 = loadContents(sh);
//				String hSql2 = SQLFormatter.unformat(targetSql);
//				if (uSql.equals(hSql2)) {
//				}
//				
//			}
//			tw.stop();
//

			if (uSql.equals(hSql)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public boolean addHistory(SQLHistory his) throws IOException {
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		boolean isAdd = false;
		String sql = his.getSql();
		SQLHistory current = currentHistory();
		
		if (!isSameHistory(sql, currentPosition)) {
			
			// SQLファイルを記録
			saveContents(his);
			history.add(his); // 最後の１つ前にする
			currentPosition = history.size() - 2; // カレントを最後から１つ前にする
			isAdd = true;
		}
		
		Collections.sort(history, new SQLHistorySorter());
		
		// 最大数を超えた履歴は削除する
		removeOverHistory();

		tw.stop();
		return isAdd;
	}

	public void remove(SQLHistory sqlHistory) throws IOException{
//		TimeWatcher tw = new TimeWatcher();
//		tw.start();
		
		removeFile(sqlHistory); //add
		history.remove(sqlHistory);
		if (currentPosition > 0) {
			currentPosition--;
		}

//		tw.stop();
	}

	// 現在の履歴を取得
	public SQLHistory currentHistory() {
		return (history.size() == 0) ? null : (SQLHistory) history.get(currentPosition);

	}

	// 前の履歴を取得
	public SQLHistory prevHisotry() {
		return (currentPosition <= 0) ? null : (SQLHistory) history.get(--currentPosition);
	}

	// 次の履歴を取得
	public SQLHistory nextHisotry() {
		return (currentPosition == history.size() - 1) ? null : (SQLHistory) history.get(++currentPosition);
	}

	// 前の履歴があるかどうか
	public boolean hasPrevHistory() {
		return (currentPosition >= 1);
	}

	// 次の履歴があるかどうか
	public boolean hasNextHistory() {
		return (history.size() - 1 > currentPosition);
	}

	// デバック用
	public int getCurrentPosition() {
		return currentPosition;
	}

	public void modifyCurrentPosition(SQLHistory target) {
		for (int i = history.size() - 1; i >= 0; i--) {
			SQLHistory sql = (SQLHistory) history.get(i);
			if (sql.equals(target)) {
				currentPosition = i;
				return;
			}
		}

	}

}
