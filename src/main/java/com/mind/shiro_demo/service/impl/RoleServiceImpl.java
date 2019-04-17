package com.mind.shiro_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mind.shiro_demo.config.code.CommonCode;
import com.mind.shiro_demo.config.code.TxResultResponse;
import com.mind.shiro_demo.config.exception.CommonException;
import com.mind.shiro_demo.service.RoleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created with IDEA
 * author:Mind
 *
 * Date:2019/4/16
 * Time:16:29
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Override
    public TxResultResponse addRole(JSONObject jsonObject) {
       TxResultResponse tx = new TxResultResponse(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg());
       log.info("【RoleServiceImpl>>>addRole】**");
       try {

       return null;
       }
       catch (CommonException e) {
           log.error("【RoleServiceImpl>>>addRole】CommonException e={}",e.getMsg());
           throw  new CommonException(e.getCode(), e.getMsg());
       } catch (Exception e) {
           e.printStackTrace();
           log.error("【RoleServiceImpl>>>addRole】Exception e={}",e.getMessage());
           throw new CommonException(CommonCode.SERVER_ERROR.getCode(), CommonCode.SERVER_ERROR.getMsg());
       }
    }
}
