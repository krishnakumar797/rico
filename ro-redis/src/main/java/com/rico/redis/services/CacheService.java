/* Licensed under Apache-2.0 */
package com.rico.redis.services;

import org.springframework.stereotype.Service;

import com.rico.redis.documents.Test;

import lombok.extern.log4j.Log4j2;
import ro.common.redis.CommonCache;

/**
 * Sample service for testing redis cache
 *
 * @author r.krishnakumar
 */
@Service
@Log4j2
public class CacheService extends CommonCache<Test> {

  public void setUser(Test u) {
    storeData("myuser", u);
  }

  public void getUser() {
    Test t = getData("myuser");
    log.info("Cache User " + t.getTestValue());
  }
}
