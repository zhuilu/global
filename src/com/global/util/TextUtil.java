package com.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本操作工具
 * @author wb-wuxiaofan
 *
 */
public class TextUtil {
    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
	public static String toDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}
	
	
	/** 
     * 半角转换为全角 
     *  
     * @param input 
     * @return 
     */  
    public static String ToSBC(String input) {  
        char[] c = input.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] == 12288) {// 全角空格为12288，半角空格为32  
                c[i] = (char) 32;  
                continue;  
            }  
            if (c[i] > 65280 && c[i] < 65375)// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248  
                c[i] = (char) (c[i] - 65248);  
        }  
        return new String(c);  
    }
    
    /** 
     * 去除特殊字符或将所有中文标号替换为英文标号 
     *  
     * @param str 
     * @return 
     */  
    public static String stringFilter(String str) {  
        str = str.replaceAll("【", "[").replaceAll("】", "]")  
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号  
        String regEx = "[『』]"; // 清除掉特殊字符  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
        return m.replaceAll("").trim();  
    } 
}
