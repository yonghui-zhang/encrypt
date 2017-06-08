package com.encrypt;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/*
 * 发送RSA私钥序列化文件
 */
public class SendRSAPrivateKey {
	
	private Properties properties = new Properties();
	private Session session = null;
	private Message message = null;
	private Multipart multipart = null;
	private void initProperties()
	{
		properties.put("maill.transport.protocol", "smtp");//连接协议
		properties.put("mail.smtp.host", "smtp.qq.com");//主机名
		properties.put("mail.smtp.port", 465);//端口号
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");//设置是否使用ssl安全连接
		properties.put("mail.debug", "true");//设置是否显示debug信息
		properties.setProperty("mail.transport.protocol", "smtp");//收邮件的协议
		
	}
	private void addRSAPrivateKeySerialize(String title, String content, File rsaPrivateKeySerialize) throws AddressException, MessagingException, UnsupportedEncodingException
	{
		this.initProperties();//初始化properties
		session = Session.getInstance(properties);//得到会话对象
		message = new MimeMessage(session);//获取邮件对象
		message.setFrom(new InternetAddress("3228482956@qq.com"));//设置发件人地址
		/*****设置收件人地址****/
		message.setRecipients(RecipientType.TO, new InternetAddress[]{new InternetAddress("1798916974@qq.com")
		/*,new InternetAddress("498060111@qq.com")*/});
		message.setSubject(title);//邮件标题
		/*****添加附件****/
		/****向multipart中添加邮件的各个部分内容，包括文本内容和附件*****/
		multipart = new MimeMultipart();
		//添加邮件正文
		BodyPart contentPart = new MimeBodyPart();
		contentPart.setContent(content, "text/html;charset=UTF-8");
		multipart.addBodyPart(contentPart);
		//添加附件的内容
		//File attachment = new File("D:\\zyhfile.txt");
		if(!rsaPrivateKeySerialize.exists())
		{
			System.out.println("附件不存在");
			return ;
		}
		BodyPart attachmentBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(rsaPrivateKeySerialize);
		attachmentBodyPart.setDataHandler(new DataHandler(source));
		attachmentBodyPart.setFileName(MimeUtility.encodeWord(rsaPrivateKeySerialize.getName()));
		multipart.addBodyPart(attachmentBodyPart);
		message.setContent(multipart);
		message.saveChanges();
	}
	public void sendQQMail(String title, String content, File rsaPrivateKeySerialize) throws AddressException, MessagingException, UnsupportedEncodingException
	{
		this.addRSAPrivateKeySerialize(title, content, rsaPrivateKeySerialize);
		//得到邮差对象
		Transport transport = session.getTransport();
		transport.connect("3228482956@qq.com", "qfnfdretdpuschac");//授权码
		transport.sendMessage(message, message.getAllRecipients());//发送邮件
		System.out.println("发送邮件完成！");
	}

}
