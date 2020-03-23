package com.rico.es;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rico.es.documents.Person;
import com.rico.es.documents.Vehicle;
import com.rico.es.services.PersonSearchService;
import com.rico.es.services.VehicleSearchService;

/**
 * 
 * Testing ElasticSearch client service
 * 
 * @author r.krishnakumar
 *
 */
@Component
public class TestESClient {

	@Autowired
	private PersonSearchService personSearchService;

	@Autowired
	private VehicleSearchService vehicleSearchService;

	/**
	 * Sample method to show the save and retrieval of data from ES
	 */
	@PostConstruct
	public void searchData() {

		Person p = new Person();
		p.setId("yuioptr");
		p.setFirstname("King");
		p.setLastname("Julien");
		p.setCreatedDate(new Date());
		// Saving ES person document
		personSearchService.savePerson(p);
		// Retrieving ES person document by Id or lastname
		personSearchService.getPersonById("yuioptr");
		personSearchService.findByStringQuery("Julien", "lastname");

		Vehicle v = new Vehicle();
		v.setId("wertportrt");
		v.setMake("Tesla");
		v.setModel("Model S");
		v.setManufacturingYear(2015);
		v.setCreatedDate(new Date());
		// Saving ES vehicle document
		vehicleSearchService.saveVehicle(v);
		// Retrieving ES vehicle document by make and within manufacturing date range
		vehicleSearchService.findByCriteriaQuery("tesla", "make", "manufacturingYear", 2014, 2017);

		// Retrieving ES vehicle document by model and within date range inclusive of
		// both upper and lower bounds
		vehicleSearchService.findByStringQuery("Model S", "model", "createdDate",
				LocalDateTime.now().minusDays(2).toDate().getTime(), LocalDateTime.now().plusDays(2).toDate().getTime(),
				true);

	}

}
