package br.vue.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import br.vue.components.game.ChatPane;
import br.vue.components.game.GridPane;
import br.vue.components.game.LogPane;

public class GamePane extends JPanel {

	private GridPane grid;
	private ChatPane chat;
	private LogPane log;

	public GamePane() {
		super();
		setLayout(new GridBagLayout());

		grid = new GridPane();
		log = new LogPane();
		chat = new ChatPane(new String[] { "bla", "bli", "blou" });
		addComponents();

	}

	private void addComponents() {
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(15, 15, 15, 15);

		gbc.weightx = gbc.weighty = 2;
		gbc.gridheight = 1;
		gbc.gridx = gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		add(grid, gbc);

		gbc.weightx = gbc.weighty = 1;
		gbc.gridy++;
		add(log, gbc);

		gbc.gridheight = 2;
		gbc.gridx++;
		gbc.gridy = 0;
		add(chat, gbc);
	}
	
	public LogPane getLog(){
		return log;
	}

	private static final long serialVersionUID = 1L;
}
