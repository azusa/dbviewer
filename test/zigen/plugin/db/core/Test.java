package zigen.plugin.db.core;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class Test extends TestCase {

	public void testCheck(){
		String s = "BIN$HGnc55/7rRPgQPeM/qQoRw==$0";
		assertEquals(true, SQLUtil.requireDoubleQuote(s));
	
	}
	
	//
	public void testCheck2(){
		String s = "BIN$k7agxkkzRPabXvQ2Tgevxg==$0";
		assertEquals(true, SQLUtil.requireDoubleQuote(s));
	
	}
	
	public void testCheck3(){
		String s = "BIN$upOLNpu4TwSIZIqXH+nexg==$0";
		assertEquals(true, SQLUtil.requireDoubleQuote(s));
	
	}
	public void testCheck4(){
		String s = "BIN$/XksMqAsS4OF0wGj44hhcA==$0";
		assertEquals(true, SQLUtil.requireDoubleQuote(s));
	
	}
	public void test1(){
		BigDecimal num = new BigDecimal("123.0");
		BigDecimal a = num.setScale(0, BigDecimal.ROUND_CEILING);
		BigDecimal b = num.setScale(0, BigDecimal.ROUND_FLOOR);
		
		System.out.println(a);
		System.out.println(b);
		
		
	}
	
	protected final String toStringForDisplay(BigDecimal num) {
		// if(Math.ceil(num) == Math.floor(num)){
		// return String.valueOf((int)num);
		// }else{
		// return String.valueOf(num);
		// }
		BigDecimal a = num.setScale(0, BigDecimal.ROUND_CEILING);
		BigDecimal b = num.setScale(0, BigDecimal.ROUND_FLOOR);
		if (a.equals(b)) {
			return a.toString();
		} else {
			return num.toString();
		}
	}

}
