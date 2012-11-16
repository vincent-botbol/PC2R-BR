package br.vue.components.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ChatPane extends JPanel {

	private JLabel[] players;
	private JScrollPane scroll;
	private JTextArea conversation;
	private JTextField message;
	private JButton sendButton;

	/**
	 * Todo : ajouter les icons aux players
	 * 
	 * @param playersName
	 */

	public ChatPane(String[] playersName) {
		super();

		setBorder(new TitledBorder("Chat"));

		setPreferredSize(new Dimension(300, 300));

		this.players = new JLabel[playersName.length];

		for (int i = 0; i < players.length; i++)
			this.players[i] = new JLabel(playersName[i]);

		setLayout(new GridBagLayout());

		conversation = new JTextArea();
		scroll = new JScrollPane(conversation);
		conversation.setEditable(false);

		message = new JTextField();

		sendButton = new JButton("Envoyer");

		addComponents();
	}

	private void addComponents() {
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for (int i = 0; i < players.length; i++) {
			add(players[i], gbc);
			gbc.gridy++;
		}

		gbc.weighty = ++gbc.weightx;
		gbc.fill = GridBagConstraints.BOTH;
		add(scroll, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;

		gbc.weighty = 0;
		add(message, gbc);

		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx++;
		add(sendButton, gbc);
	}

	private static final long serialVersionUID = 1L;
}
