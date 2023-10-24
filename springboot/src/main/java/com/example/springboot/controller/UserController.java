package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ji_ah
 * 2023-09-13 16:21
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.save(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("插入数据库错误");
            } else {
                return Result.error("系统错误");
            }
        }
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && id.equals(currentUser.getId())) {
            throw new ServiceException("不能删除当前用户");
        }
        userService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && ids.contains(currentUser.getId())) {
            throw new ServiceException("批量删除的用户包括当前用户");
        }
        userService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.list(new LambdaQueryWrapper<User>().orderByDesc(User::getId));
        return Result.success(userList);
    }

    @GetMapping("/select/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }


    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(username), User::getUsername, username);
        queryWrapper.like(StrUtil.isNotBlank(name), User::getName, name);
        queryWrapper.orderByDesc(User::getId);
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String ids,
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<User> userList;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(ids)) {  //导出指定的条件   ["1", "2", "3"]   => [1,2,3]
            //转换string类型id为int
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in(User::getId, idsArr);
        } else {  //查询全部或者根据条件查询
            queryWrapper.like(StrUtil.isNotBlank(username), User::getUsername, username);
            queryWrapper.like(StrUtil.isNotBlank(name), User::getName, name);
        }
        userList = userService.list(queryWrapper);
        writer.write(userList, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        try {
            userService.saveBatch(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("数据批量导入错误");
        }
        return Result.success();
    }
}
