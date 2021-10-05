/* Licensed under Apache-2.0 */
package ro.common.elasticsearch;

import org.springframework.data.annotation.Id;
import ro.common.utils.Doc;

/**
 * Abstract class for ES Documents
 *
 * @author r.krishnakumar
 */
public abstract class ESDoc implements Doc {

  @Id private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
