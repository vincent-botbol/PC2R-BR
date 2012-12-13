package br.model.logic;

public class Cell {

	private boolean isTouche;
	private boolean isMissed;
	private boolean isOuch;
	private int numTile;
	private boolean isOccupied;
	private boolean isReachable;

	public Cell() {
		this.isOccupied = false;
		this.isTouche = false;
		this.isMissed = false;
		this.isOuch = false;
		this.isReachable = false;
		numTile = 0;
	}

	public boolean isTouche() {
		return isTouche;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setTouche(boolean isTouche) {
		this.isTouche = isTouche;
	}

	public void setNumTile(int numTile) {
		this.numTile = numTile;
	}

	public int getNumTile() {
		return numTile;
	}

	public void setReachable(boolean isReachable) {
		this.isReachable = isReachable;
	}

	public boolean isReachable() {
		return isReachable;
	}

	public boolean isMissed() {
		return isMissed;
	}

	public void setMissed(boolean b) {
		this.isMissed = b;
	}

	public boolean isOuch() {
		return isOuch;
	}

	public void setOuch(boolean isOuch) {
		this.isOuch = isOuch;
	}
}
