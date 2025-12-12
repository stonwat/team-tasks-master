package com.ruoyi.system.controller;

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
import com.ruoyi.system.domain.TAttachment;
import com.ruoyi.system.service.ITAttachmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文件类型Controller
 * 
 * @author ruoyi
 * @date 2025-12-11
 */
@RestController
@RequestMapping("/system/attachment")
public class TAttachmentController extends BaseController
{
    @Autowired
    private ITAttachmentService tAttachmentService;

    /**
     * 查询文件类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAttachment tAttachment)
    {
        startPage();
        List<TAttachment> list = tAttachmentService.selectTAttachmentList(tAttachment);
        return getDataTable(list);
    }

    /**
     * 导出文件类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:export')")
    @Log(title = "文件类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAttachment tAttachment)
    {
        List<TAttachment> list = tAttachmentService.selectTAttachmentList(tAttachment);
        ExcelUtil<TAttachment> util = new ExcelUtil<TAttachment>(TAttachment.class);
        util.exportExcel(response, list, "文件类型数据");
    }

    /**
     * 获取文件类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(tAttachmentService.selectTAttachmentById(id));
    }

    /**
     * 新增文件类型
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:add')")
    @Log(title = "文件类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAttachment tAttachment)
    {
        return toAjax(tAttachmentService.insertTAttachment(tAttachment));
    }

    /**
     * 修改文件类型
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:edit')")
    @Log(title = "文件类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAttachment tAttachment)
    {
        return toAjax(tAttachmentService.updateTAttachment(tAttachment));
    }

    /**
     * 删除文件类型
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:remove')")
    @Log(title = "文件类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(tAttachmentService.deleteTAttachmentByIds(ids));
    }
}
