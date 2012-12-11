package br.model.logic;

public class Cell {

	private boolean hasWreckage;
	private int numTile;
	private boolean isOccupied;
	private boolean isReachable;
	private boolean isMissed;

	public Cell() {
		this.isOccupied = false;
		this.hasWreckage = false;
		this.isReachable = false;
		numTile = 0;
	}

	public boolean hasWreckage() {
		return hasWreckage;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public boolean isOccupied() {
		return isOccupied;
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

}
