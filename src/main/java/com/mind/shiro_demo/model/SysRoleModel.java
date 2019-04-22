package com.mind.shiro_demo.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * sys_role
 * @author 
 */

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleModel implements Serializable {
    /**
     * 角色id
     */
    private Integer id;

    /**
     * 负责人姓名
     */
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 20, min = 2, message = "部门名长度需要在2到15之间")
    private String principalName;


    /**
     * 负责人联系方式
     */
    private String principalTel;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 父级id
     */
    private Integer parentId;


    /**
     * 父级部门名称
     */
    private String parentName;

    /**
     * 部门层级
     */
    private String level;

    /**
     * 部门在当前层级下的顺序，由小到大
     */
    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    /**
     * 配置状态
     */
    private String confStatus;

    /**
     * 备注
     */
    @Length(message = "备注长度不能超过150", max = 150)
    private String remark;

    private Date createTime;

    private Date updateTime;
    /**
     * 前端 折叠显示
     */
    private Boolean show =true;

    /**
     * 是否有效  0未删除  1删除
     */
    private String isDelete;

    private List<SysRoleModel> child;

    private List<JSONObject> menus;

    private static final long serialVersionUID = 1L;

    public List<SysRoleModel> getChild() {
        return child;
    }

    public void setChild(List<SysRoleModel> child) {
        this.child = child;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getPrincipalTel() {
        return principalTel;
    }

    public void setPrincipalTel(String principalTel) {
        this.principalTel = principalTel;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getConfStatus() {
        return confStatus;
    }

    public void setConfStatus(String confStatus) {
        this.confStatus = confStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }


    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }


    public List<JSONObject> getMenus() {
        return menus;
    }

    public void setMenus(List<JSONObject> menus) {
        this.menus = menus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysRoleModel that = (SysRoleModel) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getPrincipalName(), that.getPrincipalName()) &&
                Objects.equals(getPrincipalTel(), that.getPrincipalTel()) &&
                Objects.equals(getRoleName(), that.getRoleName()) &&
                Objects.equals(getParentId(), that.getParentId()) &&
                Objects.equals(getParentName(), that.getParentName()) &&
                Objects.equals(getLevel(), that.getLevel()) &&
                Objects.equals(getSeq(), that.getSeq()) &&
                Objects.equals(getConfStatus(), that.getConfStatus()) &&
                Objects.equals(getRemark(), that.getRemark()) &&
                Objects.equals(getCreateTime(), that.getCreateTime()) &&
                Objects.equals(getUpdateTime(), that.getUpdateTime()) &&
                Objects.equals(getShow(), that.getShow()) &&
                Objects.equals(getIsDelete(), that.getIsDelete()) &&
                Objects.equals(getChild(), that.getChild()) &&
                Objects.equals(getMenus(), that.getMenus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrincipalName(), getPrincipalTel(), getRoleName(), getParentId(), getParentName(), getLevel(), getSeq(), getConfStatus(), getRemark(), getCreateTime(), getUpdateTime(), getShow(), getIsDelete(), getChild(), getMenus());
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"principalName\":\"")
                .append(principalName).append('\"');
        sb.append(",\"principalTel\":\"")
                .append(principalTel).append('\"');
        sb.append(",\"roleName\":\"")
                .append(roleName).append('\"');
        sb.append(",\"parentId\":\"")
                .append(parentId).append('\"');
        sb.append(",\"parentName\":\"")
                .append(parentName).append('\"');
        sb.append(",\"level\":\"")
                .append(level).append('\"');
        sb.append(",\"seq\":\"")
                .append(seq).append('\"');
        sb.append(",\"confStatus\":\"")
                .append(confStatus).append('\"');
        sb.append(",\"remark\":\"")
                .append(remark).append('\"');
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append(",\"updateTime\":\"")
                .append(updateTime).append('\"');
        sb.append(",\"show\":\"")
                .append(show).append('\"');
        sb.append(",\"isDelete\":\"")
                .append(isDelete).append('\"');
        sb.append(",\"child\":\"")
                .append(child).append('\"');
        sb.append(",\"menus\":\"")
                .append(menus).append('\"');
        sb.append('}');
        return sb.toString();
    }
}