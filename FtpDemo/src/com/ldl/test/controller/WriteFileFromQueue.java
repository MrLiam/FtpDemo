package com.ldl.test.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ldl.test.domain.PutDataToFile;
import com.ldl.test.domain.SaveDataToQueue;

public class WriteFileFromQueue {

	public static void main(String[] args) {

		//获取当前类所在的包
		/*File fileB = new File(WriteFileFromQueue.class.getResource("").getPath());
		System.out.println("fileB path: " + fileB); */
		//获取当前类所在的工程名
//		System.out.println(System.getProperty("user.dir"));
		FileWriter fw= null;
    	BufferedWriter bufw= null;
        try {
        	File file = new File(System.getProperty("user.dir")+File.separator+"upload"+File.separator+"liam.txt");
        	if(!file.exists()){
        		//file.mkdirs();
        		file.createNewFile();
        	}
			fw=new FileWriter(file,true);  
			bufw=new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		} 
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        
        for(int i = 0; i < 10; i++){
        	new Thread(new SaveDataToQueue(queue, "写入数据为：  "+i)).start();
        }
        new Thread(new PutDataToFile(queue, bufw)).start();;
		
	}

}
