package com.webRequest;

import java.util.ArrayList;
import java.util.Iterator;
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
    //添加一个URL去重方法
    public static ArrayList<String> getSingle(ArrayList<String> list) {
        ArrayList<String> tempList = new ArrayList<String>();          //1,创建新集合
        Iterator<String> it = list.iterator();              //2,根据传入的集合(老集合)获取迭代器
        while(it.hasNext()) {                  //3,遍历老集合
            String obj = it.next();                //记录住每一个元素
            if(!tempList.contains(obj)) {            //如果新集合中不包含老集合中的元素
                tempList.add(obj);                //将该元素添加
            }
        }
        return tempList;
}
