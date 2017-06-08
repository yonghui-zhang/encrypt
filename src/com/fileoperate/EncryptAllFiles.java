package com.fileoperate;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.encrypt.RSAEncryptAES;
import com.utils.Utils;

public class EncryptAllFiles extends SearchFile {
	
	@Override
	public void doSomeThings(File file) {
		System.out.println("加密操作");
		if(Utils.ifEncrypt(file))
    	{
    		System.out.println("文件不能重复加密!");
    		return ;
    	}
    	RSAEncryptAES rsaEncryptAES = RSAEncryptAES.getRSAEncryptAES();
    	try {
			rsaEncryptAES.encrypt(file);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| NoSuchAlgorithmException | NoSuchPaddingException
				| IOException | MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
	
		EncryptAllFiles encryptAllFiles = new EncryptAllFiles();
		String filePath = "D:\\dosbox";
	//	encryptAllFiles.deepFirstTravel(filePath);
	//	System.out.println("深度优先遍历：共搜索到" + encryptAllFiles.getDeepFolderNum() 
	//			+ "个文件夹;" + encryptAllFiles.getDeepFileNum() + "个文件.");
		encryptAllFiles.breadthFirstTraver(filePath);
		System.out.println("广度优先遍历：共搜索到" + encryptAllFiles.getBreadthFolderNum()
				+ "个文件夹;" + encryptAllFiles.getBreadthFileNum() + "个文件.");

	}

}
