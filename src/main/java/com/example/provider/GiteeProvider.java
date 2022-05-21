package com.example.provider;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.AccessTokenDTO;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.http.HttpClient;

@Controller
public class GiteeProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        //shit redirect_uri
        String result1 = HttpUtil.post("https://gitee.com/oauth/token?grant_type=authorization_code&redirect_uri=http://localhost:9999/callback", JSON.toJSONString(accessTokenDTO));
        System.out.println("result1 = " + result1);
        //
        JSONObject jsonObject = JSONObject.parseObject(result1);
        String access_token = (String) jsonObject.get("access_token");
        System.out.println("access_token = " + access_token);
        return access_token;
    }

    //得到用户的令牌之后就去访问用户的信息
    public String getUserInfo(String access_token) {
        String result2 = HttpRequest.get("https://gitee.com/api/v5/user?access_token=" + access_token)
                .header("Authorization", "token " + access_token).execute().body();
        //这里使用fastjson将json字符串转换成一个对象进行返回
//        System.out.println(result2);
        return result2;
    }
}
