/* Licensed under Apache-2.0 */
package ro.common.couchbase;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import ro.common.utils.Doc;

import java.util.Optional;

/**
 * Generic Common DAO spring data repository for Couchbase
 *
 * @author r.krishnakumar
 * @param <T>
 */
@DependsOn("CouchBaseConfig")
@NoRepositoryBean
public interface CommonCBRepository<T extends Doc>
    extends PagingAndSortingRepository<T, String> {

  <S extends T> S save(S entity);

  Optional<T> findById(String primaryKey);

  Iterable<T> findAll();

  Page<T> findAll(Pageable pageable);

  long count();

  void delete(T entity);

  boolean existsById(String primaryKey);
}
