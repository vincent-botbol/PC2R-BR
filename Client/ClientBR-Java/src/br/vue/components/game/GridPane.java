package br.vue.components.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class GridPane extends JPanel {

	public GridPane() {
		super();

		setLayout(new GridBagLayout());
		// setMaximumSize(newDimension(800, 800));

		setMinimumSize(new Dimension(200, 200));
		setPreferredSize(new Dimension(600, 600));

		setBackground(Color.red);
	}

	private static final long serialVersionUID = 1L;
}
