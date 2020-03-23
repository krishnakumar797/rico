package com.rico.es.services;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.rico.es.documents.Vehicle;

import ro.common.elasticsearch.CommonSearchRepository;

/**
 * Sample search service for Elastic Search
 * 
 * @author r.krishnakumar
 *
 */
@Service
public class VehicleSearchService extends CommonSearchRepository<Vehicle> {

	/**
	 * Method to save an ElasticSearch document
	 * 
	 * @param p
	 * @return
	 */
	public String saveVehicle(Vehicle p) {
		String id = save(p);
		System.out.println("SAVED VEHICLE SUCCESSFULLY " + id);
		return id;
	}

	/**
	 * Method to retrieve ES Document using id
	 * 
	 * @param id
	 */
	public void getVehicleById(String id) {
		Vehicle v = findById(id, Vehicle.class);
		System.out.println("SUCCESSFULLY RETRIEVED " + v.getId());
	}

	/**
	 * Method showing ES Doc retrieval using Criteria Query using date range
	 * 
	 * @param name
	 */
	public void findByCriteriaQuery(String fieldValue, String fieldName, String yearFieldName, Integer fromYear,
			Integer toYear) {
		CriteriaQuery cq = new CriteriaQuery(Criteria.where(yearFieldName).between(fromYear, toYear)
				.and(Criteria.where(fieldName).contains(fieldValue)));
		List<Vehicle> v = queryForList(cq, Vehicle.class, true);
		if (v == null || v.isEmpty()) {
			System.out.println("NO RESULTS FOUND");
			return;
		}
		v.forEach(vl -> {
			System.out.println("SUCCESSFULLY RETRIEVED " + vl.getModel());
		});
	}

	/**
	 * Method showing ES Doc retrieval using String Query using date range inclusive
	 * of upper bounds and lower bounds
	 * 
	 * @param fieldValue
	 */
	public void findByStringQuery(String fieldValue, String fieldName, String dateFieldName, Long fromDate, Long toDate,
			boolean isInclusive) {
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(dateFieldName).from(fromDate, isInclusive).to(toDate,
				isInclusive);
		// If either of the should query match the bool query returns the result. To
		// match all conditions use must. To exactly match phrases like "Hello World"
		// like in the same order use matchPhraseQuery
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
				.should(QueryBuilders.queryStringQuery(fieldValue).lenient(true).field(fieldName)).should(rangeQuery);

//		BoolQueryBuilder boolQuery1 = QueryBuilders.boolQuery()
//				.must(QueryBuilders.matchPhraseQuery(fieldName, fieldValue)).should(rangeQuery);

		StringQuery sq = new StringQuery(boolQuery.toString());
		List<Vehicle> v = queryForList(sq, Vehicle.class, true);
		if (v == null || v.isEmpty()) {
			System.out.println("NO RESULTS FOUND");
			return;
		}
		v.forEach(vl -> {
			System.out.println("SUCCESSFULLY RETRIEVED " + vl.getMake() + " " + vl.getManufacturingYear());
		});

	}
}
