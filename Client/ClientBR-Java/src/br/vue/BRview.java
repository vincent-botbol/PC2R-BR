package br.vue;

import java.awt.Container;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.modele.Jeu;
import br.vue.components.ConnexionPane;
import br.vue.components.MainFrame;

public class BRview {

	@SuppressWarnings("unused")
	private Jeu j;

	private MainFrame mf;

	public BRview(Jeu j) {
		this.j = j;

		Runnable r = new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}

		};

		SwingUtilities.invokeLater(r);
	}

	private void createAndShowGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Unable to load System's LooknFeel");
		}

		mf = new MainFrame();

		// Open Connexion interface
		setFrameContent(new ConnexionPane());
	}

	public void setFrameContent(Container c) {
		mf.setContentPane(c);
		mf.pack();
	}

	public MainFrame getMf() {
		return mf;
	}

}
