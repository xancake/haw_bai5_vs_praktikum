package org.haw.vs.praktikum.gwln.rest.client;

import java.util.Objects;

public class AbstractRestClient {
	private String _url;
	private String _endpoint;
	
	public AbstractRestClient(String url, String endpoint) {
		_endpoint = Objects.requireNonNull(endpoint);
		_url = url.endsWith(_endpoint) ? url : url + _endpoint;
	}
	
	public String getUrl() {
		return _url;
	}
	
	public String getEndpoint() {
		return _endpoint;
	}
}
