package org.haw.vs.praktikum.gwln.bank.rest.server;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.haw.vs.praktikum.gwln.bank.Account;
import org.haw.vs.praktikum.gwln.bank.Bank;
import org.haw.vs.praktikum.gwln.bank.BankManager;
import org.haw.vs.praktikum.gwln.bank.InsufficientFondsException;
import org.haw.vs.praktikum.gwln.bank.Transaction;
import org.haw.vs.praktikum.gwln.bank.Transfer;
import org.haw.vs.praktikum.gwln.bank.rest.AccountJsonMarshaller;
import org.haw.vs.praktikum.gwln.bank.rest.BankJsonMarshaller;
import org.haw.vs.praktikum.gwln.bank.rest.TransferJsonMarshaller;
import org.haw.vs.praktikum.gwln.client.restclient.game.Game;
import org.haw.vs.praktikum.gwln.client.restclient.game.GamesRestClient;
import org.haw.vs.praktikum.gwln.events.Event;
import org.haw.vs.praktikum.gwln.events.rest.EventJsonMarshaller;
import org.haw.vs.praktikum.gwln.events.rest.client.EventManagerRestClient;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesNotAvailableException;
import org.haw.vs.praktikum.gwln.yellowpages.YellowPagesRegistry;
import org.json.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import spark.Request;
import spark.Response;

public class BankManagerWebService {
	private static final String NAME = "Bank Service 42_1337_69";
	private static final String DESCRIPTION = "Bester Bank Service 42_1337_69";
	private static final String SERVICE = "banks";
	private static String URI;
	
	private static final BankJsonMarshaller BANK_MARSHALLER = new BankJsonMarshaller();
	private static final AccountJsonMarshaller ACCOUNT_MARSHALLER = new AccountJsonMarshaller();
	private static final TransferJsonMarshaller TRANSFER_MARSHALLER = new TransferJsonMarshaller();
	private static final EventJsonMarshaller EVENT_MARSHALLER = new EventJsonMarshaller();
	
	private static final BankManager MANAGER = new BankManager();
	
	private static int BANK_COUNTER = 0;
	private static int TRANSACTION_COUNTER = 0;
	
	private static String getBanks(Request request, Response response) {
		try {
			List<Bank> banks = MANAGER.getBanks();
			
			return BANK_MARSHALLER.marshall("/banks/", banks);
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String postBank(Request request, Response response) {
		try {
			JSONObject json = new JSONObject(request.body());
			String game = json.getString("game");
			
			Bank bank = new Bank(BANK_COUNTER++, game);
			MANAGER.addBank(bank);
			
			response.header(HttpHeader.LOCATION.asString(), URI + "/banks/" + bank.getId());
			response.status(HttpStatus.CREATED_201);
			return "Bank " + bank.getId() + " wurde erfolgreich angelegt";
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String getBank(Request request, Response response) {
		try {
			String bankId = request.params("bankId");
			
			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
			if(bank == null) {
				response.status(HttpStatus.NOT_FOUND_404);
				return "Die Bank '" + bankId + "' existiert nicht!";
			}
			
			JSONObject json = new JSONObject();
			json.put("accounts", URI + "/banks/" + bank.getId() + "/accounts");
			json.put("transfers", URI + "/banks/" + bank.getId() + "/transfers");
			return json.toString();
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String putBank(Request request, Response response) {
		try {
			// TODO: Not yet implemented
//			String bankId = request.params("bankId");
//			JSONObject json = new JSONObject(request.body());
//			String accounts = json.getString("accounts");
//			String transfers = json.getString("transfers");
//			
//			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
//			// TODO: accounts übernehmen
//			// TODO: transfers übernehmen
			
//			response.status(HttpStatus.ACCEPTED_202);
//			return "Bank '" + bankId + "' erfolgreich aktualisiert";
			
			response.status(HttpStatus.NOT_IMPLEMENTED_501);
			return "Not yet implemented";
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String postAccount(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			
			Account account = ACCOUNT_MARSHALLER.unmarshall(request.body());
			account.setUri(URI + "/banks/" + bankId + "/accounts/" + account.getId());
			MANAGER.getBank(Integer.parseInt(bankId)).addAccount(account);
			
			response.header(HttpHeader.LOCATION.asString(), account.getUri());
			response.status(HttpStatus.CREATED_201);
			return "Konto erfolgreich angelegt";
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String getAccount(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String accountId = request.params(":accountId");
			
			Account account = MANAGER.getBank(Integer.parseInt(bankId)).getAccount(accountId);
			
			return ACCOUNT_MARSHALLER.marshall(account);
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String getTransfers(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			
			List<Transfer> transfers = MANAGER.getBank(Integer.parseInt(bankId)).getTransfers();
			
			// TODO: Es sollen garnicht die Transfer-Objekte zurückgegeben werden, sondern nur die URLs
			return TRANSFER_MARSHALLER.marshall(transfers);
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String getTransferById(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String transferId = request.params(":transferId");
			
			Transfer transfer = MANAGER.getBank(Integer.parseInt(bankId)).getTransfer(Integer.parseInt(transferId));
			
			return TRANSFER_MARSHALLER.marshall(transfer);
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String postTransaction(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			
			Transaction transaction = new Transaction(TRANSACTION_COUNTER++, URI + "/banks/" + bankId + "/transaction/");
			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
			bank.createTransaction(transaction);
			
			response.header(HttpHeader.LOCATION.asString(), transaction.getUri());
			response.status(HttpStatus.CREATED_201);
			return "Transaktion angelegt auf ID: " + String.valueOf(transaction.getId());
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String getTransactionById(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String transactionId = request.params(":transactionId");
			
			Transaction transaction = MANAGER.getBank(Integer.parseInt(bankId)).getTransaction(Integer.parseInt(transactionId));
			
			return transaction.getState().toString();
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String putTransactionById(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String transactionId = request.params(":transactionId");
			
			Transaction transaction = MANAGER.getBank(Integer.parseInt(bankId)).getTransaction(Integer.parseInt(transactionId));
			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
			bank.commitTransaction(transaction);
			
			String time = String.valueOf(System.currentTimeMillis());
			List<Event> events = new ArrayList<>();
			for(Transfer transfer : transaction.getTransfers()) {
				Event eventFrom = new Event(
						"siehe Location Header", 
						bank.getGame(), 
						"Bank Transaction", 
						"Bank Transaction Event", 
						"Transaction Commited (Transfer from)", 
						transaction.getUri(), 
						transfer.getFrom().getPlayer(),
						time
				);
				Event eventTo = new Event(
						"siehe Location Header", 
						bank.getGame(), 
						"Bank Transaction", 
						"Bank Transaction Event", 
						"Transaction Commited (Transfer to)", 
						transaction.getUri(), 
						transfer.getTo().getPlayer(),
						time
				);
				events.add(eventFrom);
				events.add(eventTo);
			}
			
			try {
				meldeEvents(bank, events);
			} catch(MalformedURLException | UnirestException e) {
				// Wenn das Event nicht zugestellt werden kann, ist halt pech. Wir geben es ja trotzdem zurück
				System.err.println("[commit transaction] Event konnte nicht zugestellt werden: " + e.getMessage());
			}
			
			response.status(HttpStatus.ACCEPTED_202);
			return EVENT_MARSHALLER.marshall(events);
		} catch(InsufficientFondsException e) {
			response.status(HttpStatus.FORBIDDEN_403);
			return e.getMessage();
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String deleteTransactionById(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String transactionId = request.params(":transactionId");
			
			Transaction transaction = MANAGER.getBank(Integer.parseInt(bankId)).getTransaction(Integer.parseInt(transactionId));
			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
			bank.rollbackTransaction(transaction);
			
			Event event = new Event("siehe Location Header", 
					bank.getGame(), 
					"Bank Transaction", 
					"Bank Transaction Event", 
					"Transaction Cancelled", 
					transaction.getUri(), 
					null,
					System.currentTimeMillis()+""
			);
			
			try {
				meldeEvents(bank, event);
			} catch(MalformedURLException | UnirestException e) {
				// Wenn das Event nicht zugestellt werden kann, ist halt pech. Wir geben es ja trotzdem zurück
				System.err.println("[delete transaction] Event konnte nicht zugestellt werden: " + e.getMessage());
			}
			
			response.status(HttpStatus.ACCEPTED_202);
			return EVENT_MARSHALLER.marshall(event);
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String postTransferTo(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String to = "/" + request.params(":to");
			String amount = request.params(":amount");
			String reason = request.body();
			String transactionId = request.queryParams("transaction");
			
			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
			Account fromAccount = bank.getAccount("bank");
			Account toAccount = bank.getAccount(to.substring(to.lastIndexOf("/")+1));
			if(transactionId == null) {
				int transferId = bank.transfer(fromAccount, toAccount, Integer.parseInt(amount), reason);
				
				Event event = new Event("siehe Location Header", 
						bank.getGame(), 
						"Bank Transaction", 
						"Bank Transaction Event", 
						"Transfer to " + toAccount.getPlayer(), 
						URI + "/banks/" + bankId + "/transfers/" + transferId, 
						toAccount.getPlayer(),
						System.currentTimeMillis()+""
				);
				
				try {
					meldeEvents(bank, event);
				} catch(MalformedURLException | UnirestException e) {
					// Wenn das Event nicht zugestellt werden kann, ist halt pech. Wir geben es ja trotzdem zurück
					System.err.println("[transfer to] Event konnte nicht zugestellt werden: " + e.getMessage());
				}
				
				response.status(HttpStatus.CREATED_201);
				return EVENT_MARSHALLER.marshall(event);
			} else {
				Transaction transaction = bank.getTransaction(Integer.parseInt(transactionId));
				bank.transactionalTransfer(transaction, fromAccount, toAccount, Integer.parseInt(amount), reason);
				
				response.status(HttpStatus.CREATED_201);
				return "Transfer erfolgreich der Transaktion angehängt";
			}
		} catch(InsufficientFondsException e) {
			response.status(HttpStatus.FORBIDDEN_403);
			return e.getMessage();
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String postTransferFrom(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String from = "/" + request.params(":from");
			String amount = request.params(":amount");
			String reason = request.body();
			String transactionId = request.queryParams("transaction");
			
			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
			Account fromAccount = bank.getAccount(from.substring(from.lastIndexOf("/")+1));
			Account toAccount = bank.getAccount("bank");
			if(transactionId == null) {
				int transferId = bank.transfer(fromAccount, toAccount, Integer.parseInt(amount), reason);
				
				Event event = new Event("siehe Location Header", 
						bank.getGame(), 
						"Bank Transaction", 
						"Bank Transaction Event", 
						"Transfer from " + fromAccount.getPlayer(), 
						URI + "/banks/" + bankId + "/transfers/" + transferId, 
						fromAccount.getPlayer(),
						System.currentTimeMillis()+""
				);
				
				try {
					meldeEvents(bank, event);
				} catch(MalformedURLException | UnirestException e) {
					// Wenn das Event nicht zugestellt werden kann, ist halt pech. Wir geben es ja trotzdem zurück
					System.err.println("[transfer from] Event konnte nicht zugestellt werden: " + e.getMessage());
				}
				
				response.status(HttpStatus.CREATED_201);
				return EVENT_MARSHALLER.marshall(event);
			} else {
				Transaction transaction = bank.getTransaction(Integer.parseInt(transactionId));
				bank.transactionalTransfer(transaction, fromAccount, toAccount, Integer.parseInt(amount), reason);
				
				response.status(HttpStatus.CREATED_201);
				return "Transfer erfolgreich der Transaktion angehängt";
			}
		} catch(InsufficientFondsException e) {
			response.status(HttpStatus.FORBIDDEN_403);
			return e.getMessage();
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static String postTransferFromTo(Request request, Response response) {
		try {
			String bankId = request.params(":bankId");
			String to = "/" + request.params(":to");
			String from = "/" + request.params(":from");
			String amount = request.params(":amount");
			String reason = request.body();
			String transactionId = request.queryParams("transaction");
			
			Bank bank = MANAGER.getBank(Integer.parseInt(bankId));
			Account fromAccount = bank.getAccount(from.substring(from.lastIndexOf("/")+1));
			Account toAccount = bank.getAccount(to.substring(to.lastIndexOf("/")+1));
			
			if(transactionId == null) {
				int transferId = bank.transfer(fromAccount, toAccount, Integer.parseInt(amount), reason);
				
				String time = String.valueOf(System.currentTimeMillis());
				Event eventFrom = new Event(
						"siehe Location Header", 
						bank.getGame(), 
						"Bank Transaction", 
						"Bank Transaction Event", 
						"Transfer from " + fromAccount.getPlayer(), 
						URI + "/banks/" + bankId + "/transfers/" + transferId, 
						fromAccount.getPlayer(),
						time
				);
				Event eventTo = new Event(
						"siehe Location Header", 
						bank.getGame(), 
						"Bank Transaction", 
						"Bank Transaction Event", 
						"Transfer to " + toAccount.getPlayer(), 
						URI + "/banks/" + bankId + "/transfers/" + transferId, 
						toAccount.getPlayer(),
						time
				);
				
				try {
					meldeEvents(bank, eventFrom, eventTo);
				} catch(MalformedURLException | UnirestException e) {
					// Wenn das Event nicht zugestellt werden kann, ist halt pech. Wir geben es ja trotzdem zurück
					System.err.println("[transfer from to] Event konnte nicht zugestellt werden: " + e.getMessage());
				}
				
				response.status(HttpStatus.CREATED_201);
				return EVENT_MARSHALLER.marshall(eventFrom, eventTo);
			} else {
				Transaction transaction = bank.getTransaction(Integer.parseInt(transactionId));
				bank.transactionalTransfer(transaction, fromAccount, toAccount, Integer.parseInt(amount), reason);
				
				response.status(HttpStatus.CREATED_201);
				return "Transfer erfolgreich der Transaktion angehängt";
			}
		} catch(InsufficientFondsException e) {
			response.status(HttpStatus.FORBIDDEN_403);
			return e.getMessage();
		} catch(Exception e) {
			response.status(HttpStatus.PRECONDITION_FAILED_412);
			return e.getMessage();
		}
	}
	
	private static void meldeEvents(Bank bank, Event... events) throws MalformedURLException, UnirestException {
		meldeEvents(bank, Arrays.asList(events));
	}
	
	private static void meldeEvents(Bank bank, List<Event> events) throws MalformedURLException, UnirestException {
		URL gameURL = new URL(bank.getGame());
		GamesRestClient gamesClient = new GamesRestClient(gameURL.getProtocol() + "://" + gameURL.getAuthority());
		Game game = gamesClient.getGame(gameURL.getPath());
		JSONObject services = gamesClient.getGameServices(game.getServices());
		EventManagerRestClient eventClient = new EventManagerRestClient(services.getString("events"));
		for(Event event : events) {
			eventClient.postEvent(event);
		}
	}
	
	public static void main(String... args) throws Exception {
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			YellowPagesRegistry.getInstance().registerOrUpdateService(NAME, DESCRIPTION, SERVICE, URI + "/banks");
		} catch(YellowPagesNotAvailableException e) {
			e.printStackTrace();
		}
		
		get("/banks", BankManagerWebService::getBanks);
		post("/banks", BankManagerWebService::postBank);
		get("/banks/:bankId", BankManagerWebService::getBank);
		put("/banks/:bankId", BankManagerWebService::putBank);
		post("/banks/:bankId/accounts", BankManagerWebService::postAccount);
		get("/banks/:bankId/accounts/:accountId", BankManagerWebService::getAccount);
		get("/banks/:bankId/transfers", BankManagerWebService::getTransfers);
		get("/banks/:bankId/transfers/:transferId", BankManagerWebService::getTransferById);
		post("/banks/:bankId/transaction", BankManagerWebService::postTransaction);
		get("/banks/:bankId/transaction/:transactionId", BankManagerWebService::getTransactionById);
		put("/banks/:bankId/transaction/:transactionId", BankManagerWebService::putTransactionById);
		delete("/banks/:bankId/transaction/:transactionId", BankManagerWebService::deleteTransactionById);
		post("/banks/:bankId/transfer/to/:to/:amount", BankManagerWebService::postTransferTo);
		post("/banks/:bankId/transfer/from/:from/:amount", BankManagerWebService::postTransferFrom);
		post("/banks/:bankId/transfer/from/:from/to/:to/:amount", BankManagerWebService::postTransferFromTo);
	}
}
