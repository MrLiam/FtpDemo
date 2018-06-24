package com.ldl.test.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 将数据保存到队列中（生产者）
 * @author Administrator
 *
 */
public class SaveDataToQueue implements Runnable{
	private ConcurrentLinkedQueue<String> queue;
	private String data;
	
	public SaveDataToQueue(ConcurrentLinkedQueue<String> queue, String data) {
		this.queue = queue;
		this.data = data;
	}

	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:hh:ss sss");
		data = sdf.format(new Date())+":"+data;
		System.out.println(data);
		queue.add(data);
	}
	
}
