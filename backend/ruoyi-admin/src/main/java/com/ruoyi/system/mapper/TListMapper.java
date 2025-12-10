package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TList;

/**
 * 应用目标清单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public interface TListMapper 
{
    /**
     * 查询应用目标清单
     * 
     * @param id 应用目标清单主键
     * @return 应用目标清单
     */
    public TList selectTListById(Long id);

    /**
     * 查询应用目标清单列表
     * 
     * @param tList 应用目标清单
     * @return 应用目标清单集合
     */
    public List<TList> selectTListList(TList tList);

    /**
     * 新增应用目标清单
     * 
     * @param tList 应用目标清单
     * @return 结果
     */
    public int insertTList(TList tList);

    /**
     * 修改应用目标清单
     * 
     * @param tList 应用目标清单
     * @return 结果
     */
    public int updateTList(TList tList);

    /**
     * 删除应用目标清单
     * 
     * @param id 应用目标清单主键
     * @return 结果
     */
    public int deleteTListById(Long id);

    /**
     * 批量删除应用目标清单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTListByIds(Long[] ids);
}
