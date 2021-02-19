/* Licensed under Apache-2.0 */
package com.rico.es.services;

import com.rico.es.documents.Person;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import ro.common.elasticsearch.CommonSearchRepository;

/**
 * Sample search service for Elastic Search
 *
 * @author r.krishnakumar
 */
@Service
@Log4j2
public class PersonSearchService extends CommonSearchRepository<Person> {

  /**
   * Method to save an ElasticSearch document
   *
   * @param p
   * @return
   */
  public String savePerson(Person p) {
    p = save(p);
    log.info("SAVED PERSON SUCCESSFULLY " + p.getId());
    return p.getId();
  }

  /**
   * Method to retrieve ES Document using id
   *
   * @param id
   */
  public void getPersonById(String id) {
    Person person = findById(id, Person.class);
    log.info("SUCCESSFULLY RETRIEVED " + person.getFirstname() + "  " + person.getLastname());
  }

  /**
   * Method showing ES Doc retrieval using Criteria Query
   *
   * @param fieldValue
   * @param fieldName
   */
  public void findByCriteriaQuery(String fieldValue, String fieldName) {
    CriteriaQuery cq = new CriteriaQuery(Criteria.where(fieldName).contains(fieldValue));
    List<Person> p = queryForObjects(cq, Person.class);
    if (!p.isEmpty()) {
      log.info("SUCCESSFULLY RETRIEVED " + p.get(0).getFirstname() + "  " + p.get(0).getLastname());
    }
  }

  /**
   * Method showing ES Doc retrieval using String Query
   *
   * @param fieldValue
   */
  public void findByStringQuery(String fieldValue, String fieldName) {
    QueryBuilder query =
        QueryBuilders.boolQuery()
            .should(QueryBuilders.queryStringQuery(fieldValue).lenient(true).field(fieldName));
    StringQuery sq = new StringQuery(query.toString());
    List<Person> p = queryForObjects(sq, Person.class);
    if (p == null || p.isEmpty()) {
      return;
    }
    log.info("SUCCESSFULLY RETRIEVED " + p.get(0).getFirstname());
  }
}
