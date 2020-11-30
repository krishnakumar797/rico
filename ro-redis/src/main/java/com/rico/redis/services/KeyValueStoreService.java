/* Licensed under Apache-2.0 */
package com.rico.redis.services;

import com.rico.redis.documents.Test;
import org.springframework.stereotype.Service;
import ro.common.redis.CommonKeyValueStore;

/**
 * Sample service for testing redis keyvalue store
 *
 * @author r.krishnakumar
 */
@Service
public class KeyValueStoreService extends CommonKeyValueStore<Test> {

  public void setUser(Test t) {
    storeData("myuser", t);
  }

  public Test getUser() {
    return getData("myuser");
  }
}
