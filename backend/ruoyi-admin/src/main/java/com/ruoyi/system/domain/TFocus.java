package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * focus对象 t_focus
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public class TFocus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 重点工作名称 */
    @Excel(name = "重点工作名称")
    private String focusName;

    /** 重点工作详情 */
    @Excel(name = "重点工作详情")
    private String focusInfo;

    /** 重点工作当前进度 */
    @Excel(name = "重点工作当前进度")
    private String focusProgress;

    /** 重点工作进度完成情况（1.需求调研 2.项目规划 3.项目建设 4.项目交付 5.逾期） */
    @Excel(name = "重点工作进度完成情况", readConverterExp = "1=.需求调研,2=.项目规划,3=.项目建设,4=.项目交付,5=.逾期")
    private String focusPersent;

    /** 重点工作计划完成时间 */
    @Excel(name = "重点工作计划完成时间")
    private String focusEndtime;

    /** 重点工作负责人 */
    @Excel(name = "重点工作负责人")
    private String focusPerson;

    /** 重点工作大类 */
    @Excel(name = "重点工作大类")
    private String focusType;

    /** 重点工作工作计划 */
    @Excel(name = "重点工作工作计划")
    private String focusPlan;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setFocusName(String focusName) 
    {
        this.focusName = focusName;
    }

    public String getFocusName() 
    {
        return focusName;
    }
    public void setFocusInfo(String focusInfo) 
    {
        this.focusInfo = focusInfo;
    }

    public String getFocusInfo() 
    {
        return focusInfo;
    }
    public void setFocusProgress(String focusProgress) 
    {
        this.focusProgress = focusProgress;
    }

    public String getFocusProgress() 
    {
        return focusProgress;
    }
    public void setFocusPersent(String focusPersent) 
    {
        this.focusPersent = focusPersent;
    }

    public String getFocusPersent() 
    {
        return focusPersent;
    }
    public void setFocusEndtime(String focusEndtime) 
    {
        this.focusEndtime = focusEndtime;
    }

    public String getFocusEndtime() 
    {
        return focusEndtime;
    }
    public void setFocusPerson(String focusPerson) 
    {
        this.focusPerson = focusPerson;
    }

    public String getFocusPerson() 
    {
        return focusPerson;
    }
    public void setFocusType(String focusType) 
    {
        this.focusType = focusType;
    }

    public String getFocusType() 
    {
        return focusType;
    }
    public void setFocusPlan(String focusPlan) 
    {
        this.focusPlan = focusPlan;
    }

    public String getFocusPlan() 
    {
        return focusPlan;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("focusName", getFocusName())
            .append("focusInfo", getFocusInfo())
            .append("focusProgress", getFocusProgress())
            .append("focusPersent", getFocusPersent())
            .append("focusEndtime", getFocusEndtime())
            .append("focusPerson", getFocusPerson())
            .append("focusType", getFocusType())
            .append("focusPlan", getFocusPlan())
            .toString();
    }
}
