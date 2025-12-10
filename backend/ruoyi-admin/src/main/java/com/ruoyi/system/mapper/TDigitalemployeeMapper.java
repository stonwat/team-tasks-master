package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TDigitalemployee;

/**
 * digitalemployeeMapper接口
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
public interface TDigitalemployeeMapper 
{
    /**
     * 根据年月查询digitalemployee列表
     * 
     * @param year 年份
     * @param month 月份
     * @return digitalemployee集合
     */
    public List<TDigitalemployee> selectTDigitalemployeeListByYearMonth(TDigitalemployee tDigitalemployee);
    /**
     * 查询digitalemployee
     * 
     * @param id digitalemployee主键
     * @return digitalemployee
     */
    public TDigitalemployee selectTDigitalemployeeById(String id);

    /**
     * 查询digitalemployee列表
     * 
     * @param tDigitalemployee digitalemployee
     * @return digitalemployee集合
     */
    public List<TDigitalemployee> selectTDigitalemployeeList(TDigitalemployee tDigitalemployee);

    /**
     * 新增digitalemployee
     * 
     * @param tDigitalemployee digitalemployee
     * @return 结果
     */
    public int insertTDigitalemployee(TDigitalemployee tDigitalemployee);

    /**
     * 修改digitalemployee
     * 
     * @param tDigitalemployee digitalemployee
     * @return 结果
     */
    public int updateTDigitalemployee(TDigitalemployee tDigitalemployee);

    /**
     * 删除digitalemployee
     * 
     * @param id digitalemployee主键
     * @return 结果
     */
    public int deleteTDigitalemployeeById(String id);

    /**
     * 批量删除digitalemployee
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDigitalemployeeByIds(String[] ids);
}
