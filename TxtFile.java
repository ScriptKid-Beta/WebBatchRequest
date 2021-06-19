package com.webRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TxtFile {
    public static List<String> readTxtFile(String filePath) {
        List<String> urList=new ArrayList<String>();
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            String filename = file.getPath();
            if (file.isFile() && file.exists() && filename.endsWith(".txt")) { //判断文件是否存在，是否为TXT文件
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    System.out.println(lineTxt);
                    urList.add(lineTxt);
                }
                read.close();
            } else {
                System.out.println("找不到指定的TXT文件");
                return null;
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return null;
        }
        return urList;
    }
}