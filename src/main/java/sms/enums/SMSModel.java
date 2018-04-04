package sms.enums;
/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月4日 下午12:47:51
* 
*/
public enum SMSModel {
	SMS_128646170("SMS_128646170", "注册获取验证码"),
	SMS_129740906("SMS_129740906", "通用接受验证码模板"),
	SMS_129764430("SMS_129764430","提醒取件");
	    ;
	    private String valve;

	    private String msg;

	    private SMSModel(String valve, String msg) {
	        this.valve = valve;
	        this.msg = msg;
	    }

	    public String getValve() {
	        return valve;
	    }

	    public String getMsg() {
	        return msg;
	    }
}
