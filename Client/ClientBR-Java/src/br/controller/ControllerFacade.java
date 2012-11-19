package br.controller;

import br.model.ModelFacade;
import br.vue.ViewFacade;

public class ControllerFacade {

	final ModelFacade model;
	final ViewFacade view;

	public ControllerFacade(ModelFacade mod, ViewFacade view) {
		this.model = mod;
		this.view = view;
		// Attente si la vue n'est pas encore prête
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
		// Bouton de la fenêtre de connexion
		view.getConnexionPane().getConnect()
				.addActionListener(new ConnectListener(model, view));

		// GamePane
		// Chat
		view.getGame().getChat().getSendButton()
				.addActionListener(new ChatListener(model, view));

		// GameGrid
		GameGridListener ggl = new GameGridListener(model, view);
		// résoudre le problème du focus ... à voir
		view.getGame().getGrid().addKeyListener(ggl);
		view.getGame().getGrid().addMouseListener(ggl);

		view.getMf().addWindowListener(new MainFrameListener(model, view));
	}
}
