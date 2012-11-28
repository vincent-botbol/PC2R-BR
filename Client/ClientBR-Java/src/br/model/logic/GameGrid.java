package br.model.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Random;

import javax.swing.ImageIcon;

import br.common.UpdateArguments;
import br.model.ModelFacade;

public class GameGrid {
	private ModelFacade observable;

	private final static Image background = new ImageIcon("data/background.png")
			.getImage();
	private final static Image sea_tiles = new ImageIcon("data/sea_tiles.png")
			.getImage();

	private final static Random r = new Random();

	private final static int margin = 50 - 8, cell_size = 25;

	public final static int width = 500, height = 500;

	private Thread animate;

	private Cell[][] cell_grid;

	// field submarines
	// field drone

	public GameGrid(ModelFacade modelFacade) {
		this.observable = modelFacade;
		cell_grid = new Cell[16][];
		for (int i = 0; i < 16; i++) {
			cell_grid[i] = new Cell[16];
			for (int j = 0; j < cell_grid.length; j++) {
				cell_grid[i][j] = new Cell();
				cell_grid[i][j].setNumTile(r.nextInt(15));
			}
		}
		animate = null;
	}

	// Tentative d'animation -> très laid.
	public void startAnimating() {
		if (animate == null) {
			animate = new Thread() {

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for (int i = 0; i < 16; i++) {
							for (int j = 0; j < cell_grid.length; j++) {
								cell_grid[i][j].setNumTile((cell_grid[i][j]
										.getNumTile() + 1) % 15);
							}
						}
						observable.notifyView(UpdateArguments.REDRAW);
					}
				}
			};
			animate.start();
		}
	}

	public void processClick(Point p) {
		// Notify update
	}

	private void drawGrid(Graphics2D g2) {
		g2.setColor(Color.white);
		int i, j;
		for (i = 0; i < 16; i++) {

			for (j = 0; j < 16; j++) {
				Point dest = new Point(margin + j * cell_size + j, margin + i
						* cell_size + i), src = new Point(
						cell_grid[i][j].getNumTile() * 25, 0);
				g2.drawImage(sea_tiles, dest.x, dest.y, dest.x + 25,
						dest.y + 25, src.x, src.y, src.x + 25, src.y + 25, null);
			}
		}

		// dessiner sous-marins ici

		// dessiner drone ici

		for (i = 0; i <= 16; i++) {
			g2.drawLine(margin - 1, margin + i * cell_size + i - 1, width
					- margin, margin + i * cell_size + i - 1);
			g2.drawLine(margin + i * 25 + i, margin, margin + i * 25 + i,
					height - margin - 1);
		}
	}

	public void draw(Graphics2D g2) {

		g2.drawImage(background, 0, 0, null);
		drawGrid(g2);

	}
}
