package com.mind.shiro_demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.mind.shiro_demo.config.code.CommonCode;
import com.mind.shiro_demo.config.code.TxResultResponse;
import com.mind.shiro_demo.config.exception.CommonException;
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
    public TxResultResponse roleTree() {
        TxResultResponse tx = new TxResultResponse(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg());
        log.info("【UserController>>>roleTree】{}", "获取权限树");
        try {
            return userService.toTree();
        } catch (CommonException e) {
            log.error("【UserController>>>roleTree】CommonException,e={}", e.getMsg());
            return new TxResultResponse(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.error("【UserController>>>roleTree】Exception,e={}", e.getMessage());
            return new TxResultResponse(CommonCode.SERVER_ERROR.getCode(), "服务器内部异常!");
        }
    }

    //添加角色
    @PostMapping("/add")
    public TxResultResponse addRole(@RequestBody JSONObject requestJson) {
        TxResultResponse tx = new TxResultResponse(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg());
        log.info("【UserController>>>roleTree】添加角色{}", requestJson);
        try {
            CommonUtil.hasAllRequired(requestJson, "roleName,permissions,parentId,seq");
            return roleService.addRole(requestJson);
        } catch (CommonException e) {
            log.error("【UserController>>>roleTree】CommonException,e={}", e.getMsg());
            return new TxResultResponse(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.error("【UserController>>>roleTree】Exception,e={}", e.getMessage());
            return new TxResultResponse(CommonCode.SERVER_ERROR.getCode(), "服务器内部异常!");
        }
    }



}
