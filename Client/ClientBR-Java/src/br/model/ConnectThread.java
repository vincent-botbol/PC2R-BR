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
			// Si la socket a d�j� �t� initialis�e et connect�e
			// => re�u Double click et donc � ignorer
			if (observer.getClientSocket() != null
					&& observer.getClientSocket().isConnected())
				return;

			try {
				// On pr�vient la vue que l'on est en train de se connecter
				observer.setChanged();
				observer.notifyObservers(UpdateArguments.CONNECTION_INIT);

				// On initialise la connexion
				ClientSocket cs = new ClientSocket(host, port);
				observer.setClientSocket(cs);
				cs.connect(pseudo);

			} catch (IOException e) {
				// Si on a annul� mais que le connect g�n�re une IO
				if (!isInterrupted()) {
					observer.setChanged();
					observer.notifyObservers(UpdateArguments.CONNECTION_FAILED);
					return;
				}
			}

			// si la connexion a r�ussie ou �chou�e mais que l'on a annul�
			// entre-temps
			if (isInterrupted()) {
				rollback(CANCEL_CONNECTION);
				return;
			}

			// Lancement de la boucle d'�coute du dispatcher de Response
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
		// Si le dispatcher est instanci�, on l'annule
		if (dispatch != null) {
			// normalement, il est en attente de lecture
			// donc re�oit une InterruptedIOExc
			dispatch.cancel(true);
			// Si le thread est fini, on rollback
			if (!isAlive()) {
				rollback(CANCEL_ALL);
			}
			// Sinon, c'est g�r� dans le thread
		}
		super.interrupt();
	}
}
