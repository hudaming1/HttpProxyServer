package org.hum.httpproxyserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PipeChannel implements Runnable {

	private final byte[] buffer = new byte[4096];
	private Socket inSocket;
	private Socket outSocket;

	public PipeChannel(Socket inSocket, Socket outSocket) {
		this.inSocket = inSocket;
		this.outSocket = outSocket;
	}

	@Override
	public void run() {
		try {
			InputStream inputStream = inSocket.getInputStream();
			OutputStream outputStream = outSocket.getOutputStream();
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				if (outSocket.isClosed()) {
					break;
				}
				outputStream.write(buffer, 0 , len);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!inSocket.isClosed()) {
				try {
					inSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!outSocket.isClosed()) {
				try {
					outSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
