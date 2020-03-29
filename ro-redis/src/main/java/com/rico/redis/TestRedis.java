/* Licensed under Apache-2.0 */
package com.rico.redis;

import com.rico.redis.documents.Test;
import com.rico.redis.services.CacheService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Testing Redis cache service
 *
 * @author r.krishnakumar
 */
@Component
public class TestRedis {

  @Autowired private CacheService cacheService;

  @PostConstruct
  public void saveUser() {
    Test t = new Test();
    t.setTestValue("myuser");
    cacheService.setUser(t);
    cacheService.getUser();
  }
}
