package org.hum.httpproxyserver;

import java.io.IOException;

import org.hum.httpproxyserver.server.HttpProxyServer;

public class ApplicationStart {

	public static void main(String[] args) throws IOException {
		HttpProxyServer proxyServer = new HttpProxyServer(8002);
		proxyServer.start();
	}
}
