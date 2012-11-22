package br.model;

import java.util.Observable;

import br.common.UpdateArguments;
import br.model.logic.Chat;
import br.model.logic.GameGrid;
import br.model.logic.Players;

public class ModelFacade extends Observable {

	private GameGrid grid;
	// on verra plus tard pour les players
	private Players players;
	private Chat chat;

	public ModelFacade() {
		this.chat = new Chat(this);
		this.grid = new GameGrid(this);
		this.players = new Players();
	}

	public void notifyView(UpdateArguments e) {
		setChanged();
		notifyObservers(e);
	}

	// Getters : State Query de la vue

	public GameGrid getGrid() {
		return this.grid;
	}

	public Players getPlayers() {
		return this.players;
	}

	public Chat getChat() {
		return this.chat;
	}

	public void gotWelcomed(String login) {
		this.players.setMyPseudo(login);
		notifyView(UpdateArguments.CONNECTION_SUCCESS);
	}

}
