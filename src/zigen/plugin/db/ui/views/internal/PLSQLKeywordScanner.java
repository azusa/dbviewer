/*
 * 著作権: Copyright (c) 2007−2008 ZIGEN
 * ライセンス：Eclipse Public License - v 1.0 
 * 原文：http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.views.internal;


/**
 * SQLKeywordScannerクラス.
 * 
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [001] 2005/04/07 ZIGEN create.
 *        [002] 2005/05/29 ZIGEN SQLキーワードのシンタックスハイライトの修正.
 * 
 */
public class PLSQLKeywordScanner extends SQLKeywordScanner {

	public PLSQLKeywordScanner(ColorManager colorManager) {
		super(colorManager);
	}

	// PLSQL用にオーバライド
	protected String[] getSQL_Keyword() {
		return PLSQLKeywords;
	}

	// 色分け表示単語
	public static final String[] PLSQLKeywords = {
			/* PL/SQL予約語 */"ALL", //$NON-NLS-1$
			"ALTER", //$NON-NLS-1$
			"AND", //$NON-NLS-1$
			"ANY", //$NON-NLS-1$
			"ARRAY", //$NON-NLS-1$
			"ARROW", //$NON-NLS-1$
			"AS", //$NON-NLS-1$
			"ASC", //$NON-NLS-1$
			"AT", //$NON-NLS-1$
			"BEGIN", //$NON-NLS-1$
			"BETWEEN", //$NON-NLS-1$
			"BY", //$NON-NLS-1$
			"CASE", //$NON-NLS-1$
			"CHECK", //$NON-NLS-1$
			"CLUSTERS", //$NON-NLS-1$
			"CLUSTER", //$NON-NLS-1$
			"COLAUTH", //$NON-NLS-1$
			"COLUMNS", //$NON-NLS-1$
			"COMPRESS", //$NON-NLS-1$
			"CONNECT", //$NON-NLS-1$
			"CRASH", //$NON-NLS-1$
			"CREATE", //$NON-NLS-1$
			"CURRENT", //$NON-NLS-1$
			"DECIMAL", //$NON-NLS-1$
			"DECLARE", //$NON-NLS-1$
			"DEFAULT", //$NON-NLS-1$
			"DELETE", //$NON-NLS-1$
			"DESC", //$NON-NLS-1$
			"DISTINCT", //$NON-NLS-1$
			"DROP", //$NON-NLS-1$
			"ELSE", //$NON-NLS-1$
			"END", //$NON-NLS-1$
			"EXCEPTION", //$NON-NLS-1$
			"EXCLUSIVE", //$NON-NLS-1$
			"EXISTS", //$NON-NLS-1$
			"FETCH", //$NON-NLS-1$
			"FORM", //$NON-NLS-1$
			"FOR", //$NON-NLS-1$
			"FROM", //$NON-NLS-1$
			"GOTO", //$NON-NLS-1$
			"GRANT", //$NON-NLS-1$
			"GROUP", //$NON-NLS-1$
			"HAVING", //$NON-NLS-1$
			"IDENTIFIED", //$NON-NLS-1$
			"IF", //$NON-NLS-1$
			"IN", //$NON-NLS-1$
			"INDEXES", //$NON-NLS-1$
			"INDEX", //$NON-NLS-1$
			"INSERT", //$NON-NLS-1$
			"INTERSECT", //$NON-NLS-1$
			"INTO", //$NON-NLS-1$
			"IS", //$NON-NLS-1$
			"LIKE", //$NON-NLS-1$
			"LOCK", //$NON-NLS-1$
			"MINUS", //$NON-NLS-1$
			"MODE", //$NON-NLS-1$
			"NOCOMPRESS", //$NON-NLS-1$
			"NOT", //$NON-NLS-1$
			"NOWAIT", //$NON-NLS-1$
			"NULL", //$NON-NLS-1$
			"OF", //$NON-NLS-1$
			"ON", //$NON-NLS-1$
			"OPTION", //$NON-NLS-1$
			"OR", //$NON-NLS-1$
			"ORDER", //$NON-NLS-1$
			"OVERLAPS", //$NON-NLS-1$
			"PRIOR", //$NON-NLS-1$
			"PROCEDURE", //$NON-NLS-1$
			"PUBLIC", //$NON-NLS-1$
			"RANGE", //$NON-NLS-1$
			"RECORD", //$NON-NLS-1$
			"RESOURCE", //$NON-NLS-1$
			"REVOKE", //$NON-NLS-1$
			"SELECT", //$NON-NLS-1$
			"SHARE", //$NON-NLS-1$
			"SIZE", //$NON-NLS-1$
			"SQL", //$NON-NLS-1$
			"START", //$NON-NLS-1$
			"SUBTYPE", //$NON-NLS-1$
			"TABAUTH", //$NON-NLS-1$
			"TABLE", //$NON-NLS-1$
			"THEN", //$NON-NLS-1$
			"TO", //$NON-NLS-1$
			"TYPE", //$NON-NLS-1$
			"UNION", //$NON-NLS-1$
			"UNIQUE", //$NON-NLS-1$
			"UPDATE", //$NON-NLS-1$
			"USE", //$NON-NLS-1$
			"VALUES", //$NON-NLS-1$
			"VIEW", //$NON-NLS-1$
			"VIEWS", //$NON-NLS-1$
			"WHEN", //$NON-NLS-1$
			"WHERE", //$NON-NLS-1$
			"WITH", //$NON-NLS-1$
			/* PL/SQLキーワード */"A", //$NON-NLS-1$
			"ADD", //$NON-NLS-1$
			"AGENT", //$NON-NLS-1$
			"AGGREGATE", //$NON-NLS-1$
			"ARRAY", //$NON-NLS-1$
			"ATTRIBUTE", //$NON-NLS-1$
			"AUTHID", //$NON-NLS-1$
			"AVG", //$NON-NLS-1$
			"BFILE_BASE", //$NON-NLS-1$
			"BINARY", //$NON-NLS-1$
			"BLOB_BASE", //$NON-NLS-1$
			"BLOCK", //$NON-NLS-1$
			"BODY", //$NON-NLS-1$
			"BOTH", //$NON-NLS-1$
			"BOUND", //$NON-NLS-1$
			"BULK", //$NON-NLS-1$
			"BYTE", //$NON-NLS-1$
			"C", //$NON-NLS-1$
			"CALL", //$NON-NLS-1$
			"CALLING", //$NON-NLS-1$
			"CASCADE", //$NON-NLS-1$
			"CHAR", //$NON-NLS-1$
			"CHAR_BASE", //$NON-NLS-1$
			"CHARACTER", //$NON-NLS-1$
			"CHARSETFORM", //$NON-NLS-1$
			"CHARSETID", //$NON-NLS-1$
			"CHARSET", //$NON-NLS-1$
			"CLOB_BASE", //$NON-NLS-1$
			"CLOSE", //$NON-NLS-1$
			"COLLECT", //$NON-NLS-1$
			"COMMENT", //$NON-NLS-1$
			"COMMIT", //$NON-NLS-1$
			"COMMITTED", //$NON-NLS-1$
			"COMPILED", //$NON-NLS-1$
			"CONSTANT", //$NON-NLS-1$
			"CONSTRUCTOR", //$NON-NLS-1$
			"CONTEXT", //$NON-NLS-1$
			"CONVERT", //$NON-NLS-1$
			"COUNT", //$NON-NLS-1$
			"CURSOR", //$NON-NLS-1$
			"CUSTOMDATUM", //$NON-NLS-1$
			"DANGLING", //$NON-NLS-1$
			"DATA", //$NON-NLS-1$
			"DATE", //$NON-NLS-1$
			"DATE_BASE", //$NON-NLS-1$
			"DAY", //$NON-NLS-1$
			"DEFINE", //$NON-NLS-1$
			"DETERMINISTIC", //$NON-NLS-1$
			"DOUBLE", //$NON-NLS-1$
			"DURATION", //$NON-NLS-1$
			"ELEMENT", //$NON-NLS-1$
			"ELSIF", //$NON-NLS-1$
			"EMPTY", //$NON-NLS-1$
			"ESCAPE", //$NON-NLS-1$
			"EXCEPT", //$NON-NLS-1$
			"EXCEPTIONS", //$NON-NLS-1$
			"EXECUTE", //$NON-NLS-1$
			"EXIT", //$NON-NLS-1$
			"EXTERNAL", //$NON-NLS-1$
			"FINAL", //$NON-NLS-1$
			"FIXED", //$NON-NLS-1$
			"FLOAT", //$NON-NLS-1$
			"FORALL", //$NON-NLS-1$
			"FORCE", //$NON-NLS-1$
			"FUNCTION", //$NON-NLS-1$
			"GENERAL", //$NON-NLS-1$
			"HASH", //$NON-NLS-1$
			"HEAP", //$NON-NLS-1$
			"HIDDEN", //$NON-NLS-1$
			"HOUR", //$NON-NLS-1$
			"IMMEDIATE", //$NON-NLS-1$
			"INCLUDING", //$NON-NLS-1$
			"INDICATOR", //$NON-NLS-1$
			"INDICES", //$NON-NLS-1$
			"INFINITE", //$NON-NLS-1$
			"INSTANTIABLE", //$NON-NLS-1$
			"INT", //$NON-NLS-1$
			"INTERFACE", //$NON-NLS-1$
			"INTERVAL", //$NON-NLS-1$
			"INVALIDATE", //$NON-NLS-1$
			"ISOLATION", //$NON-NLS-1$
			"JAVA", //$NON-NLS-1$
			"LANGUAGE", //$NON-NLS-1$
			"LARGE", //$NON-NLS-1$
			"LEADING", //$NON-NLS-1$
			"LENGTH", //$NON-NLS-1$
			"LEVEL", //$NON-NLS-1$
			"LIBRARY", //$NON-NLS-1$
			"LIKE2", //$NON-NLS-1$
			"LIKE4", //$NON-NLS-1$
			"LIKEC", //$NON-NLS-1$
			"LIMIT", //$NON-NLS-1$
			"LIMITED", //$NON-NLS-1$
			"LOCAL", //$NON-NLS-1$
			"LONG", //$NON-NLS-1$
			"LOOP", //$NON-NLS-1$
			"MAP", //$NON-NLS-1$
			"MAX", //$NON-NLS-1$
			"MAXLEN", //$NON-NLS-1$
			"MEMBER", //$NON-NLS-1$
			"MERGE", //$NON-NLS-1$
			"MIN", //$NON-NLS-1$
			"MINUTE", //$NON-NLS-1$
			"MOD", //$NON-NLS-1$
			"MODIFY", //$NON-NLS-1$
			"MONTH", //$NON-NLS-1$
			"MULTISET", //$NON-NLS-1$
			"NAME", //$NON-NLS-1$
			"NAN", //$NON-NLS-1$
			"NATIONAL", //$NON-NLS-1$
			"NATIVE", //$NON-NLS-1$
			"NCHAR", //$NON-NLS-1$
			"NEW", //$NON-NLS-1$
			"NOCOPY", //$NON-NLS-1$
			"NUMBER_BASE", //$NON-NLS-1$
			"OBJECT", //$NON-NLS-1$
			"OCICOLL", //$NON-NLS-1$
			"OCIDATETIME", //$NON-NLS-1$
			"OCIDATE", //$NON-NLS-1$
			"OCIDURATION", //$NON-NLS-1$
			"OCIINTERVAL", //$NON-NLS-1$
			"OCILOBLOCATOR", //$NON-NLS-1$
			"OCINUMBER", //$NON-NLS-1$
			"OCIRAW", //$NON-NLS-1$
			"OCIREFCURSOR", //$NON-NLS-1$
			"OCIREF", //$NON-NLS-1$
			"OCIROWID", //$NON-NLS-1$
			"OCISTRING", //$NON-NLS-1$
			"OCITYPE", //$NON-NLS-1$
			"ONLY", //$NON-NLS-1$
			"OPAQUE", //$NON-NLS-1$
			"OPEN", //$NON-NLS-1$
			"OPERATOR", //$NON-NLS-1$
			"ORACLE", //$NON-NLS-1$
			"ORADATA", //$NON-NLS-1$
			"ORGANIZATION", //$NON-NLS-1$
			"ORLANY", //$NON-NLS-1$
			"ORLVARY", //$NON-NLS-1$
			"OTHERS", //$NON-NLS-1$
			"OUT", //$NON-NLS-1$
			"OVERRIDING", //$NON-NLS-1$
			"PACKAGE", //$NON-NLS-1$
			"PARALLEL_ENABLE", //$NON-NLS-1$
			"PARAMETER", //$NON-NLS-1$
			"PARAMETERS", //$NON-NLS-1$
			"PARTITION", //$NON-NLS-1$
			"PASCAL", //$NON-NLS-1$
			"PIPE", //$NON-NLS-1$
			"PIPELINED", //$NON-NLS-1$
			"PRAGMA", //$NON-NLS-1$
			"PRECISION", //$NON-NLS-1$
			"PRIVATE", //$NON-NLS-1$
			"RAISE", //$NON-NLS-1$
			"RANGE", //$NON-NLS-1$
			"RAW", //$NON-NLS-1$
			"READ", //$NON-NLS-1$
			"RECORD", //$NON-NLS-1$
			"REF", //$NON-NLS-1$
			"REFERENCE", //$NON-NLS-1$
			"REM", //$NON-NLS-1$
			"REMAINDER", //$NON-NLS-1$
			"RENAME", //$NON-NLS-1$
			"RESULT", //$NON-NLS-1$
			"RETURN", //$NON-NLS-1$
			"RETURNING", //$NON-NLS-1$
			"REVERSE", //$NON-NLS-1$
			"ROLLBACK", //$NON-NLS-1$
			"ROW", //$NON-NLS-1$
			"SAMPLE", //$NON-NLS-1$
			"SAVE", //$NON-NLS-1$
			"SAVEPOINT", //$NON-NLS-1$
			"SB1", //$NON-NLS-1$
			"SB2", //$NON-NLS-1$
			"SB4", //$NON-NLS-1$
			"SECOND", //$NON-NLS-1$
			"SEGMENT", //$NON-NLS-1$
			"SELF", //$NON-NLS-1$
			"SEPARATE", //$NON-NLS-1$
			"SEQUENCE", //$NON-NLS-1$
			"SERIALIZABLE", //$NON-NLS-1$
			"SET", //$NON-NLS-1$
			"SHORT", //$NON-NLS-1$
			"SIZE_T", //$NON-NLS-1$
			"SOME", //$NON-NLS-1$
			"SPARSE", //$NON-NLS-1$
			"SQLCODE", //$NON-NLS-1$
			"SQLDATA", //$NON-NLS-1$
			"SQLNAME", //$NON-NLS-1$
			"SQLSTATE", //$NON-NLS-1$
			"STANDARD", //$NON-NLS-1$
			"STATIC", //$NON-NLS-1$
			"STDDEV", //$NON-NLS-1$
			"STORED", //$NON-NLS-1$
			"STRING", //$NON-NLS-1$
			"STRUCT", //$NON-NLS-1$
			"STYLE", //$NON-NLS-1$
			"SUBMULTISET", //$NON-NLS-1$
			"SUBPARTITION", //$NON-NLS-1$
			"SUBSTITUTABLE", //$NON-NLS-1$
			"SUBTYPE", //$NON-NLS-1$
			"SUM", //$NON-NLS-1$
			"SYNONYM", //$NON-NLS-1$
			"TDO", //$NON-NLS-1$
			"THE", //$NON-NLS-1$
			"TIME", //$NON-NLS-1$
			"TIMESTAMP", //$NON-NLS-1$
			"TIMEZONE_ABBR", //$NON-NLS-1$
			"TIMEZONE_HOUR", //$NON-NLS-1$
			"TIMEZONE_MINUTE", //$NON-NLS-1$
			"TIMEZONE_REGION", //$NON-NLS-1$
			"TRAILING", //$NON-NLS-1$
			"TRANSAC", //$NON-NLS-1$
			"TRANSACTIONAL", //$NON-NLS-1$
			"TRUSTED", //$NON-NLS-1$
			"TYPE", //$NON-NLS-1$
			"UB1", //$NON-NLS-1$
			"UB2", //$NON-NLS-1$
			"UB4", //$NON-NLS-1$
			"UNDER", //$NON-NLS-1$
			"UNSIGNED", //$NON-NLS-1$
			"UNTRUSTED", //$NON-NLS-1$
			"USE", //$NON-NLS-1$
			"USING", //$NON-NLS-1$
			"VALIST", //$NON-NLS-1$
			"VALUE", //$NON-NLS-1$
			"VARIABLE", //$NON-NLS-1$
			"VARIANCE", //$NON-NLS-1$
			"VARRAY", //$NON-NLS-1$
			"VARYING", //$NON-NLS-1$
			"VOID", //$NON-NLS-1$
			"WHILE", //$NON-NLS-1$
			"WORK", //$NON-NLS-1$
			"WRAPPED", //$NON-NLS-1$
			"WRITE", //$NON-NLS-1$
			"YEAR", //$NON-NLS-1$
			"ZONE" //$NON-NLS-1$

	};

}
