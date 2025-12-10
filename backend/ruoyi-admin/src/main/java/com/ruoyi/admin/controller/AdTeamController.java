package com.ruoyi.admin.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.admin.domain.AdTeam;
import com.ruoyi.admin.service.IAdTeamService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 管理-团队成员Controller
 * 
 * @author ruoyi
 * @date 2025-11-19
 */
@RestController
@RequestMapping("/admin/team")
public class AdTeamController extends BaseController
{
    @Autowired
    private IAdTeamService adTeamService;

    /**
     * 查询管理-团队成员列表
     */
    @PreAuthorize("@ss.hasPermi('admin:team:list')")
    @GetMapping("/list")
    public TableDataInfo list(AdTeam adTeam)
    {
        startPage();
        List<AdTeam> list = adTeamService.selectAdTeamList(adTeam);
        return getDataTable(list);
    }

    /**
     * 导出管理-团队成员列表
     */
    @PreAuthorize("@ss.hasPermi('admin:team:export')")
    @Log(title = "管理-团队成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AdTeam adTeam)
    {
        List<AdTeam> list = adTeamService.selectAdTeamList(adTeam);
        ExcelUtil<AdTeam> util = new ExcelUtil<AdTeam>(AdTeam.class);
        util.exportExcel(response, list, "管理-团队成员数据");
    }

    /**
     * 获取管理-团队成员详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:team:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(adTeamService.selectAdTeamById(id));
    }

    /**
     * 新增管理-团队成员
     */
    @PreAuthorize("@ss.hasPermi('admin:team:add')")
    @Log(title = "管理-团队成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AdTeam adTeam)
    {
        return toAjax(adTeamService.insertAdTeam(adTeam));
    }

    /**
     * 修改管理-团队成员
     */
    @PreAuthorize("@ss.hasPermi('admin:team:edit')")
    @Log(title = "管理-团队成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AdTeam adTeam)
    {
        return toAjax(adTeamService.updateAdTeam(adTeam));
    }

    /**
     * 删除管理-团队成员
     */
    @PreAuthorize("@ss.hasPermi('admin:team:remove')")
    @Log(title = "管理-团队成员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(adTeamService.deleteAdTeamByIds(ids));
    }
}
