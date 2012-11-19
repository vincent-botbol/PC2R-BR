package br.vue.components;

import java.awt.Container;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		super("Bataille Navale Royale");

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setVisible(true);
	}

	public void setFrameContentPane(Container c) {
		setContentPane(c);
		pack();
	}

	private static final long serialVersionUID = 1L;
}
