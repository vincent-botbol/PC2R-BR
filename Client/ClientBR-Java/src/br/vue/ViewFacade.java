package br.vue;

import java.awt.Container;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.model.ModelFacade;
import br.vue.components.GamePane;
import br.vue.components.MainFrame;

public class ViewFacade implements Observer {

	@SuppressWarnings("unused")
	private ModelFacade j;

	private MainFrame mf;

	public ViewFacade(ModelFacade j) {
		this.j = j;

		// j.addObserver(this);

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
		setFrameContentPane(new GamePane());
	}

	public void setFrameContentPane(Container c) {
		mf.setContentPane(c);
		mf.pack();
	}

	public MainFrame getMf() {
		return mf;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
