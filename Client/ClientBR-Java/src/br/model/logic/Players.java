package br.model.logic;

import java.awt.Color;
import java.util.List;

public class Players {

	private String myPseudo;

	// Ordonnés par tour de jeu
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

		// première passe pour virer son nom
		final int truncedSize = allPlayers.size() - 1;
		String[] names = new String[truncedSize];
		int i = 0;
		for (String n : allPlayers) {
			if (!n.equals(myPseudo))
				names[i++] = n;
		}

		str.append(names[0]);
		if (truncedSize == 2) {
			str.append(" et " + names[1]);
		} else if (truncedSize == 3) {
			str.append(", " + names[1] + " et " + names[2]);
		}

		return str.toString();
	}

	public int getPlayerIndex(String s) {
		int res = 0;
		for (String p : allPlayers)
			if (p.equals(s))
				return res;
			else
				res++;
		System.err.println("Player not found");
		return 0;
	}

	public int getMyIndex() {
		return getPlayerIndex(myPseudo);
	}

	public static Color getPlayerColor(int idx) {
		switch (idx) {
		case 0:
			return Color.red;
		case 1:
			return Color.GRAY;
		case 2:
			return Color.green;
		default:
			return Color.yellow;
		}
	}
}
