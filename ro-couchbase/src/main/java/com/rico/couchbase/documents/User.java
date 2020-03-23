package com.rico.couchbase.documents;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

import ro.common.couchbase.CBDoc;

/**
 * Sample Couchbase document
 * 
 * @author r.krishnakumar
 *
 */
@Document
public class User extends CBDoc {

	@Field
	private String firstname;

	@Field
	private String lastname;

	public User(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}
}
