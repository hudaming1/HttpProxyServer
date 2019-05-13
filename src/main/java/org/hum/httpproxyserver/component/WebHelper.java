package org.hum.httpproxyserver.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.hum.httpproxyserver.message.HttpRequest;

public class WebHelper {

	public static HttpRequest parse(InputStream inputStream) throws IOException {
		StringBuilder fullText = new StringBuilder();
		HttpRequest httpRequest = new HttpRequest();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		
		String startLine = br.readLine();
		fullText.append(startLine).append("\n");
		
		httpRequest.setMethod(startLine.split(" ")[0]);
		
		// read headers
		String headerLine = null;
		while (!(headerLine = br.readLine()).equals("")) {
			fullText.append(headerLine).append("\n");
			if (headerLine.startsWith("Host")) {
				String[] hostHeader = headerLine.split(":");
				httpRequest.setHost(hostHeader[1].trim());
				if ("CONNECT".equals(httpRequest.getMethod())) {
					httpRequest.setPort(hostHeader.length == 3 ? Integer.parseInt(hostHeader[2]) : 443);
				} else {
					httpRequest.setPort(hostHeader.length == 3 ? Integer.parseInt(hostHeader[2]) : 80);
				}
			}
		}
		
		httpRequest.setFullText(fullText.toString());
		
		return httpRequest;
	}
}
