package br.controller;

import br.model.ModelFacade;
import br.vue.ViewFacade;

public class ControllerFacade {

	final ModelFacade model;
	final ViewFacade view;

	public ControllerFacade(ModelFacade mod, ViewFacade view) {
		this.model = mod;
		this.view = view;
		// Attente si la vue n'est pas encore pr�te
		synchronized (view) {
			try {
				if (view.getConnexionPane() == null)
					view.wait();
			} catch (InterruptedException e) {
			}
		}
		addListeners();
	}

	private void addListeners() {
		// Connexion
		// Bouton de la fen�tre de connexion
		view.getConnexionPane().getConnect()
				.addActionListener(new ConnectListener(model, view));

		// GamePane
		// Chat
		view.getGame().getChat().getSendButton()
				.addActionListener(new ChatListener(model, view));

		// GameGrid
		GameGridListener ggl = new GameGridListener(model, view);
		// r�soudre le probl�me du focus ... � voir
		view.getGame().getGrid().addKeyListener(ggl);
		view.getGame().getGrid().addMouseListener(ggl);

		view.getMf().addWindowListener(new MainFrameListener(model, view));
	}
}
