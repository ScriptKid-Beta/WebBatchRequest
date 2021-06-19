package com.webRequest;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Http {

    public static ArrayList Response (String str,String cookie, String ua,String xHeaders,String methods,String dataBody,String enctypeBody,boolean follow){
        ArrayList responseList = new ArrayList();
        int code,contentLength;
        String body,banner;
        HttpRequest response = null;
        Map<String, String> headers = new HashMap<String, String>();
            if (ua.equals("")){
                ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                        " (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";
            }
            if (!"SESSION=A202106084F2...".equals(cookie)){
                headers.put("cookie",cookie);
            }



            headers.put("User-Agent",ua);
                if (!xHeaders.equals("X-Forwarded-for: 127.0.0.1\nReferer: ...") && !xHeaders.equals("")) {
                    System.out.println(xHeaders);
                    try {
                        String[] split1 = xHeaders.split("\n");
                        for (String s : split1) {
                            String[] split2 = s.split(": ");
                            headers.put(split2[0], split2[1]);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            if (methods == "post")
            {   headers.put("Content-type",enctypeBody);
                response = HttpRequest.post(str).headers(headers).trustAllHosts().trustAllCerts().send(dataBody).followRedirects(follow);
            }
            if (methods == "head"){
                response = HttpRequest.head(str).headers(headers).trustAllCerts().trustAllHosts().followRedirects(follow);
            }
            if(methods == "get"){
                response = HttpRequest.get(str).headers(headers).trustAllHosts().trustAllCerts().followRedirects(follow);
            }
        code = response.code();
        body = response.body();
        banner = response.header("server");
        contentLength = response.contentLength();
        if (contentLength == -1) {
            try {
                contentLength = body.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        responseList.add(code);
        responseList.add(body);
        responseList.add(banner);
        responseList.add(contentLength);
        return responseList;
    }

    // 重载方法
    public static ArrayList Response (String str,String cookie, String ua,String xHeaders,String methods,String dataBody,String enctypeBody,boolean follow,String host,int port){
        ArrayList responseList = new ArrayList();
        int code,contentLength;
        String body,banner;
        Map<String, String> headers = new HashMap<String, String>();
        HttpRequest response = null;
        if (ua.equals("")){
            ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                    " (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";
        }
//        System.out.println(!"SESSION=A202106084F2...".equals(cookie));
        if (!"SESSION=A202106084F2...".equals(cookie)){
            headers.put("cookie",cookie);
        }

        headers.put("User-Agent",ua);
        if (!xHeaders.equals("X-Forwarded-for: 127.0.0.1\nReferer: ...") && !xHeaders.equals("")){
            System.out.println(xHeaders);
            try {
                String[] split1 = xHeaders.split("\n");
                for (String s : split1) {
                    String[] split2 = s.split(": ");
                    headers.put(split2[0], split2[1]);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (methods == "post")
        {   headers.put("Content-type",enctypeBody);
            response = HttpRequest.post(str).useProxy(host, port).headers(headers).trustAllHosts().trustAllCerts().send(dataBody).followRedirects(follow);
        }
        if (methods == "head"){
            response = HttpRequest.head(str).useProxy(host, port).headers(headers).trustAllCerts().trustAllHosts().followRedirects(follow);
        }
        if(methods == "get"){
            response = HttpRequest.get(str).useProxy(host, port).headers(headers).trustAllHosts().trustAllCerts().followRedirects(follow);
        }
        code = response.code();
        body = response.body();
        banner = response.header("server");
        contentLength = response.contentLength();
        if (contentLength == -1) {
            try {
                contentLength = body.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        responseList.add(code);
        responseList.add(body);
        responseList.add(banner);
        responseList.add(contentLength);
        return responseList;
    }
}
