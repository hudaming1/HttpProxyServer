package org.hum.httpproxyserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpProxyServer {

	private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(150, 400, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
	private int LISTENING_PORT = 8002;
	private ServerSocket server;
	
	public HttpProxyServer(int port) {
		this.LISTENING_PORT = port;
	}
	
	public void start() throws IOException {
		server = new ServerSocket(LISTENING_PORT);
		while (true) {
			Socket socket = server.accept();
			THREAD_POOL.execute(new ProxyTask(socket));
		}
	}
	
	public void stop() throws IOException {
		server.close();
	}
}
