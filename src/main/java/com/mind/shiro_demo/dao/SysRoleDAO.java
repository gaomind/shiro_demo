package com.mind.shiro_demo.dao;

import java.util.List;

import com.mind.shiro_demo.entity.SysRole;
import com.mind.shiro_demo.entity.SysRoleExample;
import com.mind.shiro_demo.model.SysRoleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleDAO {
    long countByExample(SysRoleExample example);

    int deleteByExample(SysRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    List<SysRole> selectByExample(SysRoleExample example);

    SysRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysRole record, @Param("example") SysRoleExample example);

    int updateByExample(@Param("record") SysRole record, @Param("example") SysRoleExample example);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);


    // personal_start
    List<SysRoleModel> list();


    SysRole selectRoleById(@Param("roleId") String roleId);
    // personal_end


}