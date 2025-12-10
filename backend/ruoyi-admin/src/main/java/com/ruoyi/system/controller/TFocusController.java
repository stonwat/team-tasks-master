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
import com.ruoyi.system.domain.TFocus;
import com.ruoyi.system.service.ITFocusService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * focusController
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@RestController
@RequestMapping("/system/focus")
public class TFocusController extends BaseController
{
    @Autowired
    private ITFocusService tFocusService;

    /**
     * 查询focus列表
     */
//    @PreAuthorize("@ss.hasPermi('system:focus:list')")
    @GetMapping("/list")
    public TableDataInfo list(TFocus tFocus)
    {
        startPage();
        List<TFocus> list = tFocusService.selectTFocusList(tFocus);
        return getDataTable(list);
    }

    /**
     * 导出focus列表
     */
    @PreAuthorize("@ss.hasPermi('system:focus:export')")
    @Log(title = "focus", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TFocus tFocus)
    {
        List<TFocus> list = tFocusService.selectTFocusList(tFocus);
        ExcelUtil<TFocus> util = new ExcelUtil<TFocus>(TFocus.class);
        util.exportExcel(response, list, "focus数据");
    }

    /**
     * 获取focus详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:focus:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(tFocusService.selectTFocusById(id));
    }

    /**
     * 新增focus
     */
    @PreAuthorize("@ss.hasPermi('system:focus:add')")
    @Log(title = "focus", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TFocus tFocus)
    {
        return toAjax(tFocusService.insertTFocus(tFocus));
    }

    /**
     * 修改focus
     */
    @PreAuthorize("@ss.hasPermi('system:focus:edit')")
    @Log(title = "focus", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TFocus tFocus)
    {
        return toAjax(tFocusService.updateTFocus(tFocus));
    }

    /**
     * 删除focus
     */
    @PreAuthorize("@ss.hasPermi('system:focus:remove')")
    @Log(title = "focus", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(tFocusService.deleteTFocusByIds(ids));
    }
}
