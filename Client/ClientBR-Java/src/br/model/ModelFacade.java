package br.model;

import java.util.List;
import java.util.Observable;

import br.common.UpdateArguments;
import br.model.logic.Chat;
import br.model.logic.Drone;
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
		getGrid().startAnimating();
		notifyView(UpdateArguments.CONN_SUCCESS);
	}

	public void setPlayers(List<String> arguments) {
		this.players.setAllPlayers(arguments);
		this.grid.setDrone(new Drone(this.players.getMyIndex()));
		notifyView(UpdateArguments.GAME_INIT);
	}

	public void resetGame() {
		players = new Players();
		notifyView(UpdateArguments.GAME_INIT);
		this.grid = new GameGrid(this);
	}
}
