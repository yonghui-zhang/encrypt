package com.fileoperate;

import java.io.File;
import java.util.LinkedList;

public class SearchFile{
	
     private int deepFileNum = 0;
     private int deepFolderNum = 0;
     
     private int breadthFileNum = 0;
     private int breadthFolderNum = 0;
     
	public int getDeepFileNum() {
		return deepFileNum;
	}
	
	public int getDeepFolderNum() {
		return deepFolderNum;
	}
	public int getBreadthFileNum() {
		return breadthFileNum;
	}
	public int getBreadthFolderNum() {
		return breadthFolderNum;
	}

	/*
	 * �ݹ������ļ���(�������)
	 */
	public void deepFirstTravel(String filePath)
	{
		File root = new File(filePath);
		if(!root.exists())
		{
			System.out.println("�ļ�" + root.getAbsolutePath() + "������");
			return ;
		}
		File[] files = root.listFiles();
		for(File file:files)
		{
			if(file.isDirectory())
			{
				this.deepFolderNum++;
				deepFirstTravel(file.getAbsolutePath());
			}else{
				this.deepFileNum++;
				System.out.println("�ļ���Ϊ:" + file.getName());
				doSomeThings(file);
			}
		}
	}
	/*
	 * �ǵݹ������ļ���(�������)
	 */
	public void breadthFirstTraver(String filePath)
	{
		File root = new File(filePath);
		if(!root.exists())
		{
			System.out.println("�ļ�" + root.getAbsolutePath() + "������");
		}
		
		LinkedList<File> list = new LinkedList<File>();
		File[] files = root.listFiles();
		for(File file:files)
		{
			if(file.isDirectory())
			{
				this.breadthFolderNum++;
				list.add(file);
			}else{
				this.breadthFileNum++;
				System.out.println("�ļ�����" + file.getName());
				doSomeThings(file);
			}
		}
		while(!list.isEmpty())
		{
			 File temp_file = list.removeFirst();
			 files = temp_file.listFiles();
			 for(File file2 : files)
			 {
			      if(file2.isDirectory())
			      {
			    	  this.breadthFolderNum++;
			    	  list.add(file2);
			      }else{
			    	  this.breadthFileNum++;
			    	  System.out.println("�ļ�����" + file2.getName());
			    	  doSomeThings(file2);
			      }
			}
		}

	}
	/*
	 * ��һЩ����
	 */
	public void doSomeThings(File file) {
	
	}
	public static void main(String[] args) {
	
		SearchFile searchFile = new SearchFile();
		String filePath = "D:\\dosbox";
		searchFile.deepFirstTravel(filePath);
		System.out.println("������ȱ�������������" + searchFile.getDeepFolderNum() 
				+ "���ļ���;" + searchFile.getDeepFileNum() + "���ļ�.");
		
	//	searchFile.breadthFirstTraver(filePath);

	}

	

}
