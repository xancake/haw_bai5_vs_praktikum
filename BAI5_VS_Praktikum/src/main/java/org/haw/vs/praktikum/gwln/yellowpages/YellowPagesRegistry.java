package org.haw.vs.praktikum.gwln.yellowpages;

import java.util.List;
import com.mashape.unirest.http.exceptions.UnirestException;

public class YellowPagesRegistry {
	private static final YellowPagesRestClient YELLOW_PAGES = new YellowPagesRestClient(YellowPagesRestClient.HAW_YELLOW_PAGES_INTERNAL);
	
	public static String registerOrUpdateService(String name, String description, String type, String uri) throws YellowPagesNotAvailableException {
		try {
			Service overwriteService = findFirstDeadService(name, description, type, uri);
			if(overwriteService == null) {
				return YELLOW_PAGES.postService(new Service(name, type, uri, description));
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
