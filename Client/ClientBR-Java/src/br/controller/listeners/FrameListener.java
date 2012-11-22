package br.controller.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import br.controller.Controller;

public class FrameListener extends WindowAdapter {

	private Controller control;

	public FrameListener(Controller control) {
		this.control = control;
	}

	@Override
	public void windowClosing(WindowEvent e) {

		if (JOptionPane.showConfirmDialog(null,
				"Voulez-vous vraiment quitter?", "AWINNERISNOTYOU",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
			control.abortConnection();
			System.exit(0);
		}

	}
}
