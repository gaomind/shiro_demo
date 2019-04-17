package com.mind.shiro_demo.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * author:Mind
 *
 * Date:2019/4/16
 * Time:16:27
 */
public interface RoleService {


    /*
     * @Description 获取部门（角色）树
     * @author Mind
     * @date 2019/4/17 17:34
     * @param []
     * @return com.mind.shiro_demo.config.code.TxResultResponse
     */
    JSONObject toTree();


    /*
     * @Description 添加角色
     * @author Mind
     * @date 2019/4/16 16:28
     * @param
     * @return
     */
    JSONObject addRole(JSONObject jsonObject);


    /*
     * @Description 更新角色
     * @author Mind
     * @date 2019/4/16 16:28
     * @param
     * @return
     */
    JSONObject upRole(JSONObject jsonObject);



    /*
     * @Description 删除角色
     * @author Mind
     * @date 2019/4/16 16:28
     * @param
     * @return
     */
    JSONObject delRole(JSONObject jsonObject);
}
