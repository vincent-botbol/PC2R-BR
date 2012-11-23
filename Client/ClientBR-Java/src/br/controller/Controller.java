package br.controller;

import br.common.UpdateArguments;
import br.controller.listeners.ChatListener;
import br.controller.listeners.ConnectListener;
import br.controller.listeners.FrameListener;
import br.controller.listeners.GameGridListener;
import br.controller.net.ClientSocket;
import br.model.ModelFacade;
import br.vue.ViewFacade;

public class Controller {

	private final ModelFacade model;
	private final ViewFacade view;
	private ClientSocket socket;

	public Controller(ModelFacade mod, ViewFacade view) {
		this.model = mod;
		this.view = view;

		// Attente si la vue n'est pas encore pr�te
		synchronized (view) {
			try {
				if (!view.isReady())
					view.wait();
			} catch (InterruptedException e) {
			}
		}
		addListeners();
	}

	private void addListeners() {
		// Connexion
		// Boutons de la fen�tre de connexion
		ConnectListener cl = new ConnectListener(view, this);

		view.getConnexionPane().getConnect().addActionListener(cl);
		view.getConnexionPane().getCancel().addActionListener(cl);

		// GamePane
		// Chat
		view.getGame().getChat().getSendButton()
				.addActionListener(new ChatListener(model, view));

		// GameGrid
		GameGridListener ggl = new GameGridListener(model, view);
		// r�soudre le probl�me du focus ... � voir
		view.getGame().getGrid().addKeyListener(ggl);
		view.getGame().getGrid().addMouseListener(ggl);

		view.getMf().addWindowListener(new FrameListener(this));
	}

	public synchronized ClientSocket getSocket() {
		if (socket == null)
			try {
				this.wait();
			} catch (InterruptedException e) {
				assert false : "getSocket failed";
			}
		return socket;
	}

	// Package visib
	void setSocket(ClientSocket socket) {
		this.socket = socket;
	}

	private CommandDispatcher dispatch;

	public void establishConnexion(String pseudo, String host, int port) {
		model.notifyView(UpdateArguments.CONNECTION_INIT);

		// execute connexion + reading incoming response thread
		dispatch = new CommandDispatcher(this, model, pseudo, host, port);
		System.out.println("Test");
		dispatch.execute();
	}

	// Appel� uniquement par le connectListener
	public void abortConnection() {
		closeConnection();
		model.notifyView(UpdateArguments.CONNECTION_ABORTED);
	}

	public void establishConnexion(String pseudo, String host) {
		this.establishConnexion(pseudo, host, 2012);
	}

	public void closeConnection() {
		if (dispatch != null) {
			dispatch.cancel(true);
		}
	}
}
