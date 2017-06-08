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
	 * 递归搜索文件夹(深度优先)
	 */
	public void deepFirstTravel(String filePath)
	{
		File root = new File(filePath);
		if(!root.exists())
		{
			System.out.println("文件" + root.getAbsolutePath() + "不存在");
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
				System.out.println("文件名为:" + file.getName());
				doSomeThings(file);
			}
		}
	}
	/*
	 * 非递归搜索文件夹(广度优先)
	 */
	public void breadthFirstTraver(String filePath)
	{
		File root = new File(filePath);
		if(!root.exists())
		{
			System.out.println("文件" + root.getAbsolutePath() + "不存在");
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
				System.out.println("文件名：" + file.getName());
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
			    	  System.out.println("文件名：" + file2.getName());
			    	  doSomeThings(file2);
			      }
			}
		}

	}
	/*
	 * 做一些事情
	 */
	public void doSomeThings(File file) {
	
	}
	public static void main(String[] args) {
	
		SearchFile searchFile = new SearchFile();
		String filePath = "D:\\dosbox";
		searchFile.deepFirstTravel(filePath);
		System.out.println("深度优先遍历：共搜索到" + searchFile.getDeepFolderNum() 
				+ "个文件夹;" + searchFile.getDeepFileNum() + "个文件.");
		
	//	searchFile.breadthFirstTraver(filePath);

	}

	

}
