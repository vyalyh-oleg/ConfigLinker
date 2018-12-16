package com.configlinker.tests.httpserver;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class SimpleThreadFactory implements ThreadFactory
{
	private ThreadFactory threadFactory = Executors.defaultThreadFactory();
	private final String poolName;
	private final boolean isDaemon;
	private int threadNumber = 0;
	
	public SimpleThreadFactory(String poolName, boolean isDaemon)
	{
		this.poolName = poolName;
		this.isDaemon = isDaemon;
	}
	
	@Override
	public Thread newThread(Runnable r)
	{
		Thread t = threadFactory.newThread(r);
		t.setName(poolName + "_pool-thread-" + threadNumber++);
		t.setDaemon(isDaemon);
		return t;
	}
}
