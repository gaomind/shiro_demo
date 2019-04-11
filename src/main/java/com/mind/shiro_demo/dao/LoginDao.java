package com.mind.shiro_demo.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IDEA
 * author:Mind
 *
 * Date:2019/4/10
 * Time:20:09
 */
public interface LoginDao {

    /**
     * 根据用户名和密码查询对应的用户
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    JSONObject getUser(@Param("username") String username, @Param("password") String password);
}
