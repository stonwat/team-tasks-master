package com.ruoyi.pms.controller;

import com.ruoyi.pms.common.ResponseResult;
import com.ruoyi.pms.dto.task.*;
import com.ruoyi.pms.service.TaskService;
import net.sf.mpxj.*;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.reader.UniversalProjectReader;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;




@RestController
@RequestMapping("/pms/task")
public class TaskController {


    @Autowired
    TaskService taskService;

    @PreAuthorize("@ss.hasPermi('pms:project:list')")
    @GetMapping("/list")
    @ResponseBody
    public ResponseResult<List<TaskListVm>> list(@ModelAttribute TaskListDm dm) throws Exception {
        List<TaskListVm> data = taskService.list(dm);
        return ResponseResult.success(data);
    }


    @PreAuthorize("@ss.hasPermi('pms:project:list')")
    @GetMapping("/getTaskBurnDownChartData")
    @ResponseBody
    public ResponseResult<TaskBurnDownChartDataVm> getTaskBurnDownChartData(@RequestParam String projectId) throws Exception {
        TaskBurnDownChartDataVm data = taskService.getTaskBurnDownChartData(projectId);
        return ResponseResult.success(data);
    }


    @PreAuthorize("@ss.hasPermi('pms:project:detail')")
    @GetMapping("/detail")
    @ResponseBody
    public ResponseResult<TaskListVm> detail(@RequestParam String id) {
        return ResponseResult.success(taskService.detail(id));
    }

    @PreAuthorize("@ss.hasPermi('pms:project:save')")
    @PostMapping("/save")
    @ResponseBody
    public ResponseResult<Void> save(@RequestBody List<TaskSaveDm> dm) throws Exception {
        taskService.save(dm);
        return ResponseResult.success();
    }



    @PreAuthorize("@ss.hasPermi('pms:project:detail')")
    @GetMapping("/export")
    @ResponseBody
    public ResponseEntity export(@RequestParam String projectId) throws Exception {
        Workbook workbook  = taskService.export(projectId);

        // 5. 将工作簿写入字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // 6. 设置响应头
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", "users.xlsx");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(outputStream.toByteArray());
    }



    @PreAuthorize("@ss.hasPermi('pms:project:save')")
    @PostMapping("/updateProgress")
    @ResponseBody
    public ResponseResult<Void> updateProgress(@RequestBody TaskUpdateProgressDm dm) throws Exception {
        taskService.updateProgress(dm);
        return ResponseResult.success();
    }

    @PreAuthorize("@ss.hasPermi('pms:project:save')")
    @PostMapping("/assignUser")
    @ResponseBody
    public ResponseResult<Void> assignUser(@RequestBody TaskAssignUserDm dm) throws Exception {
        taskService.assignUser(dm);
        return ResponseResult.success();
    }

    @PreAuthorize("@ss.hasPermi('pms:project:save')")
    @PostMapping("/updatesortnum")
    @ResponseBody
    public ResponseResult<Void> updateSortNum(@RequestBody List<TaskUpdateSortNumDm> dm) throws Exception {
        taskService.updateSortNum(dm);
//        TaskListDm dm1 = new TaskListDm();
//        dm1.setProjectId(dm.get(0).getProjectId());
//        List<TaskListVm> data = taskService.list(dm1);
        return ResponseResult.success();
    }


    @PreAuthorize("@ss.hasPermi('pms:project:delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult<Void> delete(@RequestBody List<String> ids) {
        taskService.delete(ids);
        return ResponseResult.success();
    }


    @PostConstruct
    public void test() {
        try {
            // 1. 使用 ProjectReader 自动识别文件类型（无需指定 MPPReader）
            ProjectReader reader = new UniversalProjectReader();
            ProjectFile project = reader.read("C:\\Users\\jiangjn\\Desktop\\PM\\TCIS\\CIS系统2.0.mpp");

            // 2. 打印项目元数据
            printProjectMetadata(project);

            // 3. 遍历所有任务（包括子任务）
            System.out.println("\n===== 任务层级结构 =====");
            project.getTasks().forEach(task -> printTask(task, 0));

//            // 6. 遍历项目日历
            printCalendar(project.getProjectProperties().getDefaultCalendar());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // ========== 辅助方法 ==========

    /**
     * 打印项目元数据
     */
    private static void printProjectMetadata(ProjectFile project) {
        ProjectProperties props = project.getProjectProperties();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("===== 项目属性 =====");
        System.out.println("标题: " + props.getProjectTitle());
        System.out.println("作者: " + props.getAuthor());
        System.out.println("开始日期: " + formatDateTime(props.getStartDate(), formatter));
        System.out.println("状态日期: " + formatDateTime(props.getStatusDate(), formatter));
        System.out.println("关键路径: " + props.getCategory());
    }

    /**
     * 递归打印任务层级
     */
    private static void printTask(Task task, int indentLevel) {
        String indent = generateIndent(indentLevel);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println(indent + "任务ID: " + task.getID());
        System.out.println(indent + "任务名称: " + task.getName());
        System.out.println(indent + "开始时间: " + formatDateTime(task.getStart(), formatter));
        System.out.println(indent + "结束时间: " + formatDateTime(task.getFinish(), formatter));
        System.out.println(indent + "工期: " + task.getDuration().toString());
        //进度
        System.out.println(indent + "进度: " + task.getPercentageComplete() + "%");
        //前置任务
//        System.out.println(indent + "前置任务: " + task.getPredecessors()
        for (Relation predecessor : task.getPredecessors()) {
            System.out.println(indent + "前置任务: " + predecessor.getPredecessorTask().getName());
            System.out.println(indent + "前置任务ID: " + predecessor.getPredecessorTask().getID());
        }

        //指派人
        if (task.getResourceAssignments() != null) {
            task.getResourceAssignments().forEach(assignment -> {
                if (assignment.getResource() != null) {
                    System.out.println(indent + "指派人: " + assignment.getResource().getName());
                }
            });
        }


        for (Task child : task.getChildTasks()) {
            printTask(child, indentLevel + 1);
        }
    }


    private static String generateIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    /**
     * 打印资源信息
     */
    private static void printResource(Resource resource) {
        System.out.println("资源名称: " + resource.getName());
        System.out.println("类型: " + (resource.getType() == ResourceType.WORK ? "工时" : "材料"));
        System.out.println("最大单位: " + resource.getMaxUnits());
        System.out.println("标准费率: " + resource.getStandardRate());
        System.out.println("------------------");
    }

    /**
     * 打印资源分配信息
     */
    private static void printAssignment(ResourceAssignment assignment) {
        Task task = assignment.getTask();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Resource resource = assignment.getResource();
        System.out.println("任务 [" + task.getName() + "] 分配给资源 [" + resource.getName() + "]");
        System.out.println("工作量: " + assignment.getWork() + " 小时");
        System.out.println("开始时间: " + formatDateTime(assignment.getStart(), formatter));
        System.out.println("完成时间: " + formatDateTime(assignment.getFinish(), formatter));
        System.out.println("------------------");
    }

    /** 打印日历信息 */
    private static void printCalendar(ProjectCalendar calendar) {


        System.out.println("日历名称: " + calendar.getName());



        //例外日期
        for (ProjectCalendarException calendarException : calendar.getCalendarExceptions()) {
            System.out.println(calendarException.getName());
        }


        //工作周
        for (DayType dayType : calendar.getCalendarDayTypes()) {
            // 打印日历信息
            System.out.println("日期: " + dayType.getValue());
            System.out.println(dayType.toString());
        }

        //工作日期
        for (ProjectCalendarWeek dayType : calendar.getWorkWeeks()) {
            // 打印日历信息
            System.out.println("日期: " + dayType.getName());
            System.out.println("日期开始: " + dayType.getDateRange().getStart());
            System.out.println("日期结束: " + dayType.getDateRange().getEnd());
        }


    }

    /**
     * 格式化时间（适配 Java 8 Time API）
     */
    private static String formatDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime != null ? dateTime.format(formatter) : "N/A";
    }


}
