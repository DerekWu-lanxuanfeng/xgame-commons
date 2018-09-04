package org.xgame.commons.utils.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: flame-game-aacommon
 * @File: com.flame.game.core.aacommon.string。StringUtil.java
 * @Description: String工具类
 * @Create: DerekWu 2015年4月21日 上午9:32:00
 * @version: V1.0
 */
public class StringUtil {
	/**
	 * 判断字符串是否为null或""
	 * 
	 * @param str
	 * @return
	 * @Added by
	 */
	public static boolean isEmpty(String str) {
		return null == str || "".equals(str);
	}

	/**
	 * 判断字符串是否不为null或""
	 * 
	 * @param str
	 * @return
	 * @Added by
	 */
	public static boolean isNotEmpty(String str) {
		return null != str && !"".equals(str);
	}

	/**
	 * 判断字符串是否包含子串quota
	 * 
	 * @param str
	 * @param quota
	 * @return
	 * @Added by
	 */
	public static boolean contains(String str, String quota) {
		return str.contains(quota);
	}

	/**
	 * 判断字符串是否为空白字符
	 * 
	 * @param str
	 * @return
	 * @Added by
	 */
	public static boolean isBlank(String str) {
		if (null == str)
			return true;
		return "".equals(str.trim());
	}

	/**
	 * 判断字符串是否不为空白字
	 * 
	 * @param str
	 * @return
	 * @Added by
	 */
	public static boolean isNotBlank(String str) {
		return str != null && !"".equals(str.trim());
	}

	/**
	 * 获取字符串的子串
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 * @Added by
	 */
	public static String substring(String str, int start, int end) {
		return str.substring(start, end);
	}

	/**
	 * 获取字符object在字符串str中的起始索引
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @Added by
	 */
	public static int indexOf(String str, char object) {
		return str.indexOf(object);
	}

	/**
	 * 获取字符串object在字符串str中的起始索引
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @Added by
	 */
	public static int indexOf(String str, String object) {
		return str.indexOf(object);
	}

	/**
	 * 根据对应字符串quota拆分字符串str
	 * 
	 * @param str
	 * @param quota
	 * @return
	 * @Added by
	 */
	public static String[] split(String str, String quota) {
		return str.split(quota);
	}

	/**
	 * 从字符串String中移�?第一�?匹配的object字符串并返回
	 * 
	 * @param str
	 * @param object
	 * @return
	 * @Added by
	 */
	public static String remove(String str, String object) {
		if (str.contains(object)) {
			int start = str.indexOf(object);
			return str.substring(0, start)
					+ str.substring(start + object.length());
		}
		return str;
	}

	/**
	 * 将第�?��字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperCaseFirst(String str) {
		return str.toUpperCase().substring(0, 1) + str.substring(1);
	}

	/**
	 * 将数字转化成字符串，如果超出length，将取最后length�?
	 * 
	 * @param num
	 * @param length
	 * @return
	 */
	public static String numToString(int num, int length) {
		String numStr = String.valueOf(num);
		if (numStr.length() == length) {
			return numStr;
		} else if (numStr.length() > length) {
			return numStr.substring(numStr.length() - length);
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < length - numStr.length(); i++) {
				sb.append(0);
			}
			return sb.append(numStr).toString();
		}
	}

	/**
	 * 获取字符串的长度，中文占2个字�?英文数字�?个字�?
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static int chineseLength(String value) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度�?，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取�?��字符
			String temp = value.substring(i, i + 1);
			// 判断是否为中文字�?
			if (temp.matches(chinese)) {
				// 中文字符长度�?
				valueLength += 2;
			} else {
				// 其他字符长度�?
				valueLength += 1;
			}
		}
		// 进位取整
		return valueLength;
	}
	
	/**
	 * 匹配  subStr 在 str 中存在的次數
	 * @param subStr
	 * @param str
	 * @return
	 */
	public static int matchStrNum(String subStr, String str) {
		Pattern p = Pattern.compile(Pattern.quote(subStr));
		Matcher m = p.matcher(str);
		int i = 0;
		while (m.find()) {
			i++;
		}
		return i;
	}

	public static void main(String[] args) {
		System.out.println(upperCaseFirst("activity"));
		System.out.println(numToString(31, 4));
		System.out.println(numToString(1399, 4));
		System.out.println(numToString(12285798, 4));
		System.out.println(chineseLength("sdf是打发斯蒂芬"));
		System.out.println(chineseLength("第三方水电费"));
		System.out.println(chineseLength("第三方"));
		System.out.println(chineseLength("yyyyy"));
	}

}
