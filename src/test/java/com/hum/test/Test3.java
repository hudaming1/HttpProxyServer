package com.hum.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Test3 {

	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("www.baidu.com", 80);
		InputStream inputStream = socket.getInputStream();
		int read = -1;
		while ((read = inputStream.read()) != -1) {
			System.out.println(read);
		}
		
		System.out.println("over");
	}
}
