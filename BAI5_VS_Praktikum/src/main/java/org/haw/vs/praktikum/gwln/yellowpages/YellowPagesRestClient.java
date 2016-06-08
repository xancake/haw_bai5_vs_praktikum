package org.haw.vs.praktikum.gwln.yellowpages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Client für den Yellow-Pages Web-Service von Andrej.
 */
public class YellowPagesRestClient extends AbstractRestClient {
	public static final String HAW_YELLOW_PAGES_INTERNAL = "http://172.18.0.17:4567";
	public static final String HAW_YELLOW_PAGES_EXTERNAL = "http://141.22.34.15/cnt/172.18.0.17/4567";
	
	private ServiceJsonMarshaller _marshaller;
	private String _username;
	private String _passwort;
	
	public YellowPagesRestClient(String url) throws MalformedURLException {
		this(new URL(url));
	}
	
	/**
	 * Die URL, auf der sich der Yellow-Pages Web-Service befindet.
	 * @param url Die URL des Yellow-Pages
	 * @throws MalformedURLException 
	 */
	public YellowPagesRestClient(URL url) throws MalformedURLException {
		this(url, "", "");
	}
	
	public YellowPagesRestClient(URL url, String username, String password) throws MalformedURLException {
		super(url, "/services");
		_username = username;
		_passwort = password;
		_marshaller = new ServiceJsonMarshaller();
	}
	
	/**
	 * Ermittelt eine Liste aller am Yellow-Pages registrierten Services.
	 * Dieser Aufruf entspricht der API-Funktion {@code GET /services?expanded}.
	 * @return Eine Liste aller am Yellow-Pages registrierten Services
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public List<Service> getServices() throws UnirestException {
		List<Service> services = new ArrayList<Service>();
		
		HttpResponse<JsonNode> response = Unirest.get(getURL().toExternalForm() + "?expanded")
				.basicAuth(_username, _passwort)
				.asJson();
		JSONObject responseJson = response.getBody().getObject();
		JSONArray servicesJson = responseJson.getJSONArray("services");
		for(Object jsonEntry : servicesJson) {
			JSONObject serviceJson = (JSONObject)jsonEntry;
			services.add(_marshaller.unmarshall(serviceJson.toString()));
		}
		
		return services;
	}
	
	/**
	 * Registriert den übergebenen Service am Yellow-Pages.
	 * Der Aufruf entspricht der API-Funktion {@code POST /services}.
	 * @param service Der zu registrierende Service
	 * @returns Eine URL unter der der angelegte Service aufrufbar ist
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public String postService(Service service) throws UnirestException {
		HttpResponse<String> response = Unirest.post(getURL().toExternalForm())
				.basicAuth(_username, _passwort)
				.header("Content-Type", "application/json")
				.body(_marshaller.marshall(service))
				.asString();
		return response.getHeaders().getFirst("Location");
	}
	
	/**
	 * Gibt den am Yellow-Pages registrierten Service mit der übergebenen ID zurück.
	 * Der Aufruf entspricht der API-Funktion {@code GET /services/:id?expanded}.
	 * @param id Die ID des Services
	 * @return Der registrierte Service oder {@code null}, wenn es keinen gibt 
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public Service getService(String id) throws UnirestException {
		HttpResponse<String> response = Unirest.post(getURL().toExternalForm() + "/" + id + "?expanded")
				.basicAuth(_username, _passwort)
				.asString();
		if(response.getStatus() == 404) {
			return null;
		}
		return _marshaller.unmarshall(response.getBody());
	}
	
	/**
	 * Registriert bzw. aktualisiert den Eintrag des Services hinter der übergebenen ID mit dem übergebenen Service.
	 * Der Aufruf entspricht der API-Funktion {@code PUT /services/:id}.
	 * @param id Die ID des Services
	 * @param service Die neuen Angaben des Service
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public void putService(String id, Service service) throws UnirestException {
		Unirest.put(getURL().toExternalForm() + "/" + id)
				.basicAuth(_username, _passwort)
				.header("Content-Type", "application/json")
				.body(_marshaller.marshall(service))
				.asString();
	}
	
	/**
	 * Entfernt den eingetragenen Service hinter der übergebenen ID.
	 * Der Aufruf entspricht der API-Funktion {@code DELETE /services/:id}.
	 * @param id Die ID des auszutragenden Services
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public void deleteService(String id) throws UnirestException {
		Unirest.delete(getURL().toExternalForm() + "/" + id)
				.basicAuth(_username, _passwort)
				.asString();
	}
	
	/**
	 * Gibt eine Liste aller am Yellow-Pages registrierten Services zurück, die den übergebenen Namen haben.
	 * Der Aufruf entspricht der API-Funktion {@code GET /services/of/name/:name?expanded}.
	 * @param name Der Name
	 * @return Eine Liste aller Services mit dem übergebenen Namen
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public List<Service> getServicesOfName(String name) throws UnirestException {
		List<Service> services = new ArrayList<Service>();
		
		HttpResponse<String> response = Unirest.get(getURL().toExternalForm() + "/of/name/" + name + "?expanded")
				.basicAuth(_username, _passwort)
				.asString();

		System.out.println(response.getBody());
		JSONObject responseJson = new JSONObject(response.getBody());
		JSONArray servicesJson = responseJson.getJSONArray("services");
		for(Object jsonEntry : servicesJson) {
			JSONObject serviceJson = (JSONObject)jsonEntry;
			services.add(_marshaller.unmarshall(serviceJson.toString()));
		}
		
		return services;
	}
	
	/**
	 * Gibt eine Liste aller am Yellow-Pages registrierten Services zurück, die dem übergebenen Typ entsprechen.
	 * Der Aufruf entspricht der API-Funktion {@code GET /services/of/type/:type?expanded}.
	 * @param type Der Typ
	 * @return Eine Liste aller Services mit dem übergebenen Typ
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public List<Service> getServicesOfType(String type) throws UnirestException {
		List<Service> services = new ArrayList<Service>();
		
		HttpResponse<JsonNode> response = Unirest.get(getURL().toExternalForm() + "/of/type/" + type + "?expanded")
				.basicAuth(_username, _passwort)
				.asJson();
		JSONObject responseJson = response.getBody().getObject();
		JSONArray servicesJson = responseJson.getJSONArray("services");
		for(Object jsonEntry : servicesJson) {
			JSONObject serviceJson = (JSONObject)jsonEntry;
			services.add(_marshaller.unmarshall(serviceJson.toString()));
		}
		
		return services;
	}
}
