package com.cs440.backend.backendapp3.objects;

public class ExampleObject {

	private final long id;
	private final String content;

	public ExampleObject(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}