/* Licensed under Apache-2.0 */
package com.rico.redis.documents;

import ro.common.redis.RDoc;

/**
 * Redis sample document structure
 *
 * @author r.krishnakumar
 */
public class Test extends RDoc {

  private String testValue;

  public String getTestValue() {
    return testValue;
  }

  public void setTestValue(String testValue) {
    this.testValue = testValue;
  }
}
