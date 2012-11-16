package br.vue.components;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import br.vue.components.game.ChatPane;
import br.vue.components.game.GridPane;

public class GamePane extends JPanel {

	private GridPane grid;
	private ChatPane chat;

	public GamePane() {
		super();
		setLayout(new GridBagLayout());

		grid = new GridPane();
		chat = new ChatPane(new String[] { "bla", "bli", "blou" });
		addComponents();
	}

	private void addComponents() {
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		add(grid, gbc);
		gbc.gridx++;
		add(chat, gbc);
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);
	}

	private static final long serialVersionUID = 1L;
}
