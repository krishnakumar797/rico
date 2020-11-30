/* Licensed under Apache-2.0 */
package ro.common.elasticsearch;

import java.util.List;
import org.elasticsearch.action.support.IndicesOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.StringQuery;

/**
 * Abstract Common search class for elastic search documents
 *
 * @author r.krishnakumar
 */
public abstract class CommonSearchRepository<T extends ESDoc> {

  @Autowired private ElasticsearchOperations elasticsearchOperations;

  /**
   * Returns a matched document.
   *
   * @param id represents the document id
   * @param clazz as the type of the document
   * @return
   */
  protected final T findById(String id, Class<T> clazz) {
    return elasticsearchOperations.queryForObject(GetQuery.getById(id), clazz);
  }

  /**
   * Returns a matched document.
   *
   * @param query represents the Criteria query
   * @param clazz as the type of the document
   * @return
   */
  protected final T queryForObject(CriteriaQuery query, Class<T> clazz) {
    return elasticsearchOperations.queryForObject(query, clazz);
  }

  /**
   * Returns a matched document.
   *
   * @param query represents the String query
   * @param clazz as the type of the document
   * @return
   */
  protected final T queryForObject(StringQuery query, Class<T> clazz) {
    return elasticsearchOperations.queryForObject(query, clazz);
  }

  /**
   * Returns a list of matched documents.
   *
   * @param query represents the Criteria query
   * @param clazz as the type of the document
   * @param allowAllIndices used to check in all indices for the document
   * @return
   */
  protected final List<T> queryForList(
      CriteriaQuery query, Class<T> clazz, boolean allowAllIndices) {
    // Ignoring if the indices not found
    if (allowAllIndices) {
      query.setIndicesOptions(IndicesOptions.fromOptions(true, true, true, false));
      query.addIndices("_all");
    }
    return elasticsearchOperations.queryForList(query, clazz);
  }

  /**
   * Returns a list of matched documents.
   *
   * @param query represents the String query
   * @param clazz as the type of the document
   * @param allowAllIndices used to check in all indices for the document
   * @return
   */
  protected final List<T> queryForList(StringQuery query, Class<T> clazz, boolean allowAllIndices) {
    // Ignoring if the indices not found
    if (allowAllIndices) {
      query.setIndicesOptions(IndicesOptions.fromOptions(true, true, true, false));
      query.addIndices("_all");
    }
    return elasticsearchOperations.queryForList(query, clazz);
  }

  /**
   * Saves and Index a document with Id
   *
   * @param t
   * @return
   */
  protected final String save(T t) {
    IndexQuery indexQuery = new IndexQueryBuilder().withId(t.getId()).withObject(t).build();
    return elasticsearchOperations.index(indexQuery);
  }
}
