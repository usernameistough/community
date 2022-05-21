package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.AccessTokenDTO;
import com.example.mapper.UserMapper;
import com.example.model.Account;
import com.example.provider.GiteeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Value("${value.client_id}")
    private String client_id;
    @Value("${value.client_secret}")
    private String client_secret;
    @Value("${value.redirect_uri}")
    private String redirect_uri;
    @Autowired
    GiteeProvider provider;
    @Autowired
    UserMapper userMapper;

    //接收 Github 调用 redirect_uri 返回回来的参数，然后以这些参数来获取 token
    @GetMapping("/callback")
    public String callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //把传入的 code 封装到对象中
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO(client_id, client_secret, code, redirect_uri, state);
        System.out.println(code);
        String access_token = provider.getAccessToken(accessTokenDTO);
        System.out.println("access_token = " + access_token);
        String userInfo = provider.getUserInfo(access_token);
        if (userInfo != null) {
            Account user = new Account();
            //将 JSON 字符串转为对象，之后调用对象中的属性来获得其中的值
            JSONObject jsonObject = JSON.parseObject(userInfo);
            System.out.println("jsonObject = " + jsonObject);
            //不知道什么原因无法获取到 id
            String bio = (String) jsonObject.get("bio");
            System.out.println("bio = " + bio);
            String name = (String) jsonObject.get("name");
            System.out.println("name = " + name);
            user.setBio(bio);
            user.setName(name);
            String token = UUID.randomUUID().toString();
            System.out.println("token = " + token);
            user.setToken(token);
            long l = System.currentTimeMillis();
            System.out.println("l = " + l);
            user.setGmtCreate(l);
            Long gmtCreate = user.getGmtCreate();
            System.out.println("gmtCreate = " + gmtCreate);
            user.setGmtModified(gmtCreate);
            //將獲得的 User 信息持久化存儲到數據庫中
            //测试能否执行数据库的操作！
            userMapper.insert(user);
            //將驗證用戶生成的隨機 token 存入到數據庫中
//            request.getSession().setAttribute("user", user);
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            //登录失败重新登陆
            return "redirect:/";
        }
    }
}
