package com.mind.shiro_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mind.shiro_demo.dao.SysRoleDAO;
import com.mind.shiro_demo.dao.UserDao;
import com.mind.shiro_demo.entity.SysRole;
import com.mind.shiro_demo.mapping.BeanMapper;
import com.mind.shiro_demo.model.SysRoleModel;
import com.mind.shiro_demo.service.RoleService;

import com.mind.shiro_demo.util.AccountValidatorUtil;
import com.mind.shiro_demo.util.CommonUtil;
import com.mind.shiro_demo.util.constants.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

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

    @Autowired
    private BeanMapper beanMapper;

    @Resource
    private SysRoleDAO roleDAO;

    @Resource
    private UserDao userDao;

    @Override
    public JSONObject toTree() {

        log.info("【UserServiceImpl>>>toTree】");

        List<SysRoleModel> treeModels = roleDAO.list();

      //  List<SysRoleModel> treeModels = beanMapper.mapAsList(sysRoles, SysRoleModel.class);
        Iterator<SysRoleModel> iterator = treeModels.iterator();
        //找出顶级组织机构（这里我们定义 顶级的pid为0 ）
        ArrayList<SysRoleModel> rootNodes = Lists.newArrayList();
        while (iterator.hasNext()) {
            SysRoleModel role = iterator.next();
            if (role.getParentId() == 0) {
                rootNodes.add(role);
                iterator.remove();
            }
        }
        //为当前的组织机构排序（根据seq值）
        if (!rootNodes.isEmpty()) {
            rootNodes.sort(comparator);
        }

        if (!treeModels.isEmpty() && !rootNodes.isEmpty()) {
            rootNodes.forEach(rootNode -> constructTree(rootNode, treeModels));
        }

        return CommonUtil.successJson(rootNodes);


    }

    private Comparator<SysRoleModel> comparator = new Comparator<SysRoleModel>() {
        @Override
        public int compare(SysRoleModel s1, SysRoleModel s2) {
            return s2.getSeq() - s1.getSeq();
        }
    };

    /**
     * 构造树
     *
     * @param parentNode 父节点
     * @param treeModels 剩余节点
     */
    private void constructTree(SysRoleModel parentNode, List<SysRoleModel> treeModels) {
        Iterator<SysRoleModel> iterator = treeModels.iterator();
        //保存子节点
        List<SysRoleModel> childrens = new ArrayList<>();

        while (iterator.hasNext()) {
            SysRoleModel node = iterator.next();
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


    @Override
    @Transactional
    public JSONObject addRole(JSONObject jsonObject) {
        log.info("【RoleServiceImpl>>>addRole】Param = {}", jsonObject);
        SysRole parentRole = roleDAO.selectRoleById(String.valueOf(jsonObject.getIntValue("parentId")));
        if (AccountValidatorUtil.isNullOrEmpty(parentRole)) {
            log.error("【RoleServiceImpl>>>addRole】异常parentRole为空{}", parentRole);
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }
        SysRole newRole = SysRole.builder().roleName(jsonObject.getString("roleName"))
                .confStatus("0")
                .createTime(new Date())
                .updateTime(new Date())
                .isDelete("0")
                .seq(1)
                .parentId(jsonObject.getIntValue("parentId"))
                .level(parentRole.getLevel() + "." + parentRole.getId())
                .principalName("暂无")
                .principalTel("暂无")
                .id(AccountValidatorUtil.genUniqueKeyNum())
                .build();
        int i = roleDAO.insert(newRole);
        int j = userDao.insertRolePermission(String.valueOf(newRole.getId()), (List<Integer>) jsonObject.get("permissions"));
        if (i + j != ((List<Integer>) jsonObject.get("permissions")).size()+1) {
            log.error("【RoleServiceImpl>>>addRole】异常i={}j={}", i, j);
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }
        return CommonUtil.successJson();
    }

    @Override
    @Transactional
    public JSONObject upRole(JSONObject jsonObject) {
        log.info("【RoleServiceImpl>>>upRole】Param = {}", jsonObject);
        String roleId = jsonObject.getString("roleId");
        Integer parentId=jsonObject.getIntValue("parentId");



        List<Integer> newPerms = (List<Integer>) jsonObject.get("permissions");
        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        Set<Integer> oldPerms = (Set<Integer>) roleInfo.get("permissionIds");
        //修改角色名称
        SysRole sysRole=roleDAO.selectByPrimaryKey(Integer.valueOf(roleId));
        SysRole parentRole=roleDAO.selectByPrimaryKey(parentId);
        sysRole.setParentId(jsonObject.getIntValue("parentId"));
        sysRole.setLevel(parentRole.getLevel() + "." + parentRole.getId());
        sysRole.setUpdateTime(new Date());
        sysRole.setRoleName(jsonObject.getString("roleNameAdd"));
        int i=roleDAO.updateByPrimaryKeySelective(sysRole);
        if (i!=1){
            log.error("【RoleServiceImpl>>>upRole】异常 i={}",i);
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }
        //dealRoleName(jsonObject, roleInfo);
        //添加新权限
        saveNewPermission(roleId, newPerms, oldPerms);
        //移除旧的不再拥有的权限
        removeOldPermission(roleId, newPerms, oldPerms);
        return CommonUtil.successJson();

    }

    @Override
    public JSONObject delRole(JSONObject jsonObject) {
        log.info("【RoleServiceImpl>>>delRole】Param = {}", jsonObject);

        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        List<JSONObject> users = (List<JSONObject>) roleInfo.get("users");
        if (users != null && users.size() > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10008);
        }
        userDao.removeRole(jsonObject);
        userDao.removeRoleAllPermission(jsonObject);
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

}
