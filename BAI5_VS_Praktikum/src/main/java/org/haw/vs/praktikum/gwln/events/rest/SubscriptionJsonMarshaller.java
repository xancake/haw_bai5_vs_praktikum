package org.haw.vs.praktikum.gwln.events.rest;

import java.util.List;
import org.haw.vs.praktikum.gwln.events.Subscription;
import org.json.JSONArray;

public class SubscriptionJsonMarshaller {
	public String marshall(List<Subscription> subscriptions) {
		JSONArray array = new JSONArray();
		for(Subscription subscription : subscriptions) {
			array.put(subscription.getUri());
		}
		return array.toString();
	}
}
