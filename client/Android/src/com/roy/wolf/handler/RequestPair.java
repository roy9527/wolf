package com.roy.wolf.handler;

import org.apache.http.NameValuePair;

public class RequestPair implements NameValuePair {

	private String name = "";
	private String value = "";
	
	public RequestPair(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		String str = name + "=" + value;
		return str;
	}
}
