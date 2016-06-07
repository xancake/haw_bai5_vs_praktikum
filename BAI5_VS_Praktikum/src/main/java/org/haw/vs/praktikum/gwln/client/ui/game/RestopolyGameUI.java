package org.haw.vs.praktikum.gwln.client.ui.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.*;

public class RestopolyGameUI {
	private RestopolyGameListener_I _listener;
	
	private JFrame _frame;
	private JLabel _diceResultLabel;
	private JButton _wuerfelnButton;
	private JButton _readyButton;
	
	public RestopolyGameUI(RestopolyGameListener_I listener) {
		_listener = Objects.requireNonNull(listener);
		initComponents();
		initLayout();
		initListeners();
	}
	
	private void initComponents() {
		_frame = new JFrame("Restopoly");
		_wuerfelnButton = new JButton("Würfeln");
		_wuerfelnButton.setEnabled(false);
		_readyButton = new JButton("Ready");
		_diceResultLabel = new JLabel("");
	}
	
	private void initLayout() {
		JPanel content = new JPanel(new BorderLayout());
		content.add(_diceResultLabel);
		content.add(_wuerfelnButton);
		content.add(_readyButton);

		_frame.setContentPane(content);
	}
	
	private void initListeners() {
		_wuerfelnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_listener.onWuerfeln();
			}
		});
		_readyButton.addActionListener(e -> {
			_listener.onReady();
			_wuerfelnButton.setEnabled(true);
			_readyButton.setText("End Turn");
		});
	}
	
	public void show(){
		_frame.setVisible(true);
	}
	
	public void hide(){
		_frame.setVisible(false);
	}

	public void setDiceResult(String result){
		_diceResultLabel.setText(result+" gewürfelt");
	}
}
