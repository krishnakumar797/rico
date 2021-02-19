/* Licensed under Apache-2.0 */
package ro.common.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.support.IndicesOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
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
   * Returns a matched document by id.
   *
   * @param id represents the document id
   * @param clazz as the type of the document
   * @return
   */
  protected final T findById(String id, Class<T> clazz) {
    return elasticsearchOperations.get(id, clazz);
  }

  /**
   * Returns matched documents
   *
   * @param query represents the Criteria query
   * @param clazz as the type of the document
   * @return
   */
  protected final List<T> queryForObjects(CriteriaQuery query, Class<T> clazz) {
    List<T> objectLit = new ArrayList<>();
    SearchHits<T> searchHits = elasticsearchOperations.search(query, clazz);
    if (searchHits.hasSearchHits()) {
      objectLit =
          searchHits.get().map(content -> content.getContent()).collect(Collectors.toList());
    }
    return objectLit;
  }

  /**
   * Returns matched documents
   *
   * @param query represents the String query
   * @param clazz as the type of the document
   * @return
   */
  protected final List<T> queryForObjects(StringQuery query, Class<T> clazz) {
    List<T> objectLit = new ArrayList<>();
    SearchHits<T> searchHits = elasticsearchOperations.search(query, clazz);
    if (searchHits.hasSearchHits()) {
      objectLit =
          searchHits.get().map(content -> content.getContent()).collect(Collectors.toList());
    }
    return objectLit;
  }

  /**
   * Returns matched documents from the index
   *
   * @param query
   * @param clazz
   * @param indexName
   * @return
   */
  protected final List<T> queryForObjects(StringQuery query, Class<T> clazz, String indexName) {
    List<T> objectLit = new ArrayList<>();
    SearchHits<T> searchHits =
        elasticsearchOperations.search(query, clazz, IndexCoordinates.of(indexName));
    if (searchHits.hasSearchHits()) {
      objectLit =
          searchHits.get().map(content -> content.getContent()).collect(Collectors.toList());
    }
    return objectLit;
  }

  /**
   * Returns a list of matched documents from all index
   *
   * @param query represents the Criteria query
   * @param clazz as the type of the document
   * @return
   */
  protected final List<T> queryForObjectsInAllIndex(CriteriaQuery query, Class<T> clazz) {
    query.setIndicesOptions(IndicesOptions.fromOptions(true, true, true, false));
    List<T> objectLit = new ArrayList<>();
    SearchHits<T> searchHits =
        elasticsearchOperations.search(query, clazz, IndexCoordinates.of("_all"));
    if (searchHits.hasSearchHits()) {
      objectLit =
          searchHits.get().map(content -> content.getContent()).collect(Collectors.toList());
    }
    return objectLit;
  }

  /**
   * Returns a list of matched documents.
   *
   * @param query represents the String query
   * @param clazz as the type of the document
   * @return
   */
  protected final List<T> queryForObjectsInAllIndex(StringQuery query, Class<T> clazz) {
    // Ignoring if the indices not found
    query.setIndicesOptions(IndicesOptions.fromOptions(true, true, true, false));
    List<T> objectLit = new ArrayList<>();
    SearchHits<T> searchHits =
        elasticsearchOperations.search(query, clazz, IndexCoordinates.of("_all"));
    if (searchHits.hasSearchHits()) {
      objectLit =
          searchHits.get().map(content -> content.getContent()).collect(Collectors.toList());
    }
    return objectLit;
  }

  /**
   * Saves and Index a document with index as the indexname provided in the @Document annotation
   *
   * @param t
   * @return
   */
  protected final T save(T t) {
    return elasticsearchOperations.save(t);
  }
}
