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
import com.ruoyi.system.domain.TList;
import com.ruoyi.system.service.ITListService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 应用目标清单Controller
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/system/list")
public class TListController extends BaseController
{
    @Autowired
    private ITListService tListService;

    /**
     * 查询应用目标清单列表
     */
//    @PreAuthorize("@ss.hasPermi('system:list:list')")
    @GetMapping("/list")
    public TableDataInfo list(TList tList)
    {
        startPage();
        List<TList> list = tListService.selectTListList(tList);
        return getDataTable(list);
    }

    /**
     * 导出应用目标清单列表
     */
    @PreAuthorize("@ss.hasPermi('system:list:export')")
    @Log(title = "应用目标清单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TList tList)
    {
        List<TList> list = tListService.selectTListList(tList);
        ExcelUtil<TList> util = new ExcelUtil<TList>(TList.class);
        util.exportExcel(response, list, "应用目标清单数据");
    }

    /**
     * 获取应用目标清单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:list:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tListService.selectTListById(id));
    }

    /**
     * 新增应用目标清单
     */
    @PreAuthorize("@ss.hasPermi('system:list:add')")
    @Log(title = "应用目标清单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TList tList)
    {
        return toAjax(tListService.insertTList(tList));
    }

    /**
     * 修改应用目标清单
     */
    @PreAuthorize("@ss.hasPermi('system:list:edit')")
    @Log(title = "应用目标清单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TList tList)
    {
        return toAjax(tListService.updateTList(tList));
    }

    /**
     * 删除应用目标清单
     */
    @PreAuthorize("@ss.hasPermi('system:list:remove')")
    @Log(title = "应用目标清单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tListService.deleteTListByIds(ids));
    }
}
