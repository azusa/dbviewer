package zigen.plugin.db.core;

import java.util.Iterator;

import junit.framework.TestCase;
import kry.sql.format.SqlFormatRule;
import kry.sql.tokenizer.SqlTokenizer;
import kry.sql.tokenizer.Token;

public class TokenTest extends TestCase {

	public void testCheck3(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT        *    FROM        MAAT0010    WHERE\n");
		sb.append("        (FROM) = 'A'");

		SqlFormatRule rule  = new SqlFormatRule();
		SqlTokenizer tokenizer = new SqlTokenizer(sb.toString(), rule);
		
		int offset = 0;
		for (Iterator it = tokenizer; it.hasNext();) {
			Token token = (Token) it.next();
			offset = offset + token.getX() + token.getOriginalLength();
		
			if(token.getOriginal().equals("FROM")){
				System.out.println("position offset:" + token.getIndex() + ", length:" + token.getOriginalLength() + ">>" + token.getOriginal());
					
			}
			
		}
		assertEquals(0, 0);
	}
	

	
}
