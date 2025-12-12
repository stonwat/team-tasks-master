package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TAttachmentMapper;
import com.ruoyi.system.domain.TAttachment;
import com.ruoyi.system.service.ITAttachmentService;

/**
 * 文件类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-11
 */
@Service
public class TAttachmentServiceImpl implements ITAttachmentService 
{
    @Autowired
    private TAttachmentMapper tAttachmentMapper;

    /**
     * 查询文件类型
     * 
     * @param id 文件类型主键
     * @return 文件类型
     */
    @Override
    public TAttachment selectTAttachmentById(String id)
    {
        return tAttachmentMapper.selectTAttachmentById(id);
    }

    /**
     * 查询文件类型列表
     * 
     * @param tAttachment 文件类型
     * @return 文件类型
     */
    @Override
    public List<TAttachment> selectTAttachmentList(TAttachment tAttachment)
    {
        return tAttachmentMapper.selectTAttachmentList(tAttachment);
    }

    /**
     * 新增文件类型
     * 
     * @param tAttachment 文件类型
     * @return 结果
     */
    @Override
    public int insertTAttachment(TAttachment tAttachment)
    {
        tAttachment.setId(IdUtils.fastSimpleUUID());
        tAttachment.setCreateTime(DateUtils.getNowDate());
        return tAttachmentMapper.insertTAttachment(tAttachment);
    }

    /**
     * 修改文件类型
     * 
     * @param tAttachment 文件类型
     * @return 结果
     */
    @Override
    public int updateTAttachment(TAttachment tAttachment)
    {
        return tAttachmentMapper.updateTAttachment(tAttachment);
    }

    /**
     * 批量删除文件类型
     * 
     * @param ids 需要删除的文件类型主键
     * @return 结果
     */
    @Override
    public int deleteTAttachmentByIds(String[] ids)
    {
        return tAttachmentMapper.deleteTAttachmentByIds(ids);
    }

    /**
     * 删除文件类型信息
     * 
     * @param id 文件类型主键
     * @return 结果
     */
    @Override
    public int deleteTAttachmentById(String id)
    {
        return tAttachmentMapper.deleteTAttachmentById(id);
    }
}
