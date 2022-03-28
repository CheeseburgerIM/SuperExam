package com.example.sexam.utils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.sexam.utils.Token.tokenMap;

public class MyFunction {

    public static String findKeyByValue(String v, Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (value.equals(v)) return key;
        }
        return null;
    }

    public static MyJson isLoggedIn(HttpServletRequest request) {
        MyJson myJson = new MyJson();
        // 通过请求头判断用户登录状态
        String token = request.getHeader("token");
        if (!tokenMap.containsKey(token)) {
            myJson.setStatus(403);
            myJson.setResult("fail");
            myJson.setMessage("Please login first");
        }
        // test
        myJson.setStatus(200);
        return myJson;
    }

    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        return date;
    }

}
