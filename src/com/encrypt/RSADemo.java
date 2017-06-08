 
package com.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/*
 * RAS加密算法demo
 */
public class RSADemo {

	private Cipher cipher = null;
	private KeyPairGenerator keyPairGenerator = null;
	private KeyPair keyPair = null;
	private RSAPublicKey publicKey = null;
	private RSAPrivateKey privateKey = null;
	
	private int aesKeyBytesLength = 0;
	private static int aesKeyEncryptTimes = 0;
	public RSAPublicKey getRSAPublicKey()
	{
		return publicKey;
	}
	public RSAPrivateKey getRSAPrivateKey()
	{
		return privateKey;
	}
	/*
	 * 反序列化得到RSA私钥
	 */
	public RSAPrivateKey getRSAPrivateKeyDeserialize(File serializeFile) throws ClassNotFoundException, IOException
	{
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)SerializeDemo.objectDeserialize(serializeFile);
		serializeFile.delete();
		System.out.println("RSA密钥文件已删除");
		return rsaPrivateKey;
	}
	
	/*
	 * 得到私钥序列化文件
	 */
	public File getRSAPrivateKeySerialize() throws IOException
	{
		File serializeFile = new File("D:\\RSAPrivateSerialize." + this.aesKeyBytesLength +"." + this.aesKeyEncryptTimes);
		if(!serializeFile.exists())
		{
			serializeFile.createNewFile();
		}
		SerializeDemo.objectSerialize(serializeFile, privateKey);
		return serializeFile;
	}
	public int getAesKeyBytesLength() {
		return aesKeyBytesLength;
	}
	public void setAesKeyBytesLength(int aesKeyBytesLength) {
		this.aesKeyBytesLength = aesKeyBytesLength;
	}
	
	public static int getAesKeyEncryptTimes() {
		return aesKeyEncryptTimes;
	}
	public void setAesKeyEncryptTimes(int aesKeyEncryptTimes) {
		this.aesKeyEncryptTimes = aesKeyEncryptTimes;
	}
	public void initRSACipher(boolean createRandomKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException, ClassNotFoundException
	{
		if(createRandomKey)
		{
		  keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		  keyPairGenerator.initialize(1024);
		  keyPair = keyPairGenerator.generateKeyPair();
		  publicKey = (RSAPublicKey)keyPair.getPublic();
		  privateKey = (RSAPrivateKey)keyPair.getPrivate();
		}
		cipher = Cipher.getInstance("RSA");
		
		/******打印公钥和私钥********/
/*		System.out.println("公钥：(n, e) ("+ publicKey.getModulus() + "," + publicKey.getPublicExponent() + ")");
		System.out.println("私钥：(n, d) ("+ privateKey.getModulus() + "," + privateKey.getPrivateExponent() + ")");
		
		System.out.println("公钥byte[]" + Base64.encodeBase64String(publicKey.getEncoded()));
		System.out.println("私钥byte[]" + Base64.encodeBase64String(privateKey.getEncoded()));
		
		
		/****公钥恢复****/
	/*	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey2 = (RSAPublicKey)keyFactory.generatePublic(keySpec);
		System.out.println("公钥恢复后：" + publicKey2.getModulus() + "," + publicKey2.getPublicExponent());
		
		
		/****私钥恢复***/
	/*	PKCS8EncodedKeySpec keySpec2 = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		RSAPrivateKey privateKey2 = (RSAPrivateKey)keyFactory.generatePrivate(keySpec2);
		System.out.println("私钥恢复后：" + privateKey2.getModulus() + "," + privateKey2.getPrivateExponent());
		
		/****公钥和私钥的序列化****/
	/*	File sourceFile = new File("D:\\serialize.txt");
		if(sourceFile == null)
		{
			sourceFile.createNewFile();
		}
		OutputStream outputStream  = null;
		ObjectOutputStream objectOutputStream = null;
		
		outputStream = new FileOutputStream(sourceFile);
		objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(publicKey);
		objectOutputStream.writeObject(privateKey);
		objectOutputStream.close();
		outputStream.close();
		System.out.println("公钥和私钥序列化完成");
		
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		
		inputStream = new FileInputStream(sourceFile);
		objectInputStream = new ObjectInputStream(inputStream);
		RSAPublicKey publicKey3 = (RSAPublicKey)objectInputStream.readObject();
		RSAPrivateKey privateKey3 = (RSAPrivateKey)objectInputStream.readObject();
		objectInputStream.close();
		inputStream.close();
		System.out.println("公钥反序列化后：" + publicKey3.getModulus() + "," + publicKey3.getPublicExponent());
		System.out.println("私钥反序列化后：" + privateKey3.getModulus() + "," + privateKey3.getPrivateExponent());
	*/
	}
	/*
	 * 加密文件
	 */
	public File encryptFile(File sourceFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException 
	{
		File encryptFile = null;
		encryptFile = new File("D:\\encryptRSA_jdk.exe");
		encryptFile.createNewFile();
		
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		inputStream = new FileInputStream(sourceFile);
		outputStream = new FileOutputStream(encryptFile);
		//根据公钥，对Cipher对象进行初始化
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	    
	    byte[] cache = new byte[117];//1024/8 - 11
	    int nRed = 0;
	    while((nRed=inputStream.read(cache)) != -1)
	    {
	    	byte[] result = cipher.doFinal(cache);
	    	//outputStream.write(result, 0, nRed);会产生少写的情况，导致解密失败
	    	outputStream.write(result, 0, result.length);
	    	outputStream.flush();
	    }
	    inputStream.close();
	    outputStream.close();
		
		return encryptFile;
	}
	/*
	 * 加密AES密钥
	 */
	public void encryptAESKey(File sourceFile, SecretKey aesKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
	{
		    if(!sourceFile.exists())
		    {
		    	System.out.println("要加密的文件不存在");
		    	return ;
		    }
		    File encryptFile = new File(sourceFile.getPath() + ".AESEncrypt");
		    System.out.println("encryptAESKey:" + encryptFile.getPath());
		    System.out.println(encryptFile.exists());
		    if(!encryptFile.exists())
		    {
		    	encryptFile.createNewFile();
		    	System.out.println("新文件已经创建");
		    }
		    OutputStream outputStream = null;
		    outputStream = new FileOutputStream(encryptFile);
		    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		    
		    byte[] src = aesKey.getEncoded();
		    this.setAesKeyBytesLength(src.length);
		    
			int flag = 0;
			int times = 0;
			byte[] buffer = new byte[117];
			
			while(src.length - flag >= 117)
			{
				/***分段加密***/
				System.out.println("分段加密执行");
				System.arraycopy(src, flag, buffer, 0, buffer.length);
				byte[] result = cipher.doFinal(buffer);
				outputStream.write(result, 0, result.length);
				flag = flag + 117;
				times++;
			}
			System.arraycopy(src, flag, buffer, 0, src.length-flag);
			byte[] result = cipher.doFinal(buffer);
			System.out.println("en result.length:" + result.length);
			outputStream.write(result, 0, result.length);
			times++;
			this.setAesKeyEncryptTimes(times);
			outputStream.close();
	}
	/*
	 * 解密AES密钥
	 */
	public SecretKey decryptAESKey(File sourceFile, RSAPrivateKey rsaPrivateKey) throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
	{
		if(!sourceFile.exists())
		{
			System.out.println("解密文件不存在！");
			return null;
		}
		InputStream inputStream = null;
		inputStream = new FileInputStream(sourceFile);
		cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
		
		byte[] src = new byte[this.getAesKeyBytesLength()];
		System.out.println("getAesKeyBytesLength():" + this.getAesKeyBytesLength());
		int flag = 0;
		int times = 0;
		
		byte[] buffer = new byte[128];
		int nRed = 0;
		
		while((nRed = inputStream.read(buffer)) > 0 && times < this.getAesKeyEncryptTimes())
		{
			byte[] result = cipher.doFinal(buffer);
			System.out.println("result.length:" + result.length);
			System.arraycopy(result, 0, src, flag, src.length);
			flag = flag + result.length;
			times++;
		}
		inputStream.close();
		SecretKey aesKey = new SecretKeySpec(src, "AES");
		return aesKey;
	}
	/*
	 * 解密
	 */
	public File decryptFile(File sourceFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
	{
		File decryptFile = null;
		decryptFile = new File("D:\\decryptRSA_jdk.exe");
		decryptFile.createNewFile();
		
		InputStream inputStream = null;
		OutputStream outputStream = null;
		inputStream = new FileInputStream(sourceFile);
		outputStream = new FileOutputStream(decryptFile);
		//根据公钥，对Cipher对象进行初始化
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] buffer = new byte[128];//1024/8
		int r = 0;
		while((r = inputStream.read(buffer)) > 0)
		{
			byte[] result = cipher.doFinal(buffer);
			outputStream.write(result, 0, result.length);
			outputStream.flush();
		}
		inputStream.close();
		outputStream.close();
		
		return decryptFile;
	}
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException, ClassNotFoundException {
		RSADemo rsa = new RSADemo();
		rsa.initRSACipher(true);
		/*File sourceFile = new File("D:\\jdk.exe");
		File encryptFile = rsa.encrypt(sourceFile);
		System.out.println("RSA加密完成");
		rsa.decrypt(encryptFile);
		System.out.println("RSA解密完成");*/
	
	
	}

}