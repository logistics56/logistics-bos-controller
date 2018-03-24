package com.logistics.module.request;

import java.util.Arrays;
import java.util.Date;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月22日 下午2:04:24
* 
*/
public class FixedAreaRequest {
    private String cId;

    private String cCompany;

    private String cFixedAreaLeader;

    private String cFixedAreaName;

    private String cOperatingCompany;

    private Date cOperatingTime;

    private String cOperator;

    private String cTelephone;

    private String operat;
    
    private String[] associationCustomerIds;
    
    private String[] noassociationCustomerIds;
    
    private String customerFixedAreaId;
    
    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcCompany() {
        return cCompany;
    }

    public void setcCompany(String cCompany) {
        this.cCompany = cCompany;
    }

    public String getcFixedAreaLeader() {
        return cFixedAreaLeader;
    }

    public void setcFixedAreaLeader(String cFixedAreaLeader) {
        this.cFixedAreaLeader = cFixedAreaLeader;
    }

    public String getcFixedAreaName() {
        return cFixedAreaName;
    }

    public void setcFixedAreaName(String cFixedAreaName) {
        this.cFixedAreaName = cFixedAreaName;
    }

    public String getcOperatingCompany() {
        return cOperatingCompany;
    }

    public void setcOperatingCompany(String cOperatingCompany) {
        this.cOperatingCompany = cOperatingCompany;
    }

    public Date getcOperatingTime() {
        return cOperatingTime;
    }

    public void setcOperatingTime(Date cOperatingTime) {
        this.cOperatingTime = cOperatingTime;
    }

    public String getcOperator() {
        return cOperator;
    }

    public void setcOperator(String cOperator) {
        this.cOperator = cOperator;
    }

    public String getcTelephone() {
        return cTelephone;
    }

    public void setcTelephone(String cTelephone) {
        this.cTelephone = cTelephone;
    }

	public String getOperat() {
		return operat;
	}

	public void setOperat(String operat) {
		this.operat = operat;
	}

	public String[] getAssociationCustomerIds() {
		return associationCustomerIds;
	}

	public void setAssociationCustomerIds(String[] associationCustomerIds) {
		this.associationCustomerIds = associationCustomerIds;
	}

	public String[] getNoassociationCustomerIds() {
		return noassociationCustomerIds;
	}

	public void setNoassociationCustomerIds(String[] noassociationCustomerIds) {
		this.noassociationCustomerIds = noassociationCustomerIds;
	}

	public String getCustomerFixedAreaId() {
		return customerFixedAreaId;
	}

	public void setCustomerFixedAreaId(String customerFixedAreaId) {
		this.customerFixedAreaId = customerFixedAreaId;
	}

	@Override
	public String toString() {
		return "FixedAreaRequest [cId=" + cId + ", cCompany=" + cCompany + ", cFixedAreaLeader=" + cFixedAreaLeader
				+ ", cFixedAreaName=" + cFixedAreaName + ", cOperatingCompany=" + cOperatingCompany
				+ ", cOperatingTime=" + cOperatingTime + ", cOperator=" + cOperator + ", cTelephone=" + cTelephone
				+ ", operat=" + operat + ", associationCustomerIds=" + Arrays.toString(associationCustomerIds)
				+ ", noassociationCustomerIds=" + Arrays.toString(noassociationCustomerIds) + ", customerFixedAreaId="
				+ customerFixedAreaId + "]";
	}

}
