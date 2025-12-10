package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * digitalemployee对象 t_digitalemployee
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
public class wangan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 工单总数1 */
    @Excel(name = "检出安全事故数")
    private String orderError1;

    /** 数字员工处理总数1 */
    @Excel(name = "需处置安全事件总数")
    private String orderComplete1;


    /** 工单总数5 */
    @Excel(name = "集团内排名")
    private String orderRank;

    /** 数字员工处理总数5 */
    @Excel(name = "单项赋分")
    private String orderScore;

    

    /** 应用深度现状 */
    @Excel(name = "应用深度现状")
    private String orderPersent;

    /** 数字员工分类（1.装维调度与自服务2.综维3.隐患管控与故障处置4.网优5.多模态客服机器人6.门店宝7.AI客户助理8. 精准营销智能体9.产数支撑智能体10.研发智能体11.网安研判智能体12.采供智能体13.客服助理智能体） */
    @Excel(name = "数字员工分类", readConverterExp = "1=装维调度与自服务数字员工,2=综维数字员工,3=隐患管控与故障处置数字员工,4=网优数字员工,5=客服助理智能体,6=多模态客服机器人,7=门店宝,8=AI客户助理,9=精准营销智能体,10=产数支撑智能体,11=研发智能体,12=网安研判智能体,13=采供智能体")
    private String employeeType;

    /** 数据时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataTime;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setOrderError1(String orderError1) 
    {
        this.orderError1 = orderError1;
    }

    public String getOrderError1() 
    {
        return orderError1;
    }
    public void setOrderComplete1(String orderComplete1) 
    {
        this.orderComplete1 = orderComplete1;
    }

    public String getOrderComplete1() 
    {
        return orderComplete1;
    }
    
    public void setOrderRank(String orderRank) 
    {
        this.orderRank = orderRank;
    }

    public String getOrderRank() 
    {
        return orderRank;
    }
    public void setOrderScore(String orderScore)        
    {
        this.orderScore = orderScore;
    }

    public String getOrderScore() 
    {
        return orderScore;
    }
    public void setOrderPersent(String orderPersent) 
    {
        this.orderPersent = orderPersent;
    }

    public String getOrderPersent() 
    {
        return orderPersent;
    }
    public void setEmployeeType(String employeeType) 
    {
        this.employeeType = employeeType;
    }

    public String getEmployeeType() 
    {
        return employeeType;
    }
    public void setDataTime(Date dataTime) 
    {
        this.dataTime = dataTime;
    }

    public Date getDataTime() 
    {
        return dataTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderError1", getOrderError1())
            .append("orderComplete1", getOrderComplete1())

            .append("orderRank", getOrderRank())
            .append("orderScore", getOrderScore())
            .append("orderPersent", getOrderPersent())
            .append("employeeType", getEmployeeType())
            .append("dataTime", getDataTime())
            .append("remark", getRemark())
            .toString();
    }
}
