package com.rico.es.services;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.rico.es.documents.Person;

import ro.common.elasticsearch.CommonSearchRepository;

/**
 * Sample search service for Elastic Search
 * 
 * @author r.krishnakumar
 *
 */
@Service
public class PersonSearchService extends CommonSearchRepository<Person> {

	/**
	 * Method to save an ElasticSearch document
	 * 
	 * @param p
	 * @return
	 */
	public String savePerson(Person p) {
		String id = save(p);
		System.out.println("SAVED PERSON SUCCESSFULLY " + id);
		return id;
	}

	/**
	 * Method to retrieve ES Document using id
	 * 
	 * @param id
	 */
	public void getPersonById(String id) {
		Person person = findById(id, Person.class);
		System.out.println("SUCCESSFULLY RETRIEVED " + person.getFirstname() + "  " + person.getLastname());
	}

	/**
	 * Method showing ES Doc retrieval using Criteria Query
	 * 
	 * @param name
	 */
	public void findByCriteriaQuery(String fieldValue, String fieldName) {
		CriteriaQuery cq = new CriteriaQuery(Criteria.where(fieldName).contains(fieldValue));
		Person p = queryForObject(cq, Person.class);
		System.out.println("SUCCESSFULLY RETRIEVED " + p.getFirstname() + "  " + p.getLastname());
	}

	/**
	 * Method showing ES Doc retrieval using String Query
	 * 
	 * @param fieldValue
	 */
	public void findByStringQuery(String fieldValue, String fieldName) {
		QueryBuilder query = QueryBuilders.boolQuery()
				.should(QueryBuilders.queryStringQuery(fieldValue).lenient(true).field(fieldName));
		StringQuery sq = new StringQuery(query.toString());
		Person p = queryForObject(sq, Person.class);
		if (p == null) {
			return;
		}
		System.out.println("SUCCESSFULLY RETRIEVED " + p.getFirstname());
	}
}
