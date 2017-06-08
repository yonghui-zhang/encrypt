package com.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESDemo {
	
	private KeyGenerator keyGenerator = null;
	private Cipher cipher = null;
	private SecretKey secretKey = null;
	
	public SecretKey getAESKey()
	{
		return secretKey;
	}
	public void setAESKey(SecretKey secretKey)
	{
		this.secretKey = secretKey;
	}
	public void initAESCipher(boolean createRandomKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
		if(createRandomKey)
		{
		   keyGenerator = KeyGenerator.getInstance("AES");
		   secretKey = keyGenerator.generateKey();
		}
		cipher = Cipher.getInstance("AES");
		/*
		String aesKey = null;
		aesKey = Base64.encodeBase64String(secretKey.getEncoded());
		System.out.println("AES��Կ�ǣ�" + aesKey);
		
		SecretKey key = new SecretKeySpec(secretKey.getEncoded(), "AES");
		aesKey = Base64.encodeBase64String(key.getEncoded());
		System.out.println("AES��Կ2�ǣ�" + aesKey);*/
		
	}
  
	public File encryptFile(File sourceFile, boolean append) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException
	{
		if(!sourceFile.exists())
		{
			System.out.println("Ҫ���ܵ��ļ�������");
			return null;
		}
		File encryptFile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		inputStream = new FileInputStream(sourceFile);
		encryptFile = new File(sourceFile.getPath() + ".AESEncrypt"/*"D:\\jdk_encrypt.zyh"*/);
		System.out.println("encryptFile:" + encryptFile.getPath());
		if(!encryptFile.exists())
		{
			//encryptFile.createNewFile();
			System.out.println("AES��Կ��ʧ��");
			return null;
		}
		outputStream = new FileOutputStream(encryptFile, append);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		//�Լ�����д���ļ�
		CipherInputStream cipherInputStream = new CipherInputStream(inputStream,cipher);
		
		byte[] cache = new byte[1024];
		int nRead = 0;
		while((nRead=cipherInputStream.read(cache)) != -1)
		{
			outputStream.write(cache, 0, nRead);
			//System.out.println(new String(cache));
			outputStream.flush();
		}
		cipherInputStream.close();
		inputStream.close();
		outputStream.close();
		
		sourceFile.delete();//��Դ�ļ�ɾ��
				
	   return encryptFile;	
	}
	
	public File decryptFile(File sourceFile) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException
	{
		if(!sourceFile.exists())
		{
			System.out.println("Ҫ���ܵ��ļ�������");
			return null;
		}
		File decryptFile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		String sourceName = sourceFile.getPath();
		String fileName = sourceName.substring(0, sourceName.lastIndexOf("."));
		decryptFile = new File(fileName/*"D:\\jdk_decrypt.exe"*/);
		if(!decryptFile.exists())
		{
			decryptFile.createNewFile();
		}
	    
	    cipher.init(Cipher.DECRYPT_MODE, secretKey);
		inputStream = new FileInputStream(sourceFile);
		inputStream.skip(RSADemo.getAesKeyEncryptTimes() * 128);//������Կ����
		outputStream = new FileOutputStream(decryptFile);
		CipherInputStream cipherInputStream = new CipherInputStream(inputStream,cipher);
		System.out.println("�����ֽڣ�" + RSADemo.getAesKeyEncryptTimes());
		
		byte[] buffer = new byte[1024];
		int r;
		while((r = cipherInputStream.read(buffer)) > 0)
		{
			outputStream.write(buffer, 0, r);
		}
		
		cipherInputStream.close();
		inputStream.close();
		outputStream.close();
		
		sourceFile.delete();//��Դ�ļ�ɾ��
		
		return decryptFile;
	}
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, InterruptedException {
		File sourceFile = new File("D:\\jdk.exe");
		//����
		AESDemo aes = new AESDemo();
		aes.initAESCipher(true);
		/*
		File encryptFile = aes.encryptFile(sourceFile);
		System.out.println("�������!");
		//Thread.sleep(5000);
		aes.decryptFile(encryptFile);
		System.out.println("�������");
        */
	}

}
