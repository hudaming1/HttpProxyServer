package org.hum.httpproxyserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.hum.httpproxyserver.component.WebHelper;
import org.hum.httpproxyserver.message.HttpRequest;

public class ProxyTask implements Runnable {
	
	private final String CONNECT_OK = "HTTP/1.1 200 Connection Established\r\n\r\n";
	private Socket local;
	private Socket remote;
	
	private static final ThreadPoolExecutor ThreadPool = new ThreadPoolExecutor(50, 200, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	public ProxyTask(Socket socket) {
		this.local = socket;
	}

	@Override
	public void run() {
		System.out.println("enter task");
		try {
			// 从InputStream中读取到TargetHost和Port，以及RequestMethod
			InputStream inputStream = local.getInputStream();
			HttpRequest request = WebHelper.parse(inputStream);
			System.out.println(request);
			
			// local -> remote
			remote = new Socket(request.getHost(), request.getPort());
			ThreadPool.execute(new PipeChannel(local, remote));
			
			// process https
			if ("CONNECT".equals(request.getMethod())) {
				local.getOutputStream().write(CONNECT_OK.getBytes());
				local.getOutputStream().flush();
			} else {
				remote.getOutputStream().write(request.getFullText().getBytes());
				remote.getOutputStream().flush();
			}
			
			// remote -> local
			ThreadPool.execute(new PipeChannel(remote, local));
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}
}
