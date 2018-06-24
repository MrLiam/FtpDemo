package com.ldl.test.controller;

import java.io.File;
import java.io.IOException;

import com.ldl.test.util.FtpUtil;

public class FtpController {

	public static void main(String[] args) {

		FtpUtil ftpUtil = new FtpUtil("UTF-8");  
        try {
			ftpUtil.connect("", 21, "qiangdiao_ftp", "qiangdiao_ftp_test");
			//0871/yidong/action_YYYYMMDD
	        //ftpUtil.setTimeOut(60, 60, 60);  
	        /*ftpUtil.upload("/home/testuser/文件1.txt", new File("E:/image/FTPClient/FTPClient测试/文件1.txt"));  
	        ftpUtil.download("/home/testuser/文件1.txt", new File("E:/image/FTPClient/FTPClient测试/文件1.txt"));  
	        ftpUtil.uploadDir("/home/testuser/FTPClient测试", "E:/image/FTPClient/FTPClient测试");  
	        ftpUtil.downloadDir("/home/testuser/FTPClient测试", "E:/image/FTPClient/FTPClient测试"); */ 
	          
	        //把远程文件读入到流中
	        /*ByteArrayOutputStream bos = new ByteArrayOutputStream(); //自动增长  
	        ftpUtil.retrieveFile("/home/testuser/文件1.txt", bos);  
	        System.out.println(bos.size());  
	        String contentStr = new String(bos.toByteArray(),"GBK");  
	        System.out.println(contentStr);*/  
			
			//打印父目录
			//System.out.println(ftpUtil.printParentDirectory());
			//打印工作目录
			//System.out.println(ftpUtil.printWorkingDirectory());
			
			//在工作目录下创建目录     注：只能一级一级创建，并要切换目录后才能创建下级目录
			/*String newDirectory = "0871";
			ftpUtil.makeDirectory(newDirectory);
			ftpUtil.changeWorkingDirectory("0871");
			System.out.println(ftpUtil.printWorkingDirectory());
			ftpUtil.makeDirectory("yidong");*/
			
			/*ftpUtil.changeWorkingDirectory("0871/yidong");//注：切换目录时，不能用文件File.separator,直接用 '/',不然会失败
			System.out.println(ftpUtil.printWorkingDirectory());*/
			
        	//封装创建多层目录
        	//System.out.println(ftpUtil.createDirectory("", 21, "qiangdiao_ftp", "qiangdiao_ftp_test", "mutiple/test/haha"));
			
        	/*System.out.println("------上传开始---------");
        	//切换目录
        	//ftpUtil.changeWorkingDirectory("/home/ftp/qiangdiao/0871/yidong");
        	ftpUtil.upload("/home/ftp/qiangdiao/0871/yidong/action_201806175.zip", new File(System.getProperty("user.dir")+File.separator+"upload"+File.separator+"action_201806175.zip"));  
        	System.out.println("-------上传结束----------");*/
			
			//如果没有切换工作目录就要写绝对目录
			//ftpUtil.download("/home/ftp/qiangdiao/0871/yidong/action_201806175.zip", new File(System.getProperty("user.dir")+File.separator+"upload"+File.separator+"ftp_download.zip"));  
        	
			ftpUtil.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

}
