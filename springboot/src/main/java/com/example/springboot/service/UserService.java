package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Ji_ah
 * 2023-09-13 16:20
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean save(User entity) {
        if (StrUtil.isBlank(entity.getName())) {
            entity.setName(entity.getUsername());
        }
        if (StrUtil.isBlank(entity.getPassword())) {
            entity.setPassword("123456");
        }
        if (StrUtil.isBlank(entity.getRole())) {
            entity.setRole("用户");
        }
        return super.save(entity);
    }

    public User selectByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return getOne(queryWrapper);
    }


    public User login(User user) {
        //根据username查询user信息
        User dbuser = selectByUsername(user.getUsername());
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
        User dbuser = selectByUsername(user.getUsername());
        if (dbuser != null) {
            throw new ServiceException("用户已存在");
        }
        userMapper.insert(user);
        return user;
    }

    public void resetPassword(User user) {
        User dbuser = selectByUsername(user.getUsername());
        if (dbuser == null) {
            throw new ServiceException("用户不存在");
        }
        if (!dbuser.getPhone().equals(user.getPhone())) {
            throw new ServiceException("手机号验证错误");
        }
        dbuser.setPassword("123456");   //默认密码
        updateById(dbuser);
    }
}
