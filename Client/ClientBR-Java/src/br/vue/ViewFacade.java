package br.vue;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.model.ModelFacade;
import br.vue.components.ConnectionPane;
import br.vue.components.GamePane;
import br.vue.components.MainFrame;

public class ViewFacade implements Observer {

	private ModelFacade model;

	private MainFrame mf;
	private ConnectionPane conn;
	private GamePane game;

	public ViewFacade(ModelFacade model) {
		this.model = model;

		model.addObserver(this);

		Runnable r = new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		};

		SwingUtilities.invokeLater(r);
	}

	private synchronized void createAndShowGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Unable to load System's LooknFeel");
		}

		mf = new MainFrame();
		conn = new ConnectionPane();
		game = new GamePane();

		mf.setFrameContentPane(conn);
		game.getGrid().requestFocusInWindow();
		this.notifyAll();
	}

	public MainFrame getMf() {
		return mf;
	}

	@Override
	public void update(Observable o, Object arg) {
		new UpdateWorker(model, mf, conn, game, arg).execute();
	}

	public ConnectionPane getConnexionPane() {
		return conn;
	}

	public GamePane getGame() {
		return game;
	}

}
