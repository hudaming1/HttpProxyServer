package org.hum.httpproxyserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PipeChannel {

	private static final ThreadPoolExecutor ThreadPool = new ThreadPoolExecutor(50, 200, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	public static void buildPipe(Socket clientSocket, Socket serverSocket) {
		Lock requestLock = new ReentrantLock();
		Lock responseLock = new ReentrantLock();
		// client -> server
		ThreadPool.execute(new InPipe(clientSocket, serverSocket, requestLock, responseLock));
		// server -> client
		ThreadPool.execute(new OutPipe(serverSocket, clientSocket, requestLock, responseLock));
	}
	
	private static class InPipe implements Runnable {

		private final byte[] buffer = new byte[4096];
		private Socket clientSocket;
		private Socket serverSocket;
		private Lock requestLock;
		private Lock responseLock;

		public InPipe(Socket clientSocket, Socket serverSocket, Lock requestLock, Lock responseLock) {
			this.clientSocket = clientSocket;
			this.serverSocket = serverSocket;
			this.requestLock = requestLock;
			this.responseLock = responseLock;
		}
		
		@Override
		public void run() {
			try {
				InputStream inputStream = clientSocket.getInputStream();
				OutputStream outputStream = serverSocket.getOutputStream();
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					if (serverSocket.isClosed()) {
						break;
					}
					outputStream.write(buffer, 0 , len);
					outputStream.flush();
				}
				requestLock.unlock();
				responseLock.lock();
//			} catch (SocketException e) {
//				if ("Socket closed".equals(e.getMessage())) {
//					System.out.println("ignore");
//				} else {
//					e.printStackTrace();
//				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (!clientSocket.isClosed()) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private static class OutPipe implements Runnable {

		private final byte[] buffer = new byte[4096];
		private Socket clientSocket;
		private Socket serverSocket;
		private Lock requestLock;
		private Lock responseLock;

		public OutPipe(Socket clientSocket, Socket serverSocket, Lock requestLock, Lock responseLock) {
			this.clientSocket = clientSocket;
			this.serverSocket = serverSocket;
			this.requestLock = requestLock;
			this.responseLock = responseLock;
		}

		@Override
		public void run() {
			try {
				InputStream inputStream = clientSocket.getInputStream();
				OutputStream outputStream = serverSocket.getOutputStream();
				requestLock.lock();
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					if (serverSocket.isClosed()) {
						break;
					}
					outputStream.write(buffer, 0 , len);
					outputStream.flush();
				}

				Thread.sleep(10);
				responseLock.unlock();
//			} catch (SocketException e) {
//				if ("Socket closed".equals(e.getMessage())) {
//					System.out.println("ignore");
//				} else {
//					e.printStackTrace();
//				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (!clientSocket.isClosed()) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
