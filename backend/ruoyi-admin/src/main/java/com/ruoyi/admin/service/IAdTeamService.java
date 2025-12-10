package com.ruoyi.admin.service;

import java.util.List;
import com.ruoyi.admin.domain.AdTeam;

/**
 * 管理-团队成员Service接口
 * 
 * @author ruoyi
 * @date 2025-11-19
 */
public interface IAdTeamService 
{
    /**
     * 查询管理-团队成员
     * 
     * @param id 管理-团队成员主键
     * @return 管理-团队成员
     */
    public AdTeam selectAdTeamById(String id);

    /**
     * 查询管理-团队成员列表
     * 
     * @param adTeam 管理-团队成员
     * @return 管理-团队成员集合
     */
    public List<AdTeam> selectAdTeamList(AdTeam adTeam);

    /**
     * 新增管理-团队成员
     * 
     * @param adTeam 管理-团队成员
     * @return 结果
     */
    public int insertAdTeam(AdTeam adTeam);

    /**
     * 修改管理-团队成员
     * 
     * @param adTeam 管理-团队成员
     * @return 结果
     */
    public int updateAdTeam(AdTeam adTeam);

    /**
     * 批量删除管理-团队成员
     * 
     * @param ids 需要删除的管理-团队成员主键集合
     * @return 结果
     */
    public int deleteAdTeamByIds(String[] ids);

    /**
     * 删除管理-团队成员信息
     * 
     * @param id 管理-团队成员主键
     * @return 结果
     */
    public int deleteAdTeamById(String id);
}
