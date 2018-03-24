package com.logistics.module.response;

import com.logistics.module.dto.AreaDTO;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月24日 下午1:23:34
* 
*/
public class SubAreaResponse {
    private String cId;

    private String cAssistKeyWords;

    private String cEndnum;

    private String cKeyWords;

    private String cSingle;

    private String cStartNum;

    private String cAreaId;

    private String cFixedareaId;
    
    private AreaDTO area;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcAssistKeyWords() {
        return cAssistKeyWords;
    }

    public void setcAssistKeyWords(String cAssistKeyWords) {
        this.cAssistKeyWords = cAssistKeyWords;
    }

    public String getcEndnum() {
        return cEndnum;
    }

    public void setcEndnum(String cEndnum) {
        this.cEndnum = cEndnum;
    }

    public String getcKeyWords() {
        return cKeyWords;
    }

    public void setcKeyWords(String cKeyWords) {
        this.cKeyWords = cKeyWords;
    }

    public String getcSingle() {
        return cSingle;
    }

    public void setcSingle(String cSingle) {
        this.cSingle = cSingle;
    }

    public String getcStartNum() {
        return cStartNum;
    }

    public void setcStartNum(String cStartNum) {
        this.cStartNum = cStartNum;
    }

    public String getcAreaId() {
        return cAreaId;
    }

    public void setcAreaId(String cAreaId) {
        this.cAreaId = cAreaId;
    }

    public String getcFixedareaId() {
        return cFixedareaId;
    }

    public void setcFixedareaId(String cFixedareaId) {
        this.cFixedareaId = cFixedareaId;
    }
    
    public AreaDTO getArea() {
		return area;
	}

	public void setArea(AreaDTO area) {
		this.area = area;
	}

}
