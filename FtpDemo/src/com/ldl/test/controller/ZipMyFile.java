package com.ldl.test.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ldl.test.util.ZipFileUtil;

public class ZipMyFile {

	public static void main(String[] args) {
		// 第一个参数是需要压缩的源路径；第二个参数是压缩文件的目的路径，这边需要将压缩的文件名字加上去
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
		//String zipFileName = "0871"+File.separator+"yidong"+File.separator+"action_"+sdf.format(new Date())+".zip";
		String zipFileName = "action_"+sdf.format(new Date())+".zip";
		File file = new File(System.getProperty("user.dir")+File.separator+"upload"+File.separator+zipFileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ZipFileUtil.compress(System.getProperty("user.dir")+File.separator+"upload"+File.separator+"liam.txt", System.getProperty("user.dir")+File.separator+"upload"+File.separator+zipFileName);
		
		
	}

}
