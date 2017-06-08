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

public class DecryptFileTest {
	private RSADemo rsa = null;
	private AESDemo aes = null;
	private RSAPrivateKey rsaPrivateKey = null;
	private static DecryptFileTest decryptFileTest = new DecryptFileTest();//单例模式
	
	public RSAPrivateKey getRsaPrivateKey() {
		return rsaPrivateKey;
	}

	public void setRsaPrivateKey(RSAPrivateKey rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}
	public void initRSAPrivateKey(File rsaPrivateKeyFile) throws ClassNotFoundException, IOException
	{
		   String filename = rsaPrivateKeyFile.getName();
		   int aesKeyEncryptTimes = Integer.parseInt(filename.substring(filename.lastIndexOf(".")+1, filename.length()));
		   System.out.println("文件名aesKeyEncryptTimes:" + aesKeyEncryptTimes);
		   rsa.setAesKeyEncryptTimes(aesKeyEncryptTimes);
		   
		   
		   filename = filename.substring(0, filename.lastIndexOf("."));
		   int aesKeyBytesLength = Integer.parseInt(filename.substring(filename.lastIndexOf(".")+1, filename.length()));
		   System.out.println("文件名aesKeyBytesLength:" + aesKeyBytesLength);
		   rsa.setAesKeyBytesLength(aesKeyBytesLength);
		   
		   RSAPrivateKey privateKey = rsa.getRSAPrivateKeyDeserialize(rsaPrivateKeyFile);
		   this.setRsaPrivateKey(privateKey);
	}
	
	public static DecryptFileTest getDecryptFileTest()
	{
		return decryptFileTest;
	}
	
	private DecryptFileTest()
	{
		rsa = new RSADemo();
		aes = new AESDemo();
		try {
			rsa.initRSACipher(false);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeySpecException | ClassNotFoundException
				| IOException e) {
			e.printStackTrace();
		}
		try {
			aes.initAESCipher(false);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 解密模块
	 */
	public void decrypt(File decryptFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, ClassNotFoundException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		/*********AES解密文件*********/
		 //  File srcFile = new File("D:\\jdk.exe.AESEncrypt");
		//   File file = new File("D:\\RSAPrivateSerialize.16.1");
	       SecretKey aesKey = rsa.decryptAESKey(decryptFile, this.getRsaPrivateKey());
	       aes.setAESKey(aesKey);
		   aes.decryptFile(decryptFile);
		   System.out.println("AES解密文件完成！");
	}
	
}


