package com.example.mapper;

import com.example.model.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into account (bio, name, token, gmtCreate, gmtModified)" +
            " values(#{bio}, #{name}, #{token}, #{gmtCreate}, #{gmtModified})")
    public void insert(Account user);
    @Select("select * from account where token = #{token}")
    public Account findUserByToken(@Param("token") String token);
}
