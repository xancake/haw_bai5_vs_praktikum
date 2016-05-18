package org.haw.vs.praktikum.gwln.client.service;

import static spark.Spark.post;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;
import org.haw.vs.praktikum.gwln.events.Event;
import org.haw.vs.praktikum.gwln.events.rest.EventJsonMarshaller;

import spark.Request;
import spark.Response;
import spark.Spark;

public class ClientService {

	private List<ClientServiceListener_I> listeners = new ArrayList<>();
	private EventJsonMarshaller eventJsonMarshaller = new EventJsonMarshaller();

	private String clientTurn(Request request, Response response) {
		listeners.forEach( (listener) -> listener.onTurn() );
		response.status(HttpStatus.OK_200);
		return "OK";
	}

	private String clientEvent(Request request, Response response) {
		Event event = eventJsonMarshaller.unmarshall(request.body());
		listeners.forEach( (listener) -> listener.onEvent(event) );
		response.status(HttpStatus.OK_200);
		return "OK";
	}

	public void start() {
		post("/client/turn", 	(request, response) -> clientTurn(request, response) );
		post("/client/event", 	(request, response) -> clientEvent(request, response) );
	}
	
	public void stop() {
		Spark.stop();
	}

	public void addListener(ClientServiceListener_I l) {
		listeners.add(l);
	}

	public void removeListener(ClientServiceListener_I l) {
		listeners.remove(l);
	}
	
	public static void main(String... args) {
		Spark.port(12345);
		new ClientService().start();
	}
}
