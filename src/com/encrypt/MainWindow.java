package com.encrypt;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.fileoperate.DecryptAllFiles;
import com.fileoperate.EncryptAllFiles;
import com.utils.ComputeProcess;
import com.utils.Utils;

/*
 * 主窗体
 */
public class MainWindow implements ActionListener{
	
	private File rsaPrivateKeyFile = null;
	private int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	private int windowWidth = 600;
	private int windowHeight = 600;
	private JProgressBar progressBar = null;//进度条
	private JLabel currentSpeed = null;
	private JLabel remainTime = null;
	private JLabel remainSize = null;
	private ComputeProcess computeProcess = new ComputeProcess();
	public MainWindow()
	{
		JFrame frame = new JFrame("永辉加密宝");
		JPanel panel = new JPanel();
		frame.add(panel);
		
		JButton encryptBtn = new JButton("加密文件");
		encryptBtn.setActionCommand("encryptBtn");
		encryptBtn.addActionListener(this);
		panel.add(encryptBtn);
		JButton decryptBtn = new JButton("解密文件");
		decryptBtn.setActionCommand("decryptBtn");
		decryptBtn.addActionListener(this);
		panel.add(decryptBtn);
		JButton rsaKeyBtn = new JButton("RSA私钥文件");
		rsaKeyBtn.setActionCommand("rsaKeyBtn");
		rsaKeyBtn.addActionListener(this);
		panel.add(rsaKeyBtn);
		
		progressBar = new JProgressBar();
		progressBar.setBackground(Color.BLACK);//进度条背景颜色
		progressBar.setForeground(Color.RED);//进度条颜色
		progressBar.setStringPainted(true);//进度条显示文字
		panel.add(progressBar);
		
		remainTime = new JLabel();
		double remaintime = 0.0;
		remainTime.setText("预计剩余时间:" + remaintime);
		panel.add(remainTime);
		
		currentSpeed = new JLabel();
		double currentspeed = 0.0;
		currentSpeed.setText("当前速度:" + currentspeed);
		panel.add(currentSpeed);
		
		remainSize = new JLabel();
		double remainsize = 0.0;
		remainSize.setText("当前加密情况:" + remainsize);
		panel.add(remainSize);
		
		//frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setBounds((screenWidth-windowWidth)/2, (screenHeight-windowHeight)/2, 
				windowWidth, windowHeight);
		
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/hacker.jpg"));
		frame.setIconImage(imageIcon.getImage());
		
	
		//frame.setLocation((screenWidth-windowWidth)/2, (screenHeight-windowHeight)/2);
		
		panel.setBackground(Color.PINK);
		
	}

	public void init()
	{
		this.computeProcess.setProgressBar(this.progressBar);
		this.computeProcess.setCurrentSpeed(this.currentSpeed);
		this.computeProcess.setRemainSize(this.remainSize);
		this.computeProcess.setRemainTime(this.remainTime);
	}
	public static void main(String[] args) {
		 new MainWindow().init();;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		new Thread()
    	{
    		public void run()
    		{
    			for(int i=0;i<101;i++)
    			{
    				try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    				progressBar.setValue(i);
    			}
    			progressBar.setString("升级完成");
    		}
    	}.start();
		*/
		
		
		
	   if(e.getActionCommand() == "decryptBtn")
	   {
		   if(this.rsaPrivateKeyFile == null)
		   {
			   System.out.println("还没有选择RSA私钥文件，无法解密");
			   return ;
		   }
	   }
	   JFileChooser fileChooser = new JFileChooser();
	   fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	   fileChooser.showDialog(new JLabel(), "选择文件");
	   File file = fileChooser.getSelectedFile();
	   if(e.getActionCommand() == "encryptBtn")
	   {
		   this.computeProcess.setSourceFile(file);
		   this.computeProcess.init();
		   Thread encryptThread = new Thread(this.computeProcess);
		   encryptThread.start();//开始进度条活动
		  
		   if(file.isDirectory())
		    {
		    	System.out.println("加密文件夹:" + file.getAbsolutePath());
		    	//循环遍历文件夹加密，在文件夹同级目录下产生密钥文件
		    	EncryptAllFiles encryptAllFiles = new EncryptAllFiles();
		    	encryptAllFiles.breadthFirstTraver(file.getAbsolutePath());
		    	System.out.println("广度优先遍历加密：共搜索到" + encryptAllFiles.getBreadthFolderNum()
						+ "个文件夹;" + encryptAllFiles.getBreadthFileNum() + "个文件.");
		    }else if(file.isFile())
		    {
		    	System.out.println("加密文件:" + file.getAbsolutePath());
		    	//只加密单个文件
		    	EncryptAllFiles encryptFile = new EncryptAllFiles();
		    	encryptFile.doSomeThings(file);
		    }
		    System.out.println(file.getName());
		    //加密完成提示音
		    try {
				Utils.playRemindSound();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		    //把RSA密钥文件发送到QQ邮箱
			RSAEncryptAES rsaEncrypt = RSAEncryptAES.getRSAEncryptAES();
			try {
			   rsaEncrypt.sendRSAPrivateKeyToQQMail();
			} catch (IOException | MessagingException e1) {
				e1.printStackTrace();
			}
		   
	   }else if(e.getActionCommand() == "decryptBtn")
	   {
		   if(file.isDirectory())
		    {
		    	System.out.println("解密文件夹:" + file.getAbsolutePath());
		    	//利用同级目录下的密钥文件循环遍历文件夹并进行挨个文件解密
		    	DecryptAllFiles decryptAllFiles = new DecryptAllFiles();
		    	DecryptFileTest decryptFileTest = DecryptFileTest.getDecryptFileTest();
		    	try {
					decryptFileTest.initRSAPrivateKey(this.rsaPrivateKeyFile);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
		    	decryptAllFiles.breadthFirstTraver(file.getAbsolutePath());
				System.out.println("广度优先遍历解密：共搜索到" + decryptAllFiles.getBreadthFolderNum() 
						+ "个文件夹;" + decryptAllFiles.getBreadthFileNum() + "个文件.");
		    	
		    }else if(file.isFile())
		    {
		    	System.out.println("解密文件:" + file.getAbsolutePath());
		    	//只解密单个文件
		    	DecryptAllFiles decryptFile = new DecryptAllFiles();
		    	DecryptFileTest decryptFileTest = DecryptFileTest.getDecryptFileTest();
		    	try {
					decryptFileTest.initRSAPrivateKey(this.rsaPrivateKeyFile);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
		    	decryptFile.doSomeThings(file);
		    }
		    System.out.println(file.getName());
		    //解密完成提示音
		    try {
				Utils.playRemindSound();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		   
	   }else if(e.getActionCommand() == "rsaKeyBtn")
	   {
		   if(file.isDirectory())
		    {
		    	System.out.println("文件夹:" + file.getAbsolutePath());
		    	//提示密钥是文件，不是文件夹
		    	
		    }else if(file.isFile())
		    {
		    	System.out.println("文件:" + file.getAbsolutePath());
		    	//选择了密钥文件,判断密钥文件的格式
		    	String filename = file.getName();
		    	if(filename.lastIndexOf("RSAPrivateSerialize") != -1)
		    	{
		    		this.rsaPrivateKeyFile = file;
		    		return ;
		    	}else{
		    		System.out.println("该文件不是有效的RSA私钥文件！");
		    		return ;
		    	}
		    }
		    System.out.println(file.getName());
		   
	   }
	}
	
}
