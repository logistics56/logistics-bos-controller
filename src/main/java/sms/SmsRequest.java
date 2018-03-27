package sms;
/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月25日 下午4:39:04
* 
*/
public class SmsRequest {
	
	private String telephone;
	
	private String username;
	
	private int source;
	
	private String checkcode;
	
	private String password;
	
	private String email;
	
	private String activecode;
	
	private int select;
	
	private String telephone2;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}
 
	public String getCheckcode() {
		return checkcode;
	}

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getActivecode() {
		return activecode;
	}

	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}
	
	public int getSelect() {
		return select;
	}

	public void setSelect(int select) {
		this.select = select;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	@Override
	public String toString() {
		return "SmsRequest [telephone=" + telephone + ", username=" + username + ", source=" + source + ", checkcode="
				+ checkcode + ", password=" + password + ", email=" + email + ", activecode=" + activecode + ", select="
				+ select + ", telephone2=" + telephone2 + "]";
	}


}
