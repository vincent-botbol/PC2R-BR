package br.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.model.ModelFacade;
import br.vue.ViewFacade;
import br.vue.components.ConnectionPane;

public class TestController {

	final ModelFacade model;
	final ViewFacade vue;

	public TestController(ModelFacade mod, ViewFacade view) {
		super();
		this.model = mod;
		this.vue = view;

		synchronized (vue) {
			try {
				if (vue.getConnexionPane() == null)
					vue.wait();
			} catch (InterruptedException e) {
			}
		}
		final ConnectionPane cp = vue.getConnexionPane();
		cp.getConnect().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Boutton cliqué");
				model.establishConnection(cp.getHost(), 2012, cp.getLogin());
			}
		});
	}
}
