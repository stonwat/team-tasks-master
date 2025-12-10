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
import com.ruoyi.system.domain.TTrain;
import com.ruoyi.system.service.ITTrainService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * trainController
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
@RestController
@RequestMapping("/system/train")
public class TTrainController extends BaseController
{
    @Autowired
    private ITTrainService tTrainService;

    /**
     * 查询train列表
     */
//    @PreAuthorize("@ss.hasPermi('system:train:list')")
    @GetMapping("/list")
    public TableDataInfo list(TTrain tTrain)
    {
        startPage();
        List<TTrain> list = tTrainService.selectTTrainList(tTrain);
        return getDataTable(list);
    }

    /**
     * 导出train列表
     */
    @PreAuthorize("@ss.hasPermi('system:train:export')")
    @Log(title = "train", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TTrain tTrain)
    {
        List<TTrain> list = tTrainService.selectTTrainList(tTrain);
        ExcelUtil<TTrain> util = new ExcelUtil<TTrain>(TTrain.class);
        util.exportExcel(response, list, "train数据");
    }

    /**
     * 获取train详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:train:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(tTrainService.selectTTrainById(id));
    }

    /**
     * 新增train
     */
    @PreAuthorize("@ss.hasPermi('system:train:add')")
    @Log(title = "train", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTrain tTrain)
    {
        return toAjax(tTrainService.insertTTrain(tTrain));
    }

    /**
     * 修改train
     */
    @PreAuthorize("@ss.hasPermi('system:train:edit')")
    @Log(title = "train", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTrain tTrain)
    {
        return toAjax(tTrainService.updateTTrain(tTrain));
    }

    /**
     * 删除train
     */
    @PreAuthorize("@ss.hasPermi('system:train:remove')")
    @Log(title = "train", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(tTrainService.deleteTTrainByIds(ids));
    }
}
