package zigen.plugin.db.core;

import junit.framework.TestCase;
import kry.sql.format.ISqlFormat;
import kry.sql.format.SqlFormat;
import kry.sql.format.SqlFormatException;
import kry.sql.format.SqlFormatRule;

public class TemplateFormatTest extends TestCase {

	public void testCheck1(){

		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT\r\n");
			sb.append("        ${columnName}\r\n");
			sb.append("    FROM\r\n");
			sb.append("        ${tableName}");

			SqlFormatRule fRule = new SqlFormatRule();
			fRule.setRemoveEmptyLine(true); // 空行を削除する

			ISqlFormat formatter = new SqlFormat(fRule);
			String s = formatter.format(sb.toString(), 0);
			
			
			assertEquals(sb.toString(), reConvert(s));
			
			
		} catch (SqlFormatException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	public void testCheck3(){

		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("CREATE\r\n");
			sb.append("    TABLE\r\n");
			sb.append("        ${tableName} (${columnName} ${dataType} ${notNull})");

			SqlFormatRule fRule = new SqlFormatRule();
			fRule.setRemoveEmptyLine(true); // 空行を削除する

			ISqlFormat formatter = new SqlFormat(fRule);
			String s = formatter.format(sb.toString(), 0);
			
			
			assertEquals(sb.toString(), reConvert(s));
			
			
		} catch (SqlFormatException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	static String reConvert(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(sql, " ");
		String token = null;
		int indent = 0;
		int preIndent = 0; // 1つ前のインデント

		boolean isDoru = false;
		boolean isStart = false;
		boolean isEnd = false;
		while ((token = tokenizer.nextToken()) != null) {
			if (token.trim().length() == 0) {
				indent++;
			} else {
				System.out.println(token);
				String wk = token.trim();
				if (wk.startsWith("$")) {
					isDoru = true;
					sb.append(" ");
					sb.append(StringUtil.indent(token, indent));

					preIndent = indent;
					indent = 0;

				} else if(wk.startsWith("{")){
					isStart = true;
					sb.append(StringUtil.indent(token, indent));
					preIndent = indent;
					indent = 0;
					
				} else if(wk.startsWith("}")){
					isEnd = true;
					sb.append(StringUtil.indent(token, indent));
					preIndent = indent;
					indent = 0;	
				} else {
					if (sb.length() == 0) {
						sb.append(token);
					} else if(isDoru || isStart){
						sb.append(StringUtil.indent(token, indent));
						isDoru = false;
						isStart = false;
						
					} else if(isEnd){
						sb.append(" ");
						sb.append(StringUtil.indent(token, indent));
						isEnd = false;
				
					}else{
						sb.append(" ");
						sb.append(StringUtil.indent(token, indent));
					}
					preIndent = indent;
					indent = 0;
				}

			}
		}

		return sb.toString();
	}
	
}
