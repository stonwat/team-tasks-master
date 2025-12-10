package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TListMapper;
import com.ruoyi.system.domain.TList;
import com.ruoyi.system.service.ITListService;

/**
 * 应用目标清单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@Service
public class TListServiceImpl implements ITListService 
{
    @Autowired
    private TListMapper tListMapper;

    /**
     * 查询应用目标清单
     * 
     * @param id 应用目标清单主键
     * @return 应用目标清单
     */
    @Override
    public TList selectTListById(Long id)
    {
        return tListMapper.selectTListById(id);
    }

    /**
     * 查询应用目标清单列表
     * 
     * @param tList 应用目标清单
     * @return 应用目标清单
     */
    @Override
    public List<TList> selectTListList(TList tList)
    {
        return tListMapper.selectTListList(tList);
    }

    /**
     * 新增应用目标清单
     * 
     * @param tList 应用目标清单
     * @return 结果
     */
    @Override
    public int insertTList(TList tList)
    {
        return tListMapper.insertTList(tList);
    }

    /**
     * 修改应用目标清单
     * 
     * @param tList 应用目标清单
     * @return 结果
     */
    @Override
    public int updateTList(TList tList)
    {
        return tListMapper.updateTList(tList);
    }

    /**
     * 批量删除应用目标清单
     * 
     * @param ids 需要删除的应用目标清单主键
     * @return 结果
     */
    @Override
    public int deleteTListByIds(Long[] ids)
    {
        return tListMapper.deleteTListByIds(ids);
    }

    /**
     * 删除应用目标清单信息
     * 
     * @param id 应用目标清单主键
     * @return 结果
     */
    @Override
    public int deleteTListById(Long id)
    {
        return tListMapper.deleteTListById(id);
    }
}
