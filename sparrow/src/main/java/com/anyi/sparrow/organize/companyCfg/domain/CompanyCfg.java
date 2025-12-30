package com.anyi.sparrow.organize.companyCfg.domain;

import java.io.Serializable;
import java.util.Date;

public class CompanyCfg implements Serializable {
    private Long id;

    private Long companyId;

    private String operatorId;

    private Long wxEtcTypeId;

    private Date createTime;

    private Date updateTime;

    private Long etcTypeId;

    private String qlNumber;

    private Integer skipLic;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public Long getWxEtcTypeId() {
        return wxEtcTypeId;
    }

    public void setWxEtcTypeId(Long wxEtcTypeId) {
        this.wxEtcTypeId = wxEtcTypeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getEtcTypeId() {
        return etcTypeId;
    }

    public void setEtcTypeId(Long etcTypeId) {
        this.etcTypeId = etcTypeId;
    }

    public String getQlNumber() {
        return qlNumber;
    }

    public void setQlNumber(String qlNumber) {
        this.qlNumber = qlNumber == null ? null : qlNumber.trim();
    }

    public Integer getSkipLic() {
        return skipLic;
    }

    public void setSkipLic(Integer skipLic) {
        this.skipLic = skipLic;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", companyId=").append(companyId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", wxEtcTypeId=").append(wxEtcTypeId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", etcTypeId=").append(etcTypeId);
        sb.append(", qlNumber=").append(qlNumber);
        sb.append(", skipLic=").append(skipLic);
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
        CompanyCfg other = (CompanyCfg) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getWxEtcTypeId() == null ? other.getWxEtcTypeId() == null : this.getWxEtcTypeId().equals(other.getWxEtcTypeId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getEtcTypeId() == null ? other.getEtcTypeId() == null : this.getEtcTypeId().equals(other.getEtcTypeId()))
            && (this.getQlNumber() == null ? other.getQlNumber() == null : this.getQlNumber().equals(other.getQlNumber()))
            && (this.getSkipLic() == null ? other.getSkipLic() == null : this.getSkipLic().equals(other.getSkipLic()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getWxEtcTypeId() == null) ? 0 : getWxEtcTypeId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getEtcTypeId() == null) ? 0 : getEtcTypeId().hashCode());
        result = prime * result + ((getQlNumber() == null) ? 0 : getQlNumber().hashCode());
        result = prime * result + ((getSkipLic() == null) ? 0 : getSkipLic().hashCode());
        return result;
    }
}