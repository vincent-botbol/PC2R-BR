package br.vue.components.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import br.model.ModelFacade;
import br.model.logic.GameGrid;

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
		setPreferredSize(new Dimension(500, 500));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		float ratiox = this.getSize().width / (float) GameGrid.width;
		float ratioy = this.getSize().height / (float) GameGrid.height;
		g2.scale(ratiox, ratioy);
		//on scale, comme ça c'est beau

		model.getGrid().drawGrid(g2);

		System.out.println("test");

	}

	private static final long serialVersionUID = 1L;
}
