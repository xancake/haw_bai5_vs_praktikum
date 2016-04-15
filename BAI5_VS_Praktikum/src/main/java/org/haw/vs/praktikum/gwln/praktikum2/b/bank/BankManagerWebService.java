package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import static spark.Spark.*;
import java.net.InetAddress;
import org.haw.vs.praktikum.gwln.YellowPagesRegistry;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class BankManagerWebService {
	private static final String NAME = "Bank Service 42_1337_69";
	private static final String DESCRIPTION = "Bester Bank Service 42_1337_69";
	private static final String SERVICE = "Bank Service";
	
	private static final BankManager MANAGER = new BankManager();
	
	private static String postKonto(Request request, Response response){
		String gameId = request.params(":gameId");
		
		MANAGER.createKonto(gameId);
		
		response.status(201);
		return "Konto erfolgreich angelegt";
	}
	
	private static String getKonto(Request request, Response response){
		String gameId = request.params(":gameId");
		String playerId = request.params(":playerId");
		
		Konto konto = MANAGER.getKonto(gameId, playerId);
		
		return new Gson().toJson(konto);
	}
	
	private static String postTransferTo(Request request, Response response){
		String gameId = request.params(":gameId");
		String to = request.params(":to");
		String amount = request.params(":amount");
		
		MANAGER.transferTo(gameId, MANAGER.getKonto(gameId, to), Integer.parseInt(amount));
		
		response.status(201);
		return "Transfer erfolgreich";
	}
	
	private static String postTransferFrom(Request request, Response response){
		String gameId = request.params(":gameId");
		String from = request.params(":from");
		String amount = request.params(":amount");
		
		MANAGER.transferFrom(gameId, MANAGER.getKonto(gameId, from), Integer.parseInt(amount));
		
		response.status(201);
		return "Transfer erfolgreich";
	}
	
	private static String postTransferFromTo(Request request, Response response){
		String gameId = request.params(":gameId");
		String to = request.params(":to");
		String from = request.params(":from");
		String amount = request.params(":amount");
		
		MANAGER.transferFromTo(gameId, MANAGER.getKonto(gameId, from), MANAGER.getKonto(gameId, to), Integer.parseInt(amount));
		
		response.status(201);
		return "Transfer erfolgreich";
	}
	
	public static void main(String... args) throws Exception {
		String ip = InetAddress.getLocalHost().getHostAddress();
		String uri = "http://"+ ip + ":4567/events";
		YellowPagesRegistry.registerOrUpdateService(NAME, DESCRIPTION, SERVICE, uri);
		
		post("/banks/:gameId/players", BankManagerWebService::postKonto);
		get("/banks/:gameId/players/:playerId", BankManagerWebService::getKonto);
		post("/banks/:gameId/transfer/to/:to/:amount", BankManagerWebService::postTransferTo);
		post("/banks/:gameId/transfer/from/:from/:amount", BankManagerWebService::postTransferFrom);
		post("/banks/:gameId/transfer/from/:from/to/:to/:amount", BankManagerWebService::postTransferFromTo);
	}
}
