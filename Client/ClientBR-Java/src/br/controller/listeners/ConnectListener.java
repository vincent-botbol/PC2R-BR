package br.controller.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.controller.Controller;
import br.vue.ViewFacade;

public class ConnectListener implements ActionListener {

	private ViewFacade vue;
	private Controller control;

	public ConnectListener(ViewFacade vue, Controller controller) {
		this.vue = vue;
		this.control = controller;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case "CONNECT":
			String host = vue.getConnexionPane().getHost();
			String pseudo = vue.getConnexionPane().getLogin();
			control.establishConnexion(host, pseudo);
			break;
		case "CANCEL":
			control.abortConnection();
			break;
		}

	}

}
