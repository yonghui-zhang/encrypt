package com.fileoperate;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.encrypt.DecryptFileTest;
import com.utils.Utils;

public class DecryptAllFiles extends SearchFile {

	private File rsaPrivateKeyFile = null;
	
	public File getRsaPrivateKeyFile() {
		return rsaPrivateKeyFile;
	}
	public void setRsaPrivateKeyFile(File rsaPrivateKeyFile) {
		this.rsaPrivateKeyFile = rsaPrivateKeyFile;
	}
	@Override
	public void doSomeThings(File file) {
	   System.out.println("���ܲ���");
	   if(!Utils.ifEncrypt(file))
   	   {
   		 System.out.println("���ļ�������Ч�ļ����ļ���");
   		 return ;
   	}
   	   DecryptFileTest decryptFileTest = DecryptFileTest.getDecryptFileTest();
       try {
		decryptFileTest.decrypt(file);
	   } catch (InvalidKeyException | NoSuchAlgorithmException
			| NoSuchPaddingException | InvalidKeySpecException
			| ClassNotFoundException | IllegalBlockSizeException
			| BadPaddingException | IOException e) {
		e.printStackTrace();
	   }
	}
	public static void main(String[] args) {
		DecryptAllFiles decryptAllFiles = new DecryptAllFiles();
		
		String filePath = "D:\\dosbox";
	//	decryptAllFiles.deepFirstTravel(filePath);
	//	System.out.println("������ȱ�������������" + decryptAllFiles.getDeepFolderNum() 
	//			+ "���ļ���;" + decryptAllFiles.getDeepFileNum() + "���ļ�.");
		decryptAllFiles.breadthFirstTraver(filePath);
		System.out.println("������ȱ�������������" + decryptAllFiles.getBreadthFolderNum() 
				+ "���ļ���;" + decryptAllFiles.getBreadthFileNum() + "���ļ�.");
	}

}
