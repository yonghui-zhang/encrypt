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
	 * 获取本机IP
	 */
	public static String getLocalIp() throws UnknownHostException
	{
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();
		System.out.println("本机ip:" + ip);
		return ip;
		
	}
	/*
	 * 获取本机主机名
	 */
	public static String getLocalHostName() throws UnknownHostException
	{
		InetAddress addr = InetAddress.getLocalHost();
		String hostname = addr.getHostName();
		System.out.println("本机主机名：" + hostname);
		return hostname;
	}
	/*
	 * 判断文件是否加密
	 */
	public static boolean ifEncrypt(File file)
	{
		String filename = file.getName();
		if(filename.lastIndexOf(".") == -1)//类似A文件，没有后缀，肯定没有加密
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
	 * 播放任务完成提示音
	 */
	
	public static void playRemindSound() throws IOException
	{
		/****中文路径会有乱码****/
		String filepath = Utils.class.getResource("/sound/remind.wav").getFile();
		filepath = URLDecoder.decode(filepath, "UTF-8");//解决中文路径乱码
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
