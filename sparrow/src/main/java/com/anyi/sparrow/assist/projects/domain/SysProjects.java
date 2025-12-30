package com.anyi.sparrow.assist.projects.domain;


import java.io.Serializable;
import java.util.Date;

public class SysProjects implements Serializable {
    private Integer projectId;

    private Integer projectCode;

    private String projectName;

    private Integer device;

    private Integer buildCode;

    private String versionCode;

    private Date onlineTime;

    private Integer status;

    private String downloadUrl;


    private Integer forcedUpdating;

    private Date creatTime;

    private String creator;

    private String readme;

    private static final long serialVersionUID = 1L;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Integer getDevice() {
        return device;
    }

    public void setDevice(Integer device) {
        this.device = device;
    }

    public Integer getBuildCode() {
        return buildCode;
    }

    public void setBuildCode(Integer buildCode) {
        this.buildCode = buildCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode == null ? null : versionCode.trim();
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl == null ? null : downloadUrl.trim();
    }

    public Integer getForcedUpdating() {
        return forcedUpdating;
    }

    public void setForcedUpdating(Integer forcedUpdating) {
        this.forcedUpdating = forcedUpdating;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme == null ? null : readme.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", projectId=").append(projectId);
        sb.append(", projectCode=").append(projectCode);
        sb.append(", projectName=").append(projectName);
        sb.append(", device=").append(device);
        sb.append(", buildCode=").append(buildCode);
        sb.append(", versionCode=").append(versionCode);
        sb.append(", onlineTime=").append(onlineTime);
        sb.append(", status=").append(status);
        sb.append(", downloadUrl=").append(downloadUrl);
        sb.append(", forcedUpdating=").append(forcedUpdating);
        sb.append(", creatTime=").append(creatTime);
        sb.append(", creator=").append(creator);
        sb.append(", readme=").append(readme);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysProjects other = (SysProjects) that;
        return (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getProjectCode() == null ? other.getProjectCode() == null : this.getProjectCode().equals(other.getProjectCode()))
            && (this.getProjectName() == null ? other.getProjectName() == null : this.getProjectName().equals(other.getProjectName()))
            && (this.getDevice() == null ? other.getDevice() == null : this.getDevice().equals(other.getDevice()))
            && (this.getBuildCode() == null ? other.getBuildCode() == null : this.getBuildCode().equals(other.getBuildCode()))
            && (this.getVersionCode() == null ? other.getVersionCode() == null : this.getVersionCode().equals(other.getVersionCode()))
            && (this.getOnlineTime() == null ? other.getOnlineTime() == null : this.getOnlineTime().equals(other.getOnlineTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDownloadUrl() == null ? other.getDownloadUrl() == null : this.getDownloadUrl().equals(other.getDownloadUrl()))
            && (this.getForcedUpdating() == null ? other.getForcedUpdating() == null : this.getForcedUpdating().equals(other.getForcedUpdating()))
            && (this.getCreatTime() == null ? other.getCreatTime() == null : this.getCreatTime().equals(other.getCreatTime()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getReadme() == null ? other.getReadme() == null : this.getReadme().equals(other.getReadme()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getProjectCode() == null) ? 0 : getProjectCode().hashCode());
        result = prime * result + ((getProjectName() == null) ? 0 : getProjectName().hashCode());
        result = prime * result + ((getDevice() == null) ? 0 : getDevice().hashCode());
        result = prime * result + ((getBuildCode() == null) ? 0 : getBuildCode().hashCode());
        result = prime * result + ((getVersionCode() == null) ? 0 : getVersionCode().hashCode());
        result = prime * result + ((getOnlineTime() == null) ? 0 : getOnlineTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDownloadUrl() == null) ? 0 : getDownloadUrl().hashCode());
        result = prime * result + ((getForcedUpdating() == null) ? 0 : getForcedUpdating().hashCode());
        result = prime * result + ((getCreatTime() == null) ? 0 : getCreatTime().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getReadme() == null) ? 0 : getReadme().hashCode());
        return result;
    }
}