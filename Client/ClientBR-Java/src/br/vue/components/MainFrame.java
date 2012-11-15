package br.vue.components;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		super("Bataille Navale Royale");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		pack();
		setVisible(true);
	}

	private static final long serialVersionUID = 1L;
}
