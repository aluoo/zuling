package com.anyi.common.address.domain;

import java.io.Serializable;

public class LatestUseAddress implements Serializable {
    private Long id;

    private String biz;

    private Long addressId;

    private Long reqEmpId;

    private Long rspEmpId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz == null ? null : biz.trim();
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getReqEmpId() {
        return reqEmpId;
    }

    public void setReqEmpId(Long reqEmpId) {
        this.reqEmpId = reqEmpId;
    }

    public Long getRspEmpId() {
        return rspEmpId;
    }

    public void setRspEmpId(Long rspEmpId) {
        this.rspEmpId = rspEmpId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", biz=").append(biz);
        sb.append(", addressId=").append(addressId);
        sb.append(", reqEmpId=").append(reqEmpId);
        sb.append(", rspEmpId=").append(rspEmpId);
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
        LatestUseAddress other = (LatestUseAddress) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBiz() == null ? other.getBiz() == null : this.getBiz().equals(other.getBiz()))
                && (this.getAddressId() == null ? other.getAddressId() == null : this.getAddressId().equals(other.getAddressId()))
                && (this.getReqEmpId() == null ? other.getReqEmpId() == null : this.getReqEmpId().equals(other.getReqEmpId()))
                && (this.getRspEmpId() == null ? other.getRspEmpId() == null : this.getRspEmpId().equals(other.getRspEmpId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBiz() == null) ? 0 : getBiz().hashCode());
        result = prime * result + ((getAddressId() == null) ? 0 : getAddressId().hashCode());
        result = prime * result + ((getReqEmpId() == null) ? 0 : getReqEmpId().hashCode());
        result = prime * result + ((getRspEmpId() == null) ? 0 : getRspEmpId().hashCode());
        return result;
    }
}