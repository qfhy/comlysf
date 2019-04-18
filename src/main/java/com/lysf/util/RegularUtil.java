package com.lysf.util;

import java.util.regex.Pattern;

/*
* 我们用0表示匹配正确，用1表示匹配失败
 */
public class RegularUtil {
    public static int  isTrue (String str,String regular){
        boolean isTrue = Pattern.matches(regular,str);
        if (isTrue==true){
            return 0;
        }
        return 1;
    }
}
