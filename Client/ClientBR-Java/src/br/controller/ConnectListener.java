package br.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.model.ModelFacade;
import br.vue.ViewFacade;

public class ConnectListener implements ActionListener {

	private ModelFacade model;
	private ViewFacade vue;

	public ConnectListener(ModelFacade model, ViewFacade vue) {
		this.model = model;
		this.vue = vue;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		String host = vue.getConnexionPane().getHost();
		String pseudo = vue.getConnexionPane().getLogin();
		model.establishConnection(host, pseudo);

	}

}
