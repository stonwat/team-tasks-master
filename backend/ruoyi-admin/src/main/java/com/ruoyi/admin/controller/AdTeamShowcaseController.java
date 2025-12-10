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
import com.ruoyi.admin.domain.AdTeamShowcase;
import com.ruoyi.admin.service.IAdTeamShowcaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 团队风采Controller
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
@RestController
@RequestMapping("/admin/showcase")
public class AdTeamShowcaseController extends BaseController
{
    @Autowired
    private IAdTeamShowcaseService adTeamShowcaseService;

    /**
     * 查询团队风采列表
     */
    @PreAuthorize("@ss.hasPermi('admin:showcase:list')")
    @GetMapping("/list")
    public TableDataInfo list(AdTeamShowcase adTeamShowcase)
    {
        startPage();
        List<AdTeamShowcase> list = adTeamShowcaseService.selectAdTeamShowcaseList(adTeamShowcase);
        return getDataTable(list);
    }

    /**
     * 导出团队风采列表
     */
    @PreAuthorize("@ss.hasPermi('admin:showcase:export')")
    @Log(title = "团队风采", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AdTeamShowcase adTeamShowcase)
    {
        List<AdTeamShowcase> list = adTeamShowcaseService.selectAdTeamShowcaseList(adTeamShowcase);
        ExcelUtil<AdTeamShowcase> util = new ExcelUtil<AdTeamShowcase>(AdTeamShowcase.class);
        util.exportExcel(response, list, "团队风采数据");
    }

    /**
     * 获取团队风采详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:showcase:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(adTeamShowcaseService.selectAdTeamShowcaseById(id));
    }

    /**
     * 新增团队风采
     */
    @PreAuthorize("@ss.hasPermi('admin:showcase:add')")
    @Log(title = "团队风采", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AdTeamShowcase adTeamShowcase)
    {
        return toAjax(adTeamShowcaseService.insertAdTeamShowcase(adTeamShowcase));
    }

    /**
     * 修改团队风采
     */
    @PreAuthorize("@ss.hasPermi('admin:showcase:edit')")
    @Log(title = "团队风采", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AdTeamShowcase adTeamShowcase)
    {
        return toAjax(adTeamShowcaseService.updateAdTeamShowcase(adTeamShowcase));
    }

    /**
     * 删除团队风采
     */
    @PreAuthorize("@ss.hasPermi('admin:showcase:remove')")
    @Log(title = "团队风采", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(adTeamShowcaseService.deleteAdTeamShowcaseByIds(ids));
    }
}
