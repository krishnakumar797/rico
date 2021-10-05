/* Licensed under Apache-2.0 */
package ro.common.couchbase;

import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import ro.common.utils.Doc;

/**
 * Generic Common DAO spring data repository for Couchbase
 *
 * @author r.krishnakumar
 * @param <T>
 */
@DependsOn("CouchBaseConfig")
@NoRepositoryBean
public interface CommonCBRepository<T extends Doc> extends CouchbaseRepository<T, String> {

  <S extends T> S save(S entity);

  Optional<T> findById(String primaryKey);

  List<T> findAll();

  Page<T> findAll(Pageable pageable);

  long count();

  void delete(T entity);

  boolean existsById(String primaryKey);

  /**
   * Method to generate default document filter to use with all N1ql queries
   *
   * @param clazz
   * @return
   */
  default String getDocumentFilter(Class<T> clazz) {
    return "_class=" + "\"" + clazz.getName() + "\"";
  }
}
