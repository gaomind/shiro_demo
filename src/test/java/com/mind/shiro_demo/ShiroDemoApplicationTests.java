package com.mind.shiro_demo;

import com.mind.shiro_demo.dao.SysRoleDAO;
import com.mind.shiro_demo.dao.UserDao;
import com.mind.shiro_demo.model.SysRoleModel;
import com.mind.shiro_demo.service.RoleService;
import com.mind.shiro_demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroDemoApplicationTests {

    @Resource
    private UserDao userDao;
    @Test
    public void contextLoads() {
    }


    @Resource
    private SysRoleDAO roleDAO;

    @Resource
    private RoleService roleService;
    @Test
    public void test1(){
        List<SysRoleModel> s =roleDAO.list();
        System.out.println(s.size());
    }
}
