package com.rico.es.documents;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import ro.common.elasticsearch.ESDoc;

/**
 * Elastic search sample document structure
 * 
 * @author r.krishnakumar
 *
 */
@Document(createIndex = true, indexName = "person", shards = 2, replicas = 1)
public class Person extends ESDoc {

	@Field(type = FieldType.Text)
	private String firstname;
	@Field(type = FieldType.Text)
	private String lastname;
	@Field(type = FieldType.Date, index = false, store = true, format = DateFormat.date_hour_minute_second_millis)
	private Date createdDate;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
