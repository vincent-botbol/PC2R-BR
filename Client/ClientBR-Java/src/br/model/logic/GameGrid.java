package br.model.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
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
	private final static Color vertTransparent = new Color(0, 255, 0, 50),
			rougeTransparent = new Color(255, 0, 0, 150);

	private final static Random r = new Random();

	public final static int margin = 50 - 8, cell_size = 25;

	public final static int width = 500, height = 500;

	private Thread animate;

	private Cell[][] cell_grid;

	private List<Submarine> submarines;
	private Drone drone;

	private Submarine current;

	private int nbActions;

	private boolean isLaserActivable;

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
		submarines = new ArrayList<>();
		nbActions = 0;

		animate = null;
		isLaserActivable = false;
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

	// Renvoie la liste des actions à réaliser
	public List<String> processClick(Point p) {
		List<String> liste = new ArrayList<>();
		if (!cell_grid[p.x][p.y].isReachable()) {
			observable.notifyView(UpdateArguments.NOTREACHABLE);
			return liste;
		}
		if (isLaserActivation(p)) {
			if (!isLaserActivable) {
				observable.notifyView(UpdateArguments.NOTRIGGERABLE);
				return liste;
			}
			liste.add("E");
			nbActions--;
			observable.notifyView(UpdateArguments.LASER);
			isLaserActivable = false;
		} else {
			Point posInitiale = drone.getLocation();
			boolean isLeftMove = posInitiale.x > p.x;
			boolean isUpMove = posInitiale.y > p.y;
			// calcule les commandes de mouvement à donner pour réaliser
			// l'action
			for (int i = 0; i < Math.abs(posInitiale.x - p.x); i++) {
				liste.add(isLeftMove ? "L" : "R");
				nbActions--;
			}
			for (int i = 0; i < Math.abs(posInitiale.y - p.y); i++) {
				liste.add(isUpMove ? "U" : "D");
				nbActions--;
			}
		}

		// Mets à jour la grille
		// normalement, jamais négatif, vérif client
		updateTurn(p.x, p.y, nbActions);

		observable.notifyView(UpdateArguments.DOTURN);

		return liste;
	}

	private boolean isLaserActivation(Point p) {
		return p.equals(drone);
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

		if (current != null) {
			current.drawSubmarine(g2);
		}

		// dessine les sous-marins
		for (Submarine s : submarines) {
			s.drawSubmarine(g2);
		}

		// dessine le drone
		// le drone est instancié lors de l'établissement des différents joueurs
		if (drone != null && drone.isActive()) {
			drone.drawDrone(g2);
		}

		// Dessins des cases atteignables
		for (i = 0; i < 16; i++) {
			for (j = 0; j < 16; j++) {
				Point dest = new Point(margin + j * cell_size + j, margin + i
						* cell_size + i);

				if (cell_grid[i][j].isOuch()) {
					// dessine croix
					g2.setStroke(new BasicStroke(2.f));
					g2.setColor(Color.red);
					g2.drawLine(dest.y, dest.x, dest.y + cell_size, dest.x
							+ cell_size);
					g2.drawLine(dest.y, dest.x + cell_size, dest.y + cell_size,
							dest.x);
					g2.setStroke(new BasicStroke(1.f));
				}
				if (cell_grid[i][j].isTouche()) {
					// dessine +
					g2.setStroke(new BasicStroke(2.f));
					g2.setColor(Color.green);
					g2.drawLine(dest.y + cell_size / 2, dest.x, dest.y
							+ cell_size / 2, dest.x + cell_size);
					g2.drawLine(dest.y, dest.x + cell_size / 2, dest.y
							+ cell_size, dest.x + cell_size / 2);
					g2.setStroke(new BasicStroke(1.f));
				}
				if (cell_grid[i][j].isMissed()) {
					// dessine rond
					g2.setStroke(new BasicStroke(2.f));
					g2.setColor(Color.magenta);
					g2.drawOval(dest.y + cell_size / 8, dest.x + cell_size / 8,
							3 * cell_size / 4, 3 * cell_size / 4);
					g2.setStroke(new BasicStroke(1.f));
				}

				if (cell_grid[i][j].isReachable()) {
					if (drone.x == i && drone.y == j && isLaserActivable) {
						g2.setColor(rougeTransparent);
					} else if (drone.x == i && drone.y == j) {
						continue;
					} else {
						g2.setColor(vertTransparent);
					}

					g2.fillRect(dest.y, dest.x, cell_size, cell_size);
				}
			}
		}

		g2.setColor(Color.white);
		for (i = 0; i <= 16; i++) {
			if (i < 16) {
				g2.drawString((i + 1) + "", 50 + 26 * i, 500 - 20);
				g2.drawString((char) ('A' - i + 15) + "", 20, 60 + 26 * i);
			}
			g2.drawLine(margin, margin + i * cell_size + i - 1, width - margin,
					margin + i * cell_size + i - 1);
			g2.drawLine(margin + i * 25 + i, margin, margin + i * 25 + i,
					height - margin - 1);
		}

	}

	public void draw(Graphics2D g2) {
		g2.drawImage(background, 0, 0, null);
		drawGrid(g2);
	}

	// coordonnées graphique en entrée
	// on place sur la grille dans les mêmes
	public void placeTmpShip(int i, int j, int size, boolean vertical) {

		if ((vertical && (i > 15 || j > 15 - (size - 1) || i < 0 || j < 0))
				|| (!vertical && (i > 15 - (size - 1) || j > 15 || i < 0 || j < 0)))
			return;
		current = new Submarine(i, j, vertical, size, observable.getPlayers()
				.getMyIndex());

		observable.notifyView(UpdateArguments.REDRAW);
	}

	public void putShip() {
		for (int i = 0; i < current.getSize(); i++) {
			if (current.isVertical()) {
				cell_grid[current.getY() + i][current.getX()].setOccupied(true);
				System.out.println("Debug : "
						+ (char) ('A' + 15 - (current.getY() + i)) + " ; "
						+ current.getX());
			} else {
				cell_grid[current.getY()][current.getX() + i].setOccupied(true);
				System.out.println("Debug : "
						+ (char) ('A' + 15 - current.getY()) + " ; "
						+ (current.getX() + i));
			}
		}

		submarines.add(current);
		current = null;
	}

	public void putPlayerShip(int x, int y, int size, boolean vertical, int num) {
		// Pour le spectateur, plus tard.
		submarines.add(new Submarine(x, y, vertical, size, num));
	}

	public Submarine getCurrent() {
		return current;
	}

	public boolean isCurrentPositionValid() {
		for (int i = 0; i < current.getSize(); i++) {
			if (current.isVertical()) {
				if (cell_grid[current.getY() + i][current.getX()].isOccupied())
					return false;
			} else {
				if (cell_grid[current.getY()][current.getX() + i].isOccupied())
					return false;
			}
		}
		return true;
	}

	public boolean hasActionLeft() {
		return nbActions != 0;
	}

	public void updateTurn(int x, int y, int nbAction) {
		drone.setActive(true);
		drone.setLocation(x, y);
		this.nbActions = nbAction;
		if (nbAction <= 0) {
			stopTurn();
			return;
		}

		updateReachableCells();

		observable.notifyView(UpdateArguments.REDRAW);
	}

	private void updateReachableCells() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (nbActions - Math.abs(drone.x - i) - Math.abs(drone.y - j) >= 0)
					cell_grid[i][j].setReachable(true);
				else
					cell_grid[i][j].setReachable(false);
			}
		}
	}

	public void startTurn(int x, int y, int nbAction) {
		isLaserActivable = true;
		updateTurn(x, y, nbAction);
	}

	public void stopTurn() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				cell_grid[i][j].setReachable(false);
			}
		}
		nbActions = 0;
		observable.notifyView(UpdateArguments.ENDTURN);
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public int getNbActionsLeft() {
		return nbActions;
	}

	public void putOuch(int x, int y) {
		cell_grid[x][y].setOuch(true);
		observable.notifyView(UpdateArguments.OUCH);
	}

	public void putMiss(int x, int y) {
		cell_grid[x][y].setMissed(true);
		observable.notifyView(UpdateArguments.MISS);
	}

	public void putTouche(int x, int y) {
		cell_grid[x][y].setTouche(true);
		observable.notifyView(UpdateArguments.TOUCHE);
	}

}
