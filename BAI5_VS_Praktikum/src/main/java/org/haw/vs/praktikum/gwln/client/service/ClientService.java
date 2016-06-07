package org.haw.vs.praktikum.gwln.client.service;

import static spark.Spark.post;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;
import org.haw.vs.praktikum.gwln.events.Event;
import org.haw.vs.praktikum.gwln.events.rest.EventJsonMarshaller;

import spark.Request;
import spark.Response;
import spark.Spark;

public class ClientService {

	private String _uri;

	private List<ClientServiceListener_I> listeners = new ArrayList<>();
	private EventJsonMarshaller eventJsonMarshaller = new EventJsonMarshaller();

	private String clientTurn(Request request, Response response) {
		listeners.forEach(ClientServiceListener_I::onTurn);
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
		try {
			_uri = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			post("/client/turn", this::clientTurn);
			post("/client/event", this::clientEvent);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
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

	public String getUri(){
		return _uri;
	}
}
