package com.rico.redis;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rico.redis.documents.Test;
import com.rico.redis.services.CacheService;

/**
 * Testing Redis cache service
 * 
 * @author r.krishnakumar
 *
 */
@Component
public class TestRedis {

	@Autowired
	private CacheService cacheService;

	@PostConstruct
	public void saveUser() {
		Test t = new Test();
		t.setTestValue("myuser");
		cacheService.setUser(t);
		cacheService.getUser();
	}
}
