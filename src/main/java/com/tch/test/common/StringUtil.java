package com.tch.test.common;

public class StringUtil {

	/**
	 * 中文转unicode
	 * @param chineseStr
	 * @return
	 */
	public static String chineseToUnicode(String chineseStr){  
        String result="";  
        for (int i = 0; i < chineseStr.length(); i++){  
            int chr1 = (char) chineseStr.charAt(i);  
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)  
                result+="\\u" + Integer.toHexString(chr1);  
            }else{  
                result+=chineseStr.charAt(i);  
            }  
        }  
        return result;  
    }
	
}
