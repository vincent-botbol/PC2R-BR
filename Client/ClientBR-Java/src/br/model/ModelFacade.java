package br.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Observable;

import br.common.UpdateArguments;
import br.model.logic.Chat;
import br.model.logic.GameGrid;
import br.model.logic.Players;
import br.model.net.ClientSocket;

public class ModelFacade extends Observable {

	private GameGrid grid;
	// on verra plus tard pour les players
	private Players players;
	private Chat chat;
	private ConnectThread connectThread;
	private CommandDispatcher dispatcher;

	private ClientSocket clientSocket;

	public ModelFacade() {
		this.chat = new Chat(this);
		this.grid = new GameGrid(this);
	}

	@Override
	public void setChanged() {
		super.setChanged();
	}

	// M�thodes = Appels du controleur
	public void establishConnection(String host, final String name) {
		this.establishConnection(name, host, 2012);
	}

	/*
	 * - Initialisation de la socket - Connexion au serveur - D�part du
	 * scheduler (apr�s la connexion) - Initialisation des joueurs (seulement
	 * avec notre pseudo)
	 */
	// sync avec le cancel pour �tre s�r
	public synchronized void establishConnection(final String name,
			final String host, final int port) {

		connectThread = new ConnectThread(this, name, host, port);
		connectThread.start();
	}

	public synchronized void cancelConnection() {
		/*
		 * Si on annule la connexion trop tard (i.e, avant l'arriv�e d'un
		 * deuxi�me joueur lorsque l'on est tout seul) , on ferme la connexion
		 * d�j� �tablie
		 */
		// arr�te �galement le worker
		this.connectThread.interrupt();
		setChanged();
		notifyObservers(UpdateArguments.CONNECTION_ABORTED);
	}

	public void processClick(Point p, Graphics2D graphics) {
		grid.processClick(p, graphics);
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

	public void closeSocket() {
		try {
			clientSocket.close();
		} catch (NullPointerException e) { // pour pas de prise de t�te
		}
	}

	// package visibility pour les threads de connexion et d'�coute
	void setClientSocket(ClientSocket cs) {
		this.clientSocket = cs;
	}

	ClientSocket getClientSocket() {
		return this.clientSocket;
	}

	void setPlayers(Players players) {
		this.players = players;
	}

	CommandDispatcher getDispatcher() {
		return dispatcher;
	}

	void setDispatcher(CommandDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

}
