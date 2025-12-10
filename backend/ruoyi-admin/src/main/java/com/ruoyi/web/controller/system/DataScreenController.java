package com.ruoyi.web.controller.system;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.http.HttpUtils;
// import com.ruoyi.system.domain.ResourceMonitorData;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 数据大屏Controller
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
@RestController
@RequestMapping("/system/datascreen")
public class DataScreenController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(DataScreenController.class);
    
    @Autowired
    private ISysConfigService configService;
    
    /**
     * 爬取资源监控数据
     */
    @GetMapping("/resource/monitor")
    public AjaxResult getResourceMonitorData(
            // @RequestParam String gmtCreateStart,
            // @RequestParam String gmtCreateEnd,
            // @RequestParam(defaultValue = "86400") String step,
            // @RequestParam String resourceType)
            @RequestParam() String gmtCreateStart,
            @RequestParam() String gmtCreateEnd,
            @RequestParam(defaultValue = "86400") String step,
            @RequestParam() String resourceType)
    {
        try {
            // 获取Cookie配置
            SysConfig config = new SysConfig();
            config.setConfigName("AI中台登录cookie");
            List<SysConfig> list = configService.selectConfigList(config);
            String Cookie = list.get(0).getConfigValue();
            
            // 对日期参数进行URL编码，确保格式为 2025-09-11%2008%3A43%3A45
            String encodedGmtCreateStart = URLEncoder.encode(gmtCreateStart, StandardCharsets.UTF_8.toString());
            String encodedGmtCreateEnd = URLEncoder.encode(gmtCreateEnd, StandardCharsets.UTF_8.toString());
            
            // 构建请求URL，使用编码后的日期参数
            String url = "http://136.192.111.150:30188/ai-platform-api/maasmgr/monitor/resource/views"
                    + "?gmtCreateStart=" + encodedGmtCreateStart
                    + "&gmtCreateEnd=" + encodedGmtCreateEnd
                    + "&step=" + step
                    + "&resourceType=" + resourceType;
            
            log.info("开始爬取资源监控数据，URL: {}", url);
            log.info("编码前日期参数 - gmtCreateStart: {}, gmtCreateEnd: {}", gmtCreateStart, gmtCreateEnd);
            log.info("编码后日期参数 - gmtCreateStart: {}, gmtCreateEnd: {}", encodedGmtCreateStart, encodedGmtCreateEnd);
            
            // 发送带Cookie的HTTP请求
            String response = HttpUtils.sendGetWithCookie(url, Cookie);
            
            if (response == null || response.isEmpty()) {
                log.error("获取资源监控数据失败：响应为空");
                return error("获取资源监控数据失败");
            }
            
            // 解析响应数据
            JSONObject jsonResponse = JSON.parseObject(response);
            Integer code = jsonResponse.getInteger("code");
            String msg = jsonResponse.getString("msg");
            Boolean success = jsonResponse.getBoolean("success");
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            
            if (code == null || code != 200 || !Boolean.TRUE.equals(success)) {
                log.error("资源监控API返回错误，code: {}, msg: {}", code, msg);
                return error("资源监控API返回错误: " + msg);
            }
            
            // 处理数据，将usedInfo层级展平
            List<Map<String, Object>> processedData = new ArrayList<>();
            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject item = dataArray.getJSONObject(i);
                    Map<String, Object> flattenedItem = new HashMap<>();
                    
                    // 添加基础字段
                    flattenedItem.put("date", item.getString("date"));
                    flattenedItem.put("weekDay", item.getString("weekDay"));
                    
                    // 展平usedInfo字段
                    JSONObject usedInfo = item.getJSONObject("usedInfo");
                    if (usedInfo != null) {
                        flattenedItem.put("usedRate", usedInfo.getDouble("usedRate"));
                        flattenedItem.put("usedResource", usedInfo.getDouble("usedResource"));
                        flattenedItem.put("totalResource", usedInfo.getDouble("totalResource"));
                        flattenedItem.put("unusedResource", usedInfo.getDouble("unusedResource"));
                        flattenedItem.put("requestResource", usedInfo.getDouble("requestResource"));
                        flattenedItem.put("unit", usedInfo.getString("unit"));
                        flattenedItem.put("memoryUnit", usedInfo.getString("memoryUnit"));
                        flattenedItem.put("memoryUsed", usedInfo.getInteger("memoryUsed"));
                        flattenedItem.put("memoryTotal", usedInfo.getInteger("memoryTotal"));
                        flattenedItem.put("memoryRest", usedInfo.getInteger("memoryRest"));
                    }
                    
                    processedData.add(flattenedItem);
                }
            }
            
            log.info("成功获取并处理资源监控数据，共{}条记录", processedData.size());
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("code", code);
            result.put("msg", msg);
            result.put("data", processedData);
            result.put("success", success);
            
            return success(result);
            
        } catch (Exception e) {
            log.error("爬取资源监控数据异常", e);
            return error("爬取资源监控数据异常: " + e.getMessage());
        }
    }


        /**
     * 爬取资源监控今日实时数据
     */
    @GetMapping("/resource/view")
    public AjaxResult getResourceMonitorView()
    {
        try {
            // 获取Cookie配置
            SysConfig config = new SysConfig();
            config.setConfigName("AI中台登录cookie");
            List<SysConfig> list = configService.selectConfigList(config);
            String Cookie = list.get(0).getConfigValue();
            
            // 构建请求URL，使用编码后的日期参数
            String url = "http://136.192.111.150:30188/ai-platform-api/maasmgr/monitor/views";
            
            log.info("开始爬取资源监控数据，URL: {}", url);
            
            // 发送带Cookie的HTTP请求
            String response = HttpUtils.sendGetWithCookie(url, Cookie);
            
            if (response == null || response.isEmpty()) {
                log.error("获取资源监控数据失败：响应为空");
                return error("获取资源监控数据失败");
            }
            
            // 解析响应数据
            JSONObject jsonResponse = JSON.parseObject(response);
            Integer code = jsonResponse.getInteger("code");
            String msg = jsonResponse.getString("msg");
            Boolean success = jsonResponse.getBoolean("success");
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            
            if (code == null || code != 200 || !Boolean.TRUE.equals(success)) {
                log.error("资源监控API返回错误，code: {}, msg: {}", code, msg);
                return error("资源监控API返回错误: " + msg);
            }

            
            
            log.info("成功获取并处理资源监控数据，共{}条记录", dataArray.size());
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("code", code);
            result.put("msg", msg);
            result.put("data", dataArray);
            result.put("success", success);
            
            return success(result);
            
        } catch (Exception e) {
            log.error("爬取资源监控数据异常", e);
            return error("爬取资源监控数据异常: " + e.getMessage());
        }
    }        /**
     * 爬取资源监控详情数据
     */
    @GetMapping("/cluster/view")
    public AjaxResult getResourceclusterView()
    {
        try {
            // 获取Cookie配置
            SysConfig config = new SysConfig();
            config.setConfigName("AI中台登录cookie");
            List<SysConfig> list = configService.selectConfigList(config);
            String Cookie = list.get(0).getConfigValue();
            
            // 构建请求URL，使用编码后的日期参数
            String url = "http://136.192.111.150:30188/ai-platform-api/maasmgr/monitor/cluster/views";
            
            log.info("开始爬取资源监控数据，URL: {}", url);
            
            // 发送带Cookie的HTTP请求
            String response = HttpUtils.sendGetWithCookie(url, Cookie);
            
            if (response == null || response.isEmpty()) {
                log.error("获取资源监控数据失败：响应为空");
                return error("获取资源监控数据失败");
            }
            
            // 解析响应数据
            JSONObject jsonResponse = JSON.parseObject(response);
            Integer code = jsonResponse.getInteger("code");
            String msg = jsonResponse.getString("msg");
            Boolean success = jsonResponse.getBoolean("success");
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            
            if (code == null || code != 200 || !Boolean.TRUE.equals(success)) {
                log.error("资源监控API返回错误，code: {}, msg: {}", code, msg);
                return error("资源监控API返回错误: " + msg);
            }

            
            
            log.info("成功获取并处理资源监控数据，共{}条记录", dataArray.size());
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("code", code);
            result.put("msg", msg);
            result.put("data", dataArray);
            result.put("success", success);
            
            return success(result);
            
        } catch (Exception e) {
            log.error("爬取资源监控数据异常", e);
            return error("爬取资源监控数据异常: " + e.getMessage());
        }
    }
    
    /**
     * 爬取模型集市详情数据
     */
    @GetMapping("/mode/view")
    public AjaxResult getResourcemodeView()
    {
        try {
            // 获取Cookie配置
            SysConfig config = new SysConfig();
            config.setConfigName("AI中台登录cookie");
            List<SysConfig> list = configService.selectConfigList(config);
            String Cookie = list.get(0).getConfigValue();
            
            // 构建请求URL，使用编码后的日期参数
            String url = "http://136.192.111.150:30188/ai-platform-api/maasmgr/maas-model/page?modelName=&groupId=&creatType=&supportTuning=false&hasDemoApp=false&releaseMode=&sortField=default&size=10&current=1";

            
            
            log.info("开始爬取资源监控数据，URL: {}", url);
            
            // 发送带Cookie的HTTP请求
            String response = HttpUtils.sendGetWithCookie(url, Cookie);
            
            if (response == null || response.isEmpty()) {
                log.error("获取资源监控数据失败：响应为空");
                return error("获取资源监控数据失败");
            }
            
            // 解析响应数据
            JSONObject jsonResponse = JSON.parseObject(response);
            Integer code = jsonResponse.getInteger("code");
            String msg = jsonResponse.getString("msg");
            Boolean success = jsonResponse.getBoolean("success");
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            
            if (code == null || code != 200 || !Boolean.TRUE.equals(success)) {
                log.error("资源监控API返回错误，code: {}, msg: {}", code, msg);
                return error("资源监控API返回错误: " + msg);
            }

            
            
            log.info("成功获取并处理资源监控数据，共{}条记录", dataArray.size());
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("code", code);
            result.put("msg", msg);
            result.put("data", dataArray);
            result.put("success", success);
            
            return success(result);
            
        } catch (Exception e) {
            log.error("爬取资源监控数据异常", e);
            return error("爬取资源监控数据异常: " + e.getMessage());
        }
    }

    /**
     * 爬取数据集集市详情数据
     */
    @GetMapping("/data/view")
    public AjaxResult getDatasetManagePage()
    {
        try {
            // 获取Cookie配置
            SysConfig config = new SysConfig();
            config.setConfigName("AI中台登录cookie");
            List<SysConfig> list = configService.selectConfigList(config);
            String Cookie = list.get(0).getConfigValue();
            
            // 构建请求URL
            String url = "http://136.192.111.150:30188/ai-platform-api/maasmgr/data/manage/page";
            

            // 构建固定的JSON请求体
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("current", 1);
            jsonBody.put("size", 10);
            jsonBody.put("datasetName", "");
            jsonBody.put("applyType", new ArrayList<>());
            jsonBody.put("datasetProperty", 1);
            jsonBody.put("createBySelf", false);
            jsonBody.put("sortField", "default");
            
            log.info("开始爬取数据集数据，URL: {}, 请求体: {}", url, jsonBody.toJSONString());
            
            // 发送带Cookie的POST请求
            String response = HttpUtils.sendPostWithCookie(url, jsonBody.toJSONString(), Cookie);
            
            if (response == null || response.isEmpty()) {
                log.error("获取数据集数据失败：响应为空");
                return error("获取数据集数据失败");
            }
            
            // 解析响应数据
            JSONObject jsonResponse = JSON.parseObject(response);
            Integer code = jsonResponse.getInteger("code");
            String msg = jsonResponse.getString("msg");
            Boolean success = jsonResponse.getBoolean("success");
            Object data = jsonResponse.get("data");
            
            if (code == null || code != 200 || !Boolean.TRUE.equals(success)) {
                log.error("数据集API返回错误，code: {}, msg: {}", code, msg);
                return error("数据集API返回错误: " + msg);
            }
            
            log.info("成功获取并处理数据集数据");
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("code", code);
            result.put("msg", msg);
            result.put("data", data);
            result.put("success", success);
            
            return success(result);
            
        } catch (Exception e) {
            log.error("爬取数据集数据异常", e);
            return error("爬取数据集数据异常: " + e.getMessage());
        }
    }
}