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
	private static String URI;
	
	private static String postAccount(Request request, Response response) {
		String gameId = request.params(":gameId");
		
		Account account = AccountJsonMarshaller.unmarshall(new JSONObject(request.body()));
		MANAGER.createAccount(gameId, account);
		
		response.header("Location", URI + "/banks/" + gameId + "/accounts/" + account.getId());
		response.status(201);
		return "Konto erfolgreich angelegt";
	}
	
	private static String getAccount(Request request, Response response) {
		String gameId = request.params(":gameId");
		String accountId = request.params(":accountId");
		
		Account account = MANAGER.getAccount(gameId, accountId);
		
		return AccountJsonMarshaller.marshall(account).toString();
	}
	
	private static String postTransferTo(Request request, Response response) {
		String gameId = request.params(":gameId");
		String to = "/" + request.params(":to");
		String amount = request.params(":amount");
		
		String reason = request.body();
		
		MANAGER.transferTo(gameId, MANAGER.getAccount(gameId, to.substring(to.lastIndexOf("/")+1)), Integer.parseInt(amount), reason);
		
		response.status(201);
		return "Transfer erfolgreich";
	}
	
	private static String postTransferFrom(Request request, Response response) {
		try {
			String gameId = request.params(":gameId");
			String from = "/" + request.params(":from");
			String amount = request.params(":amount");
			
			MANAGER.transferFrom(gameId, MANAGER.getAccount(gameId, from.substring(from.lastIndexOf("/")+1)), Integer.parseInt(amount));
			
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
			
			MANAGER.transferFromTo(gameId, MANAGER.getAccount(gameId, from.substring(from.lastIndexOf("/")+1)), MANAGER.getAccount(gameId, to.substring(to.lastIndexOf("/")+1)), Integer.parseInt(amount));
			
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
	
	private static String getAlive(Request request, Response response){
		response.status(200);
		return "I'm alive";
	}
	
	public static void main(String... args) throws Exception {
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			YellowPagesRegistry.registerOrUpdateService(NAME, DESCRIPTION, SERVICE, URI + "/banks");
		} catch(YellowPagesNotAvailableException e) {
			e.printStackTrace();
		}
		
		get("/banks", BankManagerWebService::getAlive);
		post("/banks/:gameId/accounts", BankManagerWebService::postAccount);
		get("/banks/:gameId/accounts/:accountId", BankManagerWebService::getAccount);
		post("/banks/:gameId/transfer/to/:to/:amount", BankManagerWebService::postTransferTo);
		post("/banks/:gameId/transfer/from/:from/:amount", BankManagerWebService::postTransferFrom);
		post("/banks/:gameId/transfer/from/:from/to/:to/:amount", BankManagerWebService::postTransferFromTo);
	}
}
