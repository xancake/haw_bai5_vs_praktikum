package org.haw.vs.praktikum.gwln.praktikum2.b.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.haw.vs.praktikum.gwln.praktikum2.b.client.games.Game;

public class RestopolyLobbyUI {
	private static final String TITLE = "Restopoly Lobby";
	
	private JFrame _frame;
	private JList<Game> _gameList;
	private JButton _beitretenButton;
	
	private RestopolyLobbyListener_I _listener;
	
	public RestopolyLobbyUI(RestopolyLobbyListener_I listener) {
		_listener = Objects.requireNonNull(listener);
		initComponents();
		initLayout();
		initListeners();
	}
	
	private void initComponents() {
		_frame = new JFrame(TITLE);
		_gameList = new JList<>();
		_beitretenButton = new JButton("Beitreten");
	}
	
	private void initLayout() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(_beitretenButton);
		
		JPanel content = new JPanel(new BorderLayout());
		
		content.add(new JScrollPane(_gameList), BorderLayout.CENTER);
		content.add(buttonPanel, BorderLayout.SOUTH);
		
		_frame.setContentPane(content);
	}
	
	private void initListeners() {
		_beitretenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_listener.onBeitreten(_gameList.getSelectedValue());
			}
		});
	}
	
	public void setGames(List<Game> games) {
		_gameList.setListData(games.toArray(new Game[0]));
	}
	
	public void show(){
		_frame.setVisible(true);
	}
	
	public void hide(){
		_frame.setVisible(false);
	}
}
