package com.anyi.sparrow.assist.system.domain;

import java.io.Serializable;
import java.util.Date;

public class ReportErros implements Serializable {
    private Long id;

    private Long etcTypeId;

    private String erroCnt;

    private String erroCode;

    private String clientCnt;

    private Integer erroCount;

    private Integer erroCurCount;

    private Byte canHandle;

    private String reasonHandle;

    private Byte callCenterAvail;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEtcTypeId() {
        return etcTypeId;
    }

    public void setEtcTypeId(Long etcTypeId) {
        this.etcTypeId = etcTypeId;
    }

    public String getErroCnt() {
        return erroCnt;
    }

    public void setErroCnt(String erroCnt) {
        this.erroCnt = erroCnt == null ? null : erroCnt.trim();
    }

    public String getErroCode() {
        return erroCode;
    }

    public void setErroCode(String erroCode) {
        this.erroCode = erroCode == null ? null : erroCode.trim();
    }

    public String getClientCnt() {
        return clientCnt;
    }

    public void setClientCnt(String clientCnt) {
        this.clientCnt = clientCnt == null ? null : clientCnt.trim();
    }

    public Integer getErroCount() {
        return erroCount;
    }

    public void setErroCount(Integer erroCount) {
        this.erroCount = erroCount;
    }

    public Integer getErroCurCount() {
        return erroCurCount;
    }

    public void setErroCurCount(Integer erroCurCount) {
        this.erroCurCount = erroCurCount;
    }

    public Byte getCanHandle() {
        return canHandle;
    }

    public void setCanHandle(Byte canHandle) {
        this.canHandle = canHandle;
    }

    public String getReasonHandle() {
        return reasonHandle;
    }

    public void setReasonHandle(String reasonHandle) {
        this.reasonHandle = reasonHandle == null ? null : reasonHandle.trim();
    }

    public Byte getCallCenterAvail() {
        return callCenterAvail;
    }

    public void setCallCenterAvail(Byte callCenterAvail) {
        this.callCenterAvail = callCenterAvail;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", etcTypeId=").append(etcTypeId);
        sb.append(", erroCnt=").append(erroCnt);
        sb.append(", erroCode=").append(erroCode);
        sb.append(", clientCnt=").append(clientCnt);
        sb.append(", erroCount=").append(erroCount);
        sb.append(", erroCurCount=").append(erroCurCount);
        sb.append(", canHandle=").append(canHandle);
        sb.append(", reasonHandle=").append(reasonHandle);
        sb.append(", callCenterAvail=").append(callCenterAvail);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
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
        ReportErros other = (ReportErros) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getEtcTypeId() == null ? other.getEtcTypeId() == null : this.getEtcTypeId().equals(other.getEtcTypeId()))
            && (this.getErroCnt() == null ? other.getErroCnt() == null : this.getErroCnt().equals(other.getErroCnt()))
            && (this.getErroCode() == null ? other.getErroCode() == null : this.getErroCode().equals(other.getErroCode()))
            && (this.getClientCnt() == null ? other.getClientCnt() == null : this.getClientCnt().equals(other.getClientCnt()))
            && (this.getErroCount() == null ? other.getErroCount() == null : this.getErroCount().equals(other.getErroCount()))
            && (this.getErroCurCount() == null ? other.getErroCurCount() == null : this.getErroCurCount().equals(other.getErroCurCount()))
            && (this.getCanHandle() == null ? other.getCanHandle() == null : this.getCanHandle().equals(other.getCanHandle()))
            && (this.getReasonHandle() == null ? other.getReasonHandle() == null : this.getReasonHandle().equals(other.getReasonHandle()))
            && (this.getCallCenterAvail() == null ? other.getCallCenterAvail() == null : this.getCallCenterAvail().equals(other.getCallCenterAvail()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getEtcTypeId() == null) ? 0 : getEtcTypeId().hashCode());
        result = prime * result + ((getErroCnt() == null) ? 0 : getErroCnt().hashCode());
        result = prime * result + ((getErroCode() == null) ? 0 : getErroCode().hashCode());
        result = prime * result + ((getClientCnt() == null) ? 0 : getClientCnt().hashCode());
        result = prime * result + ((getErroCount() == null) ? 0 : getErroCount().hashCode());
        result = prime * result + ((getErroCurCount() == null) ? 0 : getErroCurCount().hashCode());
        result = prime * result + ((getCanHandle() == null) ? 0 : getCanHandle().hashCode());
        result = prime * result + ((getReasonHandle() == null) ? 0 : getReasonHandle().hashCode());
        result = prime * result + ((getCallCenterAvail() == null) ? 0 : getCallCenterAvail().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}