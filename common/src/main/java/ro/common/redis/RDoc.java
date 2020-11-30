/* Licensed under Apache-2.0 */
package ro.common.redis;

import ro.common.utils.Doc;

/**
 * Abstract class for redis documents
 *
 * @author r.krishnakumar
 */
public abstract class RDoc implements Doc {

  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
