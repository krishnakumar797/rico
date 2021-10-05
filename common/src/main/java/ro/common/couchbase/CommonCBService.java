/* Licensed under Apache-2.0 */
package ro.common.couchbase;

import static com.couchbase.client.java.kv.MutateInSpec.upsert;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.kv.LookupInResult;
import com.couchbase.client.java.kv.LookupInSpec;
import com.couchbase.client.java.kv.MutateInResult;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.result.SearchResult;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import ro.common.utils.Doc;

/**
 * Generic Common DAO spring data repository for Couchbase
 *
 * @author r.krishnakumar
 * @param <T>
 */
@DependsOn("CouchBaseConfig")
public abstract class CommonCBService<T extends Doc> {

  private CouchbaseTemplate couchbaseTemplate;
  private Cluster cluster;

  public CommonCBService(CouchbaseTemplate couchbaseTemplate, Cluster cluster) {
    this.couchbaseTemplate = couchbaseTemplate;
    this.cluster = cluster;
  }

  /**
   * Method to update sub document value for a specified document
   *
   * @param document
   * @param docId
   * @param subDocumentPath
   * @param value
   * @return
   */
  public MutateInResult updateSubDocument(
      Class<T> document, String docId, String subDocumentPath, String value) {
    return couchbaseTemplate
        .getCollection(document.getName())
        .mutateIn(docId, Arrays.asList(upsert(subDocumentPath, value)));
  }

  /**
   * Method to retrieve sub document for a parent document
   *
   * @param docId
   * @param subDocumentPath
   * @return
   */
  public LookupInResult retrieveSubDocument(
      Class<T> document, String docId, String subDocumentPath) {
    return couchbaseTemplate
        .getCollection(document.getName())
        .lookupIn(docId, Collections.singletonList(LookupInSpec.get(subDocumentPath)));
  }

  /**
   * Method to execute search query
   *
   * @param query
   * @return
   */
  public SearchResult search(Class<T> document, SearchQuery query) {
    return cluster.searchQuery(document.getName(), query);
  }
}
