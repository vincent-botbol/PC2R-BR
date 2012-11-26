package br.vue.components.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import br.model.ModelFacade;

public class GridPane extends JPanel {

	/**
	 * Nécéssaire pour le query state du graphique
	 */
	private ModelFacade model;

	public GridPane(ModelFacade model) {
		super();

		this.model = model;

		setLayout(new GridBagLayout());

		setMinimumSize(new Dimension(200, 200));
		setPreferredSize(new Dimension(600, 600));

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		model.getGrid().drawGrid(g2);
		
	}

	private static final long serialVersionUID = 1L;
}
