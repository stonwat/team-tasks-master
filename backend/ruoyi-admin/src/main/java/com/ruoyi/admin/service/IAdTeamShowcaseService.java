package com.ruoyi.admin.service;

import java.util.List;
import com.ruoyi.admin.domain.AdTeamShowcase;

/**
 * 团队风采Service接口
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
public interface IAdTeamShowcaseService 
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
     * 批量删除团队风采
     * 
     * @param ids 需要删除的团队风采主键集合
     * @return 结果
     */
    public int deleteAdTeamShowcaseByIds(String[] ids);

    /**
     * 删除团队风采信息
     * 
     * @param id 团队风采主键
     * @return 结果
     */
    public int deleteAdTeamShowcaseById(String id);
}
