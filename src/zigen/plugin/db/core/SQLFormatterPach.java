/*
 * ���쌠: Copyright (c) 2007�|2008 ZIGEN
 * ���C�Z���X�FEclipse Public License - v 1.0 
 * �����Fhttp://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

public class SQLFormatterPach {

	public static final String LINE_SEP = "\n"; // BlancoFormattert�ɂ��킹�āA���s�R�[�h��\n�ň���

	/**
	 * SQL���`
	 * 
	 * @param sql
	 * @return
	 */
	public static String format(String sql) {
		sql = formatOpenParen(sql);
		sql = formatCase(sql);
		// 2007/11/22 �o�C���h�ϐ���:�̌�ɋ󔒂�����Ȃ��悤�Ƀp�b�`��K��
		sql = formatCoron(sql);
		return sql;
	}

	private static final int SCOPE_NONE = 0;

	private static final int SCOPE_BETWEEN = 100;

	// "("�̑O�̋󔒂���菜��
	private static String formatOpenParen(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer t3 = new StringTokenizer(sql, " ");
		String token3 = null;
		int indent = 0;
		int preIndent = 0; // 1�O�̃C���f���g
		String preWord = "";

		int scope = SCOPE_NONE;
		boolean setBetweenAnd = false;

		while ((token3 = t3.nextToken()) != null) {
			if (token3.trim().length() == 0) {
				indent++;
			} else {
				if (!sb.toString().endsWith(LINE_SEP) && token3.startsWith("(")) {

					if ("=".equalsIgnoreCase(preWord) || "IN".equalsIgnoreCase(preWord) || "THEN".equalsIgnoreCase(preWord)) {
						sb.append(" ");
						sb.append(token3);

					} else if ("AND".equalsIgnoreCase(token3) || "OR".equalsIgnoreCase(preWord)) {
						sb.append(LINE_SEP);
						sb.append(" ");
						sb.append(StringUtil.indent(token3, preIndent));

					} else {
						// ���s�ŏI����Ă��Ȃ� AND "(" �̏ꍇ
						sb.append(token3);
					}

				} else {
					if (sb.length() == 0) {
						sb.append(StringUtil.indent(token3, indent));

					} else if ("BETWEEN".equalsIgnoreCase(token3)) {
						scope = SCOPE_BETWEEN;
						sb.append(" ");
						sb.append(StringUtil.indent(token3, indent));
						setBetweenAnd = false;

					} else if ("AND".equalsIgnoreCase(token3)) {

						if (scope == SCOPE_BETWEEN) {
							if (!setBetweenAnd) {
								sb = new StringBuffer(sb.toString().trim()); // ���̉��s���Ȃ���
								sb.append(" ");
								sb.append(token3);
								setBetweenAnd = true;
							} else {
								sb.append(" ");
								sb.append(StringUtil.indent(token3, indent));
							}

						} else {
							sb.append(" ");
							sb.append(StringUtil.indent(token3, indent));
						}
					} else {
						// �s���I�h�̌�͋󔒂����Ȃ�
						if (!sb.toString().endsWith(".")) {
							sb.append(" ");
						}
						sb.append(StringUtil.indent(token3, indent));

					}

					preIndent = indent;
					indent = 0;
				}

				preWord = token3;

			}
		}

		return sb.toString();
	}

	// CASE WHEN�Ή�
	private static String formatCase(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer t3 = new StringTokenizer(sql, " ");
		String token3 = null;
		int indent = 0;
		int preIndent = 0; // 1�O�̃C���f���g

		boolean isCase = false;
		while ((token3 = t3.nextToken()) != null) {
			if (token3.trim().length() == 0) {
				indent++;
			} else {
				String wk = token3.trim().toUpperCase(); // ���s�폜�A�啶���ϊ�
				if (wk.endsWith("CASE")) {
					isCase = true;
					sb.append(" ");
					sb.append(StringUtil.indent(token3, indent));

					preIndent = indent;
					indent = 0;

				} else if ("WHEN".equalsIgnoreCase(token3)) {
					if (isCase) {
						sb.append(" ");
						sb.append("    "); // �X�y�[�X�S��
						sb.append(StringUtil.indent(token3, preIndent));
					} else {
						sb.append(LINE_SEP);
						sb.append(" ");
						sb.append(StringUtil.indent(token3, preIndent));
					}
					isCase = false;

				} else if ("THEN".equalsIgnoreCase(token3)) {
					sb = new StringBuffer(sb.toString().trim()); // ���̉��s���Ȃ���
					sb.append(" ");
					sb.append(token3);

				} else {

					if (sb.length() == 0) {
						sb.append(token3);

					} else if (sb.toString().toUpperCase().endsWith("WHEN") || sb.toString().toUpperCase().endsWith("THEN")) {
						sb.append(" ");
						sb.append(token3);
					} else {
						sb.append(" ");
						sb.append(StringUtil.indent(token3, indent));
						isCase = false; // CASE�̌��[TABLE].[COLUMN]������ꍇ��z��
					}

					preIndent = indent;
					indent = 0;
				}

			}
		}

		return sb.toString();
	}

	
	// BIND�ϐ�(:)�Ή�
	private static String formatCoron(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer t3 = new StringTokenizer(sql, " ");
		String token3 = null;
		int indent = 0;
		int preIndent = 0; // 1�O�̃C���f���g

		boolean isCoron = false;
		while ((token3 = t3.nextToken()) != null) {
			if (token3.trim().length() == 0) {
				indent++;
			} else {
				//String wk = token3.trim().toUpperCase(); // ���s�폜�A�啶���ϊ�
				String wk = token3.trim(); // ���s�폜�A�啶���ϊ��͕s�v
				if (wk.endsWith(":")) {
					isCoron = true;
					sb.append(" ");
					sb.append(StringUtil.indent(token3, indent));

					preIndent = indent;
					indent = 0;

				} else {
					if (sb.length() == 0) {
						sb.append(token3);
					} else if(isCoron){
						sb.append(StringUtil.indent(token3, indent));
						isCoron = false;
					}else{
						sb.append(" ");
						sb.append(StringUtil.indent(token3, indent));
					}
					preIndent = indent;
					indent = 0;
				}

			}
		}

		return sb.toString();
	}
}
