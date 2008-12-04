package zigen.plugin.db.core;

import junit.framework.TestCase;
import zigen.plugin.db.preference.SQLFormatPreferencePage;

public class SQLFormatterPachTest extends TestCase {

	private void check(StringBuffer sb){
		int type = SQLFormatPreferencePage.TYPE_DBVIEWER;
		assertEquals(sb.toString(), SQLFormatter.format(sb.toString(), type, true));
	}
	public void testSelectFormat1() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        COUNT(*)\n");
		sb.append("    FROM\n");
		sb.append("        tbl A\n");
		sb.append("    WHERE\n");
		sb.append("        (\n");
		sb.append("            (\n");
		sb.append("                val = 1\n");
		sb.append("            )\n");
		sb.append("            OR\n");
		sb.append("            (\n");
		sb.append("                val = 2\n");
		sb.append("            )\n");
		sb.append("        )\n");
		check(sb);		
	}
	public void testSelectFormat2() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        COUNT(*)\n");
		sb.append("    FROM\n");
		sb.append("        tbl A\n");
		sb.append("    WHERE\n");
		sb.append("        (\n");
		sb.append("            (\n");
		sb.append("                val IN (\n");
		sb.append("                    1\n");
		sb.append("                    ,2\n");
		sb.append("                )\n");
		sb.append("            )\n");
		sb.append("            OR\n");
		sb.append("            (\n");
		sb.append("                val = 2\n");
		sb.append("            )\n");
		sb.append("        )\n");

		check(sb);		
	}
	
	public void testSelectFormat3() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        COUNT(COL)\n");
		sb.append("    FROM\n");
		sb.append("        tbl A\n");
		sb.append("    WHERE\n");
		sb.append("        (\n");
		sb.append("            (\n");
		sb.append("                val = 1\n");
		sb.append("            )\n");
		sb.append("            OR\n");
		sb.append("            (\n");
		sb.append("                val = 2\n");
		sb.append("            )\n");
		sb.append("        )\n");
		check(sb);		
	}
	
	public void testSelectToCharFormat1() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        MAX(COL)\n");
		sb.append("    FROM\n");
		sb.append("        tbl A\n");
		sb.append("    WHERE\n");
		sb.append("        (\n");
		sb.append("            (\n");
		sb.append("                val = 1\n");
		sb.append("            )\n");
		sb.append("            OR\n");
		sb.append("            (\n");
		sb.append("                val = 2\n");
		sb.append("            )\n");
		sb.append("        )\n");
		check(sb);		
	}
	public void testSelectBetweenFormat1() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        A.*\n");
		sb.append("    FROM\n");
		sb.append("        tbl A\n");
		sb.append("    WHERE\n");
		sb.append("        (\n");
		sb.append("            a LIKE 2\n");
		sb.append("            OR val = 1\n");
		sb.append("        )\n");
		sb.append("        AND val = 2\n");
		sb.append("        AND val BETWEEN 1 AND 10\n");
		check(sb);		
	}
	public void testSelectBetweenFormat2() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        A.*\n");
		sb.append("    FROM\n");
		sb.append("        tbl A\n");
		sb.append("    WHERE\n");
		sb.append("        (\n");
		sb.append("            a LIKE 2\n");
		sb.append("            OR val = 1\n");
		sb.append("        )\n");
		sb.append("        AND val1 BETWEEN 1 AND 10\n");
		sb.append("        AND val2 BETWEEN 2 AND 20\n");
		sb.append("        AND val3 = 2\n");
		check(sb);		
	}
	
	
	public void testSelectBetweenFormat3() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        A.*\n");
		sb.append("    FROM\n");
		sb.append("        tbl A\n");
		sb.append("    WHERE\n");
		sb.append("        (\n");
		sb.append("            a LIKE 2\n");
		sb.append("            OR val = BETWEEN 1 AND 10\n");
		sb.append("        )\n");
		sb.append("        AND val BETWEEN 1 AND 10\n");
		sb.append("        AND val = 2\n");
		check(sb);		
	}
	
	
	public void testSelectCaseFormat1(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        ROWNUM SEQ\n");
		sb.append("        ,(\n");
		sb.append("            CASE\n");
		sb.append("                TBL1.COL1\n");
		sb.append("                WHEN 4 THEN 'Ç`'\n");
		sb.append("                WHEN 9 THEN 'Ça'\n");
		sb.append("                WHEN 2 THEN 'Çb'\n");
		sb.append("            END\n");
		sb.append("        ) COL1\n");
		sb.append("        ,TBL1.COL2\n");
		sb.append("    FROM\n");
		sb.append("        MYTABLE TBL1\n");
		sb.append("        ,(\n");
		sb.append("            SELECT\n");
		sb.append("                    COL\n");
		sb.append("                    ,COL2\n");
		sb.append("                    ,SUM(\n");
		sb.append("                        GENKA * SURYO\n");
		sb.append("                    ) GENKA_SUM\n");
		sb.append("                    ,SUM(\n");
		sb.append("                        BAIKA * SURYO\n");
		sb.append("                    ) BAIKA_SUM\n");
		sb.append("                FROM\n");
		sb.append("                    TEST\n");
		sb.append("                GROUP BY\n");
		sb.append("                    COL1\n");
		sb.append("                    ,COL2\n");
		sb.append("        ) DEN2\n");
		sb.append("        ,TBL2 TBL2\n");
		sb.append("    WHERE\n");
		sb.append("        TBL.COL1 = TBL2.COL1\n");
		sb.append("        AND TBL.COL2 = TBL2.COL2\n");
		sb.append("        AND TBL.HOGE = 1\n");
		check(sb);

	}
	
	public void testSelectCaseFormat2(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        EMP.TANAERRFLG\n");
		sb.append("        ,CASE\n");
		sb.append("            WHEN EMP.TNKHENFLG = 1 THEN 'Åõ'\n");
		sb.append("            WHEN EMP.TNKHENFLG = 2 THEN 'Åõ'\n");
		sb.append("            ELSE ''\n");
		sb.append("        END AS HOGE\n");
		sb.append("    FROM\n");
		sb.append("        EMP\n");

		check(sb);

	}
	
	public void testInsertFormat1(){
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT\n");
		sb.append("    INTO\n");
		sb.append("        SCOTT.EMP22(\n");
		sb.append("            EMPNO\n");
		sb.append("            ,ENAME\n");
		sb.append("            ,JOB\n");
		sb.append("            ,MGR\n");
		sb.append("            ,HIREDATE\n");
		sb.append("            ,SAL2\n");
		sb.append("            ,COMM\n");
		sb.append("            ,DEPTNO\n");
		sb.append("            ,TIMESTAMP\n");
		sb.append("        )\n");
		sb.append("    VALUES(\n");
		sb.append("        4\n");
		sb.append("        ,'BL	tAKE'\n");
		sb.append("        ,'MANAGER'\n");
		sb.append("        ,7839\n");
		sb.append("        ,to_date(\n");
		sb.append("            '1981-05-01 00:00:00'\n");
		sb.append("            ,'YYYY-MM-DD HH24:MI:SS'\n");
		sb.append("        )\n");
		sb.append("        ,2850.0\n");
		sb.append("        ,0.0\n");
		sb.append("        ,30\n");
		sb.append("        ,NULL\n");
		sb.append("    )\n");
		check(sb);

	}
	public void testUpdateFormat1() {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE\n");
		sb.append("        AAA\n");
		sb.append("    SET\n");
		sb.append("        COL1 = '1'\n");
		sb.append("    WHERE\n");
		sb.append("        COL1 = (\n");
		sb.append("            SELECT\n");
		sb.append("                    MAX(EMAIL)\n");
		sb.append("                FROM\n");
		sb.append("                    BBS_MAIN\n");
		sb.append("                WHERE\n");
		sb.append("                    EMAIL = 'test1@yahoo.co.jp'\n");
		sb.append("        )\n");

		check(sb);		
	}
		
	public void testCreateTable(){
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE\n");
		sb.append("    TABLE\n");
		sb.append("        Company(\n");
		sb.append("            Company_Code CHAR(003) NOT NULL\n");
		sb.append("            ,Company_Mix VARCHAR(100) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Company_Hlf VARCHAR(100) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Company_Eng VARCHAR(100) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Company_Abbr VARCHAR(010) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Cutoff_Type CHAR(001) NOT NULL WITH DEFAULT\n");
		sb.append("            ,POS_Code_Initial VARCHAR(007) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Regist_Date TIMESTAMP NOT NULL WITH DEFAULT\n");
		sb.append("            ,Regist_User_Id VARCHAR(010) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Regist_User_Name VARCHAR(050) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Regist_Program VARCHAR(030) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Update_Date TIMESTAMP NOT NULL WITH DEFAULT\n");
		sb.append("            ,Update_User_Id VARCHAR(010) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Update_User_Name VARCHAR(050) NOT NULL WITH DEFAULT\n");
		sb.append("            ,Update_Program VARCHAR(030) NOT NULL WITH DEFAULT\n");
		sb.append("        )\n");
		check(sb);		

	}
	public void testUpdateFormat2() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT\n");
		sb.append("        t.HOGE AS HOGESTR\n");
		sb.append("    FROM\n");
		sb.append("        HOGEHOGE t\n");
		sb.append("    WHERE\n");
		sb.append("        :PRAM3 = :PRAM1\n");
		sb.append("        AND t.TENCD = :PRAM2\n");
		sb.append("        AND t.FLG = 0\n");
		sb.append("    ORDER BY\n");
		sb.append("        t.HOGE\n");
		sb.append("        ,:PRAM3");

		check(sb);
	}
}
