package br.model.logic;

import java.util.List;

public class Players {

	private String myPseudo;

	// Ordonn�s par tour de jeu
	private List<String> allPlayers;

	public void setAllPlayers(List<String> allPlayers) {
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

	public String everyoneButMeToString() {
		StringBuilder str = new StringBuilder();
		for (String s : allPlayers)
			if (!s.equals(myPseudo))
				str.append(s + " ");
		return str.toString().trim();
	}

}
