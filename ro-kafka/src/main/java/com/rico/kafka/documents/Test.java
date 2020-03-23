package com.rico.kafka.documents;

import ro.common.utils.Doc;

/**
 * Sample document to share using the queue service. Place these files in common
 * place to avoid the deserialization errors.
 * 
 * @author r.krishnakumar
 *
 */
public class Test implements Doc {

	private String id;

	private String testValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTestValue() {
		return testValue;
	}

	public void setTestValue(String testValue) {
		this.testValue = testValue;
	}
}
