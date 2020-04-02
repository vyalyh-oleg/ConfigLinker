/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <vyalyh.oleg@gmail.com>, <crispcode.net@gmail.com>, <oleg@crispcode.net>

      Licensed under the Apache License, Version 2.0 (the "License"); you may not
      use this file except in compliance with the License. You may obtain a copy
      of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
      License for the specific language governing permissions and limitations
      under the License.
 */


package net.crispcode.configlinker.loaders;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


final class ConfigLinkerThreadFactory implements ThreadFactory
{
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final String namePrefix;
	
	public ConfigLinkerThreadFactory(String name)
	{
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = "ConfigLinker-" + name + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
	}
	
	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(true);
		t.setPriority(3);
		return t;
	}
}
