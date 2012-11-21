package br.model.logic;

import java.util.List;

public class Players {

	private String myPseudo;

	// Ordonn�s par tour de jeu
	private List<String> allPlayers;

	public Players(String myPseudo) {
		this.myPseudo = myPseudo;
	}

	public void setOtherPlayersName(List<String> allPlayers) {
		this.allPlayers = allPlayers;
	}

	public List<String> getAllPlayers() {
		return allPlayers;
	}

	public String getMyPseudo() {
		return myPseudo;
	}
}