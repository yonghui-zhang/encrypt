package com.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SerializeDemo {
	
	/*
	 * �������л�
	 */
	public static void objectSerialize(File sourceFile, Object object) throws IOException
	{
		if(!sourceFile.exists())
		{
			System.out.println("���л��ļ�δ������");
			return ;
		}
		/****�������л���*****/
		OutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;
		
		outputStream = new FileOutputStream(sourceFile);
		objectOutputStream = new ObjectOutputStream(outputStream);
		
		objectOutputStream.writeObject(object);
		
		
		objectOutputStream.close();
		outputStream.close();
		System.out.println("�������л���ɣ�");
	}
	/*
	 * �������л�
	 */
	public static Object objectDeserialize(File sourceFile) throws IOException, ClassNotFoundException
	{
		if(!sourceFile.exists())
		{
			System.out.println("�����л��ļ������ڣ�");		
			return null;
		}
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		
		inputStream = new FileInputStream(sourceFile);
		objectInputStream = new ObjectInputStream(inputStream);
		Object object = objectInputStream.readObject();
		
		objectInputStream.close();
		inputStream.close();
		System.out.println("�����л����");
		return object;
	}
	

}
