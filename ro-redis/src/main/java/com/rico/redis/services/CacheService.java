/* Licensed under Apache-2.0 */
package com.rico.redis.services;

import com.rico.redis.documents.Test;
import org.springframework.stereotype.Service;
import ro.common.redis.CommonCache;

/**
 * Sample service for testing redis cache
 *
 * @author r.krishnakumar
 */
@Service
public class CacheService extends CommonCache<Test> {

  public void setUser(Test u) {
    storeData("myuser", u);
  }

  public void getUser() {
    Test t = getData("myuser");
    System.out.println("Cache User " + t.getTestValue());
  }
}
