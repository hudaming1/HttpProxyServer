package com.hum.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class HttpPostTest {

	public static void main(String[] args) throws IOException {
		byte[] buffer = new byte[4096];
		ServerSocket server = new ServerSocket(8888);
		Socket socket = server.accept();
		int len = 0;
		InputStream inputStream = socket.getInputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			if (buffer.length == 4096) {
				System.out.print(new String(buffer));
			} else {
				byte[] tmp = Arrays.copyOf(buffer, len);
				System.out.print(new String(tmp));
			}
		}
		socket.getOutputStream().write("OK".getBytes());
		socket.getOutputStream().flush();
		socket.close();
		server.close();
	}
}