package br.vue.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class ConnectionPane extends JPanel {

	private final JLabel login_label = new JLabel("Pseudo"),
			server_label = new JLabel("Serveur");

	private JTextField login, server;

	private JButton connect;

	private JLabel info_start;

	private GridBagConstraints gbc;

	public ConnectionPane() {
		super();
		setBorder(new TitledBorder("Connexion"));

		setLayout(new GridBagLayout());

		server = new JTextField("localhost", 20);
		server.setActionCommand("CONNECT");
		server_label.setLabelFor(server);

		login = new JTextField("Test", 20);
		login.setActionCommand("CONNECT");
		login_label.setLabelFor(login);

		connect = new JButton("Connexion");
		connect.setActionCommand("CONNECT");

		info_start = new JLabel();

		gbc = new GridBagConstraints();
		addComponents();

	}

	private void addComponents() {
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = gbc.gridy = 0;
		add(server_label, gbc);

		gbc.gridx++;
		add(server, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		add(login_label, gbc);

		gbc.gridx++;
		add(login, gbc);

		gbc.gridy++;
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(connect, gbc);
	}

	/**
	 * A appeler par le dispatcher
	 * 
	 * @param s
	 */
	public void showInfo(String s) {
		String text = "<html>" + s.replace("\n", "<br/>") + "</html>";
		// first show up
		if (info_start.getText().isEmpty()) {
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.gridwidth = 2;
			add(info_start, gbc);
		}

		info_start.setText(text);
		// Dynamic resize - trick #342
		SwingUtilities.getWindowAncestor(this).pack();
	}

	public JButton getConnect() {
		return connect;
	}

	public JTextField getServerComponent() {
		return server;
	}

	public JTextField getLoginComponent() {
		return server;
	}

	public String getHost() {
		return server.getText();
	}

	public String getLogin() {
		return login.getText();
	}

	private static final long serialVersionUID = 1L;
}
