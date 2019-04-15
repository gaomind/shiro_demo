package com.mind.shiro_demo.controller;

import com.mind.shiro_demo.config.code.TxResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IDEA
 * author:Mind
 *
 * Date:2019/4/11
 * Time:20:23
 */

@RestController
@RequestMapping("/permission")
public class PermissionController {
    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);


    //测试组织部门树
    @PostMapping("/tree")
    public TxResultResponse toTree() {
        List<>



        //查询所有的组织结构
        List<SysDeptModel> treeModels = sysDeptService.selectPage(new SysDeptModel(), null);
        Iterator<SysDeptModel> iterator = treeModels.iterator();
        //找出顶级组织机构（这里我们定义 顶级的pid为0 ）
        ArrayList<SysDeptModel> rootNodes = Lists.newArrayList();
        while (iterator.hasNext()) {
            SysDeptModel node = iterator.next();
            if (node.getParentId() == 0) {
                rootNodes.add(node);
                iterator.remove();
            }
        }
        //为当前的组织机构排序（根据seq值）
        if (!rootNodes.isEmpty()) {
            rootNodes.sort(comparator);
        }
        //每隔从顶级开始递归寻找他们的子节点
        if (!treeModels.isEmpty() && !rootNodes.isEmpty()) {
            rootNodes.forEach(rootNode -> constructTree(rootNode, treeModels));
        }
        //返回结果
        return ResponseEnvelopFactory.success(rootNodes);
    }





}
