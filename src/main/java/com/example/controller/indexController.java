package com.example.controller;


import com.example.mapper.UserMapper;
import com.example.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController {
    @Autowired
    UserMapper userMapper;
    //每次訪問這個頁面的時候都會進入到這裏查看數據庫中是否有這個用戶的 token 如果有説明他已經登錄了，就會查看這個用戶的信息，并把它傳回到前端頁面進行展示。
    @GetMapping("/")
    public String getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")) {
                String cookieValue = cookie.getValue();
                //將所有的 token 存入到 List 集合中
                Account user = userMapper.findUserByToken(cookieValue);
                //存在該用戶，就將他設置到 session 中供前端頁面展示數據，退出循環
                if(user != null) {
                    request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
        return "index";
    }
}
