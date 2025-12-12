package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TAttachment;

/**
 * 文件类型Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-11
 */
public interface TAttachmentMapper 
{
    /**
     * 查询文件类型
     * 
     * @param id 文件类型主键
     * @return 文件类型
     */
    public TAttachment selectTAttachmentById(Long id);

    /**
     * 查询文件类型列表
     * 
     * @param tAttachment 文件类型
     * @return 文件类型集合
     */
    public List<TAttachment> selectTAttachmentList(TAttachment tAttachment);

    /**
     * 新增文件类型
     * 
     * @param tAttachment 文件类型
     * @return 结果
     */
    public int insertTAttachment(TAttachment tAttachment);

    /**
     * 修改文件类型
     * 
     * @param tAttachment 文件类型
     * @return 结果
     */
    public int updateTAttachment(TAttachment tAttachment);

    /**
     * 删除文件类型
     * 
     * @param id 文件类型主键
     * @return 结果
     */
    public int deleteTAttachmentById(Long id);

    /**
     * 批量删除文件类型
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAttachmentByIds(Long[] ids);
}
