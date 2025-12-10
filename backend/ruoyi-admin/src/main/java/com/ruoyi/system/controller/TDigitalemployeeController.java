package com.ruoyi.system.controller;

import java.util.*;
import org.apache.poi.xssf.usermodel.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.ITDigitalemployeeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import java.nio.charset.Charset;
import java.util.SortedMap;


/**
 * digitalemployeeController
 * 
 * @author ruoyi
 * @date 2025-10-28
 */
@RestController
@RequestMapping("/system/digitalemployee")
public class TDigitalemployeeController extends BaseController
{
    @Autowired
    private ITDigitalemployeeService tDigitalemployeeService;

    /**
     * 查询digitalemployee列表
     */
    @PreAuthorize("@ss.hasPermi('system:digitalemployee:list')")
    @GetMapping("/list")
    public TableDataInfo list(TDigitalemployee tDigitalemployee)
    {
        startPage();
        List<TDigitalemployee> list = tDigitalemployeeService.selectTDigitalemployeeList(tDigitalemployee);
        return getDataTable(list);
    }


    /**
     * 导出digitalemployee列表
     */
    @PreAuthorize("@ss.hasPermi('system:digitalemployee:export')")
    @Log(title = "digitalemployee", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TDigitalemployee tDigitalemployee)
    {
        System.out.println("-------------------------------- 导出接口接收到的employeeType: " + tDigitalemployee.getEmployeeType());
        List<TDigitalemployee> list = tDigitalemployeeService.selectTDigitalemployeeList(tDigitalemployee);
        

        switch (tDigitalemployee.getEmployeeType()) {
            case "1":
                System.out.println("-------------------------------- 使用装维数据导出");
                List<zhuangwei> zhuangweiList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                zhuangwei zw = new zhuangwei();
                zw.setId(employee.getId());
                zw.setOrderError1(employee.getOrderError1());
                zw.setOrderComplete1(employee.getOrderComplete1());
                zw.setOrderRank(employee.getOrderRank());
                zw.setOrderScore(employee.getOrderScore());
                zw.setOrderPersent(employee.getOrderPersent());
                zw.setEmployeeType(employee.getEmployeeType());
                zw.setDataTime(employee.getDataTime());
                zw.setRemark(employee.getRemark());
                zhuangweiList.add(zw);
                }
            // 转换完成后，统一导出zhuangwei类型的数据
                ExcelUtil<zhuangwei> util1 = new ExcelUtil<zhuangwei>(zhuangwei.class);
                util1.exportExcel(response, zhuangweiList, "装维调度与自服务数字员工数据");
                break;
        
            case "2":
                System.out.println("-------------------------------- 使用综维数据导出");
                List<zongwei> zongweiList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    zongwei zw = new zongwei();
                    zw.setId(employee.getId());
                    zw.setOrderError1(employee.getOrderError1());
                    zw.setOrderComplete1(employee.getOrderComplete1());
                    zw.setOrderRank(employee.getOrderRank());
                    zw.setOrderScore(employee.getOrderScore());
                    zw.setOrderPersent(employee.getOrderPersent());
                    zw.setEmployeeType(employee.getEmployeeType());
                    zw.setDataTime(employee.getDataTime());
                    zw.setRemark(employee.getRemark());
                    zongweiList.add(zw);
                }
                // 转换完成后，统一导出zongwei类型的数据
                ExcelUtil<zongwei> util2 = new ExcelUtil<zongwei>(zongwei.class);
                util2.exportExcel(response, zongweiList, "综维数字员工数据");
                break;
            case "3":
                System.out.println("-------------------------------- 使用隐患管控与故障处置数字员工数据导出");
                List<yinhuan> yinhuanList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    yinhuan yw = new yinhuan();
                    yw.setId(employee.getId());
                    yw.setOrderError1(employee.getOrderError1());
                    yw.setOrderComplete1(employee.getOrderComplete1());
                    yw.setOrderRank(employee.getOrderRank());
                    yw.setOrderScore(employee.getOrderScore());
                    yw.setOrderPersent(employee.getOrderPersent());
                    yw.setEmployeeType(employee.getEmployeeType());
                    yw.setDataTime(employee.getDataTime());
                    yw.setRemark(employee.getRemark());
                    yinhuanList.add(yw);
                }
                // 转换完成后，统一导出yinhuan类型的数据
                ExcelUtil<yinhuan> util3 = new ExcelUtil<yinhuan>(yinhuan.class);
                util3.exportExcel(response, yinhuanList, "隐患管控与故障处置数字员工数据");
                break;
            case "4":
                System.out.println("-------------------------------- 使用网优数据导出");
                List<wangyou> wangyouList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    wangyou wy = new wangyou();
                    wy.setId(employee.getId());
                    wy.setOrderError1(employee.getOrderError1());
                    wy.setOrderComplete1(employee.getOrderComplete1());
                    wy.setOrderRank(employee.getOrderRank());
                    wy.setOrderScore(employee.getOrderScore());
                    wy.setOrderPersent(employee.getOrderPersent());
                    wy.setEmployeeType(employee.getEmployeeType());
                    wy.setDataTime(employee.getDataTime());
                    wy.setRemark(employee.getRemark());
                    wangyouList.add(wy);
                }
                // 转换完成后，统一导出wangyou类型的数据
                ExcelUtil<wangyou> util4 = new ExcelUtil<wangyou>(wangyou.class);
                util4.exportExcel(response, wangyouList, "网优数字员工数据");
                break;
            case "5":
                System.out.println("-------------------------------- 使用客服助理智能体数据导出");
                List<kefu> kefuList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    kefu kf = new kefu();
                    kf.setId(employee.getId());
                    kf.setOrderError1(employee.getOrderError1());
                    kf.setOrderComplete1(employee.getOrderComplete1());
                    kf.setOrderError2(employee.getOrderError2());
                    kf.setOrderComplete2(employee.getOrderComplete2());
                    kf.setOrderError3(employee.getOrderError3());
                    kf.setOrderComplete3(employee.getOrderComplete3());
                    kf.setOrderError4(employee.getOrderError4());
                    kf.setOrderComplete4(employee.getOrderComplete4());
                    kf.setOrderError5(employee.getOrderError5());
                    kf.setOrderComplete5(employee.getOrderComplete5());
                    kf.setOrderRank(employee.getOrderRank());
                    kf.setOrderScore(employee.getOrderScore());
                    kf.setOrderPersent(employee.getOrderPersent());
                    kf.setEmployeeType(employee.getEmployeeType());
                    kf.setDataTime(employee.getDataTime());
                    kf.setRemark(employee.getRemark());
                    kefuList.add(kf);
                }
                // 转换完成后，统一导出kefu类型的数据
                ExcelUtil<kefu> util5 = new ExcelUtil<kefu>(kefu.class);
                util5.exportExcel(response, kefuList, "客服助理智能体数据");
                break;
            case "6":
                System.out.println("-------------------------------- 多模态客服机器人数据导出");
                List<duomotai> duomotaiList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    duomotai dt = new duomotai();
                    dt.setId(employee.getId());
                    dt.setOrderError1(employee.getOrderError1());
                    dt.setOrderComplete1(employee.getOrderComplete1());
                    dt.setOrderRank(employee.getOrderRank());
                    dt.setOrderScore(employee.getOrderScore());
                    dt.setOrderPersent(employee.getOrderPersent());
                    dt.setEmployeeType(employee.getEmployeeType());
                    dt.setDataTime(employee.getDataTime());
                    dt.setRemark(employee.getRemark());
                    duomotaiList.add(dt);
                }
                // 转换完成后，统一导出duomotai类型的数据
                ExcelUtil<duomotai> util6 = new ExcelUtil<duomotai>(duomotai.class);
                util6.exportExcel(response, duomotaiList, "多模态客服机器人数据");
                break;
            case "7":
                System.out.println("-------------------------------- 门店宝数据导出");
                List<mendianbao> mendianbaoList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    mendianbao mdb = new mendianbao();
                    mdb.setId(employee.getId());
                    mdb.setOrderError1(employee.getOrderError1());
                    mdb.setOrderComplete1(employee.getOrderComplete1());
                    mdb.setOrderRank(employee.getOrderRank());
                    mdb.setOrderScore(employee.getOrderScore());
                    mdb.setOrderPersent(employee.getOrderPersent());
                    mdb.setEmployeeType(employee.getEmployeeType());
                    mdb.setDataTime(employee.getDataTime());
                    mdb.setRemark(employee.getRemark());
                    mendianbaoList.add(mdb);
                }
                // 转换完成后，统一导出mendianbao类型的数据
                ExcelUtil<mendianbao> util7 = new ExcelUtil<mendianbao>(mendianbao.class);
                util7.exportExcel(response, mendianbaoList, "门店宝数据");
                break;
            case "8":
                System.out.println("-------------------------------- AI客户助理数据导出");
                List<kehu> kehuList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    kehu k = new kehu();
                    k.setId(employee.getId());
                    k.setOrderError1(employee.getOrderError1());
                    k.setOrderComplete1(employee.getOrderComplete1());
                    k.setOrderRank(employee.getOrderRank());
                    k.setOrderScore(employee.getOrderScore());
                    k.setOrderPersent(employee.getOrderPersent());
                    k.setEmployeeType(employee.getEmployeeType());
                    k.setDataTime(employee.getDataTime());
                    k.setRemark(employee.getRemark());
                    kehuList.add(k);
                }
                // 转换完成后，统一导出kehu类型的数据
                ExcelUtil<kehu> util8 = new ExcelUtil<kehu>(kehu.class);
                util8.exportExcel(response, kehuList, "AI客户助理数据");
                break;
            case "9":
                System.out.println("-------------------------------- 精准营销智能体数据导出");
                List<yingxiao> yingxiaoList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    yingxiao yx = new yingxiao();
                    yx.setId(employee.getId());
                    yx.setOrderError1(employee.getOrderError1());
                    yx.setOrderComplete1(employee.getOrderComplete1());
                    yx.setOrderError2(employee.getOrderError2());
                    yx.setOrderComplete2(employee.getOrderComplete2());
                    yx.setOrderRank(employee.getOrderRank());
                    yx.setOrderScore(employee.getOrderScore());
                    yx.setOrderPersent(employee.getOrderPersent());
                    yx.setEmployeeType(employee.getEmployeeType());
                    yx.setDataTime(employee.getDataTime());
                    yx.setRemark(employee.getRemark());
                    yingxiaoList.add(yx);
                }
                // 转换完成后，统一导出yingxiao类型的数据
                ExcelUtil<yingxiao> util9 = new ExcelUtil<yingxiao>(yingxiao.class);
                util9.exportExcel(response, yingxiaoList, "精准营销智能体数据");
                break;
            case "10":
                System.out.println("-------------------------------- 产数支撑智能体数据导出");
                List<chanshu> chanshuList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    chanshu cs = new chanshu();
                    cs.setId(employee.getId());
                    cs.setOrderError1(employee.getOrderError1());
                    cs.setOrderComplete1(employee.getOrderComplete1());
                    cs.setOrderError2(employee.getOrderError2());
                    cs.setOrderComplete2(employee.getOrderComplete2());
                    cs.setOrderRank(employee.getOrderRank());
                    cs.setOrderScore(employee.getOrderScore());
                    cs.setOrderPersent(employee.getOrderPersent());
                    cs.setEmployeeType(employee.getEmployeeType());
                    cs.setDataTime(employee.getDataTime());
                    cs.setRemark(employee.getRemark());
                    chanshuList.add(cs);
                }
                // 转换完成后，统一导出chanshu类型的数据
                ExcelUtil<chanshu> util10 = new ExcelUtil<chanshu>(chanshu.class);
                util10.exportExcel(response, chanshuList, "产数支撑智能体数据");
                break;
            case "11":
                System.out.println("-------------------------------- 研发智能体数据导出");
                List<yanfa> yanfaList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    yanfa yf = new yanfa();
                    yf.setId(employee.getId());
                    yf.setOrderError1(employee.getOrderError1());
                    yf.setOrderComplete1(employee.getOrderComplete1());
                    yf.setOrderRank(employee.getOrderRank());
                    yf.setOrderScore(employee.getOrderScore());
                    yf.setOrderPersent(employee.getOrderPersent());
                    yf.setEmployeeType(employee.getEmployeeType());
                    yf.setDataTime(employee.getDataTime());
                    yf.setRemark(employee.getRemark());
                    yanfaList.add(yf);
                }
                // 转换完成后，统一导出yanfa类型的数据
                ExcelUtil<yanfa> util11 = new ExcelUtil<yanfa>(yanfa.class);
                util11.exportExcel(response, yanfaList, "研发智能体数据");
                break;
            case "12":
                System.out.println("-------------------------------- 网安研判智能体数据导出");
                List<wangan> wanganList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    wangan wg = new wangan();
                    wg.setId(employee.getId());
                    wg.setOrderError1(employee.getOrderError1());
                    wg.setOrderComplete1(employee.getOrderComplete1());
                    wg.setOrderRank(employee.getOrderRank());
                    wg.setOrderScore(employee.getOrderScore());
                    wg.setOrderPersent(employee.getOrderPersent());
                    wg.setEmployeeType(employee.getEmployeeType());
                    wg.setDataTime(employee.getDataTime());
                    wg.setRemark(employee.getRemark());
                    wanganList.add(wg);
                }
                // 转换完成后，统一导出wangan类型的数据
                ExcelUtil<wangan> util12 = new ExcelUtil<wangan>(wangan.class);
                util12.exportExcel(response, wanganList, "网安研判智能体数据");
                break;
            case "13":
                System.out.println("-------------------------------- 采供智能体数据导出");
                List<caigong> caigongList = new ArrayList<>();
                for (TDigitalemployee employee : list) {
                    caigong cg = new caigong();
                    cg.setId(employee.getId());
                    cg.setOrderError1(employee.getOrderError1());
                    cg.setOrderComplete1(employee.getOrderComplete1());
                    cg.setOrderRank(employee.getOrderRank());
                    cg.setOrderScore(employee.getOrderScore());
                    cg.setOrderPersent(employee.getOrderPersent());
                    cg.setEmployeeType(employee.getEmployeeType());
                    cg.setDataTime(employee.getDataTime());
                    cg.setRemark(employee.getRemark());
                    caigongList.add(cg);
                }
                // 转换完成后，统一导出caigong类型的数据
                ExcelUtil<caigong> util13 = new ExcelUtil<caigong>(caigong.class);
                util13.exportExcel(response, caigongList, "采供智能体数据");
                break;
            default:
                ExcelUtil<TDigitalemployee> util = new ExcelUtil<TDigitalemployee>(TDigitalemployee.class);
                util.exportExcel(response, list, "digitalemployee数据");
                break;
        }
        // 根据employeeType值决定导出类型和文件名
        // if("1".equals(tDigitalemployee.getEmployeeType())){
        //     System.out.println("-------------------------------- 使用装维数据导出");
        //     List<zhuangwei> zhuangweiList = new ArrayList<>();
        //     for (TDigitalemployee employee : list) {
        //         zhuangwei zw = new zhuangwei();
        //         zw.setId(employee.getId());
        //         zw.setOrderError1(employee.getOrderError1());
        //         zw.setOrderComplete1(employee.getOrderComplete1());
        //         zw.setOrderRank(employee.getOrderRank());
        //         zw.setOrderScore(employee.getOrderScore());
        //         zw.setOrderPersent(employee.getOrderPersent());
        //         zw.setEmployeeType(employee.getEmployeeType());
        //         zw.setDataTime(employee.getDataTime());
        //         zw.setRemark(employee.getRemark());
        //         zhuangweiList.add(zw);
        //     }
        //     // 转换完成后，统一导出zhuangwei类型的数据
        //     ExcelUtil<zhuangwei> util = new ExcelUtil<zhuangwei>(zhuangwei.class);
        //     util.exportExcel(response, zhuangweiList, "装维数据");
            
        // }

        // else{
        //     System.out.println("-------------------------------- 使用digitalemployee数据导出");
        //     // 其他类型使用原始TDigitalemployee类型导出
        //     ExcelUtil<TDigitalemployee> util = new ExcelUtil<TDigitalemployee>(TDigitalemployee.class);
        //     util.exportExcel(response, list, "digitalemployee数据");
        // }
        // System.out.println("-------------------------------- 导出完成");
    }

    /**
     * 获取digitalemployee详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:digitalemployee:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(tDigitalemployeeService.selectTDigitalemployeeById(id));
    }

    /**
     * 新增digitalemployee
     */
    @PreAuthorize("@ss.hasPermi('system:digitalemployee:add')")
    @Log(title = "digitalemployee", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TDigitalemployee tDigitalemployee)
    {
        return toAjax(tDigitalemployeeService.insertTDigitalemployee(tDigitalemployee));
    }

    /**
     * 修改digitalemployee
     */
    @PreAuthorize("@ss.hasPermi('system:digitalemployee:edit')")
    @Log(title = "digitalemployee", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TDigitalemployee tDigitalemployee)
    {
        return toAjax(tDigitalemployeeService.updateTDigitalemployee(tDigitalemployee));
    }

    /**
     * 删除digitalemployee
     */
    @PreAuthorize("@ss.hasPermi('system:digitalemployee:remove')")
    @Log(title = "digitalemployee", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(tDigitalemployeeService.deleteTDigitalemployeeByIds(ids));
    }


    /**
     * 根据年月查询digitalemployee列表
     */
    // @PreAuthorize("@ss.hasPermi('system:digitalemployee:query')")
    @GetMapping("/listByYearMonth")
    public TableDataInfo listByYearMonth(TDigitalemployee tDigitalemployee)
    {
        startPage();
        List<TDigitalemployee> list = tDigitalemployeeService.selectTDigitalemployeeListByYearMonth(tDigitalemployee);
        return getDataTable(list);
    }
}
