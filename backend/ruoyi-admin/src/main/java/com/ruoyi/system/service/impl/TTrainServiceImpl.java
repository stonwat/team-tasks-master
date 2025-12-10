package com.ruoyi.system.service.impl;
import java.util.UUID;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TTrainMapper;
import com.ruoyi.system.domain.TTrain;
import com.ruoyi.system.service.ITTrainService;

/**
 * trainService业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
@Service
public class TTrainServiceImpl implements ITTrainService 
{
    @Autowired
    private TTrainMapper tTrainMapper;

    /**
     * 查询train
     * 
     * @param id train主键
     * @return train
     */
    @Override
    public TTrain selectTTrainById(String id)
    {
        return tTrainMapper.selectTTrainById(id);
    }

    /**
     * 查询train列表
     * 
     * @param tTrain train
     * @return train
     */
    @Override
    public List<TTrain> selectTTrainList(TTrain tTrain)
    {
        return tTrainMapper.selectTTrainList(tTrain);
    }

    /**
     * 新增train
     * 
     * @param tTrain train
     * @return 结果
     */
    @Override
    public int insertTTrain(TTrain tTrain)
    {
        tTrain.setId(UUID.randomUUID().toString());
        return tTrainMapper.insertTTrain(tTrain);
    }

    /**
     * 修改train
     * 
     * @param tTrain train
     * @return 结果
     */
    @Override
    public int updateTTrain(TTrain tTrain)
    {
        return tTrainMapper.updateTTrain(tTrain);
    }

    /**
     * 批量删除train
     * 
     * @param ids 需要删除的train主键
     * @return 结果
     */
    @Override
    public int deleteTTrainByIds(String[] ids)
    {
        return tTrainMapper.deleteTTrainByIds(ids);
    }

    /**
     * 删除train信息
     * 
     * @param id train主键
     * @return 结果
     */
    @Override
    public int deleteTTrainById(String id)
    {
        return tTrainMapper.deleteTTrainById(id);
    }
}
