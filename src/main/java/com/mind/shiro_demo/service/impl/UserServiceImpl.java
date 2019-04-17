package com.mind.shiro_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mind.shiro_demo.config.code.CommonCode;
import com.mind.shiro_demo.config.code.TxResultResponse;
import com.mind.shiro_demo.config.exception.CommonException;
import com.mind.shiro_demo.dao.UserDao;
import com.mind.shiro_demo.model.SysRole;
import com.mind.shiro_demo.service.UserService;
import com.mind.shiro_demo.util.CommonUtil;
import com.mind.shiro_demo.util.constants.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: hxy
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    @Resource
    private UserDao userDao;


    /**
     * 装饰公司列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listUser(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = userDao.countUser(jsonObject);
        List<JSONObject> list = userDao.listUser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 添加用户
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject addUser(JSONObject jsonObject) {
        int exist = userDao.queryExistUsername(jsonObject);
        if (exist > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }
        userDao.addUser(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 查询所有的角色
     * 在添加/修改用户的时候要使用此方法
     *
     * @return
     */
    @Override
    public JSONObject getAllRoles() {
        List<JSONObject> roles = userDao.getAllRoles();
        return CommonUtil.successPage(roles);
    }

    /**
     * 修改用户
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject updateUser(JSONObject jsonObject) {
        userDao.updateUser(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 角色列表
     *
     * @return
     */
    @Override
    public JSONObject listRole() {
        List<JSONObject> roles = userDao.listRole();
        return CommonUtil.successPage(roles);
    }

    /**
     * 查询所有权限, 给角色分配权限时调用
     *
     * @return
     */
    @Override
    public JSONObject listAllPermission() {
        List<JSONObject> permissions = userDao.listAllPermission();
        return CommonUtil.successPage(permissions);
    }

    /**
     * 添加角色
     *
     * @param jsonObject
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject addRole(JSONObject jsonObject) {
        userDao.insertRole(jsonObject);
        userDao.insertRolePermission(jsonObject.getString("roleId"), (List<Integer>) jsonObject.get("permissions"));
        return CommonUtil.successJson();
    }

    /**
     * 修改角色
     *
     * @param jsonObject
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject updateRole(JSONObject jsonObject) {
        String roleId = jsonObject.getString("roleId");
        List<Integer> newPerms = (List<Integer>) jsonObject.get("permissions");
        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        Set<Integer> oldPerms = (Set<Integer>) roleInfo.get("permissionIds");
        //修改角色名称
        dealRoleName(jsonObject, roleInfo);
        //添加新权限
        saveNewPermission(roleId, newPerms, oldPerms);
        //移除旧的不再拥有的权限
        removeOldPermission(roleId, newPerms, oldPerms);
        return CommonUtil.successJson();
    }

    /**
     * 修改角色名称
     *
     * @param paramJson
     * @param roleInfo
     */
    private void dealRoleName(JSONObject paramJson, JSONObject roleInfo) {
        String roleName = paramJson.getString("roleName");
        if (!roleName.equals(roleInfo.getString("roleName"))) {
            userDao.updateRoleName(paramJson);
        }
    }

    /**
     * 为角色添加新权限
     *
     * @param newPerms
     * @param oldPerms
     */
    private void saveNewPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitInsert = new ArrayList<>();
        for (Integer newPerm : newPerms) {
            if (!oldPerms.contains(newPerm)) {
                waitInsert.add(newPerm);
            }
        }
        if (waitInsert.size() > 0) {
            userDao.insertRolePermission(roleId, waitInsert);
        }
    }

    /**
     * 删除角色 旧的 不再拥有的权限
     *
     * @param roleId
     * @param newPerms
     * @param oldPerms
     */
    private void removeOldPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitRemove = new ArrayList<>();
        for (Integer oldPerm : oldPerms) {
            if (!newPerms.contains(oldPerm)) {
                waitRemove.add(oldPerm);
            }
        }
        if (waitRemove.size() > 0) {
            userDao.removeOldPermission(roleId, waitRemove);
        }
    }

    /**
     * 删除角色
     *
     * @param jsonObject
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject deleteRole(JSONObject jsonObject) {
        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        List<JSONObject> users = (List<JSONObject>) roleInfo.get("users");
        if (users != null && users.size() > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10008);
        }
        userDao.removeRole(jsonObject);
        userDao.removeRoleAllPermission(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public TxResultResponse toTree() {
        TxResultResponse tx = new TxResultResponse(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg());
        log.info("【UserServiceImpl>>>toTree】**");
        try {
            List<SysRole> treeModels=userDao.list();
            Iterator<SysRole> iterator =treeModels.iterator();
            //找出顶级组织机构（这里我们定义 顶级的pid为0 ）
            ArrayList<SysRole> rootNodes = Lists.newArrayList();
            while (iterator.hasNext()) {
                SysRole role =iterator.next();
                if (role.getParentId() == 0){
                    rootNodes.add(role);
                    iterator.remove();
                }
            }
            //为当前的组织机构排序（根据seq值）
            if(!rootNodes.isEmpty()){
                rootNodes.sort(comparator);
            }

            if(!treeModels.isEmpty() && !rootNodes.isEmpty()){
                rootNodes.forEach(rootNode -> constructTree(rootNode,treeModels));
            }
            tx.setData(rootNodes);
            return tx;
        }
        catch (CommonException e) {
            log.error("【UserServiceImpl>>>toTree】CommonException e={}",e.getMsg());
            throw  new CommonException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【UserServiceImpl>>>toTree】Exception e={}",e.getMessage());
            throw new CommonException(CommonCode.SERVER_ERROR.getCode(), CommonCode.SERVER_ERROR.getMsg());
        }
    }

    private Comparator<SysRole> comparator = new Comparator<SysRole>() {
        @Override
        public int compare(SysRole s1, SysRole s2) {
            return s2.getSeq() - s1.getSeq();
        }
    };
    /**
     * 构造树
     *
     * @param parentNode 父节点
     * @param treeModels 剩余节点
     */
    private void constructTree(SysRole parentNode, List<SysRole> treeModels) {
        Iterator<SysRole> iterator = treeModels.iterator();
        //保存子节点
        List<SysRole> childrens = new ArrayList<>();

        while (iterator.hasNext()) {
            SysRole node = iterator.next();
            //找出下一级的节点
            if (parentNode.getId().equals(node.getParentId())) {
                childrens.add(node);
                iterator.remove();
            }
        }
        //为当前节点排序
        if (!childrens.isEmpty()) {
            childrens.sort(comparator);
        }
        //设置当前子节点为当前父节点的子集
        if (!CollectionUtils.isEmpty(childrens)) {
            parentNode.setChild(childrens);
        }
        //递归进行上述步骤  当我们的子节点没有时就一个顶级组织的递归就结束了
        //当treeModels 空时 我们所有的顶级节点都把递归执行完了 就结束了
        if (!CollectionUtils.isEmpty(treeModels) && !childrens.isEmpty()) {
            childrens.forEach(node -> constructTree(node, treeModels));
        }
    }
}
