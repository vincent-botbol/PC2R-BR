package br.model.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Drone extends Point {

	private boolean isActive;
	private int playerNum;

	public Drone(int playerNum) {
		isActive = false;
		this.playerNum = playerNum;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void drawDrone(Graphics2D g2) {
		double x = GameGrid.margin + this.x * GameGrid.cell_size;
		double y = GameGrid.margin + this.y * GameGrid.cell_size;

		double halfcell = GameGrid.cell_size / 2;

		Color c = null;

		switch (playerNum) {
		case 0:
			c = Color.red;
			break;
		case 1:
			x += halfcell;
			// g2.setColor(Color.blue);
			c = Color.GRAY;
			break;
		case 2:
			y += halfcell;
			c = Color.green;
			break;
		case 3:
			x += halfcell;
			y += halfcell;
			c = Color.yellow;
			break;
		}

		g2.setColor(Color.white);
		g2.fillOval((int) (x + this.x), (int) (y + this.y),
				(int) (4 * halfcell / 5), (int) (4 * halfcell / 5));
		g2.setColor(c);
		g2.fillOval((int) (x + this.x), (int) (y + this.y),
				(int) (halfcell / 2), (int) (halfcell / 2));
	}
}
