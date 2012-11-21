package br.model;

import java.io.IOException;

import br.common.UpdateArguments;
import br.model.logic.Players;
import br.model.net.ClientSocket;

public class ConnectThread extends Thread {

	private ModelFacade observer;
	private String host;
	private int port;
	private String pseudo;
	private CommandDispatcher dispatch;

	public static final Object lock = new Object();

	private static final int CANCEL_CONNECTION = 1;
	private static final int CANCEL_ALL = 2;

	public ConnectThread(ModelFacade facade, String pseudo, String host,
			int port) {
		this.observer = facade;
		this.pseudo = pseudo;
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		synchronized (lock) {
			// Si la socket a déjà été initialisée et connectée
			// => reçu Double click et donc à ignorer
			if (observer.getClientSocket() != null
					&& observer.getClientSocket().isConnected())
				return;

			try {
				// On prévient la vue que l'on est en train de se connecter
				observer.setChanged();
				observer.notifyObservers(UpdateArguments.CONNECTION_INIT);

				// On initialise la connexion
				ClientSocket cs = new ClientSocket(host, port);
				observer.setClientSocket(cs);
				cs.connect(pseudo);

			} catch (IOException e) {
				// Si on a annulé mais que le connect génère une IO
				if (!isInterrupted()) {
					observer.setChanged();
					observer.notifyObservers(UpdateArguments.CONNECTION_FAILED);
					return;
				}
			}

			// si la connexion a réussie ou échouée mais que l'on a annulé
			// entre-temps
			if (isInterrupted()) {
				rollback(CANCEL_CONNECTION);
				return;
			}

			// Lancement de la boucle d'écoute du dispatcher de Response
			dispatch = new CommandDispatcher(observer);
			dispatch.execute();
			observer.setDispatcher(dispatch);
			observer.setPlayers(new Players(pseudo));

			if (isInterrupted())
				rollback(CANCEL_ALL);
		}
	}

	private void rollback(int level) {
		switch (level) {
		case CANCEL_ALL:
			observer.setDispatcher(null);
			observer.setPlayers(null);
		case CANCEL_CONNECTION:
			observer.getClientSocket().close();
			observer.setClientSocket(null);
		}
		observer.setChanged();
		observer.notifyObservers(UpdateArguments.CONNECTION_ABORTED);
	}

	// Gestion
	@Override
	public void interrupt() {
		// Si le dispatcher est instancié, on l'annule
		if (dispatch != null) {
			// normalement, il est en attente de lecture
			// donc reçoit une InterruptedIOExc
			dispatch.cancel(true);
			// Si le thread est fini, on rollback
			if (!isAlive()) {
				rollback(CANCEL_ALL);
			}
			// Sinon, c'est géré dans le thread
		}
		super.interrupt();
	}
}
