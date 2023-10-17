package com.example.springboot.mapper;

import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Ji_ah
 * 2023-09-13 16:20
 */
@Mapper
public interface UserMapper {

    @Insert("insert into `user`(username, password, name, phone, email, address, avatar,role) " + "values(#{username}, #{password}, #{name}, #{phone}, #{email}, #{address}, #{avatar}, #{role}) ")
    void insert(User user);

    @Update("update `user` set username=#{username},password=#{password}," + "name=#{name},phone=#{phone},email=#{email},address=#{address},avatar=#{avatar}" + "where id=#{id}")
    void updateUser(User user);

    @Delete("delete from `user` where id=#{id}")
    void deleteUser(Integer id);

    @Select("select * from `user` order by id desc")
    List<User> selectAll();

    @Select("select * from user where id=#{id}")
    User selectById(Integer id);


    @Select("select * from `user` where username like concat('%',#{username},'%') and" +
            " name like concat('%',#{name},'%') limit #{skipNum},#{pageSize}")
    List<User> selectByPage(@Param("skipNum") Integer skipNum, @Param("pageSize") Integer pageSize,
                            @Param("username") String username, @Param("name") String name);

    @Select("select * from `user` where username=#{username}")
    User selectByUsername(String username);
}
