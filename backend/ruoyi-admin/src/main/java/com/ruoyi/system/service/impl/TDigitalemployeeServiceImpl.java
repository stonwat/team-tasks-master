package com.ruoyi.system.service.impl;
import java.util.UUID;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TDigitalemployeeMapper;
import com.ruoyi.system.domain.TDigitalemployee;
import com.ruoyi.system.service.ITDigitalemployeeService;

/**
 * digitalemployeeService业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
@Service
public class TDigitalemployeeServiceImpl implements ITDigitalemployeeService 
{
    @Autowired
    private TDigitalemployeeMapper tDigitalemployeeMapper;

    /**
     * 根据年月查询digitalemployee列表
     * 
     * @param year 年份
     * @param month 月份
     * @return digitalemployee集合
     */
    @Override
    public List<TDigitalemployee> selectTDigitalemployeeListByYearMonth(TDigitalemployee tDigitalemployee)
    {
        return tDigitalemployeeMapper.selectTDigitalemployeeListByYearMonth(tDigitalemployee);
    }

    /**
     * 查询digitalemployee
     * 
     * @param id digitalemployee主键
     * @return digitalemployee
     */
    @Override
    public TDigitalemployee selectTDigitalemployeeById(String id)
    {
        return tDigitalemployeeMapper.selectTDigitalemployeeById(id);
    }

    /**
     * 查询digitalemployee列表
     * 
     * @param tDigitalemployee digitalemployee
     * @return digitalemployee
     */
    @Override
    public List<TDigitalemployee> selectTDigitalemployeeList(TDigitalemployee tDigitalemployee)
    {
        return tDigitalemployeeMapper.selectTDigitalemployeeList(tDigitalemployee);
    }

    /**
     * 新增digitalemployee
     * 
     * @param tDigitalemployee digitalemployee
     * @return 结果
     */
    @Override
    public int insertTDigitalemployee(TDigitalemployee tDigitalemployee)
    {
        tDigitalemployee.setId(UUID.randomUUID().toString());
        return tDigitalemployeeMapper.insertTDigitalemployee(tDigitalemployee);
    }

    /**
     * 修改digitalemployee
     * 
     * @param tDigitalemployee digitalemployee
     * @return 结果
     */
    @Override
    public int updateTDigitalemployee(TDigitalemployee tDigitalemployee)
    {
        return tDigitalemployeeMapper.updateTDigitalemployee(tDigitalemployee);
    }

    /**
     * 批量删除digitalemployee
     * 
     * @param ids 需要删除的digitalemployee主键
     * @return 结果
     */
    @Override
    public int deleteTDigitalemployeeByIds(String[] ids)
    {
        return tDigitalemployeeMapper.deleteTDigitalemployeeByIds(ids);
    }

    /**
     * 删除digitalemployee信息
     * 
     * @param id digitalemployee主键
     * @return 结果
     */
    @Override
    public int deleteTDigitalemployeeById(String id)
    {
        return tDigitalemployeeMapper.deleteTDigitalemployeeById(id);
    }
}
