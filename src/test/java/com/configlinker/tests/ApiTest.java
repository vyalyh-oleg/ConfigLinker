package com.configlinker.tests;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


class ApiTest {

	@Test
	void PropertyFileTest() throws InterruptedException {
		System.out.println("PropertyFileTest");
		Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
			add(PropertyFileConfig.class);
		}};
		ConfigSet configSet = ConfigSetFactory.create(interfaces);


		PropertyFileConfig config = configSet.getConfigObject(PropertyFileConfig.class);


		System.out.println(config.getBasicName());
		System.out.println(config.getEmailArray().getClass().getSimpleName() + " : " + Arrays.toString(config.getEmailArray()));
		System.out.println(config.getEmailList().getClass().getSimpleName() + " : " + config.getEmailList());
		System.out.println(config.getKeyWithSpace());
		System.out.println(config.getWithSymbols());

//		Thread.sleep(15000);
		System.out.println(config.getEmailArray().getClass().getSimpleName() + " : " + Arrays.toString(config.getEmailArray()));
		System.out.println();
	}

	@Test
	void ClasspathTest() throws InterruptedException {
		System.out.println("ClasspathTest");
		Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
			add(ClasspathFileConfig.class);
		}};
		ConfigSet configSet = ConfigSetFactory.create(interfaces);


		ClasspathFileConfig config = configSet.getConfigObject(ClasspathFileConfig.class);

		System.out.println(config.getBasicName());
		System.out.println(config.getEmailArray().getClass().getSimpleName() + " : " + Arrays.toString(config.getEmailArray()));
		System.out.println(config.getEmailList().getClass().getSimpleName() + " : " + config.getEmailList());
		System.out.println(config.getKeyWithSpace());
		System.out.println(config.getWithSymbols());


//		Thread.sleep(15000);
		System.out.println(config.getEmailArray().getClass().getSimpleName() + " : " + Arrays.toString(config.getEmailArray()));
		System.out.println();
	}

	@Test
	void httpNetworkTest()
	{
		System.out.println("httpNetworkTest");
		Set<Class<?>> interfaces = new HashSet<Class<?>>() {{
			add(HttpConfig.class);
		}};
		ConfigSet configSet = ConfigSetFactory.create(interfaces);
		HttpConfig httpConfig = configSet.getConfigObject(HttpConfig.class);

		Assertions.assertEquals(httpConfig.getNumber(), 1);
		Assertions.assertEquals(httpConfig.getString(), "one");
	}
}
