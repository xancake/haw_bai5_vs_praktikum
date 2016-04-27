package org.haw.vs.praktikum.gwln.yellowpages;

import org.json.JSONObject;

public class ServiceJsonMarshaller {
	/**
	 * Verpackt den übergebenen {@link Service} in ein {@link JSONObject}.
	 * @param service Der zu verpackende Service
	 * @return Die JSON-Repräsentation des Objekts
	 */
	public String marshall(Service object) {
		JSONObject json = new JSONObject();
		json.put("name",        object.getName());
		json.put("service",     object.getService());
		json.put("uri",         object.getUri());
		json.put("description", object.getDescription());
		return json.toString();
	}
	
	/**
	 * Entpackt ein {@link Service}-Objekt aus dem übergebenen {@link JSONObject}.
	 * @param json Das zu entpackende JSON
	 * @return Der entpackte Service
	 */
	public Service unmarshall(String source) {
		JSONObject json = new JSONObject(source);
		String id          = json.getString("_uri").replace("/services/", "");
		String name        = json.getString("name");
		String service     = json.getString("service");
		String uri         = json.getString("uri");
		String status      = json.optString("status");
		String description = json.optString("description");
		return new Service(id, name, service, uri, status, description);
	}
}
