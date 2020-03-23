package com.rico.kafka.utils;

public enum QueueTopics {

	TEST("test"), USER("user");

	private String val;

	QueueTopics(String val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return val;
	}

}
