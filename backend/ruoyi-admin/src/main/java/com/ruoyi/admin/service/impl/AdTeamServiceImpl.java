package com.ruoyi.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.admin.mapper.AdTeamMapper;
import com.ruoyi.admin.domain.AdTeam;
import com.ruoyi.admin.service.IAdTeamService;

/**
 * 管理-团队成员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-19
 */
@Service
public class AdTeamServiceImpl implements IAdTeamService 
{
    @Autowired
    private AdTeamMapper adTeamMapper;

    /**
     * 查询管理-团队成员
     * 
     * @param id 管理-团队成员主键
     * @return 管理-团队成员
     */
    @Override
    public AdTeam selectAdTeamById(String id)
    {
        return adTeamMapper.selectAdTeamById(id);
    }

    /**
     * 查询管理-团队成员列表
     * 
     * @param adTeam 管理-团队成员
     * @return 管理-团队成员
     */
    @Override
    public List<AdTeam> selectAdTeamList(AdTeam adTeam)
    {
        return adTeamMapper.selectAdTeamList(adTeam);
    }

    /**
     * 新增管理-团队成员
     * 
     * @param adTeam 管理-团队成员
     * @return 结果
     */
    @Override
    public int insertAdTeam(AdTeam adTeam)
    {
        return adTeamMapper.insertAdTeam(adTeam);
    }

    /**
     * 修改管理-团队成员
     * 
     * @param adTeam 管理-团队成员
     * @return 结果
     */
    @Override
    public int updateAdTeam(AdTeam adTeam)
    {
        return adTeamMapper.updateAdTeam(adTeam);
    }

    /**
     * 批量删除管理-团队成员
     * 
     * @param ids 需要删除的管理-团队成员主键
     * @return 结果
     */
    @Override
    public int deleteAdTeamByIds(String[] ids)
    {
        return adTeamMapper.deleteAdTeamByIds(ids);
    }

    /**
     * 删除管理-团队成员信息
     * 
     * @param id 管理-团队成员主键
     * @return 结果
     */
    @Override
    public int deleteAdTeamById(String id)
    {
        return adTeamMapper.deleteAdTeamById(id);
    }
}
