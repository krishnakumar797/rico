/* Licensed under Apache-2.0 */
package ro.common.couchbase;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import lombok.Getter;
import lombok.Setter;
import ro.common.utils.Doc;

/**
 * Abstract class for couchbase documents
 *
 * @author r.krishnakumar
 */
@Getter
@Setter
public abstract class CBDoc implements Doc {

  @Id
  @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
  private String id;
}
