package com.example.springboot.service;

import com.example.springboot.exception.ServiceException;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ji_ah
 * 2023-09-13 16:20
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void insertUser(User user) {
        userMapper.insert(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    public void batchDelete(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteUser(id);
        }
    }

    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectByPage(Integer pageNum, Integer pageSize, String username, String name) {
        Integer skipNum = (pageNum - 1) * pageSize;
        return userMapper.selectByPage(skipNum, pageSize, username, name);
    }


    public User login(User user) {
        //根据username查询user信息
        User dbuser = userMapper.selectByUsername(user.getUsername());
        if (dbuser == null) {
            throw new ServiceException("账号不存在");
        }
        if (!user.getPassword().equals(dbuser.getPassword())) {
            throw new ServiceException("密码错误");
        }
        String token = TokenUtils.createToken(dbuser.getId().toString(), dbuser.getPassword());
        dbuser.setToken(token);
        return dbuser;
    }

    public User register(User user) {
        User dbuser = userMapper.selectByUsername(user.getUsername());
        if (dbuser != null) {
            throw new ServiceException("用户已存在");
        }
        userMapper.insert(user);
        return user;
    }
}
