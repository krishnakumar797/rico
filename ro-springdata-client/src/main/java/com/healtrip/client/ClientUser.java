package com.healtrip.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healtrip.client.services.GrpcClientService;
import com.healtrip.client.services.SearchService;

@Component
public class ClientUser {

	@Autowired
	private GrpcClientService clientService;

	@Autowired
	private SearchService searchService;

	@PostConstruct
	public void sendGreeting() {
//		String greeting = clientService.receiveGreeting("krishna");
//		System.out.println("GREETINGS " + greeting);
//		Person p = new Person();
//		p.setId("yuioptr");
//		p.setFirstname("krishna");
//		p.setLastname("kumar");
//		searchService.savePerson(p);

		// searchService.getPersonById("yuioptr");
		searchService.findByStringQuery("kumar");
	}

}
