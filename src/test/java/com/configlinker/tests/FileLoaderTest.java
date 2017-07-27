package com.configlinker.tests;

import org.junit.jupiter.api.Test;


class FileLoaderTest {


	public static void main(String[] args) throws InterruptedException {
		ApiTest apiTest = new ApiTest();

		apiTest.PropertyFileTest();
		apiTest.ClasspathTest();

//		Thread.currentThread().join();
	}
}