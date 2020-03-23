package com.rico.couchbase;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rico.couchbase.documents.User;
import com.rico.couchbase.services.UserService;

/**
 * 
 * Testing Couchbase service
 * 
 * @author r.krishnakumar
 *
 */
@Component
public class TestCouchBase {

	@Autowired
	private UserService userService;
	
    /**
     * Sample method showing saving and retrieval of a couchbase document
     */
	@PostConstruct
	public void saveUser() {
		User u = new User("private", "t");
		userService.save(u);
	}
}
