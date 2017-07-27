package com.configlinker.loaders;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


final class ConfigLinkerThreadFactory implements ThreadFactory {
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final String namePrefix;

	public ConfigLinkerThreadFactory(String name) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = "ConfigLinker-" + name + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(true);
		t.setPriority(3);
		return t;
	}
}
