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
 * ����RSA˽Կ���л��ļ�
 */
public class SendRSAPrivateKey {
	
	private Properties properties = new Properties();
	private Session session = null;
	private Message message = null;
	private Multipart multipart = null;
	private void initProperties()
	{
		properties.put("maill.transport.protocol", "smtp");//����Э��
		properties.put("mail.smtp.host", "smtp.qq.com");//������
		properties.put("mail.smtp.port", 465);//�˿ں�
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");//�����Ƿ�ʹ��ssl��ȫ����
		properties.put("mail.debug", "true");//�����Ƿ���ʾdebug��Ϣ
		properties.setProperty("mail.transport.protocol", "smtp");//���ʼ���Э��
		
	}
	private void addRSAPrivateKeySerialize(String title, String content, File rsaPrivateKeySerialize) throws AddressException, MessagingException, UnsupportedEncodingException
	{
		this.initProperties();//��ʼ��properties
		session = Session.getInstance(properties);//�õ��Ự����
		message = new MimeMessage(session);//��ȡ�ʼ�����
		message.setFrom(new InternetAddress("3228482956@qq.com"));//���÷����˵�ַ
		/*****�����ռ��˵�ַ****/
		message.setRecipients(RecipientType.TO, new InternetAddress[]{new InternetAddress("1798916974@qq.com")
		/*,new InternetAddress("498060111@qq.com")*/});
		message.setSubject(title);//�ʼ�����
		/*****��Ӹ���****/
		/****��multipart������ʼ��ĸ����������ݣ������ı����ݺ͸���*****/
		multipart = new MimeMultipart();
		//����ʼ�����
		BodyPart contentPart = new MimeBodyPart();
		contentPart.setContent(content, "text/html;charset=UTF-8");
		multipart.addBodyPart(contentPart);
		//��Ӹ���������
		//File attachment = new File("D:\\zyhfile.txt");
		if(!rsaPrivateKeySerialize.exists())
		{
			System.out.println("����������");
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
		//�õ��ʲ����
		Transport transport = session.getTransport();
		transport.connect("3228482956@qq.com", "qfnfdretdpuschac");//��Ȩ��
		transport.sendMessage(message, message.getAllRecipients());//�����ʼ�
		System.out.println("�����ʼ���ɣ�");
	}

}
