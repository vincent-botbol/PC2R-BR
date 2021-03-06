package br.vue.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import br.model.ModelFacade;
import br.vue.components.game.ChatPane;
import br.vue.components.game.GridPane;
import br.vue.components.game.LogPane;

public class GamePane extends JPanel {

	private GridPane grid;
	private ChatPane chat;
	private LogPane log;

	private GridBagConstraints gbc;

	public GamePane(ModelFacade model) {
		super();

		setLayout(new GridBagLayout());

		setMinimumSize(new Dimension(300, 300));

		grid = new GridPane(model);
		log = new LogPane();
		chat = new ChatPane();

		gbc = new GridBagConstraints();

		addComponents();
	}

	private void addComponents() {
		gbc.insets = new Insets(15, 15, 15, 15);

		gbc.weightx = gbc.weighty = 2;
		gbc.gridheight = 1;
		gbc.gridx = gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		add(grid, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = gbc.weighty = 1;
		gbc.gridy++;
		add(log, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 2;
		gbc.gridx++;
		gbc.gridy = 0;
		add(chat, gbc);
	}

	public LogPane getLog() {
		return log;
	}

	public GridPane getGrid() {
		return grid;
	}

	public ChatPane getChat() {
		return chat;
	}

	@Override
	public void paintComponents(Graphics g) {

		super.paintComponents(g);
	}

	private static final long serialVersionUID = 1L;

}
