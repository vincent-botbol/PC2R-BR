package br.controller;

import java.io.IOException;

import br.controller.listeners.ChatListener;
import br.controller.listeners.ConnectListener;
import br.controller.listeners.FrameListener;
import br.controller.listeners.GameGridListener;
import br.controller.net.ClientSocket;
import br.controller.net.Request;
import br.model.ModelFacade;
import br.vue.ViewFacade;

public class Controller {

	private final ModelFacade model;
	private final ViewFacade view;
	private ClientSocket socket;
	private GameGridListener ggl;

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

		view.getConnexionPane().getServerComponent().addActionListener(cl);
		view.getConnexionPane().getLoginComponent().addActionListener(cl);
		view.getConnexionPane().getCheck_pass().addActionListener(cl);
		view.getConnexionPane().getCheck_spect().addActionListener(cl);
		view.getConnexionPane().getConnect().addActionListener(cl);
		view.getConnexionPane().getRegister().addActionListener(cl);

		// GamePane
		// Chat
		ChatListener chatl = new ChatListener(this, view);
		view.getGame().getChat().getSendButton().addActionListener(chatl);
		view.getGame().getChat().getSaisie().addActionListener(chatl);

		// GameGrid
		ggl = new GameGridListener(model, view, this);

		view.getGame().getGrid().addMouseListener(ggl);
		view.getGame().getGrid().addMouseMotionListener(ggl);

		// Frame
		view.getMf().addWindowListener(new FrameListener(this));
	}

	// visib package
	void setSocket(ClientSocket socket) {
		this.socket = socket;
	}

	public void establishConnexion(final String pseudo, String host) {
		new CommandDispatcher(model, this, view, host, pseudo).execute();
	}

	/**
	 * mode spectateur
	 */
	public void establishConnexion(String host) {
		new CommandDispatcher(model, this, view, host).execute();
	}

	/**
	 * Login / Register
	 */
	public void establishConnexion(String pseudo, String pass, String host,
			boolean isRegister) {
		new CommandDispatcher(model, this, view, host, pseudo, pass, isRegister)
				.execute();
	}

	// Appelé uniquement par le connectListener
	public void abortConnection() {
		closeConnection();
	}

	public void makeRequest(Request r) {
		if (socket != null)
			try {
				socket.makeRequest(r);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			// Dans le cas où la connexion n'est pas initialisée
		}
	}

	public GameGridListener getGameGridListener() {
		return ggl;
	}

}
