package org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RestopolyGameUI {
	private RestopolyGameListener_I _listener;
	
	private JFrame _frame;
	private JButton _wuerfelnButton;
	
	public RestopolyGameUI(RestopolyGameListener_I listener) {
		_listener = Objects.requireNonNull(listener);
		initComponents();
		initLayout();
		initListeners();
	}
	
	private void initComponents() {
		_frame = new JFrame("Restopoly");
		_wuerfelnButton = new JButton("Würfeln");
	}
	
	private void initLayout() {
		JPanel content = new JPanel(new BorderLayout());
		
		content.add(_wuerfelnButton);
		
		_frame.setContentPane(content);
	}
	
	private void initListeners() {
		_wuerfelnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_listener.onWuerfeln();
			}
		});
	}
	
	public void show(){
		_frame.setVisible(true);
	}
	
	public void hide(){
		_frame.setVisible(false);
	}
}
