package com.ruoyi.admin.mapper;

import java.util.List;
import com.ruoyi.admin.domain.AdTeamShowcase;

/**
 * 团队风采Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
public interface AdTeamShowcaseMapper 
{
    /**
     * 查询团队风采
     * 
     * @param id 团队风采主键
     * @return 团队风采
     */
    public AdTeamShowcase selectAdTeamShowcaseById(String id);

    /**
     * 查询团队风采列表
     * 
     * @param adTeamShowcase 团队风采
     * @return 团队风采集合
     */
    public List<AdTeamShowcase> selectAdTeamShowcaseList(AdTeamShowcase adTeamShowcase);

    /**
     * 新增团队风采
     * 
     * @param adTeamShowcase 团队风采
     * @return 结果
     */
    public int insertAdTeamShowcase(AdTeamShowcase adTeamShowcase);

    /**
     * 修改团队风采
     * 
     * @param adTeamShowcase 团队风采
     * @return 结果
     */
    public int updateAdTeamShowcase(AdTeamShowcase adTeamShowcase);

    /**
     * 删除团队风采
     * 
     * @param id 团队风采主键
     * @return 结果
     */
    public int deleteAdTeamShowcaseById(String id);

    /**
     * 批量删除团队风采
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAdTeamShowcaseByIds(String[] ids);
}
