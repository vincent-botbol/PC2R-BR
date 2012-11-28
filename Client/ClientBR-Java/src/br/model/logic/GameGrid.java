package br.model.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import br.model.ModelFacade;

public class GameGrid {
	@SuppressWarnings("unused")
	private ModelFacade observable;

	private final static Image background = Toolkit.getDefaultToolkit()
			.getImage("data/background.png");

	public final static int width = 500, height = 500;

	public GameGrid(ModelFacade modelFacade) {
		this.observable = modelFacade;
		// Create empty grid
	}

	public void processClick(Point p) {
		// Notify update
	}

	public void drawGrid(Graphics2D g2) {

		g2.drawImage(background, 0, 0, null);
		g2.setColor(Color.red);
		g2.fillRect(0, 0, 100, 100);
		g2.setColor(Color.blue);
		g2.fillRect(0, 0, 50, 50);

		// Todo : rajouter les tiles de mer en random avec un timer histoire de
		// faire joli.
	}
}
