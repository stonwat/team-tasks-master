package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TTrain;

/**
 * trainService接口
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
public interface ITTrainService 
{
    /**
     * 查询train
     * 
     * @param id train主键
     * @return train
     */
    public TTrain selectTTrainById(String id);

    /**
     * 查询train列表
     * 
     * @param tTrain train
     * @return train集合
     */
    public List<TTrain> selectTTrainList(TTrain tTrain);

    /**
     * 新增train
     * 
     * @param tTrain train
     * @return 结果
     */
    public int insertTTrain(TTrain tTrain);

    /**
     * 修改train
     * 
     * @param tTrain train
     * @return 结果
     */
    public int updateTTrain(TTrain tTrain);

    /**
     * 批量删除train
     * 
     * @param ids 需要删除的train主键集合
     * @return 结果
     */
    public int deleteTTrainByIds(String[] ids);

    /**
     * 删除train信息
     * 
     * @param id train主键
     * @return 结果
     */
    public int deleteTTrainById(String id);
}
