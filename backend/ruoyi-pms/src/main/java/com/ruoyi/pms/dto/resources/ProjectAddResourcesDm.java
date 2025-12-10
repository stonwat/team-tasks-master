package com.ruoyi.pms.dto.resources;

import java.util.List;

public class ProjectAddResourcesDm {

    private List<Long> userIds;

    private String projectId;

    private String isManager;
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public String getIsManager() {
        return isManager;
    }

    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }
}
