package org.haw.vs.praktikum.gwln.client.ui.lobby;

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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.haw.vs.praktikum.gwln.client.restclient.game.Game;
import org.haw.vs.praktikum.gwln.yellowpages.Service;

public class RestopolyLobbyUI {
	private static final String TITLE = "Restopoly Lobby";
	
	private JFrame _frame;
	private JLabel _gameServiceLabel;
	private JList<Game> _gameList;
	private JButton _connectGameServiceButton;
	private JButton _createGameButton;
	private JButton _refreshButton;
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
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_gameServiceLabel = new JLabel();
		_gameList = new JList<>();
		_connectGameServiceButton = new JButton("Verbinden");
		_connectGameServiceButton.setToolTipText("Mit einem Game-Service verbinden");
		_createGameButton = new JButton("Anlegen");
		_refreshButton = new JButton("Aktualisieren");
		_beitretenButton = new JButton("Beitreten");
		_beitretenButton.setEnabled(false);
	}
	
	private void initLayout() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(_connectGameServiceButton);
		buttonPanel.add(_createGameButton);
		buttonPanel.add(_refreshButton);
		buttonPanel.add(_beitretenButton);
		
		JPanel content = new JPanel(new BorderLayout());
		
		content.add(_gameServiceLabel, BorderLayout.NORTH);
		content.add(new JScrollPane(_gameList), BorderLayout.CENTER);
		content.add(buttonPanel, BorderLayout.SOUTH);
		
		_frame.setContentPane(content);
		_frame.pack();
		_frame.setLocationRelativeTo(null);
	}
	
	private void initListeners() {
		_gameList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				_beitretenButton.setEnabled(_gameList.getSelectedValue() != null);
			}
		});
		_connectGameServiceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_listener.onMitGameServiceVerbinden();
			}
		});
		_createGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_listener.onSpielAnlegen();
			}
		});
		_refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_listener.onAktualisieren();
			}
		});
		_beitretenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_listener.onBeitreten(_gameList.getSelectedValue());
			}
		});
	}
	
	public void setGameService(Service service) {
		_gameServiceLabel.setText(service.getName() + " (id " + service.getId() + ")");
	}
	
	public void setGames(List<Game> games) {
		_gameList.setListData(games.toArray(new Game[0]));
	}
	
	public void setGameButtonsEnabled(boolean enable) {
		_createGameButton.setEnabled(enable);
		_refreshButton.setEnabled(enable);
		_beitretenButton.setEnabled(enable);
	}
	
	public void showFehlermeldung(String fehlermeldung) {
		JOptionPane.showMessageDialog(_frame, fehlermeldung, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	public void show(){
		_frame.setVisible(true);
	}
	
	public void hide(){
		_frame.setVisible(false);
	}
}
