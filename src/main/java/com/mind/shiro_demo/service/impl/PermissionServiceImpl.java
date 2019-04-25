package com.mind.shiro_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mind.shiro_demo.dao.PermissionDao;
import com.mind.shiro_demo.dao.SysUserDAO;
import com.mind.shiro_demo.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author: hxy
 * @description:
 * @date: 2017/10/30 13:15
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private SysUserDAO userDAO;
    /**
     * 查询某用户的 角色  菜单列表   权限列表
     *
     * @param username
     * @return
     */
    @Override
    public JSONObject getUserPermission(String username) {
        JSONObject userPermission = getUserPermissionFromDB(username);
        return userPermission;
    }

    /**
     * 从数据库查询用户权限信息
     *
     * @param username
     * @return
     */
    private JSONObject getUserPermissionFromDB(String username) {
        JSONObject userPermission = permissionDao.getUserPermission(username);
        //管理员角色ID为1
      //  int adminRoleId = 1;
        String confStatus="1";// 1 代表是管理员
        //如果是管理员
       // String roleIdKey = "roleId";
        if (confStatus == userPermission.getString("confStatus")) {
            //查询所有菜单  所有权限
            Set<String> menuList = permissionDao.getAllMenu();
            Set<String> permissionList = permissionDao.getAllPermission();
            userPermission.put("menuList", menuList);
            userPermission.put("permissionList", permissionList);
        }
        JSONObject userInfo=userDAO.selectUserInfoForLogin(username);
        userPermission.put("userInfo", userInfo);
        return userPermission;
    }
}
