package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Utils {
	/*
	 * ��ȡ����IP
	 */
	public static String getLocalIp() throws UnknownHostException
	{
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();
		System.out.println("����ip:" + ip);
		return ip;
		
	}
	/*
	 * ��ȡ����������
	 */
	public static String getLocalHostName() throws UnknownHostException
	{
		InetAddress addr = InetAddress.getLocalHost();
		String hostname = addr.getHostName();
		System.out.println("������������" + hostname);
		return hostname;
	}
	/*
	 * �ж��ļ��Ƿ����
	 */
	public static boolean ifEncrypt(File file)
	{
		String filename = file.getName();
		if(filename.lastIndexOf(".") == -1)//����A�ļ���û�к�׺���϶�û�м���
		{
			return false;
		}
		String suffix = filename.substring(filename.lastIndexOf("."), filename.length());
		
		if(suffix.equals(".AESEncrypt"))
		{
			return true;
		}
		return false;
	}
	/*
	 * �������������ʾ��
	 */
	
	public static void playRemindSound() throws IOException
	{
		/****����·����������****/
		String filepath = Utils.class.getResource("/sound/remind.wav").getFile();
		filepath = URLDecoder.decode(filepath, "UTF-8");//�������·������
		System.out.println("filepath:" + filepath);
		InputStream is = new FileInputStream(filepath);
		AudioStream as = new AudioStream(is);
		AudioPlayer.player.start(as);
	}
	
	public static void main(String[] args) throws IOException
	{
		Utils.playRemindSound();
	}

}
