package com.mind.shiro_demo.service;

import com.alibaba.fastjson.JSONObject;
import com.mind.shiro_demo.config.code.TxResultResponse;

/**
 * Created with IDEA
 * author:Mind
 *
 * Date:2019/4/16
 * Time:16:27
 */
public interface RoleService {


    TxResultResponse toTree();


    /*
     * @Description 添加角色
     * @author Mind
     * @date 2019/4/16 16:28
     * @param
     * @return
     */
    TxResultResponse addRole(JSONObject jsonObject);

}
