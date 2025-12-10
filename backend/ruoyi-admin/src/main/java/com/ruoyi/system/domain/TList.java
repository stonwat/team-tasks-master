package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应用目标清单对象 t_list
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public class TList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 场景分类 */
    @Excel(name = "场景分类")
    private String type;

    /** 部门 */
    @Excel(name = "部门")
    private String department;

    /** 场景应用 */
    @Excel(name = "场景应用")
    private String application;

    /** 指标 */
    @Excel(name = "指标")
    private String index1;

    /** 目标 */
    @Excel(name = "目标")
    private String target;

    /** 当前分数 */
    @Excel(name = "当前分数")
    private String score;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public String getDepartment() 
    {
        return department;
    }
    public void setApplication(String application) 
    {
        this.application = application;
    }

    public String getApplication() 
    {
        return application;
    }
    public void setIndex1(String index1)
    {
        this.index1 = index1;
    }

    public String getIndex1()
    {
        return index1;
    }
    public void setTarget(String target) 
    {
        this.target = target;
    }

    public String getTarget() 
    {
        return target;
    }
    public void setScore(String score) 
    {
        this.score = score;
    }

    public String getScore() 
    {
        return score;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("department", getDepartment())
            .append("application", getApplication())
            .append("index1", getIndex1())
            .append("target", getTarget())
            .append("score", getScore())
            .toString();
    }
}
