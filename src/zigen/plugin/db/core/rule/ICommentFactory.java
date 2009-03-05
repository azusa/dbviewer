package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.util.Map;


public interface ICommentFactory {
	
	public Map getRemarkMap(Connection con, String owner) throws Exception;
	
	public String getDbName();
}
