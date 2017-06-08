package com.utils;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.fileoperate.SearchFile;

public class ComputeProcess extends SearchFile implements Runnable{
	private JProgressBar progressBar = null;
	private int maxValue = 0;
	private int currentValue = 0;
	
	private JLabel currentSpeed = null;
	private JLabel remainTime = null;
	private JLabel remainSize = null;
	private long files_total_size = 0;//所有非加密文件的总大小
	private long files_encrypt_size = 0;//所有已加密文件的总大小
	private int filenum = 0;//文件个数
	private long encryptSize = 0;//加密之后总大小
	private long first_encryptSize = 0;//第一次
	private boolean ifencrypt = true;
	public int maxvalue_times = 0;
	
	private File sourceFile = null;//源文件
	private File encryptFile = null;//加密文件
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	
	public void setFirst_encryptSize(long first_encryptSize) {
		this.first_encryptSize = first_encryptSize;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public void setEncryptFile(File encryptFile) {
		this.encryptFile = encryptFile;
	}
	
	public JLabel getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(JLabel currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public JLabel getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(JLabel remainTime) {
		this.remainTime = remainTime;
	}

	public JLabel getRemainSize() {
		return remainSize;
	}

	public void setRemainSize(JLabel remainSize) {
		this.remainSize = remainSize;
	}
     
	/*
	 * 初始化
	 */
	public void init()
	{
		this.computeEncryptJob(this.sourceFile);
		this.setFirst_encryptSize(this.encryptSize);
		/***可能会发生异常**/
		long maxvalue = this.first_encryptSize + this.files_encrypt_size;
		int times = 0;
		while(maxvalue > (long)Integer.MAX_VALUE)
		{
			maxvalue = maxvalue/1024;
			times++;
		}
		this.maxvalue_times = times;
		this.progressBar.setMaximum((int)maxvalue);
		this.maxValue = (int)maxvalue;
		this.progressBar.setMinimum(0);
	}

	/*
	 * 计算加密任务
	 * 获得源文件大小+源文件个数*128 
	 */
	public void computeEncryptJob(File srcFile)
	{
		
		this.deepFirstTravel(srcFile.getAbsolutePath());
		filenum = this.getDeepFileNum();//取得文件个数
	//	System.out.println(filenum);
		//获得源文件大小
	//	System.out.println(this.files_total_size);
	//	System.out.println(this.files_encrypt_size);
		encryptSize = this.files_total_size + filenum*128;
	}
	/*
	 * 计算解密任务
	 * 获得加密文件 - 文件个数*128
	 */
	public void computeDecryptJob(File encryptFile)
	{
		
	}
	@Override
	public void run() {
	
	/***计算并实时更新进度条和显示信息**/
	while(this.currentValue < this.maxValue)
	{
			
		if(ifencrypt)
		{
			/**加密**/
			     /***加密情况显示***/
			this.remainSize.setText("已加密/未加密: " + 
			       this.memoryUnitConvert(this.files_encrypt_size) + "/" +
			       this.memoryUnitConvert(this.files_total_size));
			System.out.println("已加密/未加密: " + 
				       this.memoryUnitConvert(this.files_encrypt_size) + "/" +
				       this.memoryUnitConvert(this.files_total_size));
			   /***当前加密速度****/
			long before_files_encrypt_size = this.files_encrypt_size;
			this.files_encrypt_size = 0;//将上次的记录清0
			this.files_total_size = 0;//将上次的记录清0
			long startTime = System.nanoTime();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//休眠1秒
			this.computeEncryptJob(this.sourceFile);
			long endTime = System.nanoTime();//1纳秒 = 10亿分之一秒
			System.out.println("end-start:" + (endTime-startTime));
			double seconds = ((double)(endTime-startTime))/1000000000;//转换成秒
			System.out.println("seconds:" + seconds);
			double current_encrypt_speed = ((double)(this.files_encrypt_size - before_files_encrypt_size))/seconds;
			this.currentSpeed.setText("加密速度：" + this.memoryUnitConvert((long) current_encrypt_speed) + "/s");  
			System.out.println("加密速度：" + this.memoryUnitConvert((long) current_encrypt_speed) + "/s");
			/****预计剩余时间****/
			this.remainTime.setText("预计剩余时间:" + this.timeUnitConvert((long) (this.encryptSize /current_encrypt_speed)));
			System.out.println("预计剩余时间:" + this.timeUnitConvert((long) (this.encryptSize /current_encrypt_speed)));
			/*****更新进度条****/
			int times = 0;
			long value = this.files_encrypt_size;
			
			while(times < this.maxvalue_times)
			{
				value = value/1024;
				times++;
			}
			this.progressBar.setValue((int)value);
			this.currentValue = (int)value;
		}else{
			/**解密**/
		}
	  }
	}
	
	public String timeUnitConvert(long seconds)
	{
		double MINUTE = 60;//1分钟 =60秒
		double HOUR = 3600;//1小时=60*60
		double DAY = 86400;//一天 = 60*60*24 秒
		
		if(seconds < MINUTE)//1分钟
		{
			return new String("预计还需" + seconds + "秒");
		}else if(seconds < HOUR)//1小时
		{
			return new String("预计还需" + String.format("%.1f", (double)(seconds/MINUTE)) + "分钟");
		}else if(seconds < DAY)//一天
		{
			return new String("预计还需" + String.format("%.1f", (double)(seconds/HOUR)) + "小时");
		}else{//大于一天
			return new String("预计大于1天");
		}
		
	}
	/*
	 * 智能内存单位换算 1M=1024KB=1024*1024B
	 */
	public String memoryUnitConvert(long size)
	{
		long VALUE_1024 = (long)1024;//1024
		long VALUE_1024_2 = (long)1048576;//1024*1024
		long VALUE_1024_3 = (long)1073741824.0;//1024*1024*1024
		long VALUE_1024_4 = (long)1099511627776.0;//1024*1024*1024*1024
		if(size < VALUE_1024)//1KB
		{
			double newsize = size/(1024*1.0);
			return new String(String.format("%.1f",newsize) + "KB");
		}else if(size < VALUE_1024_2)//1M
		{
			double newsize = size/(1024*1.0);
			return new String(String.format("%.1f",newsize) + "KB");
		    
		}else if(size < VALUE_1024_3)//1G
		{
			double newsize = size/(1024*1.0)/(1024*1.0);
			return new String(String.format("%.1f",newsize) + "M");
			
		}else if(size < VALUE_1024_4)//1T
		{
			double newsize = size/(1024*1.0)/(1024*1.0)/(1024*1.0);
			return new String(String.format("%.1f",newsize) + "G");
		}
		return "容量太大";
		
	}

	@Override
	public void doSomeThings(File file) {
		
		if(Utils.ifEncrypt(file))
		{
			//是加密文件
			this.files_encrypt_size = this.files_encrypt_size + file.length();
		}else{
			/***计算所有非加密文件的总大小****/
			this.files_total_size = this.files_total_size + file.length();
		}
	}

	public static void main(String[] args)
	{
//	   ComputeProcess computeProcess = new ComputeProcess();
	///   File file = new File("D:\\masm");
	//   computeProcess.computeEncryptJob(file);
	 //  System.out.println(file.length());
		long size = (long)1024*1024*129*1024;
	//	long size = (long) 1099511627776.0;
	//	System.out.println(computeProcess.memoryUnitConvert(size));
	/*	long start = System.nanoTime();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.nanoTime();
		System.out.println((double)(end-start));
		double seconds = ((double) (end-start))/(1000000000);
		System.out.println(seconds);*/
		long a = 100;
		double b = 8;
		System.out.println(a/b);
		
	}
}
