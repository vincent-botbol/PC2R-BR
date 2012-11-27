package br.vue.components;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		super("Bataille Navale Royale");

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		setMinimumSize(new Dimension(400, 300));
	}

	public void setFrameContentPane(Container c) {
		setContentPane(c);
		pack();
	}

	private static final long serialVersionUID = 1L;

}
