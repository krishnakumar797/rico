/* Licensed under Apache-2.0 */
package com.healtrip.client.services;

import com.healtrip.client.documents.Person;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import ro.common.elasticsearch.CommonSearchRepository;

@Service
public class SearchService extends CommonSearchRepository<Person> {

  public String savePerson(Person p) {
    String id = save(p);
    System.out.println("SAVED SUCCESSFULLY " + id);
    return id;
  }

  public void getPersonById(String id) {
    Person person = findById(id, Person.class);
    System.out.println("SUCCESSFULLY RETRIVED " + person.getFirstname());
  }

  public void findByCriteriaQuery(String name) {
    CriteriaQuery cq = new CriteriaQuery(Criteria.where("firstname").contains(name));
    Person p = queryForObject(cq, Person.class);
    System.out.println("SUCCESSFULLY RETRIVED " + p.getFirstname());
  }

  public void findByStringQuery(String name) {
    QueryBuilder query =
        QueryBuilders.boolQuery()
            .should(QueryBuilders.queryStringQuery(name).lenient(true).field("lastname"));
    StringQuery sq = new StringQuery(query.toString());
    Person p = queryForObject(sq, Person.class);
    if (p == null) {
      return;
    }
    System.out.println("SUCCESSFULLY RETRIVED " + p.getLastname());
  }
}
