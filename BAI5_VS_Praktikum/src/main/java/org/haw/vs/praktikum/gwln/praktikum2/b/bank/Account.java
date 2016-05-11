package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import org.haw.vs.praktikum.gwln.datatypes.bounded.MutableBoundedInteger;

public class Account {
	private String _id;
	private String _uri;
	private String _player;
	private MutableBoundedInteger _saldo;
	
	public Account(String player, int saldo) {
		String tempPlayer = "/" + player;
		_id = tempPlayer.substring(tempPlayer.lastIndexOf("/")+1);
		_player = player;
		_saldo = new MutableBoundedInteger(0, Integer.MAX_VALUE, saldo);
	}
	
	public String getId() {
		return _id;
	}
	
	public String getUri() {
		return _uri;
	}
	
	public String getPlayer() {
		return _player;
	}
	
	public int getSaldo() {
		return _saldo.getValue();
	}
	
	void setUri(String uri) {
		_uri = uri;
	}
	
	void setSaldo(int saldo) {
		_saldo.setValue(saldo);
	}
	
	void alterSaldo(int amount) {
		_saldo.alterValue(amount);
	}
	
	@Override
	public String toString() {
		return "Account [id=" + _id + ", player=" + _player + ", saldo=" + _saldo + "]";
	}
}
