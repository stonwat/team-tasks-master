package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TFocus;

/**
 * focusService接口
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public interface ITFocusService 
{
    /**
     * 查询focus
     * 
     * @param id focus主键
     * @return focus
     */
    public TFocus selectTFocusById(String id);

    /**
     * 查询focus列表
     * 
     * @param tFocus focus
     * @return focus集合
     */
    public List<TFocus> selectTFocusList(TFocus tFocus);

    /**
     * 新增focus
     * 
     * @param tFocus focus
     * @return 结果
     */
    public int insertTFocus(TFocus tFocus);

    /**
     * 修改focus
     * 
     * @param tFocus focus
     * @return 结果
     */
    public int updateTFocus(TFocus tFocus);

    /**
     * 批量删除focus
     * 
     * @param ids 需要删除的focus主键集合
     * @return 结果
     */
    public int deleteTFocusByIds(String[] ids);

    /**
     * 删除focus信息
     * 
     * @param id focus主键
     * @return 结果
     */
    public int deleteTFocusById(String id);
}
