package org.haw.vs.praktikum.gwln.client.ui.lobby;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.haw.vs.praktikum.gwln.client.restclient.game.Game;

@SuppressWarnings("serial")
public class GameListCellRenderer extends JLabel implements ListCellRenderer<Game> {
	public GameListCellRenderer() {
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Game> list, Game value, int index, boolean isSelected, boolean cellHasFocus) {
		setText(value.getName() + " (id: " + value.getId() + ")");
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		return this;
	}
}
