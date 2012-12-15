package br.model.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Submarine {

	private int x, y;
	private boolean isVertical;
	private int size;

	private int playerNum;

	public Submarine(int x, int y, boolean isVertical, int size, int playerNum) {
		this.x = x;
		this.y = y;
		this.isVertical = isVertical;
		this.size = size;
		this.playerNum = playerNum;
	}

	public Submarine(int num, List<Point> positions) {
		Point origin = new Point(17, 17);

		if (positions.size() > 1) {
			this.isVertical = positions.get(0).x == positions.get(1).x;
		} else {
			this.isVertical = true;
		}

		for (Point p : positions) {
			if (p.x < origin.x)
				origin = p;
			if (p.y < origin.y)
				origin = p;
		}

		this.x = origin.x;
		this.y = origin.y;

		this.size = positions.size();
		this.playerNum = num;
	}

	public void drawSubmarine(Graphics2D g2) {
		double x = GameGrid.margin + this.x * GameGrid.cell_size;
		double y = GameGrid.margin + this.y * GameGrid.cell_size;

		double halfcell = GameGrid.cell_size / 2;

		Color c = g2.getColor();

		switch (playerNum) {
		case 0:
			g2.setColor(Color.red);
			break;
		case 1:
			x += halfcell;
			g2.setColor(Color.gray);
			break;
		case 2:
			y += halfcell;
			g2.setColor(Color.green);
			break;
		case 3:
			x += halfcell;
			y += halfcell;
			g2.setColor(Color.yellow);
			break;
		}

		for (int i = 0; i < size; i++) {
			Rectangle2D.Double rect = null;
			if (isVertical) {
				rect = new Rectangle2D.Double(x + this.x, y + i
						* GameGrid.cell_size + (i + this.y), halfcell, halfcell);
			} else {
				rect = new Rectangle2D.Double(x + i * GameGrid.cell_size
						+ (i + this.x), y + this.y, halfcell, halfcell);
			}
			g2.fill(rect);
		}
		g2.setColor(c);
	}

	// index inversés sur l'ordonnée
	public List<String> getIndexes() {
		List<String> args = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			if (isVertical) {
				args.add(this.x + "");
				args.add((char) ('P' - this.y - i) + "");
			} else {
				args.add((this.x + i) + "");
				args.add((char) ('P' - this.y) + "");
			}
		}

		return args;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	public boolean isVertical() {
		return isVertical;
	}
}
