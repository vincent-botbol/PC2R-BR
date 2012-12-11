package br.model.logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Submarine {

	private final static Image red = new ImageIcon("data/sub_red.png")
			.getImage(), blue = new ImageIcon("data/sub_blue.png").getImage(),
			green = new ImageIcon("data/sub_green.png").getImage(),
			yellow = new ImageIcon("data/sub_yellow.png").getImage();

	private int x, y;
	private boolean isVertical;
	private int size;

	private int playerNum;

	public Submarine(int x, int y, boolean orientation, int size, int playerNum) {
		super();
		this.x = x;
		this.y = y;
		this.isVertical = orientation;
		this.size = size;
		this.playerNum = playerNum;
	}

	public AffineTransform getTransform() {
		AffineTransform aff = new AffineTransform();

		aff.translate(50 + y * GameGrid.cell_size, 50 + x * GameGrid.cell_size);

		// on scale selon la taille

		// on la tourne de 90° si elle est horizontale
		if (!isVertical) {
			aff.rotate(-Math.PI / 2);
		}
		return aff;
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
			// g2.setColor(Color.blue);
			g2.setColor(Color.GRAY);
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

	public Image getImage() {
		switch (playerNum) {
		case 0:
			return red;
		case 1:
			return blue;
		case 2:
			return green;
		default:
			return yellow;
		}
	}

	public List<String> getIndexes() {
		List<String> args = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			if (isVertical) {
				args.add((this.y + i) + "");
				args.add((char) (this.x + 'A') + "");
			} else {
				args.add((this.y) + "");
				args.add((char) (this.x + 'A' + i) + "");
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
