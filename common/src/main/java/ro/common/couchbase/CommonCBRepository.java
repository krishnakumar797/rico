/* Licensed under Apache-2.0 */
package ro.common.couchbase;

import java.util.Optional;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import com.couchbase.client.core.message.kv.subdoc.multi.Lookup;
import com.couchbase.client.core.message.kv.subdoc.multi.Mutation;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.result.SearchQueryResult;
import com.couchbase.client.java.subdoc.DocumentFragment;

import ro.common.utils.Doc;

/**
 * Generic Common DAO spring data repository for Couchbase
 *
 * @author r.krishnakumar
 * @param <T>
 * @param <String>
 */
@DependsOn("CouchBaseConfig")
@NoRepositoryBean
public interface CommonCBRepository<T extends Doc>
    extends CouchbasePagingAndSortingRepository<T, String> {

 <S extends T> S save(S entity);

  Optional<T> findById(String primaryKey);

  Iterable<T> findAll();

  Page<T> findAll(Pageable pageable);

  long count();

  void delete(T entity);

  boolean existsById(String primaryKey);

  /**
   * Method to execute N1ql queries
   *
   * @param query
   * @return
   */
  default N1qlQueryResult execute(N1qlQuery query) {
    return getCouchbaseOperations().getCouchbaseBucket().query(query);
  }

  /**
   * Method to generate default document filter to use with all N1ql queries
   *
   * @param clazz
   * @return
   */
  default String getDocumentFilter(Class<T> clazz) {
    return "_class=" + "\"" + clazz.getName() + "\"";
  }

  /**
   * Method to execute search query
   *
   * @param query
   * @return
   */
  default SearchQueryResult search(SearchQuery query) {
    return getCouchbaseOperations().getCouchbaseBucket().query(query);
  }

  /**
   * Method to update sub document value for a specified document
   *
   * @param docId
   * @param subDocumentPath
   * @param value
   * @return
   */
  default DocumentFragment<Mutation> updateSubDocument(
      String docId, String subDocumentPath, String value) {
    return getCouchbaseOperations()
        .getCouchbaseBucket()
        .mutateIn(docId)
        .upsert(subDocumentPath, value)
        .execute();
  }

  /**
   * Method to retrieve sub document for a parent document
   *
   * @param docId
   * @param subDocumentPath
   * @return
   */
  default DocumentFragment<Lookup> retrieveSubDocument(String docId, String subDocumentPath) {
    return getCouchbaseOperations()
        .getCouchbaseBucket()
        .lookupIn(docId)
        .get(subDocumentPath)
        .execute();
  }
}
