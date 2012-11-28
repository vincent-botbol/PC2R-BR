package br.model.logic;

public class Cell {

	private boolean isOccupied;
	private boolean hasWreckage;
	private int numTile;

	public Cell() {

		this.isOccupied = false;
		this.hasWreckage = false;
		numTile = 0;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public boolean HasWreckage() {
		return hasWreckage;
	}

	public boolean isHasWreckage() {
		return hasWreckage;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public void setWreckage(boolean hasWreckage) {
		this.hasWreckage = hasWreckage;
	}

	public void setNumTile(int numTile) {
		this.numTile = numTile;
	}

	public int getNumTile() {
		return numTile;
	}

}
