package sms.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.lang.RandomStringUtils;

public class MailUtils {
	private static String smtp_host = "smtp.163.com"; // 网易
	private static String from = "lizhn95@163.com"; // 使用当前账户
	private static String username = "lizhn95@163.com"; // 邮箱账户
	private static String password = "lizhen1995"; // 邮箱授权码
	
	public static String activeUrl = "http://localhost:8081/logistics-bos-controller/customer/activeMail";

	public static void sendMail(String subject, String content, String to) {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setContent(content, "text/html;charset=utf-8");
			Transport transport = session.getTransport();
			transport.connect(smtp_host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}

	public static void main(String[] args) {
		
		// 生成激活码
		String activecode = RandomStringUtils.randomNumeric(32);
		System.out.println(activecode);
		String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"
				+ MailUtils.activeUrl + "?telephone=15555555555&activecode=" + activecode + "'>速运快递邮箱绑定地址</a>";
		sendMail("駃达快递测试激活邮件", content, "1594064654@qq.com");
		
		System.out.println("成功");
		
	}
}
