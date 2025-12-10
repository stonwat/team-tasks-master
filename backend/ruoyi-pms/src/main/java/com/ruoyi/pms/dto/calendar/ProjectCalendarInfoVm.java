package com.ruoyi.pms.dto.calendar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProjectCalendarInfoVm {
    //假日
    List<List<String>> exceptionDates = new ArrayList<>();
    //工作周
    List<Long> workWeekDays = new ArrayList<>();

    List<List<String>> workDates = new ArrayList<>();

    //工作时间段
    List<List<String>> workHours = new ArrayList<>();

    public List<List<String>> getExceptionDates() {
        return exceptionDates;
    }

    public void setExceptionDates(List<List<String>> exceptionDates) {
        this.exceptionDates = exceptionDates;
    }

    public List<Long> getWorkWeekDays() {
        return workWeekDays;
    }

    public void setWorkWeekDays(List<Long> workWeekDays) {
        this.workWeekDays = workWeekDays;
    }

    public List<List<String>> getWorkDates() {
        return workDates;
    }

    public void setWorkDates(List<List<String>> workDates) {
        this.workDates = workDates;
    }

    public List<List<String>> getWorkHours() {
        return workHours;
    }

    public void setWorkHours(List<List<String>> workHours) {
        this.workHours = workHours;
    }
}
