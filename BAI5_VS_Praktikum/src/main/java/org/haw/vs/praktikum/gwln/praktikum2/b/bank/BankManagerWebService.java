package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import static spark.Spark.*;
import java.net.InetAddress;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesNotAvailableException;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesRegistry;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class BankManagerWebService {
	private static final String NAME = "Bank Service 42_1337_69";
	private static final String DESCRIPTION = "Bester Bank Service 42_1337_69";
	private static final String SERVICE = "Bank Service";
	
	private static final BankManager MANAGER = new BankManager();
	
	private static String postKonto(Request request, Response response) {
		String gameId = request.params(":gameId");
		
		JSONObject body = new JSONObject(request.body());
		String player = "/" + body.getString("player");
		int saldo = body.getInt("saldo");
		
		MANAGER.createKonto(gameId, player.substring(player.lastIndexOf("/")+1), saldo);
		
		response.status(201);
		return "Konto erfolgreich angelegt";
	}
	
	private static String getKonto(Request request, Response response) {
		String gameId = request.params(":gameId");
		String accountId = request.params(":accountId");
		
		Konto konto = MANAGER.getKonto(gameId, accountId);
		
		JSONObject responseJson = new JSONObject();
		responseJson.put("player", konto.getId());
		responseJson.put("saldo", konto.getKontostand());
		
		return responseJson.toString();
	}
	
	private static String postTransferTo(Request request, Response response) {
		String gameId = request.params(":gameId");
		String to = "/" + request.params(":to");
		String amount = request.params(":amount");
		
		String reason = request.body();
		
		MANAGER.transferTo(gameId, MANAGER.getKonto(gameId, to.substring(to.lastIndexOf("/")+1)), Integer.parseInt(amount), reason);
		
		response.status(201);
		return "Transfer erfolgreich";
	}
	
	private static String postTransferFrom(Request request, Response response) {
		try {
			String gameId = request.params(":gameId");
			String from = "/" + request.params(":from");
			String amount = request.params(":amount");
			
			MANAGER.transferFrom(gameId, MANAGER.getKonto(gameId, from.substring(from.lastIndexOf("/")+1)), Integer.parseInt(amount));
			
			response.status(201);
			return "Transfer erfolgreich";
		} catch(InsufficientFondsException e) {
			response.status(403);
			return e.getMessage();
		} catch(Exception e) {
			response.status(412);
			return e.getMessage();
		}
	}
	
	private static String postTransferFromTo(Request request, Response response) {
		try {
			String gameId = request.params(":gameId");
			String to = "/" + request.params(":to");
			String from = "/" + request.params(":from");
			String amount = request.params(":amount");
			
			MANAGER.transferFromTo(gameId, MANAGER.getKonto(gameId, from.substring(from.lastIndexOf("/")+1)), MANAGER.getKonto(gameId, to.substring(to.lastIndexOf("/")+1)), Integer.parseInt(amount));
			
			response.status(201);
			return "Transfer erfolgreich";
		} catch(InsufficientFondsException e) {
			response.status(403);
			return e.getMessage();
		} catch(Exception e) {
			response.status(412);
			return e.getMessage();
		}
	}
	
	public static void main(String... args) throws Exception {
		try {
			String uri = "http://"+ InetAddress.getLocalHost().getHostAddress() + ":4567/banks";
			YellowPagesRegistry.registerOrUpdateService(NAME, DESCRIPTION, SERVICE, uri);
		} catch(YellowPagesNotAvailableException e) {
			e.printStackTrace();
		}
		
		post("/banks/:gameId/accounts", BankManagerWebService::postKonto);
		get("/banks/:gameId/accounts/:accountId", BankManagerWebService::getKonto);
		post("/banks/:gameId/transfer/to/:to/:amount", BankManagerWebService::postTransferTo);
		post("/banks/:gameId/transfer/from/:from/:amount", BankManagerWebService::postTransferFrom);
		post("/banks/:gameId/transfer/from/:from/to/:to/:amount", BankManagerWebService::postTransferFromTo);
	}
}
