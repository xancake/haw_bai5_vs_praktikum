package org.haw.vs.praktikum.gwln.rest.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class AbstractRestClient {
	private URL _url;
	
	public AbstractRestClient(String url, String endpoint) throws MalformedURLException {
		this(new URL(url.endsWith(endpoint) ? url : url + endpoint));
	}
	
	public AbstractRestClient(URL url, String endpoint) throws MalformedURLException {
		this(new URL(url, endpoint));
	}
	
	public AbstractRestClient(URL url) {
		_url = Objects.requireNonNull(url);
	}
	
	public URL getURL() {
		return _url;
	}
}
