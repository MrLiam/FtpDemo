package com.ldl.test.domain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 将队列中的数据写进文件中（消费者）
 * @author Administrator
 *
 */
public class PutDataToFile implements Runnable {
	private ConcurrentLinkedQueue<String> queue;
	private BufferedWriter buff;
	
	public PutDataToFile(ConcurrentLinkedQueue<String> queue, BufferedWriter buff) {
		super();
		this.queue = queue;
		this.buff = buff;
	}



	@Override
	public void run() {
		while(true){
			if(!queue.isEmpty()){
				try {
					buff.write(queue.poll());
					buff.newLine();
					buff.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		}
		
		
	}

}
