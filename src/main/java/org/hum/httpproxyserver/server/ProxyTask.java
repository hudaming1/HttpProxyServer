package org.hum.httpproxyserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ProxyTask implements Runnable {
	
	private Socket local;
	private Socket remote;
	
	public ProxyTask(Socket socket) {
		this.local = socket;
	}

	@Override
	public void run() {
		
		// 从InputStream中读取到TargetHost和Port，以及RequestMethod
		try {
			InputStream inputStream = local.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
