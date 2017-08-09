package com.configlinker.tests;

class FileLoaderTest {


	public static void main(String[] args) throws InterruptedException {
		ApiTest apiTest = new ApiTest();

		apiTest.PropertyFileTest();
		apiTest.ClasspathTest();

//		Thread.currentThread().join();
	}
}