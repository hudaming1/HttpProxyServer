package org.hum.httpproxyserver;

import java.io.IOException;

import org.hum.httpproxyserver.server.HttpProxyServer;

public class ApplicationStart {

	public static void main(String[] args) throws IOException {
		/**
		 * 现在两个问题没有解决
		 * 1.close事件到底是怎么触发的？inSocket正read呢，Server怎么就close了？
		 * 2.https://news.baidu.com为什么还是访问不了
		 */
		HttpProxyServer proxyServer = new HttpProxyServer(8002);
		proxyServer.start();
	}
}
