package com.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
//設置接受到的用戶的信息
public class User {
    private Integer id;
    private String bio;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
}
