package com.ruoyi.admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.admin.mapper.AdTeamShowcaseMapper;
import com.ruoyi.admin.domain.AdTeamShowcase;
import com.ruoyi.admin.service.IAdTeamShowcaseService;

/**
 * 团队风采Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
@Service
public class AdTeamShowcaseServiceImpl implements IAdTeamShowcaseService 
{
    @Autowired
    private AdTeamShowcaseMapper adTeamShowcaseMapper;

    /**
     * 查询团队风采
     * 
     * @param id 团队风采主键
     * @return 团队风采
     */
    @Override
    public AdTeamShowcase selectAdTeamShowcaseById(String id)
    {
        return adTeamShowcaseMapper.selectAdTeamShowcaseById(id);
    }

    /**
     * 查询团队风采列表
     * 
     * @param adTeamShowcase 团队风采
     * @return 团队风采
     */
    @Override
    public List<AdTeamShowcase> selectAdTeamShowcaseList(AdTeamShowcase adTeamShowcase)
    {
        return adTeamShowcaseMapper.selectAdTeamShowcaseList(adTeamShowcase);
    }

    /**
     * 新增团队风采
     * 
     * @param adTeamShowcase 团队风采
     * @return 结果
     */
    @Override
    public int insertAdTeamShowcase(AdTeamShowcase adTeamShowcase)
    {
        adTeamShowcase.setCreateTime(DateUtils.getNowDate());
        return adTeamShowcaseMapper.insertAdTeamShowcase(adTeamShowcase);
    }

    /**
     * 修改团队风采
     * 
     * @param adTeamShowcase 团队风采
     * @return 结果
     */
    @Override
    public int updateAdTeamShowcase(AdTeamShowcase adTeamShowcase)
    {
        adTeamShowcase.setUpdateTime(DateUtils.getNowDate());
        return adTeamShowcaseMapper.updateAdTeamShowcase(adTeamShowcase);
    }

    /**
     * 批量删除团队风采
     * 
     * @param ids 需要删除的团队风采主键
     * @return 结果
     */
    @Override
    public int deleteAdTeamShowcaseByIds(String[] ids)
    {
        return adTeamShowcaseMapper.deleteAdTeamShowcaseByIds(ids);
    }

    /**
     * 删除团队风采信息
     * 
     * @param id 团队风采主键
     * @return 结果
     */
    @Override
    public int deleteAdTeamShowcaseById(String id)
    {
        return adTeamShowcaseMapper.deleteAdTeamShowcaseById(id);
    }
}
