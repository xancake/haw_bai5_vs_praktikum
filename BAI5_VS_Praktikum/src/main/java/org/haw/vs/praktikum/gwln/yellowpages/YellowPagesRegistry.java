package org.haw.vs.praktikum.gwln.yellowpages;

import java.util.List;
import com.mashape.unirest.http.exceptions.UnirestException;

public class YellowPagesRegistry {
	private static final String YELLOW_PAGES_URL = "http://172.18.0.5:4567";
//	private static final String YELLOW_PAGES_URL = "http://141.22.34.15/cnt/172.18.0.5/4567";
	
	private static final YellowPagesRestClient YELLOW_PAGES = new YellowPagesRestClient(YELLOW_PAGES_URL);
	
	public static String registerOrUpdateService(String name, String description, String type, String uri) throws YellowPagesNotAvailableException {
		try {
			Service overwriteService = findFirstDeadService(name, description, type, uri);
			if(overwriteService == null) {
				YELLOW_PAGES.postService(new Service(name, type, uri, description));
				List<Service> services = YELLOW_PAGES.getServices();
				return services.get(services.size()-1).getId();
			} else {
				YELLOW_PAGES.putService(overwriteService.getId(), new Service(name, type, uri, description));
				return overwriteService.getId();
			}
		} catch(UnirestException e) {
			throw new YellowPagesNotAvailableException(e);
		}
	}
	
	private static Service findFirstDeadService(String name, String description, String type, String uri) throws UnirestException {
		// Alle Services sammeln, die zu unserem Namen und Typen passen
		List<Service> services = YELLOW_PAGES.getServicesOfName(name);
		services.retainAll(YELLOW_PAGES.getServicesOfType(type));
		
		// Einen "toten" Service zurückgeben, wenn es einen gibt
		for(Service service : services) {
			if("dead".equals(service.getStatus())) {
				return service;
			}
		}
		
		// Es gibt keinen Service, also null zurückgeben
		return null;
	}
}
