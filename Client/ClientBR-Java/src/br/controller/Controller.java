package br.controller;

import java.io.IOException;

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
		// Boutons de la fenetre de connexion
		ConnectListener cl = new ConnectListener(view, this);

		view.getConnexionPane().getConnect().addActionListener(cl);
		view.getConnexionPane().getCancel().addActionListener(cl);
		view.getConnexionPane().getServerComponent().addActionListener(cl);
		view.getConnexionPane().getLoginComponent().addActionListener(cl);

		// GamePane
		// Chat
		view.getGame().getChat().getSendButton()
				.addActionListener(new ChatListener(model, view));

		// GameGrid
		GameGridListener ggl = new GameGridListener(model, view);
		view.getGame().getGrid().addMouseListener(ggl);

		view.getMf().addWindowListener(new FrameListener(this));
	}

	// visib package
	void setSocket(ClientSocket socket) {
		this.socket = socket;
	}

	public void establishConnexion(final String pseudo, String host,
			final int port) {
		new CommandDispatcher(model, this, pseudo, host, port).execute();
	}

	// Appelé uniquement par le connectListener
	public void abortConnection() {
		closeConnection();
		model.notifyView(UpdateArguments.CONN_ABORTED);
	}

	public void establishConnexion(String pseudo, String host) {
		this.establishConnexion(pseudo, host, 2012);
	}

	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("should not happen");
			e.printStackTrace();
		}
	}
}
