/* Licensed under Apache-2.0 */
package com.rico.redis;

import com.rico.redis.documents.Test;
import com.rico.redis.services.KeyValueStoreService;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Testing Redis Keyvalue store service
 *
 * @author r.krishnakumar
 */
@Component
@Log4j2
public class TestRedis {

  @Autowired private KeyValueStoreService keyValueStoreService;

  @PostConstruct
  public void saveUser() {
    Test t = new Test();
    t.setTestValue("myuser");
    keyValueStoreService.setUser(t);
    t = keyValueStoreService.getUser();
    log.info("KEY VALUE STORE USER " + t.getTestValue());
  }
}
