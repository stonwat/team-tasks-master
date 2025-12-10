package com.ruoyi.system.service.impl;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TFocusMapper;
import com.ruoyi.system.domain.TFocus;
import com.ruoyi.system.service.ITFocusService;

/**
 * focusService业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@Service
public class TFocusServiceImpl implements ITFocusService 
{
    @Autowired
    private TFocusMapper tFocusMapper;

    /**
     * 查询focus
     * 
     * @param id focus主键
     * @return focus
     */
    @Override
    public TFocus selectTFocusById(String id)
    {
        return tFocusMapper.selectTFocusById(id);
    }

    /**
     * 查询focus列表
     * 
     * @param tFocus focus
     * @return focus
     */
    @Override
    public List<TFocus> selectTFocusList(TFocus tFocus)
    {
        return tFocusMapper.selectTFocusList(tFocus);
    }

    /**
     * 新增focus
     * 
     * @param tFocus focus
     * @return 结果
     */
    @Override
    public int insertTFocus(TFocus tFocus)
    {
                tFocus.setId(UUID.randomUUID().toString());
        return tFocusMapper.insertTFocus(tFocus);
    }

    /**
     * 修改focus
     * 
     * @param tFocus focus
     * @return 结果
     */
    @Override
    public int updateTFocus(TFocus tFocus)
    {
        return tFocusMapper.updateTFocus(tFocus);
    }

    /**
     * 批量删除focus
     * 
     * @param ids 需要删除的focus主键
     * @return 结果
     */
    @Override
    public int deleteTFocusByIds(String[] ids)
    {
        return tFocusMapper.deleteTFocusByIds(ids);
    }

    /**
     * 删除focus信息
     * 
     * @param id focus主键
     * @return 结果
     */
    @Override
    public int deleteTFocusById(String id)
    {
        return tFocusMapper.deleteTFocusById(id);
    }
}
