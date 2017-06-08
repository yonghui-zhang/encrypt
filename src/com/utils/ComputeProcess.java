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
	private long files_total_size = 0;//���зǼ����ļ����ܴ�С
	private long files_encrypt_size = 0;//�����Ѽ����ļ����ܴ�С
	private int filenum = 0;//�ļ�����
	private long encryptSize = 0;//����֮���ܴ�С
	private long first_encryptSize = 0;//��һ��
	private boolean ifencrypt = true;
	public int maxvalue_times = 0;
	
	private File sourceFile = null;//Դ�ļ�
	private File encryptFile = null;//�����ļ�
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
	 * ��ʼ��
	 */
	public void init()
	{
		this.computeEncryptJob(this.sourceFile);
		this.setFirst_encryptSize(this.encryptSize);
		/***���ܻᷢ���쳣**/
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
	 * �����������
	 * ���Դ�ļ���С+Դ�ļ�����*128 
	 */
	public void computeEncryptJob(File srcFile)
	{
		
		this.deepFirstTravel(srcFile.getAbsolutePath());
		filenum = this.getDeepFileNum();//ȡ���ļ�����
	//	System.out.println(filenum);
		//���Դ�ļ���С
	//	System.out.println(this.files_total_size);
	//	System.out.println(this.files_encrypt_size);
		encryptSize = this.files_total_size + filenum*128;
	}
	/*
	 * �����������
	 * ��ü����ļ� - �ļ�����*128
	 */
	public void computeDecryptJob(File encryptFile)
	{
		
	}
	@Override
	public void run() {
	
	/***���㲢ʵʱ���½���������ʾ��Ϣ**/
	while(this.currentValue < this.maxValue)
	{
			
		if(ifencrypt)
		{
			/**����**/
			     /***���������ʾ***/
			this.remainSize.setText("�Ѽ���/δ����: " + 
			       this.memoryUnitConvert(this.files_encrypt_size) + "/" +
			       this.memoryUnitConvert(this.files_total_size));
			System.out.println("�Ѽ���/δ����: " + 
				       this.memoryUnitConvert(this.files_encrypt_size) + "/" +
				       this.memoryUnitConvert(this.files_total_size));
			   /***��ǰ�����ٶ�****/
			long before_files_encrypt_size = this.files_encrypt_size;
			this.files_encrypt_size = 0;//���ϴεļ�¼��0
			this.files_total_size = 0;//���ϴεļ�¼��0
			long startTime = System.nanoTime();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//����1��
			this.computeEncryptJob(this.sourceFile);
			long endTime = System.nanoTime();//1���� = 10�ڷ�֮һ��
			System.out.println("end-start:" + (endTime-startTime));
			double seconds = ((double)(endTime-startTime))/1000000000;//ת������
			System.out.println("seconds:" + seconds);
			double current_encrypt_speed = ((double)(this.files_encrypt_size - before_files_encrypt_size))/seconds;
			this.currentSpeed.setText("�����ٶȣ�" + this.memoryUnitConvert((long) current_encrypt_speed) + "/s");  
			System.out.println("�����ٶȣ�" + this.memoryUnitConvert((long) current_encrypt_speed) + "/s");
			/****Ԥ��ʣ��ʱ��****/
			this.remainTime.setText("Ԥ��ʣ��ʱ��:" + this.timeUnitConvert((long) (this.encryptSize /current_encrypt_speed)));
			System.out.println("Ԥ��ʣ��ʱ��:" + this.timeUnitConvert((long) (this.encryptSize /current_encrypt_speed)));
			/*****���½�����****/
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
			/**����**/
		}
	  }
	}
	
	public String timeUnitConvert(long seconds)
	{
		double MINUTE = 60;//1���� =60��
		double HOUR = 3600;//1Сʱ=60*60
		double DAY = 86400;//һ�� = 60*60*24 ��
		
		if(seconds < MINUTE)//1����
		{
			return new String("Ԥ�ƻ���" + seconds + "��");
		}else if(seconds < HOUR)//1Сʱ
		{
			return new String("Ԥ�ƻ���" + String.format("%.1f", (double)(seconds/MINUTE)) + "����");
		}else if(seconds < DAY)//һ��
		{
			return new String("Ԥ�ƻ���" + String.format("%.1f", (double)(seconds/HOUR)) + "Сʱ");
		}else{//����һ��
			return new String("Ԥ�ƴ���1��");
		}
		
	}
	/*
	 * �����ڴ浥λ���� 1M=1024KB=1024*1024B
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
		return "����̫��";
		
	}

	@Override
	public void doSomeThings(File file) {
		
		if(Utils.ifEncrypt(file))
		{
			//�Ǽ����ļ�
			this.files_encrypt_size = this.files_encrypt_size + file.length();
		}else{
			/***�������зǼ����ļ����ܴ�С****/
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
