package br.model.logic;

import java.util.List;

public class Players {

	private String myPseudo;

	// Ordonnés par tour de jeu
	private List<String> allPlayers;

	public void setOtherPlayersName(List<String> allPlayers) {
		this.allPlayers = allPlayers;
	}

	public List<String> getAllPlayers() {
		return allPlayers;
	}

	public String getMyPseudo() {
		return myPseudo;
	}

	public void setMyPseudo(String myPseudo) {
		this.myPseudo = myPseudo;
	}

}
