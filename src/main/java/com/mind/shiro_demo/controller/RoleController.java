package com.mind.shiro_demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.mind.shiro_demo.service.RoleService;
import com.mind.shiro_demo.service.UserService;
import com.mind.shiro_demo.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IDEA
 * author:Mind
 *
 * Date:2019/4/16
 * Time:16:13
 */


@RestController
@RequestMapping("/role")
public class RoleController {


    private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    //获取角色树
    @PostMapping("/roleTree")
    public JSONObject roleTree() {
        log.info("【UserController>>>roleTree】{}", "获取权限树");
        return roleService.toTree();

    }

    //添加角色
    @PostMapping("/add")
    public JSONObject addRole(@RequestBody JSONObject requestJson) {

        log.info("【UserController>>>roleTree】添加角色{}", requestJson);
        //   CommonUtil.hasAllRequired(requestJson, "roleName,permissions,parentId,seq");
        CommonUtil.hasAllRequired(requestJson, "roleName,parentId,seq");

        return roleService.addRole(requestJson);

    }
    //修改角色
    @PostMapping("/up")
    public JSONObject upRole(@RequestBody JSONObject requestJson) {
        log.info("【UserController>>>roleTree】添加角色{}", requestJson);
        //   CommonUtil.hasAllRequired(requestJson, "roleName,permissions,parentId,seq");
        CommonUtil.hasAllRequired(requestJson, "roleName,parentId,seq");
        return roleService.upRole(requestJson);

    }

    //修改角色
    @PostMapping("/del")
    public JSONObject delRole(@RequestBody JSONObject requestJson) {
        log.info("【UserController>>>roleTree】添加角色{}", requestJson);
        //   CommonUtil.hasAllRequired(requestJson, "roleName,permissions,parentId,seq");
        CommonUtil.hasAllRequired(requestJson, "roleId");
        return roleService.delRole(requestJson);
    }


}
