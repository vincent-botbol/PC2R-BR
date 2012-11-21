package br.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import br.model.ModelFacade;
import br.vue.ViewFacade;

public class MainFrameListener extends WindowAdapter {

	private ModelFacade model;

	public MainFrameListener(ModelFacade model, ViewFacade view) {
		this.model = model;
	}

	@Override
	public void windowClosing(WindowEvent e) {

		if (JOptionPane.showConfirmDialog(null,
				"Voulez-vous vraiment quitter?", "AWINNERISNOTYOU",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
			model.closeSocket();
			System.exit(0);
		}

	}
}
