package com.ruoyi.pms.service.imp;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.pms.domain.*;
import com.ruoyi.pms.dto.calendar.ProjectCalendarInfoVm;
import com.ruoyi.pms.dto.calendar.WorkTimeListVm;
import com.ruoyi.pms.dto.resources.ProjectListResourcesVm;
import com.ruoyi.pms.dto.task.*;
import com.ruoyi.pms.mapper.*;
import com.ruoyi.pms.service.CalendarService;
import com.ruoyi.pms.service.DynamicsService;
import com.ruoyi.pms.service.ProjectService;
import com.ruoyi.pms.service.TaskService;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Task;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.function.Consumer;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImp implements TaskService {
    @Resource
    TaskMapper taskMapper;
    @Resource
    TaskCollaboratorMapper taskCollaboratorMapper;

    @Autowired
    CalendarService calendarService;


    @Resource
    ProjectMapper projectMapper;
    @Resource
    ProjectProgressMapper projectProgressMapper;

    @Resource
    ProjectProgressDailyMapper projectProgressDailyMapper;

    @Autowired
    DynamicsService dynamicsService;

    @Resource
    ProjectResourcesMapper projectResourcesMapper;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<TaskListVm> list(TaskListDm dm) throws Exception {
        List<TaskListVm> taskListVmList = taskMapper.listTask(dm.getProjectId(), null);

        return taskListVmList;
//        HashMap<String, Object> map = listForData(dm);
//        return (List<TaskListVm>) map.get("tree");
    }

    private HashMap<String, Object> listForData(TaskListDm dm) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();

        LambdaQueryWrapper<ProjectTask> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ProjectTask::getProjectId, dm.getProjectId());
        //列表格式
        List<TaskListVm> taskListVmList = taskMapper.listTask(dm.getProjectId(), null);

        ProjectCalendarInfoVm projectCalendarInfoVm = calendarService.getProjectCalendarInfo(dm.getProjectId());

        //数树格式Pid
        List<TaskListVm> pidTreeList = listToPidTree(taskListVmList);

        //深度排序
        List<TaskListVm> finalTaskListVmList = deepSort(pidTreeList);

        //前置任务ID
        List<TaskListVm> predecessorIdTree = listToPredecessorIdTree(taskListVmList);

        //叶节点路径
        List<List<TaskListVm>> leafPaths = getLeafPaths(predecessorIdTree);

        HashMap<String, TaskListVm> map = new HashMap<>();
        for (TaskListVm item : taskListVmList) {
            map.put(item.getId(), item);
        }
        //设置前置任务编号
        taskListVmList.forEach(item -> {
            if (StringUtils.hasText(item.getPredecessorId())) {
                TaskListVm predecessorTask = map.get(item.getPredecessorId());
                item.setPredecessor((finalTaskListVmList.indexOf(predecessorTask) + 1) + "");
            } else {
                item.setPredecessor("");
            }
        });

        //设置排序号
        finalTaskListVmList.forEach(taskListVm -> {
            taskListVm.setSortNum((double) finalTaskListVmList.indexOf(taskListVm));
        });

        //计算叶节点日期
        //calculateLeafNodeDate(leafPaths, projectCalendarInfoVm);

        //计算枝节点的日期及工时
        TaskListVm root = new TaskListVm();
        root.setChildren(pidTreeList);
        calculateBranchNodeDate(root);

        //计算叶节点日期
        //calculateLeafNodeDate(leafPaths, projectCalendarInfoVm);


        taskListVmList.forEach(taskListVm -> {
            taskListVm.getChild().clear();
        });

        resultMap.put("tree", pidTreeList);
        resultMap.put("list", taskListVmList);
        resultMap.put("project_progress", root.getProgress());
        resultMap.put("project_duration", root.getDuration());
        resultMap.put("start_date", root.getStartDate());
        resultMap.put("end_date", root.getEndDate());
        return resultMap;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateProjectProgress(String projectId) throws Exception {
//        ProjectProgress projectProgress = saveProjectProgress(projectId);
//
//        List<TaskListVm> taskListVmList = taskMapper.listTask(dm.getProjectId(), null);
//
//        TaskListDm dm = new TaskListDm();
//        dm.setProjectId(projectId);
//        HashMap<String, Object> map = listForData(dm);
//        projectProgress.setProgress((int) map.get("project_progress"));
//        projectProgress.setDuration((Double) map.get("project_duration"));
//        projectProgress.setStartDate((Date) map.get("start_date"));
//        projectProgress.setEndDate((Date) map.get("end_date"));
//        projectProgress.setProgressUpdateTime(new Date());
//        projectProgressMapper.updateById(projectProgress);
//
//
////        List<TaskListVm> taskListVmList = (List<TaskListVm>) map.get("list");
////        List<TaskUpdatePlanDm> tasks = new ArrayList<>();
////        taskListVmList.forEach(taskListVm -> {
////            TaskUpdatePlanDm task = new TaskUpdatePlanDm();
////            task.setId(taskListVm.getId());
////            task.setProgress(taskListVm.getProgress());
////            task.setStartDate(dateFormat.format(taskListVm.getStartDate()));
////            task.setEndDate(dateFormat.format(taskListVm.getEndDate()));
////            task.setDuration(taskListVm.getDuration());
////            tasks.add(task);
////        });
////        taskMapper.batchUpdate(tasks);
//
//        long count = taskListVmList.stream().filter(taskListVm -> taskListVm.getChildren().isEmpty() && taskListVm.getProgress() == 100).count();
//
//        saveProjectProgressDaily(projectId, projectProgress.getProgress(), (int) count);
//
//
//        dynamicsService.save(projectId, "更新了项目进度,当前项目进度为" + projectProgress.getProgress() + "%");
//
//
//    }

    private void saveProjectProgressDaily(String projectId, int progress, int quota) {

        // 获取今天的日期
        LocalDate today = LocalDate.now();

        //将LocalDate转换为Date类型
        Date todayDate = java.sql.Date.valueOf(today);
        //查询当天的projectProgressDaily
        LambdaQueryWrapper<ProjectProgressDaily> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectProgressDaily::getDaily, todayDate);
        queryWrapper.eq(ProjectProgressDaily::getProjectId, projectId);

        List<ProjectProgressDaily> projectProgressDailys = projectProgressDailyMapper.selectList(queryWrapper);

        if (projectProgressDailys.isEmpty()) {
            ProjectProgressDaily projectProgressDaily = new ProjectProgressDaily();
            projectProgressDaily.setId(UUID.randomUUID().toString());
            projectProgressDaily.setProjectId(projectId);
            projectProgressDaily.setDaily(new Date());
            projectProgressDaily.setQuota(quota);
            projectProgressDaily.setProgress(progress);
            projectProgressDailyMapper.insert(projectProgressDaily);
        } else {

            for (ProjectProgressDaily projectProgressDaily : projectProgressDailys) {
                projectProgressDaily.setProgress(progress);
                projectProgressDaily.setQuota(quota);
                projectProgressDailyMapper.updateById(projectProgressDaily);
            }
        }
    }


//    //计算叶节点开始日期、结束日期、工期
//    private void calculateLeafNodeDate(List<List<TaskListVm>> leafPaths, ProjectCalendarInfoVm projectCalendarInfoVm) {
//
//        TaskListVm pre = null;
//
//        for (List<TaskListVm> arr : leafPaths) {
//            for (TaskListVm item : arr) {
//                if (item.getChildren().size() > 0) {
//                    pre = item;
//                    continue;
//                }
//                if ("1".equals(item.getTaskType())) {
//                    // 手动任务
//                    // 将日期转换为毫秒数
////                    long startMillis = item.getStartDate().getTime();
////                    long endMillis = item.getEndDate().getTime();
//                    double duration = 0;
//                    Date startDate = item.getStartDate();
//                    startDate.setHours(projectCalendarInfoVm.getStartTime().getHours());
//                    startDate.setMinutes(projectCalendarInfoVm.getStartTime().getMinutes());
//                    startDate.setSeconds(projectCalendarInfoVm.getStartTime().getSeconds());
//
//
//                    Date endDate = item.getEndDate();
//                    endDate.setHours(projectCalendarInfoVm.getEndTime().getHours());
//                    endDate.setMinutes(projectCalendarInfoVm.getEndTime().getMinutes());
//                    endDate.setSeconds(projectCalendarInfoVm.getEndTime().getSeconds());
//
//                    while (startDate.getTime() <= endDate.getTime()) {
//                        Calendar currentDate = Calendar.getInstance();
//                        currentDate.setTime(startDate);
//                        if (isWorkDay(currentDate, projectCalendarInfoVm.getWorkWeeks(), projectCalendarInfoVm.getWorkDates(), projectCalendarInfoVm.getExceptionDates())) {
//                            duration++;
//                        }
//                        startDate = new Date(startDate.getTime() + 24 * 60 * 60 * 1000);
//                    }
//                    // 将毫秒数转换为天数并返回
//                    item.setDuration(duration);
//                } else {
//                    // 自动任务
//                    if (item.getDuration() == null) {
//                        item.setDuration(1d);
//                    }
//                    // 判断是否第一条
//                    // 判断是否有工期
//                    if (pre == null) {
//                        // 1999年1月1日
//                        item.setStartDate(new Date(1999, 1, 1));
//                        item.getStartDate().setHours(projectCalendarInfoVm.getStartTime().getHours());
//                        item.getStartDate().setMinutes(projectCalendarInfoVm.getStartTime().getMinutes());
//                        item.getStartDate().setSeconds(projectCalendarInfoVm.getStartTime().getSeconds());
//
//
//                        long millisDifference = (long) (item.getDuration() * 24L * 60 * 60 * 1000);
//                        // 结束时间等于开始时间 加工期
//                        item.setEndDate(new Date(item.getStartDate().getTime() + millisDifference));
//                        item.getEndDate().setHours(projectCalendarInfoVm.getEndTime().getHours());
//                        item.getEndDate().setMinutes(projectCalendarInfoVm.getEndTime().getMinutes());
//                        item.getEndDate().setSeconds(projectCalendarInfoVm.getEndTime().getSeconds());
//
//
//                        item.setPredecessorId(null);
//                        item.setPredecessor("");
//                    } else {
//                        // 前置任务的结束时间等于当前任务的开始时间+1天等于开始时间
//                        if ("1".equals(pre.getTaskType())) {
//                            Date preEndDate = new Date(pre.getEndDate().getTime());
//
//                            item.setStartDate(addWorkDays(preEndDate, 1d, projectCalendarInfoVm));
//
//                        } else {
//                            Date preEndDate = new Date(pre.getEndDate().getTime());
//                            item.setStartDate(new Date(preEndDate.getTime()));
//                        }
//                        item.setEndDate(addWorkDays(item.getStartDate(), item.getDuration(), projectCalendarInfoVm));
//                        // System.out.println("----------------");
//                    }
//                }
//                pre = item;
//            }
//        }
//    }
//
//    private Date addWorkDays(Date startDate, Double numWorkDays, ProjectCalendarInfoVm projectCalendarInfoVm) {
//        // 一天的工作时间（小时）
//        int workHoursPerDay = projectCalendarInfoVm.getWorkHoursPerDay();
//        double remainingWorkDays = numWorkDays * workHoursPerDay;
//        Calendar currentDate = Calendar.getInstance();
//        currentDate.setTime(startDate);
//
//        // 循环，直到剩余工作日数为0
//        while (remainingWorkDays > 0) {
//            currentDate.add(Calendar.HOUR_OF_DAY, 1);
//            // 如果是工作日
//            if (isWorkDay(currentDate, projectCalendarInfoVm.getWorkWeeks(), projectCalendarInfoVm.getWorkDates(), projectCalendarInfoVm.getExceptionDates())) {
//                // 如果当前时间处于工作时间段内
//                Time currentTime = new Time(currentDate.getTime().getHours(), currentDate.getTime().getMinutes(), currentDate.getTime().getSeconds());
//                if (isWorkingHour(currentTime.getTime(), projectCalendarInfoVm.getWorkTimes())) {
//                    // 减去当前小时所占的工作日数
//                    remainingWorkDays -= 1;
//                }
//            }
//            Date startDate1 = currentDate.getTime();
//            String ss = "";
//        }
//        return currentDate.getTime();
//    }
//
//    private boolean isWorkDay(Calendar date, List<Long> workWeeks, List<Date> workDays, List<Date> exceptionDates) {
//
//        Date curDate = date.getTime();
//        curDate.setHours(0);
//        curDate.setMinutes(0);
//        curDate.setSeconds(0);
//
//        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
//        //判断当前日期是否在workDays里面
//        if (workDays.contains(curDate)) {
//            return true;
//        }
//
//        if (exceptionDates.contains(curDate)) {
//            return false;
//        }
//
//        if (workWeeks.contains((long) dayOfWeek - 1)) {
//            return true;
//        } else {
//            return false;
//        }
//
//
//    }
//
//    private boolean isWorkingHour(long curTime, List<WorkTimeListVm> workTimes) {
//        for (WorkTimeListVm workTime : workTimes) {
////            Calendar calendar = Calendar.getInstance();
////            calendar.setTimeInMillis(workTime.get(0).getTime());
////            // 给Calendar增加一小时
////            calendar.add(Calendar.HOUR_OF_DAY, 1);
////            long startTime = new Time(calendar.getTimeInMillis()).getTime();
//            long startTime = workTime.getStartTime().getTime();
//            long endTime = workTime.getEndTime().getTime();
//            if (curTime > startTime && curTime <= endTime) {
//                return true;
//            }
//        }
//        return false;
//    }

    //计算枝节点开始日期、结束日期、工期
    public void calculateBranchNodeDate(TaskListVm root) {
        traverseUpFromLeafHelper(root, node -> {


            if (node.getChildren().isEmpty()) {
                return;
            }
            if ("1".equals(node.getTaskType())) {
                return;
            }

            Date minStartDate = this.getEarliestStartDateNode(node.getChildren()).getStartDate();
            Date maxEndDate = this.getLatestEndDateNode(node.getChildren()).getEndDate();


            //合计 node.getChildren() 中的 duration
            double duration = node.getChildren().stream().mapToDouble(TaskListVm::getDuration).sum();

            double completedDuration = node.getChildren().stream().mapToDouble(a -> {
                return ((double) a.getProgress() / 100) * a.getDuration();
            }).sum();

            //进度
            node.setProgress((int) (completedDuration / duration * 100));

            node.setStartDate(minStartDate);
            node.setEndDate(maxEndDate);

//            long startMillis = node.getStartDate().getTime();
//
//            long endMillis = node.getEndDate().getTime();
//            // 计算相差的毫秒数
//            long millisDifference = endMillis - startMillis;
//            //millisDifference 转为double
//            double millisDifferenceDouble = (double) millisDifference;
            // 将毫秒数转换为天数并返回
            node.setDuration(duration);


        });
    }

    private TaskListVm getLatestEndDateNode(List<TaskListVm> children) {
        return children.stream().max((task1, task2) -> task1.getEndDate().compareTo(task2.getEndDate())).orElse(null);
    }

    private TaskListVm getEarliestStartDateNode(List<TaskListVm> children) {
        return children.stream().min((task1, task2) -> task1.getStartDate().compareTo(task2.getStartDate())).orElse(null);
    }

    private void traverseUpFromLeafHelper(TaskListVm node, Consumer<TaskListVm> callback) {
        if (node.getChildren().isEmpty()) {
            // 如果当前节点没有子节点，调用回调函数
            callback.accept(node);
        } else {
            // 遍历当前节点的子节点
            for (TaskListVm child : node.getChildren()) {
                // 递归调用自身，向下遍历子节点
                traverseUpFromLeafHelper(child, callback);
            }
            // 遍历完所有子节点后，调用回调函数
            callback.accept(node);
        }
    }

    private void getLeafPathsHelper(TaskListVm node, List<TaskListVm> path, List<List<TaskListVm>> leafPaths) {
        path.add(node);
        if (node.getChild().isEmpty()) {
            // 使用深拷贝将路径添加到结果列表中
            leafPaths.add(path);
        } else {
            for (TaskListVm child : node.getChild()) {
                getLeafPathsHelper(child, new ArrayList<>(path), leafPaths);
            }
        }
    }

    private List<List<TaskListVm>> getLeafPaths(List<TaskListVm> list) {
        TaskListVm root = new TaskListVm();
        root.setChild(list);
        List<List<TaskListVm>> leafPaths = new ArrayList<>();
        getLeafPathsHelper(root, new ArrayList<>(), leafPaths);
        leafPaths.forEach(leafPath -> {
            leafPath.remove(0);
        });
        return leafPaths;
    }

    private List<TaskListVm> listToPidTree(List<TaskListVm> list) {
        HashMap<String, TaskListVm> map = new HashMap<>();
        for (TaskListVm item : list) {
            map.put(item.getId(), item);
        }
        for (TaskListVm item : list) {
            if (!StringUtils.hasText(item.getpId())) {
                continue;
            }
            TaskListVm pTask = map.get(item.getpId());
            pTask.getChildren().add(item);
        }
        List<TaskListVm> treeList = list.stream().filter(item -> item.getpId() == null).collect(Collectors.toList());
        return treeList;
    }

    private List<TaskListVm> listToPredecessorIdTree(List<TaskListVm> list) {
        HashMap<String, TaskListVm> map = new HashMap<>();
        for (TaskListVm item : list) {
            map.put(item.getId(), item);
        }
        for (TaskListVm item : list) {
            if (!StringUtils.hasText(item.getPredecessorId())) {
                continue;
            }

            TaskListVm pTask = map.get(item.getPredecessorId());
            if (pTask != null) {
                pTask.getChild().add(item);
            }
        }
        List<TaskListVm> treeList = list.stream().filter(item -> item.getPredecessorId() == null).collect(Collectors.toList());
        return treeList;
    }

    private List<TaskListVm> deepSort(List<TaskListVm> list) {
        List<TaskListVm> newList = new ArrayList<>();
        TaskListVm root = new TaskListVm();
        root.setChildren(list);
        dfs(root, newList);
        return newList;
    }

    public void dfs(TaskListVm root, List<TaskListVm> newList) {
        if (StringUtils.hasText(root.getId())) newList.add(root);
        for (TaskListVm child : root.getChildren()) {
            dfs(child, newList);
        }
    }

    @Override
    public TaskListVm detail(String id) {

        List<TaskListVm> taskDetailVmList = this.taskMapper.listTask(null, id);

        if (taskDetailVmList.size() < 1) {
            throw new RuntimeException("任务不存在");
        }
        return taskDetailVmList.get(0);
    }

    //计算项目进度

    //    @Transactional(rollbackFor = Exception.class)
    private void updateProjectProgress(String projectId) {


        List<TaskListVm> list = taskMapper.listTask(projectId, null);

        //计算项目进度
        List<String> pIds = list.stream().map(a -> a.getpId()).collect(Collectors.toList());
        List<TaskListVm> notPids = list.stream().filter(a -> !pIds.contains(a.getId())).collect(Collectors.toList());
        double d = notPids.stream().mapToDouble(a -> {
            return a.getDuration();
        }).sum();
        double p = notPids.stream().mapToDouble(a -> {
            return a.getDuration() * a.getProgress();
        }).sum();
        double progress = Math.round(p / d);
        double projectDuration = notPids.stream().mapToDouble(TaskListVm::getDuration).sum();
        Date projectStartDate = notPids.stream().map(TaskListVm::getStartDate).min(Date::compareTo).orElse(null);
        Date projectEndDate = notPids.stream().map(TaskListVm::getEndDate).max(Date::compareTo).orElse(null);
        int projectTaskCount = notPids.size();


        //更新项目进度
        ProjectProgress projectProgress = projectProgressMapper.selectById(projectId);

        if (projectProgress == null) {
            projectProgress = new ProjectProgress();
            projectProgress.setId(projectId);
            projectProgress.setTaskUpdateTime(new Date());
            projectProgress.setDuration(projectDuration);
            projectProgress.setStartDate(projectStartDate);
            projectProgress.setEndDate(projectEndDate);
            projectProgress.setTaskCount(projectTaskCount);
            projectProgress.setProgress((int) (progress));
            projectProgressMapper.insert(projectProgress);
        } else {
            projectProgress.setTaskUpdateTime(new Date());
            projectProgress.setDuration(projectDuration);
            projectProgress.setStartDate(projectStartDate);
            projectProgress.setEndDate(projectEndDate);
            projectProgress.setTaskCount(projectTaskCount);
            projectProgress.setProgress((int) (progress));
            projectProgressMapper.updateById(projectProgress);
        }
        saveProjectProgressDaily(projectId, projectProgress.getProgress(), (int) 0);
        dynamicsService.save(projectId, "更新了项目进度,当前项目进度为" + projectProgress.getProgress() + "%");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<TaskSaveDm> dm) throws Exception {

        if (dm.isEmpty())
            return;
        for (TaskSaveDm taskSaveDm : dm) {
            if (taskSaveDm.getDuration() == null || taskSaveDm.getDuration() == 0) {
                taskSaveDm.setDuration(1d);
            }
            taskSaveDm.setSortNum((double) dm.indexOf(taskSaveDm));
        }

//        //计算项目进度
//        List<String> pIds = dm.stream().map(a -> a.getpId()).collect(Collectors.toList());
//        List<TaskSaveDm> notPids = dm.stream().filter(a -> !pIds.contains(a.getId())).collect(Collectors.toList());
//        double d = notPids.stream().mapToDouble(a -> {
//            return a.getDuration();
//        }).sum();
//        double p = notPids.stream().mapToDouble(a -> {
//            return a.getDuration() * a.getProgress();
//        }).sum();
//        double progress = Math.round(p / d);
//        double projectDuration = notPids.stream().mapToDouble(TaskSaveDm::getDuration).sum();
//        Date projectStartDate = notPids.stream().map(TaskSaveDm::getStartDate).min(Date::compareTo).orElse(null);
//        Date projectEndDate = notPids.stream().map(TaskSaveDm::getEndDate).max(Date::compareTo).orElse(null);
//        int projectTaskCount = notPids.size();

        LambdaQueryWrapper<ProjectTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectTask::getProjectId, dm.get(0).getProjectId());
        List<ProjectTask> projectTasks = taskMapper.selectList(queryWrapper);
        //找出要删除的项目
        //    List<TaskListVm> list = taskMapper.listTask(dm.get(0).getProjectId(), null);
        List<String> listIds = projectTasks.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<String> dmIds = dm.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<String> deleteIds = listIds.stream().filter(a -> !dmIds.contains(a)).collect(Collectors.toList());

        //找出要新增和修改的项目
        List<TaskSaveDm> add = dm.stream().filter(a -> a.getState().equals("add")).collect(Collectors.toList());
        List<TaskSaveDm> update = dm.stream().filter(a -> a.getState().equals("edit")).collect(Collectors.toList());


        //更新项目进度
//        ProjectProgress projectProgress = projectProgressMapper.selectById(dm.get(0).getProjectId());

//        if (projectProgress == null) {
//            projectProgress = new ProjectProgress();
//            projectProgress.setId(dm.get(0).getProjectId());
//            projectProgress.setTaskUpdateTime(new Date());
//            projectProgress.setDuration(projectDuration);
//            projectProgress.setStartDate(projectStartDate);
//            projectProgress.setEndDate(projectEndDate);
//            projectProgress.setTaskCount(projectTaskCount);
//            projectProgress.setProgress((int) (progress));
//            projectProgressMapper.insert(projectProgress);
//        } else {
//            projectProgress.setTaskUpdateTime(new Date());
//            projectProgress.setDuration(projectDuration);
//            projectProgress.setStartDate(projectStartDate);
//            projectProgress.setEndDate(projectEndDate);
//            projectProgress.setTaskCount(projectTaskCount);
//            projectProgress.setProgress((int) (progress));
//            projectProgressMapper.updateById(projectProgress);
//        }


        if (!deleteIds.isEmpty()) {
            taskMapper.deleteBatchIds(deleteIds);
            LambdaQueryWrapper<TaskCollaborator> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.in(TaskCollaborator::getTaskId, deleteIds);
            taskCollaboratorMapper.delete(deleteWrapper);
        }
        if (!add.isEmpty()) {
            addTask(add);
        }
        if (!update.isEmpty()) {
            updateTask(update, projectTasks);

//            for (TaskSaveDm taskSaveDm : update) {
////                TaskUpdateProgressDm taskUpdateProgressDm = new TaskUpdateProgressDm();
////                taskUpdateProgressDm.setId(taskSaveDm.getId());
////                taskUpdateProgressDm.setProgress(taskSaveDm.getProgress());
//                //updateProgress(taskUpdateProgressDm);
//            }
        }


        updateProjectProgress(dm.get(0).getProjectId());


    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Workbook export(String projectId) throws Exception {
        List<TaskListVm> list = taskMapper.listTask(projectId, null);
        List<TaskListVm> tree = listToPidTree(list);
        for (TaskListVm taskListVm : tree) {
            printTask(taskListVm, 0);
        }
        //生成excel文件,并返回文件路径

        // 1. 创建工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("数据报表");

        // 2. 创建表头
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);


        Row headerRow = sheet.createRow(0);
        String[] headers = {"编号", "任务名", "工期", "开始时间", "开始时间", "进度", "指派"};
        Integer[] headersWidth = {2000, 10000, 2000, 3000, 3000, 2000, 3000};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cellStyle = workbook.createCellStyle();
            font = workbook.createFont();
            font.setBold(true);
            if (i == 1) {
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
            } else {
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
            }
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //设置列宽
            sheet.setColumnWidth(i, headersWidth[i]);
        }

        int rowNum = 1;
        for (TaskListVm taskListVm : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(list.indexOf(taskListVm) + 1);
            row.createCell(1).setCellValue(taskListVm.getIndent() + taskListVm.getTaskName());
            row.createCell(2).setCellValue(taskListVm.getDuration());
            row.createCell(3).setCellValue(dateFormat1.format(taskListVm.getStartDate()));
            row.createCell(4).setCellValue(dateFormat1.format(taskListVm.getEndDate()));
            row.createCell(5).setCellValue(taskListVm.getProgress());
            row.createCell(6).setCellValue(taskListVm.getAssignUserName());
            //遍历

            for (int i = 0; i < headers.length; i++) {
                cellStyle = workbook.createCellStyle();
                font = workbook.createFont();
                if (!taskListVm.getChildren().isEmpty()) {
                    font.setBold(true);
                }
                if (i == 1) {
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                } else {
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                }
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyle.setFont(font);
                row.getCell(i).setCellStyle(cellStyle);
            }


        }

//        // 4. 自动调整列宽
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }

        return workbook;
    }

    private void printTask(TaskListVm task, int indentLevel) {
        String indent = generateIndent(indentLevel);
        task.setIndent(indent);
        for (TaskListVm child : task.getChildren()) {
            printTask(child, indentLevel + 1);
        }
    }

    private static String generateIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("        ");
        }
        return sb.toString();
    }


    private ProjectProgress saveProjectProgress(String projectId) {
        ProjectProgress projectProgress = projectProgressMapper.selectById(projectId);
        if (projectProgress == null) {
            projectProgress = new ProjectProgress();
            projectProgress.setId(projectId);
            projectProgress.setProgress(0);
            Date date = new Date();
            projectProgress.setProgressUpdateTime(date);
            projectProgress.setTaskUpdateTime(date);
            projectProgressMapper.insert(projectProgress);
        } else {
            projectProgress.setTaskUpdateTime(new Date());
            projectProgressMapper.updateById(projectProgress);
        }

        return projectProgress;

    }

    private void addTask(List<TaskSaveDm> dm) {

        //"576dcf5e-4331-4742-83c7-c704d6e15bb4";按这个格式生成 全部为0的UUID
        //String taskId = UUID.randomUUID().toString();

        List<ProjectTask> taskList = new ArrayList<>();
        List<TaskCollaborator> taskCollaboratorList = new ArrayList<>();
        for (TaskSaveDm taskSaveDm : dm) {
            Long userId = SecurityUtils.getUserId();
            ProjectTask projectTask = new ProjectTask();
            projectTask.setUserId(userId);
            projectTask.setId(taskSaveDm.getId());
            projectTask.setProjectId(taskSaveDm.getProjectId());
            projectTask.setTaskName(taskSaveDm.getTaskName());
            projectTask.setTaskType(taskSaveDm.getTaskType());
            projectTask.setStartDate(taskSaveDm.getStartDate());
            projectTask.setEndDate(taskSaveDm.getEndDate());
            projectTask.setDuration(taskSaveDm.getDuration());
            projectTask.setProgress(taskSaveDm.getProgress());
            projectTask.setPredecessorId(taskSaveDm.getPredecessorId());
            projectTask.setAssignUserId(taskSaveDm.getAssignUserId());
            projectTask.setSortNum(taskSaveDm.getSortNum());
            projectTask.setpId(taskSaveDm.getpId());
            projectTask.setDescription(taskSaveDm.getDescription());
            if (taskSaveDm.getProgress() == 100) {
                projectTask.setCompletionDate(new Date());
            } else {
                projectTask.setCompletionDate(null);
            }
            taskList.add(projectTask);
            for (Long collaboratorUserId : taskSaveDm.getCollaboratorUserIds()) {
                TaskCollaborator taskCollaborator = new TaskCollaborator();
                taskCollaborator.setTaskId(taskSaveDm.getId());
                taskCollaborator.setUserId(collaboratorUserId);
                taskCollaborator.setId(UUID.randomUUID().toString());
                taskCollaboratorList.add(taskCollaborator);
            }
        }

        if (!taskList.isEmpty()) {
            taskMapper.insertBatchSomeColumn(taskList);
        }
        if (!taskCollaboratorList.isEmpty()) {
            taskCollaboratorMapper.insertBatchSomeColumn(taskCollaboratorList);
        }


//        Long userId = SecurityUtils.getUserId();
//        ProjectTask projectTask = new ProjectTask();
//        projectTask.setUserId(userId);
//        projectTask.setId(dm.getId());
//        projectTask.setProjectId(dm.getProjectId());
//        projectTask.setTaskName(dm.getTaskName());
//        projectTask.setTaskType(dm.getTaskType());
//        projectTask.setStartDate(dm.getStartDate());
//        projectTask.setEndDate(dm.getEndDate());
//        projectTask.setDuration(dm.getDuration());
//        projectTask.setProgress(dm.getProgress());
//        projectTask.setPredecessorId(dm.getPredecessorId());
//        projectTask.setAssignUserId(dm.getAssignUserId());
//        projectTask.setSortNum(dm.getSortNum());
//        projectTask.setpId(dm.getpId());
//        projectTask.setDescription(dm.getDescription());
//        if (dm.getProgress() == 100) {
//            projectTask.setCompletionDate(new Date());
//        } else {
//            projectTask.setCompletionDate(null);
//        }
//
//        taskMapper.insert(projectTask);
//        List<TaskCollaborator> taskCollaboratorList = new ArrayList<>();
//        for (Long collaboratorUserId : dm.getCollaboratorUserIds()) {
//            TaskCollaborator taskCollaborator = new TaskCollaborator();
//            taskCollaborator.setTaskId(dm.getId());
//            taskCollaborator.setUserId(collaboratorUserId);
//            taskCollaborator.setId(UUID.randomUUID().toString());
//            taskCollaboratorList.add(taskCollaborator);
//        }
//        if (!taskCollaboratorList.isEmpty()) {
//            //插入协作者
//            taskCollaboratorMapper.insertBatchSomeColumn(taskCollaboratorList);
//        }


    }

    private void updateTask(List<TaskSaveDm> updates, List<ProjectTask> list) throws Exception {


        List<ProjectTask> updateList = new ArrayList<>();
        List<TaskCollaborator> taskCollaboratorList = new ArrayList<>();

        for (TaskSaveDm update : updates) {

            ProjectTask projectTask = list.stream().filter(task -> task.getId().equals(update.getId())).findFirst().orElseThrow(null);
            if (projectTask == null) {
                throw new Exception("任务不存在");
            }
//                    .set(ProjectTask::getTaskName, dm.getTaskName())       // 设置字段，允许 null
//                    .set(ProjectTask::getTaskType, dm.getTaskType())
//                    .set(ProjectTask::getStartDate, dm.getStartDate())
//                    .set(ProjectTask::getEndDate, dm.getEndDate())
//                    .set(ProjectTask::getDuration, dm.getDuration())
//                    .set(ProjectTask::getPredecessorId, dm.getPredecessorId())
//                    .set(ProjectTask::getAssignUserId, dm.getAssignUserId())
//                    .set(ProjectTask::getSortNum, dm.getSortNum())
//                    .set(ProjectTask::getpId, dm.getpId())
//                    .set(ProjectTask::getDescription, dm.getDescription());


            projectTask.setTaskName(update.getTaskName());
            projectTask.setTaskType(update.getTaskType());
            projectTask.setStartDate(update.getStartDate());
            projectTask.setEndDate(update.getEndDate());
            projectTask.setDuration(update.getDuration());
            projectTask.setPredecessorId(update.getPredecessorId());
            projectTask.setAssignUserId(update.getAssignUserId());
            projectTask.setSortNum(update.getSortNum());
            projectTask.setpId(update.getpId());
            projectTask.setDescription(update.getDescription());

            if (!(projectTask.getProgress() == update.getProgress() && projectTask.getProgress() == 100)) {
                Date completionDate = null;
                if (update.getProgress() == 100) {
                    completionDate = new Date();
                }
                projectTask.setProgress(update.getProgress());
                projectTask.setCompletionDate(completionDate);
            }
            updateList.add(projectTask);
            for (Long collaboratorUserId : update.getCollaboratorUserIds()) {
                TaskCollaborator taskCollaborator = new TaskCollaborator();
                taskCollaborator.setTaskId(update.getId());
                taskCollaborator.setUserId(collaboratorUserId);
                taskCollaborator.setId(UUID.randomUUID().toString());
                taskCollaboratorList.add(taskCollaborator);
            }
        }

        //删除协助人

        List<String> taskIds = updateList.stream().map(ProjectTask::getId).collect(Collectors.toList());
        if(!taskIds.isEmpty()){
            LambdaQueryWrapper<TaskCollaborator> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(TaskCollaborator::getTaskId, taskIds);
            taskCollaboratorMapper.delete(wrapper);
        }

        //插入协助人
        if(!taskCollaboratorList.isEmpty())
        {
            taskCollaboratorMapper.insertBatchSomeColumn(taskCollaboratorList);
        }

        //批量更新
        if(!updateList.isEmpty()){
            taskMapper.batchUpdate(updateList);
        }

//
//        ProjectTask projectTask = taskMapper.selectById(dm.getId());
//        if (projectTask == null) {
//            throw new Exception("任务不存在");
//        }
//        LambdaUpdateWrapper<ProjectTask> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(ProjectTask::getId, projectTask.getId())  // 根据 ID 更新
//                .set(ProjectTask::getTaskName, dm.getTaskName())       // 设置字段，允许 null
//                .set(ProjectTask::getTaskType, dm.getTaskType())
//                .set(ProjectTask::getStartDate, dm.getStartDate())
//                .set(ProjectTask::getEndDate, dm.getEndDate())
//                .set(ProjectTask::getDuration, dm.getDuration())
//                .set(ProjectTask::getPredecessorId, dm.getPredecessorId())
//                .set(ProjectTask::getAssignUserId, dm.getAssignUserId())
//                .set(ProjectTask::getSortNum, dm.getSortNum())
//                .set(ProjectTask::getpId, dm.getpId())
//                .set(ProjectTask::getDescription, dm.getDescription());
//
//
//        if (!(projectTask.getProgress() == dm.getProgress() && projectTask.getProgress() == 100)) {
//            Date completionDate = null;
//            if (dm.getProgress() == 100) {
//                completionDate = new Date();
//            }
//            updateWrapper.set(ProjectTask::getProgress, dm.getProgress());
//            updateWrapper.set(ProjectTask::getCompletionDate, completionDate);
//        }
//        int rows = taskMapper.update(null, updateWrapper);  // 传入 null 表示不基于实体更新
//        if (rows < 1) {
//            throw new Exception("更新失败");
//        }
//
//        LambdaQueryWrapper<TaskCollaborator> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(TaskCollaborator::getTaskId, dm.getId());
//        taskCollaboratorMapper.delete(wrapper);
//
//        List<TaskCollaborator> taskCollaboratorList = new ArrayList<>();
//        for (Long collaboratorUserId : dm.getCollaboratorUserIds()) {
//            TaskCollaborator taskCollaborator = new TaskCollaborator();
//            taskCollaborator.setTaskId(dm.getId());
//            taskCollaborator.setUserId(collaboratorUserId);
//            taskCollaborator.setId(UUID.randomUUID().toString());
//            taskCollaboratorList.add(taskCollaborator);
//        }
//
//        if (!taskCollaboratorList.isEmpty()) {
//            //插入协作者
//            taskCollaboratorMapper.insertBatchSomeColumn(taskCollaboratorList);
//        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> ids) {
        taskMapper.deleteBatchIds(ids);
        //找出PID为IDS的任务一起删除
        LambdaQueryWrapper<ProjectTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ProjectTask::getpId, ids);
        taskMapper.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUser(TaskAssignUserDm dm) throws Exception {
        ProjectTask projectTask = taskMapper.selectById(dm.getTaskId());
        if (projectTask == null) {
            throw new Exception("任务不存在");
        }
        projectTask.setAssignUserId(dm.getAssignUserId());
        taskMapper.updateById(projectTask);


        LambdaQueryWrapper<ProjectResources> queryWrapper = new QueryWrapper<ProjectResources>().lambda();
        queryWrapper.eq(ProjectResources::getUserId, dm.getAssignUserId());
        List<ProjectResources> list = projectResourcesMapper.selectList(queryWrapper);
        if (list.isEmpty()) {
            ProjectResources projectResources = new ProjectResources();
            projectResources.setUserId(dm.getAssignUserId());
            projectResources.setProjectId(projectTask.getProjectId());
            projectResources.setId(UUID.randomUUID().toString());
            projectResources.setIsManager("0");
            projectResourcesMapper.insert(projectResources);
        }

        ProjectListResourcesVm user = projectResourcesMapper.detailResources(projectTask.getAssignUserId());
        dynamicsService.save(projectTask.getProjectId(), "任务分配给" + user.getNickName());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProgress(TaskUpdateProgressDm dm) throws Exception {
        ProjectTask projectTask = taskMapper.selectById(dm.getId());
        if (projectTask == null) {
            throw new Exception("任务不存在");
        }

        if (projectTask.getProgress() == dm.getProgress() && projectTask.getProgress() == 100) {
            return;
        }


        Date completionDate = null;
        if (dm.getProgress() == 100) {
            completionDate = new Date();
        }
        //使用显示更新
        UpdateWrapper<ProjectTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", dm.getId()) // 指定更新条件
                .set("progress", dm.getProgress()) // 更新进度
                .set("completion_date", completionDate); // 更新完成日期
// 执行更新
        taskMapper.update(null, updateWrapper);

        updateProjectProgress(projectTask.getProjectId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSortNum(List<TaskUpdateSortNumDm> list) {
        for (TaskUpdateSortNumDm item : list) {
            if (item.getId().equals(item.getpId())) {
                item.setpId(null);
            }
            item.setSortNum((double) (list.indexOf(item) + 1));
        }
        taskMapper.batchUpdateSortNum(list);
    }

    @Override
    public TaskBurnDownChartDataVm getTaskBurnDownChartData(String projectId) throws Exception {
        TaskBurnDownChartDataVm taskBurnDownChartDataVm = new TaskBurnDownChartDataVm();


        List<TaskListVm> list = taskMapper.listTask(projectId, null);

        List<String> pIds = list.stream().map(TaskListVm::getpId).collect(Collectors.toList());
        List<TaskListVm> data = list.stream().filter(item -> !pIds.contains(item.getId())).collect(Collectors.toList());

        Date projectStartDate = data.stream().map(TaskListVm::getStartDate).min(Date::compareTo).orElse(null);
        Date projectEndDate = data.stream().map(TaskListVm::getEndDate).max(Date::compareTo).orElse(null);

        if (projectStartDate == null) {
            return taskBurnDownChartDataVm;
        }

        if (projectEndDate == null) {
            return taskBurnDownChartDataVm;
        }

        if (projectStartDate.after(projectEndDate)) {
            return taskBurnDownChartDataVm;
        }

        //计划进度Map
        LinkedHashMap<String, Double> planProgressMap = new LinkedHashMap<>();

        //实际进度Map
        LinkedHashMap<String, Double> actualProgressMap = new LinkedHashMap<>();

        //预测进度Map
        LinkedHashMap<String, Double> predictingProgressMap = new LinkedHashMap<>();


        //计划进度
        //项目总工作量
        double total = data.stream().mapToDouble(item -> 100 * item.getDuration()).sum();
        Date startDate = projectStartDate;
        String startDateStr = dateFormat1.format(startDate);
        Date endDate = projectEndDate;
        endDate = DateUtils.addDays(endDate, 1);

        //生成计划进度燃尽图数据
        while (startDate.before(endDate)) {
            startDateStr = dateFormat1.format(startDate);
            Date finalStartDate1 = DateUtils.addDays(startDate, 1);
            ;
            List<TaskListVm> beforeTaskList = data.stream().filter(item -> item.getEndDate().before(finalStartDate1)).collect(Collectors.toList());
            double dd = beforeTaskList.stream().mapToDouble(item -> 100 * item.getDuration()).sum();
            planProgressMap.put(startDateStr, (total - dd) / total * 100);
            startDate = DateUtils.addDays(startDate, 1);
        }

        //写入进度计划进度燃尽图数据
        for (Map.Entry<String, Double> entry : planProgressMap.entrySet()) {
            String key = entry.getKey();
            taskBurnDownChartDataVm.getxAxisData().add(key);
            Integer map1Value = entry.getValue().intValue();
            taskBurnDownChartDataVm.getPlannedProgressData().add(map1Value);
        }

        //获取今天计划进度
        Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date normalizedStartDate = DateUtils.truncate(projectStartDate, Calendar.DAY_OF_MONTH);
        Date normalizedEndDate = DateUtils.truncate(projectEndDate, Calendar.DAY_OF_MONTH);
        if (today.before(normalizedStartDate)) {
            // 未开始
            taskBurnDownChartDataVm.setPlanProgress(0);
        } else if (today.after(normalizedEndDate)) {
            // 已结束
            taskBurnDownChartDataVm.setPlanProgress(100);
        } else {
            // 进行中
            String todayStr = dateFormat1.format(today);
            double planProgress = planProgressMap.getOrDefault(todayStr, 0.0);
            taskBurnDownChartDataVm.setPlanProgress(100 - (int) Math.round(planProgress));
        }


        //生成实际进度燃尽图数据
        today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        startDate = projectStartDate;
        startDateStr = dateFormat1.format(startDate);

        endDate = DateUtils.addDays(today, 1);

        LambdaQueryWrapper<ProjectProgressDaily> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectProgressDaily::getProjectId, projectId)
                .orderByAsc(ProjectProgressDaily::getDaily);

        List<ProjectProgressDaily> projectProgressDailyList = projectProgressDailyMapper.selectList(wrapper);


        if (!projectProgressDailyList.isEmpty()) {
            double progerss = 100;
            while (startDate.before(endDate)) {
                startDateStr = dateFormat1.format(startDate);
                //查询某天进度
                String finalStartDateStr = startDateStr;
                List<ProjectProgressDaily> projectProgressDailys = projectProgressDailyList.stream().filter(item -> {
                    String projectProgressDailysStr = dateFormat1.format(item.getDaily());
                    return finalStartDateStr.equals(projectProgressDailysStr);
                }).collect(Collectors.toList());


                if (projectProgressDailys.isEmpty()) {
                    actualProgressMap.put(startDateStr, progerss);
                    predictingProgressMap.put(startDateStr, null);

                    ProjectProgressDaily projectProgressDaily = new ProjectProgressDaily();
                    projectProgressDaily.setProjectId(projectId);
                    projectProgressDaily.setDaily(startDate);
                    projectProgressDaily.setProgress(100 - (int) progerss);
                    projectProgressDaily.setQuota(0);
                    projectProgressDailyList.add(projectProgressDaily);

                } else {
                    progerss = (double) (100 - projectProgressDailys.get(0).getProgress());
                    actualProgressMap.put(startDateStr, progerss);
                    predictingProgressMap.put(startDateStr, null);
                }
                if (progerss <= 0) {

                }

                startDate = DateUtils.addDays(startDate, 1);
            }

            //写入进度实际进度燃尽图数据
            for (Map.Entry<String, Double> entry : actualProgressMap.entrySet()) {
                String key = entry.getKey();
                taskBurnDownChartDataVm.getxAxisData().add(key);
                Integer map1Value = entry.getValue().intValue();
                taskBurnDownChartDataVm.getActualProgressData().add(map1Value);
            }

            //设置实际进度
            projectProgressDailyList = projectProgressDailyList.stream().sorted(Comparator.comparing(ProjectProgressDaily::getDaily)).collect(Collectors.toList());
            ProjectProgressDaily lastProjectProgressDaily = projectProgressDailyList.get(projectProgressDailyList.size() - 1);
            taskBurnDownChartDataVm.setActualProgress(lastProjectProgressDaily.getProgress());

            //进度偏差
            taskBurnDownChartDataVm.setProgressDeterioration(taskBurnDownChartDataVm.getPlanProgress() - taskBurnDownChartDataVm.getActualProgress());

        }


        //预测进度
        double predictingProgress = 100d;
        Date predictingStartDate = projectStartDate;
        String predictingStarrDateStr = dateFormat1.format(predictingStartDate);


        if (!projectProgressDailyList.isEmpty()) {


            //取actualProgressMap  最后一项目


            projectProgressDailyList = projectProgressDailyList.stream().sorted(Comparator.comparing(ProjectProgressDaily::getDaily)).collect(Collectors.toList());
            ProjectProgressDaily lastProjectProgressDaily = projectProgressDailyList.get(projectProgressDailyList.size() - 1);
            predictingProgress = lastProjectProgressDaily.getProgress();
            predictingStartDate = lastProjectProgressDaily.getDaily();
            predictingStarrDateStr = dateFormat1.format(predictingStartDate);
            predictingProgressMap.put(predictingStarrDateStr, 100 - predictingProgress);
        }


        //进度落后预测
        if (taskBurnDownChartDataVm.getProgressDeterioration() > 0) {

            //计算计划进度每天完成多少进度
            double day1 = DateUtils.differentDaysByMillisecond(projectStartDate, projectEndDate);
            double v1 = 100 / day1;
            double v2 = predictingProgress / v1;//实际进度
            double v3 = taskBurnDownChartDataVm.getPlanProgress() / v1;//计划进度

            //取整数，往上取
            v2 = Math.ceil(v2);
            v3 = Math.ceil(v3);
            double v4 = v3 - v2;
            Date sDate = DateUtils.addDays(predictingStartDate, 1);
            sDate = DateUtils.addDays(sDate, -(int) v4);
            Date eDate = DateUtils.addDays(projectEndDate, 1);
//            String sDateStr = dateFormat1.format(sDate);
            //从计算进度取出 sDate 到 projectEndDate 的天数的数据
            while (sDate.before(eDate)) {
//                Date ss = DateUtils.addDays(sDate, -(int) v4);
                if (!planProgressMap.containsKey(dateFormat1.format(sDate))) {
                    break;
                }
                double p1 = planProgressMap.get(dateFormat1.format(sDate));
                Date ss = DateUtils.addDays(sDate, (int) v4);
                predictingProgressMap.put(dateFormat1.format(ss), p1);
                sDate = DateUtils.addDays(sDate, 1);
            }


//            //计算 st 到 现在 有多少天
//            double day = DateUtils.differentDaysByMillisecond(new Date(), projectStartDate);
//            //实际进度
//            double atualProgress = taskBurnDownChartDataVm.getActualProgress();
//            //每日平均进度
//            double v = atualProgress / day;
//            while (predictingProgress < 100) {
//                predictingStartDate = DateUtils.addDays(predictingStartDate, 1);
//                predictingStarrDateStr = dateFormat1.format(predictingStartDate);
//                predictingProgress += v;
//                if (predictingProgress >= 100) {
//                    predictingProgress = 100;
//                }
//                predictingProgressMap.put(predictingStarrDateStr, 100 - predictingProgress);
//            }
        }

        //进度超前预测
        if (taskBurnDownChartDataVm.getProgressDeterioration() <= 0) {
            while (predictingProgress < 100) {

                predictingStartDate = DateUtils.addDays(predictingStartDate, 1);
                predictingStarrDateStr = dateFormat1.format(predictingStartDate);

                if (!planProgressMap.containsKey(predictingStarrDateStr)) {
                    break;
                }
                //计划进度
                double planProgress = 100 - planProgressMap.get(predictingStarrDateStr);
                //当前进度偏差
                double deteriorationProgress = taskBurnDownChartDataVm.getProgressDeterioration();
                predictingProgress = planProgress - deteriorationProgress;
                if (predictingProgress >= 100) {
                    predictingProgress = 100;
                }
                predictingProgressMap.put(predictingStarrDateStr, 100 - predictingProgress);
            }
        }


        //获取实际进度最后一个值更新到预测进度第一个值
//        String lastKey = actualProgressMap.keySet().stream().reduce((first, second) -> second).orElse(null);
//        Double lastValue = actualProgressMap.get(lastKey);
//        predictingProgressMap.put(lastKey, lastValue);

        for (Map.Entry<String, Double> entry : predictingProgressMap.entrySet()) {
            String key = entry.getKey();
            taskBurnDownChartDataVm.getxAxisData().add(key);
            if (entry.getValue() != null) {
                Integer map1Value = entry.getValue().intValue();
                taskBurnDownChartDataVm.getPredictingProgress().add(map1Value);
            } else {
                taskBurnDownChartDataVm.getPredictingProgress().add(null);
            }
        }

        List<String> axisData = taskBurnDownChartDataVm.getxAxisData().stream().distinct().collect(Collectors.toList());
        String lastKey = axisData.get(axisData.size() - 1);
        Date lastDate = DateUtils.parseDate(lastKey, "yyyy-MM-dd");
        lastDate = DateUtils.addDays(lastDate, 1);
        axisData.add(dateFormat1.format(lastDate));

        taskBurnDownChartDataVm.setxAxisData(axisData);

        return taskBurnDownChartDataVm;
    }


}
