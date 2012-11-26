package br.model.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import br.model.ModelFacade;

public class GameGrid {
	@SuppressWarnings("unused")
	private ModelFacade observable;

	public GameGrid(ModelFacade modelFacade) {
		this.observable = modelFacade;
	}

	public void processClick(Point p) {

	}

	public void processKey(int keycode) {

	}

	public void drawGrid(Graphics2D g2) {
		Rectangle2D r2 = new Rectangle2D.Double(0, 0, 100, 100);

		g2.setBackground(Color.blue);
		g2.setColor(Color.red);
		g2.draw(r2);

	}
}
