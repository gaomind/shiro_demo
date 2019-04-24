package com.mind.shiro_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IDEA
 * author:Mind
 *
 * Date:2019/4/22
 * Time:16:41
 */

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserModel {

    private Integer id;

    /**
     * 角色名称
     */
    private Integer roleName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     *  用户状态
     */
    private String status;
    /**
     * 登陆时间
     */
    private Date loginTime;

    /**
     * 创建时间
     */
    private Date createTime;



}
