package br.vue.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class ConnexionPane extends JPanel {

	private final JLabel login_label = new JLabel("Pseudo"),
			server_label = new JLabel("Serveur");

	private JTextField login, server;

	private JButton connect;

	private JLabel info_start;

	private GridBagConstraints gbc;

	public ConnexionPane() {
		super();
		setBorder(new TitledBorder("Connexion"));

		setLayout(new GridBagLayout());

		login = new JTextField(20);
		login_label.setLabelFor(login);

		server = new JTextField(20);
		server_label.setLabelFor(server);

		connect = new JButton("Connexion");

		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showInfo("Va niquer ta mère!");

			}
		});

		info_start = new JLabel();

		gbc = new GridBagConstraints();
		addComponents();
	}

	private void addComponents() {
		gbc.insets = new Insets(5, 5, 5, 5);

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
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(connect, gbc);
	}

	/**
	 * A appeler par le dispatcher
	 * 
	 * @param s
	 */
	public void showInfo(String s) {
		// Dynamic resize
		if (info_start.getText().isEmpty()) {
			gbc.gridy++;
			gbc.anchor = GridBagConstraints.CENTER;
			info_start.setText(s);
			add(info_start, gbc);
			SwingUtilities.getWindowAncestor(this).pack();
		} else {
			info_start.setText(s);
		}
	}

	private static final long serialVersionUID = 1L;
}
