package com.mind.shiro_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mind.shiro_demo.config.code.CommonCode;
import com.mind.shiro_demo.config.code.TxResultResponse;
import com.mind.shiro_demo.config.exception.CommonException;
import com.mind.shiro_demo.dao.SysRoleDAO;
import com.mind.shiro_demo.dao.UserDao;
import com.mind.shiro_demo.entity.SysRole;
import com.mind.shiro_demo.mapping.BeanMapper;
import com.mind.shiro_demo.model.SysRoleModel;
import com.mind.shiro_demo.service.RoleService;

import com.mind.shiro_demo.util.AccountValidatorUtil;
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
    public TxResultResponse toTree() {
        TxResultResponse tx = new TxResultResponse(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg());
        log.info("【UserServiceImpl>>>toTree】**");
        try {
            List<SysRole> sysRoles = roleDAO.list();

            List<SysRoleModel> treeModels = beanMapper.mapAsList(sysRoles, SysRoleModel.class);
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
            tx.setData(rootNodes);
            return tx;
        } catch (CommonException e) {
            log.error("【UserServiceImpl>>>toTree】CommonException e={}", e.getMsg());
            throw new CommonException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【UserServiceImpl>>>toTree】Exception e={}", e.getMessage());
            throw new CommonException(CommonCode.SERVER_ERROR.getCode(), CommonCode.SERVER_ERROR.getMsg());
        }
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
    public TxResultResponse addRole(JSONObject jsonObject) {
        TxResultResponse tx = new TxResultResponse(CommonCode.SUCCESS.getCode(), CommonCode.SUCCESS.getMsg());
        log.info("【RoleServiceImpl>>>addRole】Param = {}", jsonObject);
        try {
            SysRole parentRole = roleDAO.selectRoleById(String.valueOf(jsonObject.getIntValue("parentId")));

            if (AccountValidatorUtil.isNullOrEmpty(parentRole)) {
                log.error("【RoleServiceImpl>>>addRole】异常parentRole为空{}", parentRole);
                throw new CommonException(CommonCode.ERROR.getCode(), CommonCode.PARAM_ERROR.getMsg());
            }
            SysRole newRole = SysRole.builder().roleName(jsonObject.getString("roleName"))
                    .confStatus("1")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .isDelete("0")
                    .parentId(jsonObject.getIntValue("parentId"))
                    .level(parentRole.getLevel() + "." + parentRole.getId())
                    .principalName(jsonObject.getString("principalName"))
                    .principalTel(jsonObject.getString("principalTel"))
                    .build();
            int i=roleDAO.insert(newRole);
            int j=userDao.insertRolePermission(jsonObject.getString("roleId"), (List<Integer>) jsonObject.get("permissions"));
            if (i+j!=2){
                log.error("【RoleServiceImpl>>>addRole】异常i={}j={}",i,j);
                throw new CommonException(CommonCode.ERROR.getCode(),CommonCode.PARAM_ERROR.getMsg());
            }
            return tx;
        } catch (CommonException e) {
            log.error("【RoleServiceImpl>>>addRole】CommonException e={}", e.getMsg());
            throw new CommonException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【RoleServiceImpl>>>addRole】Exception e={}", e.getMessage());
            throw new CommonException(CommonCode.SERVER_ERROR.getCode(), CommonCode.SERVER_ERROR.getMsg());
        }
    }
}
