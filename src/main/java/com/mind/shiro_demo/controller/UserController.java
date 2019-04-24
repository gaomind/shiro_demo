package com.mind.shiro_demo.controller;

import com.alibaba.fastjson.JSONObject;

import com.mind.shiro_demo.service.UserService;
import com.mind.shiro_demo.util.CommonUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: hxy
 * @description: 用户/角色/权限相关controller
 * @date: 2017/11/2 10:19
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    /**
     * 查询用户列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions("user:list")
    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return userService.listUser(CommonUtil.request2Json(request));
    }

    @RequiresPermissions("user:add")
    @PostMapping("/addUser")
    public JSONObject addUser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "username,tel,nickname,roleId,isLeader");// 0 指定为部门负责人 1 未指定
        return userService.addUser(requestJson);
    }

    @RequiresPermissions("user:update")
    @PostMapping("/updateUser")
    public JSONObject updateUser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " nickname,roleId, deleteStatus, userId,isLeader,tel");
        return userService.updateUser(requestJson);
    }

    @RequiresPermissions(value = {"user:add", "user:update"}, logical = Logical.OR)
    @GetMapping("/getAllRoles")
    public JSONObject getAllRoles() {
        return userService.getAllRoles();
    }

    /**
     * 角色列表
     *
     * @return
     */
    @RequiresPermissions("role:list")
    @GetMapping("/listRole")
    public JSONObject listRole() {
        return userService.listRole();
    }

    /**
     * 查询所有权限, 给角色分配权限时调用
     *
     * @return
     */
    @RequiresPermissions("role:list")
    @GetMapping("/listAllPermission")
    public JSONObject listAllPermission() {
        return userService.listAllPermission();
    }

    /**
     * 新增角色
     *
     * @return
     */
    @RequiresPermissions("role:add")
    @PostMapping("/addRole")
    public JSONObject addRole(@RequestBody JSONObject requestJson) {
        System.out.println(requestJson);
        CommonUtil.hasAllRequired(requestJson, "roleName,permissions");
        return userService.addRole(requestJson);
    }

    /**
     * 修改角色
     *
     * @return
     */
    @RequiresPermissions("role:update")
    @PostMapping("/updateRole")
    public JSONObject updateRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "roleId,roleName,permissions");
        return userService.updateRole(requestJson);
    }

    /**
     * 删除角色
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("role:delete")
    @PostMapping("/deleteRole")
    public JSONObject deleteRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "roleId");
        return userService.deleteRole(requestJson);
    }


    /**
     * 修改密码
     */
    @PostMapping("/upPassword")
    public JSONObject upPassword(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "userId,oldPassword,newPassword");
        return userService.upPassword(requestJson);
    }

    /**
     * 修改密码
     */
    @PostMapping("/cleanPassword")
    public JSONObject cleanPassword(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "userId");
        return userService.cleanPassword(requestJson);
    }


}
