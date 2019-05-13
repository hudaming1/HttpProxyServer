package org.hum.httpproxyserver.message;

import lombok.Data;

@Data
public class HttpRequest {

	private String method;
	private String host;
	private Integer port;
	private String fullText;
	
	@Override
	public String toString() {
		return fullText;
	}
}
