package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * train对象 t_train
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
public class TTrain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 培训/交流名称 */
    @Excel(name = "培训/交流名称")
    private String trainName;

    /** 培训/交流详情 */
    @Excel(name = "培训/交流详情")
    private String trainInfo;

    /** 培训/交流时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "培训/交流时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trainTime;

    /** 培训/交流单位 */
    @Excel(name = "培训/交流单位")
    private String trainCompany;

    /** 培训/交流地点 */
    @Excel(name = "培训/交流地点")
    private String trainPlace;

    /** 培训/交流人员 */
    @Excel(name = "培训/交流人员")
    private String trainPerson;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setTrainName(String trainName) 
    {
        this.trainName = trainName;
    }

    public String getTrainName() 
    {
        return trainName;
    }
    public void setTrainInfo(String trainInfo) 
    {
        this.trainInfo = trainInfo;
    }

    public String getTrainInfo() 
    {
        return trainInfo;
    }
    public void setTrainTime(Date trainTime) 
    {
        this.trainTime = trainTime;
    }

    public Date getTrainTime() 
    {
        return trainTime;
    }
    public void setTrainCompany(String trainCompany) 
    {
        this.trainCompany = trainCompany;
    }

    public String getTrainCompany() 
    {
        return trainCompany;
    }
    public void setTrainPlace(String trainPlace) 
    {
        this.trainPlace = trainPlace;
    }

    public String getTrainPlace() 
    {
        return trainPlace;
    }
    public void setTrainPerson(String trainPerson) 
    {
        this.trainPerson = trainPerson;
    }

    public String getTrainPerson() 
    {
        return trainPerson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("trainName", getTrainName())
            .append("trainInfo", getTrainInfo())
            .append("trainTime", getTrainTime())
            .append("trainCompany", getTrainCompany())
            .append("trainPlace", getTrainPlace())
            .append("trainPerson", getTrainPerson())
            .toString();
    }
}
