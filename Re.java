package com.webRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Re {
    public static String Title (String str){
        String is_title = "<title>(.*)</title>";
        Matcher matcher = Pattern.compile(is_title).matcher(str);
        if (matcher.find()){
            return matcher.group(1);
        }else{
            return ("未获取到title");
        }
    }
}
